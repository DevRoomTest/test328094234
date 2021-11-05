package net.ryanland.colossus.sys.interactions.menu;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyAction;
import net.ryanland.colossus.command.CommandException;
import net.ryanland.colossus.events.CommandEvent;
import net.ryanland.colossus.events.MessageCommandEvent;
import net.ryanland.colossus.events.SlashEvent;

public interface InteractionMenu {

    void send(Message message) throws CommandException;

    void send(Interaction interaction) throws CommandException;

    default void send(CommandEvent event) throws CommandException {
        if (event instanceof MessageCommandEvent)
            send(((MessageCommandEvent) event).getMessage());
        else if (event instanceof SlashEvent)
            send(((SlashEvent) event).getInteraction());
        throw new IllegalArgumentException();
    }
}
