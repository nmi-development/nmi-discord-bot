package com.github.nmi.nmibot.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.MessageBuilder;

/**
 * @author happyzleaf
 * @since 30/07/2018
 */
public class SuccCommand extends Command {
	public SuccCommand() {
		this.name = "succ";
		ownerCommand = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.getChannel().sendFile(this.getClass().getResourceAsStream("/test.gif"), "regole.gif", new MessageBuilder("Bella lì colbrì.").build()).queue();
	}
}
