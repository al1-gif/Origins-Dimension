package meh.shuuphe.originsdimension.renderer;

import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.dataticket.DataTicket;

public class RenderState extends LivingEntityRenderState implements GeoRenderState {

    private final Map<DataTicket<?>, Object> data = new HashMap<>();

    public float flightPitch;

    @Override
    public <D> void addGeckolibData(DataTicket<D> dataTicket, @Nullable D value) {
        data.put(dataTicket, value);
    }

    @Override
    public boolean hasGeckolibData(DataTicket<?> dataTicket) {
        return data.containsKey(dataTicket);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D> D getGeckolibData(DataTicket<D> dataTicket) {
        return (D) data.get(dataTicket);
    }

    @Override
    public Map<DataTicket<?>, Object> getDataMap() {
        return data;
    }
}