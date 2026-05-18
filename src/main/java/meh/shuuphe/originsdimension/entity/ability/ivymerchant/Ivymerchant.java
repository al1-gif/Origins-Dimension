package meh.shuuphe.originsdimension.entity.ability.ivymerchant;

import com.mojang.serialization.Codec;
import meh.shuuphe.originsdimension.registry.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Merchant;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class Ivymerchant extends PathAwareEntity implements GeoEntity, Merchant {
    private static final RawAnimation IDLE =
            RawAnimation.begin().thenLoop("ivy_oleander.animation.idle");
    private static final RawAnimation WALK =
            RawAnimation.begin().thenLoop("ivy_oleander.animation.walk");
    private static final RawAnimation RUN =
            RawAnimation.begin().thenLoop("ivy_oleander.animation.run");
    private static final RawAnimation TRADING_ANIM =
            RawAnimation.begin().thenLoop("ivy_oleander.animation.trading");
    private static final RawAnimation GREETINGS =
            RawAnimation.begin().thenPlay("ivy_oleander.animation.greetings");
    private static final RawAnimation REACT_TO_EGG =
            RawAnimation.begin().thenPlay("ivy_oleander.animation.reaction_to_egg");
    private static final RawAnimation TRADE_START =
            RawAnimation.begin().thenPlay("ivy_oleander.animation.trade_start");
    private static final RawAnimation TRADE_STOP =
            RawAnimation.begin().thenPlay("ivy_oleander.animation.trade_stop");
    private static final TrackedData<Boolean> IS_FLEEING =
            DataTracker.registerData(Ivymerchant.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_BUSY =
            DataTracker.registerData(Ivymerchant.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_TRADING =
            DataTracker.registerData(Ivymerchant.class, TrackedDataHandlerRegistry.BOOLEAN);
    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    private boolean isDying = false;
    private boolean hasGreeted = false;
    private boolean hasEggTrust = false;
    private int ticksAlive = 0;
    private int greetingTicks = 0;
    private int reactToEggTicks = 0;
    private int tradeStartTicks = 0;
    private int tradeStopTicks = 0;
    private int scaredTicks = 0;
    private PlayerEntity scaredOfPlayer = null;
    private PlayerEntity pendingTradePlayer = null;
    private PlayerEntity eggPlayer = null;
    private PlayerEntity currentCustomer = null;
    private TradeOfferList offers = null;
    private int merchantExp = 0;
    private PlayerEntity greetingPlayer = null;

    public Ivymerchant(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.FOLLOW_RANGE, 24.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.ARMOR, 6.0);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_FLEEING, false);
        builder.add(IS_BUSY,    false);
        builder.add(IS_TRADING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FleeGoal());
        this.goalSelector.add(2, new Goal() {
            { this.setControls(EnumSet.of(Control.LOOK)); }
            @Override public boolean canStart()       { return currentCustomer != null; }
            @Override public boolean shouldContinue() { return currentCustomer != null; }
            @Override public void tick()              { getLookControl().lookAt(currentCustomer, 30, 30); }
        });
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0) {
            @Override public boolean canStart() {
                return !dataTracker.get(IS_BUSY) && greetingTicks <= 0 && currentCustomer == null && super.canStart();
            }
            @Override public boolean shouldContinue() {
                return !dataTracker.get(IS_BUSY) && greetingTicks <= 0 && currentCustomer == null && super.shouldContinue();
            }
        });
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(5, new LookAroundGoal(this));
    }

    private class FleeGoal extends Goal {
        FleeGoal() { this.setControls(EnumSet.of(Control.MOVE)); }

        private Vec3d getCentroid() {
            double x = 0, y = 0, z = 0; int n = 0;
            if (scaredTicks > 0 && scaredOfPlayer != null && squaredDistanceTo(scaredOfPlayer) < 64.0) {
                Vec3d p = scaredOfPlayer.getEntityPos(); x += p.x; y += p.y; z += p.z; n++;
            }
            for (HostileEntity e : getEntityWorld().getEntitiesByClass(HostileEntity.class, getBoundingBox().expand(10), e -> squaredDistanceTo(e) < 100)) {
                Vec3d p = e.getEntityPos(); x += p.x; y += p.y; z += p.z; n++;
            }
            return n > 0 ? new Vec3d(x / n, y / n, z / n) : null;
        }

        @Override public boolean canStart() {
            return getCentroid() != null;
        }
        @Override public boolean shouldContinue() {
            return getCentroid() != null;
        }

        @Override
        public void tick() {
            Vec3d c = getCentroid();
            if (c == null) return;
            dataTracker.set(IS_FLEEING, true);
            Vec3d target = getEntityPos().add(getEntityPos().subtract(c).normalize().multiply(10));
            getNavigation().startMovingTo(target.x, target.y, target.z, 1.5);
        }

        @Override
        public void stop() {
            dataTracker.set(IS_FLEEING, false);
            getNavigation().stop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isDead()) return;
        if (ticksAlive < 20) { ticksAlive++; return; }

        if (dataTracker.get(IS_TRADING)) {
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            if (currentCustomer != null) this.getLookControl().lookAt(currentCustomer, 30, 30);
            return;
        }

        if (reactToEggTicks > 0) {
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            if (eggPlayer != null) this.getLookControl().lookAt(eggPlayer, 30, 30);
            if (--reactToEggTicks == 0 && !this.getEntityWorld().isClient()) {
                if (eggPlayer != null) {
                    eggPlayer.sendMessage(Text.literal("Ivy now trusts you enough to trade wit u!"), true);
                    pendingTradePlayer = eggPlayer;
                    eggPlayer = null;
                }
                tradeStartTicks = 29;
                this.triggerAnim("action", "trade_start");
                this.getEntityWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.IVY_TRADE_START, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            }
            return;
        }

        if (tradeStartTicks > 0) {
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            if (--tradeStartTicks == 0 && pendingTradePlayer instanceof ServerPlayerEntity spe && !spe.isDisconnected()) {
                currentCustomer    = spe;
                pendingTradePlayer = null;
                dataTracker.set(IS_BUSY,    false);
                dataTracker.set(IS_TRADING, true);
                spe.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                        (syncId, inv, p) -> new MerchantScreenHandler(syncId, inv, this),
                        this.getDisplayName()
                ));
                sendTradeOffers(spe.currentScreenHandler.syncId, getOffers(), 0, merchantExp, false, false);
            }
            return;
        }

        if (tradeStopTicks > 0) {
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            if (--tradeStopTicks == 0) dataTracker.set(IS_BUSY, false);
            return;
        }

        if (greetingTicks > 0) {
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);

            if (greetingPlayer != null) {
                this.getLookControl().lookAt(greetingPlayer, 30, 30);
            }

            if (--greetingTicks <= 0) {
                greetingPlayer = null;
                dataTracker.set(IS_BUSY, false);
            }

            return;
        }
        if (scaredTicks > 0 && --scaredTicks == 0) scaredOfPlayer = null;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.getEntityWorld().isClient()) return ActionResult.SUCCESS;
        if (isDying || ticksAlive < 20 || dataTracker.get(IS_BUSY) || greetingTicks > 0) return ActionResult.PASS;

        if (!hasGreeted) {
            hasGreeted    = true;
            greetingTicks = 55;
            greetingPlayer = player;
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            this.triggerAnim("action", "greetings");
            player.sendMessage(Text.literal("Ivy: Hey-ho, stranger! I have appropriate items for you, if you show me one dragon egg of any kind."), false);
            return ActionResult.SUCCESS;
        }

        if (!hasEggTrust) {
            if (player.getMainHandStack().isOf(Items.DRAGON_EGG)) {
                hasEggTrust     = true;
                reactToEggTicks = 66;
                eggPlayer       = player;
                dataTracker.set(IS_BUSY, true);
                this.getNavigation().stop();
                this.setVelocity(Vec3d.ZERO);
                this.getLookControl().lookAt(player, 30, 30);
                this.triggerAnim("action", "reaction_to_egg");
                this.getEntityWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.IVY_REACTION_TO_EGG, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            } else {
                player.sendMessage(Text.literal("Ivy: Hey-ho, stranger! I have appropriate items for you, if you show me one dragon egg of any kind."), false);
            }
            return ActionResult.SUCCESS;
        }

        if (currentCustomer == null) {
            pendingTradePlayer = player;
            tradeStartTicks    = 29;
            dataTracker.set(IS_BUSY, true);
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            this.triggerAnim("action", "trade_start");
            this.getEntityWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.IVY_TRADE_START, SoundCategory.NEUTRAL, 1.0f, 1.0f);

        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean hurt = super.damage(world, source, amount);
        if (hurt && source.getAttacker() instanceof PlayerEntity attacker) {
            scaredOfPlayer = attacker;
            scaredTicks    = 120;
            greetingTicks  = 0;
        }
        return hurt;
    }

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);
        isDying = true;
        dataTracker.set(IS_BUSY,    false);
        dataTracker.set(IS_TRADING, false);
        greetingTicks = 0;
        this.triggerAnim("action", "trade_stop");
    }

    @Override
    protected void writeCustomData(WriteView view) {
        super.writeCustomData(view);
        view.put("HasGreeted",  Codec.BOOL, hasGreeted);
        view.put("HasEggTrust", Codec.BOOL, hasEggTrust);
        if (offers != null && !offers.isEmpty())
            view.put("Offers", TradeOfferList.CODEC, offers);
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        hasGreeted  = view.read("HasGreeted",  Codec.BOOL).orElse(false);
        hasEggTrust = view.read("HasEggTrust", Codec.BOOL).orElse(false);
        offers      = view.read("Offers", TradeOfferList.CODEC).orElse(null);
    }

    @Override
    public void setCustomer(@Nullable PlayerEntity customer) {
        if (this.currentCustomer != null && customer == null) {
            dataTracker.set(IS_BUSY,    true);
            dataTracker.set(IS_TRADING, false);
            this.getNavigation().stop();
            this.setVelocity(Vec3d.ZERO);
            tradeStopTicks = 29;
            this.triggerAnim("action", "trade_stop");
            this.getEntityWorld().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.IVY_TRADE_STOP, SoundCategory.NEUTRAL, 1.0f, 1.0f);
        }
        this.currentCustomer = customer;
    }

    @Override
    public TradeOfferList getOffers() {
        if (offers == null) {
            offers = new TradeOfferList();
            offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 8),  new ItemStack(Items.ENDER_PEARL),          10, 5,  0.05f));
            offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 5),  new ItemStack(Items.BLAZE_ROD, 2),         10, 5,  0.05f));
            offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 3),  new ItemStack(Items.EXPERIENCE_BOTTLE, 3), 15, 3,  0.05f));
            offers.add(new TradeOffer(new TradedItem(Items.EMERALD, 15), new ItemStack(Items.TOTEM_OF_UNDYING),      2, 20, 0.05f));
        }
        return offers;
    }

    @Override public void trade(TradeOffer offer) {
        offer.use(); }
    @Override public @Nullable PlayerEntity getCustomer() {
        return currentCustomer; }
    @Override public boolean canInteract(PlayerEntity p) {
        return currentCustomer == p && isAlive() && p.canInteractWithEntity(this, 4.0); }
    @Override public void onSellingItem(ItemStack stack) {}
    @Override public int getExperience() {
        return merchantExp; }
    @Override public void setExperienceFromServer(int exp) {
        this.merchantExp = exp; }
    @Override public boolean isLeveledMerchant() {
        return false; }
    @Override public boolean isClient() {
        return this.getEntityWorld().isClient(); }
    @Override public SoundEvent getYesSound() {
        return SoundEvents.ENTITY_VILLAGER_YES; }
    public void setOffersFromServer(TradeOfferList o) {
        this.offers = o; }

    public void sendTradeOffers(int syncId, TradeOfferList offers, int levelProgress, int experience, boolean leveledUp, boolean refreshed) {
        if (currentCustomer instanceof ServerPlayerEntity spe)
            spe.sendTradeOffers(syncId, offers, levelProgress, experience, leveledUp, refreshed);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("movement", 2, state -> {
            if (isDying) return PlayState.STOP;
            if (dataTracker.get(IS_TRADING))
                return state.setAndContinue(TRADING_ANIM);
            if (dataTracker.get(IS_BUSY) || greetingTicks > 0)
                return state.setAndContinue(IDLE);
            if (state.isMoving())
                return state.setAndContinue(dataTracker.get(IS_FLEEING) ? RUN : WALK);
            return state.setAndContinue(IDLE);
        }));

        AnimationController<Ivymerchant> action = new AnimationController<>("action", 3, state -> PlayState.STOP);
        action.triggerableAnim("greetings",       GREETINGS);
        action.triggerableAnim("reaction_to_egg", REACT_TO_EGG);
        action.triggerableAnim("trade_start",     TRADE_START);
        action.triggerableAnim("trade_stop",      TRADE_STOP);
        controllers.add(action);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return cache; }
}