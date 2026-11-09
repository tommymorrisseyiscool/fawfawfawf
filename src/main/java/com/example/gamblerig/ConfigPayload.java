package com.example.gamblerig;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigPayload(boolean win, Identifier item) implements CustomPayload {
    public static final CustomPayload.Id<ConfigPayload> ID =
        new CustomPayload.Id<>(Identifier.of("gamblerig", "config"));
    public static final PacketCodec<RegistryByteBuf, ConfigPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.BOOLEAN, ConfigPayload::win,
        Identifier.PACKET_CODEC, ConfigPayload::item,
        ConfigPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
}
