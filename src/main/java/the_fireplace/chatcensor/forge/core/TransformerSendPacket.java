package the_fireplace.chatcensor.forge.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import the_fireplace.chatcensor.ChatCensor;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

@SuppressWarnings({"UseOfSystemOutOrSystemErr", "SameParameterValue"})
public class TransformerSendPacket implements IClassTransformer {
    private static final String[] NAMES_SENDPACKET = new String[]{ "a", "sendPacket" };
    private static final String[] DESCS_SENDPACKET = new String[]{"(Lht;)V", "(Lnet/minecraft/network/Packet;)V"};
    private String packetDesc, sPacketChatDesc, entityPlayerMPDesc, netHandlerPlayServer, player;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer")){
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(basicClass);
            reader.accept(node, 0);

            transformNetHandlerPlayServer(node);

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }
        return basicClass;
    }

    private void transformNetHandlerPlayServer(ClassNode node){
        MethodNode sendPacket = node.methods
                .stream()
                .filter(method -> ArrayUtils.contains(DESCS_SENDPACKET, method.desc) && ArrayUtils.contains(NAMES_SENDPACKET, method.name))
                .findAny()
                .orElseThrow(() -> {
                    logMethods("sendPacket", node);
                    return new IllegalStateException("Chat Censor failed modifying NetHandlerPlayServer - could not find sendPacket. The mod has generated logs to help pinpointing the issue, please include them in your report.");
                });
        if(sendPacket.desc.equals("(Lht;)V")) {
            packetDesc = "Lht;";
            sPacketChatDesc = "Lin;";
            entityPlayerMPDesc = "Loq;";
            netHandlerPlayServer = "pa";
            player = "b";
        } else {
            packetDesc = "Lnet/minecraft/network/Packet;";
            sPacketChatDesc = "Lnet/minecraft/network/play/server/SPacketChat;";
            entityPlayerMPDesc = "Lnet/minecraft/entity/player/EntityPlayerMP;";
            netHandlerPlayServer = "net/minecraft/network/NetHandlerPlayServer";
            player = "player";
        }

        try{
            transformSendPacket(sendPacket);
        }catch(Throwable t){
            logInstructions(sendPacket);
            throw new IllegalStateException("Chat Censor failed modifying NetHandlerPlayServer. The mod has generated logs to help pinpointing the issue, please include them in your report.", t);
        }
    }

    private void transformSendPacket(MethodNode method){
        if(ChatCensor.getMinecraftHelper() != null)
            ChatCensor.getMinecraftHelper().getLogger().debug("Transforming sendPacket...");
        else
            System.out.println("Transforming sendPacket...");

        InsnList instructions = method.instructions;
        int insertionIndex = -1;
        int packetInIndex = -1;
        int spacketIndex = -1;

        for(LocalVariableNode node: method.localVariables) {
            if (node.desc.equals(packetDesc))
                packetInIndex = node.index;
            else if(node.desc.equals(sPacketChatDesc))
                spacketIndex = node.index;
        }

        for (int index = 0, instrcount = instructions.size(); index < instrcount; index++) {
            if (checkMethodInstruction(instructions.get(index), INVOKEVIRTUAL, "getChatVisibility", "C") &&
                    instructions.get(index - 1).getOpcode() == Opcodes.GETFIELD
            ) {
                insertionIndex = index + 1;
                break;
            }
        }

        if (insertionIndex < 0)
            throw new IllegalStateException("Could not find entry point.");
        if (packetInIndex < 0)
            throw new IllegalStateException("Could not find packetIn index.");
        if (spacketIndex < 0)
            throw new IllegalStateException("Could not find spacketchat index.");

        if(ChatCensor.getMinecraftHelper() != null)
            ChatCensor.getMinecraftHelper().getLogger().debug("Found insertion point at " + insertionIndex + ".");
        else
            System.out.println("Found insertion point at " + insertionIndex + ".");

        InsnList inserted = new InsnList();
        inserted.add(new VarInsnNode(Opcodes.ALOAD, 0));
        inserted.add(new FieldInsnNode(Opcodes.GETFIELD, netHandlerPlayServer, player, entityPlayerMPDesc));
        inserted.add(new VarInsnNode(Opcodes.ALOAD, spacketIndex));
        inserted.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "the_fireplace/chatcensor/util/NetworkUtils", "createModifiedChat", '('+entityPlayerMPDesc+sPacketChatDesc+')'+sPacketChatDesc, false));
        inserted.add(new VarInsnNode(Opcodes.ASTORE, packetInIndex));

        instructions.insert(instructions.get(insertionIndex), inserted);
    }

    private static boolean checkMethodInstruction(AbstractInsnNode instruction, int opcode, String name1, String name2){
        if (instruction.getOpcode() != opcode){
            return false;
        }

        String name = ((MethodInsnNode)instruction).name;
        return name.equals(name1) || name.equals(name2);
    }

    private static void logMethods(String missingMethod, ClassNode owner){
        if(ChatCensor.getMinecraftHelper() != null) {
            ChatCensor.getMinecraftHelper().getLogger().error("Chat Censor could not find NetHandlerPlayServer.{}, generating debug logs...", missingMethod);

            for (MethodNode method : owner.methods)
                ChatCensor.getMinecraftHelper().getLogger().error("> {} .. {}", method.name, method.desc);
        } else {
            System.err.println(String.format("Chat Censor could not find NetHandlerPlayServer.%s, generating debug logs...", missingMethod));

            for (MethodNode method : owner.methods)
                System.err.println(String.format("> %s .. %s", method.name, method.desc));
        }
    }

    private static void logInstructions(MethodNode method){
        TraceMethodVisitor visitor = new TraceMethodVisitor(new Textifier());

        for(Iterator<AbstractInsnNode> iter = method.instructions.iterator(); iter.hasNext();){
            iter.next().accept(visitor);
        }

        int index = 0;

        for(Object obj:visitor.p.getText())
            if(ChatCensor.getMinecraftHelper() != null)
                ChatCensor.getMinecraftHelper().getLogger().error("> {}: {}", ++index, StringUtils.stripEnd(obj.toString(), null));
            else
                System.err.println(String.format("> %s: %s", ++index, StringUtils.stripEnd(obj.toString(), null)));
    }
}
