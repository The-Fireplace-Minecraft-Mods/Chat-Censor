package the_fireplace.chatcensor.forge.mixin;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.chatcensor.util.NetworkUtils;

@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerMixin {
    @Shadow
    public EntityPlayerMP player;

    @ModifyVariable(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), argsOnly = true)
    public Packet<?> modifiedPacket(Packet<?> packetIn) {
        if(packetIn instanceof SPacketChat)
            return NetworkUtils.createModifiedChat(player, (SPacketChat) packetIn);
        return packetIn;
    }

    @Inject(at=@At("HEAD"), method = "sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void sendPacket(Packet<?> packetIn, CallbackInfo info) {
        if(packetIn == null)
            info.cancel();
    }

    //value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;sendMessage(Lnet/minecraft/util/text/ITextComponent;Z)V"
    @Inject(at=@At("HEAD"), method = "processChatMessage(Lnet/minecraft/network/play/client/CPacketChatMessage;)V")
    public void processChatMessage(CPacketChatMessage packetIn, CallbackInfo info) {//ITextComponent itextcomponent
        //TODO will Bukkit mess this up?
        ITextComponent comp = new TextComponentTranslation("chat.type.text", this.player.getDisplayName(), ForgeHooks.newChatWithLinks(packetIn.getMessage()));
        NetworkUtils.messageSenders.putIfAbsent(comp.getUnformattedText().hashCode(), player.getUniqueID());
        NetworkUtils.messageSendersBackup.putIfAbsent("<"+player.getName()+">", player.getUniqueID());
    }
}
