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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.chatcensor.util.NetworkUtils;

@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerMixin {
    @Shadow
    public EntityPlayerMP player;

    //@ModifyArg()
    @Inject(at=@At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/EntityPlayerMP;getChatVisibility()Lnet/minecraft/entity/player/EntityPlayer$EnumChatVisibility;"/*, shift = At.Shift.AFTER, by = 2*/), method = "sendPacket(Lnet/minecraft/network/Packet;)V")
    public void sendPacket(Packet<?> packetIn, CallbackInfo info) {
        packetIn = NetworkUtils.createModifiedChat(player, (SPacketChat) packetIn);
        if(packetIn == null)
            info.cancel();
    }
//value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;sendMessage(Lnet/minecraft/util/text/ITextComponent;Z)V"
    @Inject(at=@At("HEAD"), method = "processChatMessage(Lnet/minecraft/network/play/client/CPacketChatMessage;)V")
    public void processChatMessage(CPacketChatMessage packetIn, CallbackInfo info) {//ITextComponent itextcomponent
        //TODO wait for Bukkit transformations?
        ITextComponent comp = new TextComponentTranslation("chat.type.text", this.player.getDisplayName(), ForgeHooks.newChatWithLinks(packetIn.getMessage()));
        NetworkUtils.messageSenders.putIfAbsent(comp.getUnformattedText().hashCode(), player.getUniqueID());
    }
}
