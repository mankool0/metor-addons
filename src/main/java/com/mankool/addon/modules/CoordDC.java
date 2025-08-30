package com.mankool.addon.modules;

import com.mankool.addon.Addons;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BlockPosSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.AutoReconnect;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CoordDC extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<BlockPos> targetPos = sgGeneral.add(new BlockPosSetting.Builder()
        .name("target-coordinates")
        .description("Coordinates to disconnect at")
        .defaultValue(new BlockPos(0, 0, 0))
        .build()
    );

    private final Setting<Double> distance = sgGeneral.add(new DoubleSetting.Builder()
        .name("distance")
        .description("Distance from coordinates to disconnect")
        .defaultValue(5.0)
        .min(0.1)
        .sliderRange(0.1, 200.0)
        .build()
    );

    private final Setting<Boolean> disableAutoReconnect = sgGeneral.add(new BoolSetting.Builder()
        .name("disable-auto-reconnect")
        .description("Disables auto-reconnect when disconnecting")
        .defaultValue(true)
        .build()
    );

    public CoordDC() {
        super(Addons.CATEGORY, "coord-dc", "Disconnects when next to set coordinates");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null) return;

        Vec3d playerPos = mc.player.getPos();
        BlockPos target = targetPos.get();
        Vec3d targetVec = new Vec3d(target.getX(), target.getY(), target.getZ());

        double dist = playerPos.distanceTo(targetVec);

        if (dist <= distance.get()) {
            if (disableAutoReconnect.get()) {
                AutoReconnect autoReconnect = Modules.get().get(AutoReconnect.class);
                if (autoReconnect != null && autoReconnect.isActive()) {
                    autoReconnect.toggle();
                }
            }

            ClientPlayNetworkHandler handler = mc.getNetworkHandler();
            if (handler != null) {
                handler.onDisconnect(new DisconnectS2CPacket(Text.literal("Reached target coordinates")));
                toggle();
            }
        }
    }
}
