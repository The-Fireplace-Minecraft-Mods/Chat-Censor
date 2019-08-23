package the_fireplace.chatcensor.sponge.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NetHandlerPlayServer.class)
public class MixinNetHandlerPlayServer {

    @Shadow
    public EntityPlayerMP player;

    @Inject(method="sendPacket", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/EntityPlayerMP;getChatVisibility()Lnet/minecraft/entity/player/EntityPlayer$EnumChatVisibility;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT/*, constraints = "USECHATCENSORMIXIN(1)"*/)
    public void onSendPacket(Packet<?> packetIn, CallbackInfo ci, SPacketChat spacketchat, EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility) {
        packetIn = the_fireplace.chatcensor.util.NetworkUtils.createModifiedChat(player, spacketchat);
    }
}
