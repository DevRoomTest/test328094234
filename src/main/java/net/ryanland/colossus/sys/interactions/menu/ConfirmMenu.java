package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.ryanland.colossus.command.executor.functional_interface.CommandRunnable;
import net.ryanland.colossus.sys.interactions.ButtonClickContainer;
import net.ryanland.colossus.sys.interactions.ButtonHandler;
import net.ryanland.colossus.sys.interactions.InteractionUtil;
import net.ryanland.colossus.sys.message.PresetBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public record ConfirmMenu(String description, CommandRunnable confirmAction,
                          String confirmedDescription)
    implements InteractionMenu {

    @Override
    public void send(Interaction interaction) {
        // Create buttons
        List<Button> buttons = Arrays.asList(
            Button.success("confirm", "Confirm")
                .withEmoji(Emoji.fromUnicode("✅")),
            Button.danger("dismiss", "Cancel")
                .withEmoji(Emoji.fromUnicode("❎"))
        );

        // Create embed
        PresetBuilder embed = new PresetBuilder()
            .setTitle("Confirm")
            .setDescription(description)
            .addLogo();

        // Send the message and set the action rows
        InteractionHook hook = interaction.replyEmbeds(embed.build())
            .addActionRows(InteractionUtil.of(buttons))
            .setEphemeral(true)
            .complete();

        // Add a listener for when a button is clicked
        ButtonHandler.addListener(hook, buttonEvent -> new ButtonHandler.ButtonListener(
                interaction.getUser().getIdLong(),
                clickEvent -> new ButtonClickContainer(
                    event -> {
                        switch (event.getComponentId()) {
                            case "confirm" -> {
                                event.deferEdit().queue();
                                event.getHook()
                                    .editOriginalComponents(Collections.emptyList())
                                    .setEmbeds(embed.setDescription(confirmedDescription).build())
                                    .queue();
                                confirmAction.execute();
                            }
                            case "dismiss" -> {
                                event.deferEdit().queue();
                                event.getHook()
                                    .editOriginalComponents(Collections.emptyList())
                                    .setEmbeds(embed.setDescription("Action canceled.").build())
                                    .queue();
                            }
                        }
                    }
                )
            )
        );
    }

    @Override
    public void send(Message message) {
        // Create buttons
        List<Button> buttons = Arrays.asList(
            Button.success("confirm", "Confirm")
                .withEmoji(Emoji.fromUnicode("✅")),
            Button.danger("dismiss", "Cancel")
                .withEmoji(Emoji.fromUnicode("❎"))
        );

        // Create embed
        PresetBuilder embed = new PresetBuilder()
            .setTitle("Confirm")
            .setDescription(description)
            .addLogo();

        // Send the message and set the action rows
        Message msg = message.replyEmbeds(embed.build())
            .setActionRows(InteractionUtil.of(buttons))
            .complete();

        // Add a listener for when a button is clicked
        ButtonHandler.addListener(msg, buttonEvent -> new ButtonHandler.ButtonListener(
                msg.getAuthor().getIdLong(),
                clickEvent -> new ButtonClickContainer(
                    event -> {
                        switch (event.getComponentId()) {
                            case "confirm" -> {
                                event.deferEdit().queue();
                                event.getHook()
                                    .editOriginalComponents(Collections.emptyList())
                                    .setEmbeds(embed.setDescription(confirmedDescription).build())
                                    .queue();
                                confirmAction.execute();
                            }
                            case "dismiss" -> {
                                event.deferEdit().queue();
                                event.getHook()
                                    .editOriginalComponents(Collections.emptyList())
                                    .setEmbeds(embed.setDescription("Action canceled.").build())
                                    .queue();
                            }
                        }
                    }
                )
            )
        );
    }

}
