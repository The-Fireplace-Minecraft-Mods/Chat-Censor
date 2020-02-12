package the_fireplace.chatcensor.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import the_fireplace.chatcensor.data.PlayerDataManager;
import the_fireplace.chatcensor.util.PermissionManager;
import the_fireplace.chatcensor.util.translation.TranslationUtil;

public class ChatCensorCommands {
    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register(Commands.literal("togglecensor").requires((source) -> source.getEntity() instanceof ServerPlayerEntity && PermissionManager.hasPermissionOrNoManager((ServerPlayerEntity) source.getEntity(), "command.togglecensor")).executes((command) -> {
            boolean newIgnoresCensor = !PlayerDataManager.getIgnoresCensor(command.getSource().asPlayer().getUniqueID());
            PlayerDataManager.setIgnoresCensor(command.getSource().asPlayer().getUniqueID(), newIgnoresCensor);
            command.getSource().sendFeedback(TranslationUtil.getTranslation(command.getSource().asPlayer().getUniqueID(), "commands.togglecensor."+(newIgnoresCensor ? "uncensored" : "censored")), false);
            return Command.SINGLE_SUCCESS;
        }));
    }
}
