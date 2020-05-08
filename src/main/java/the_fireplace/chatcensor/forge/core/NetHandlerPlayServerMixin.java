package the_fireplace.chatcensor.forge.core;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.chatcensor.util.NetworkUtils;

@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerMixin {
    @Shadow
    public EntityPlayerMP player;

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Inject(at=@At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/EntityPlayerMP;getChatVisibility()Lnet/minecraft/entity/player/EntityPlayer$EnumChatVisibility;", shift = At.Shift.AFTER, by = 1), method = "sendPacket(Lnet/minecraft/network/Packet;)V")
    public void sendPacket(Packet<?> packetIn, CallbackInfo info) {
        packetIn = NetworkUtils.createModifiedChat(player, (SPacketChat) packetIn);
        if(packetIn == null)
            return;
    }
}
