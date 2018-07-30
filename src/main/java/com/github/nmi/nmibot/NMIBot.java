package com.github.nmi.nmibot;

import com.github.nmi.nmibot.command.SuccCommand;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author happyzleaf
 * @since 30/07/2018
 */
public class NMIBot {
	private static Logger LOGGER = LoggerFactory.getLogger("NMIBot");
	private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static void main(String[] args) {
		new NMIBot(new File(System.getProperty("user.dir"), "nmiresources")).start();
	}
	
	private File path;
	private Config config;
	
	public JDA jda;
	public EventWaiter waiter;
	
	public NMIBot(File path) {
		this.path = path;
		if (!this.path.exists()) {
			this.path.mkdirs();
		}
		
		File configFile = new File(path, "config.json");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				FileUtils.writeStringToFile(configFile, GSON.toJson(new Config()), StandardCharsets.UTF_8);
				LOGGER.info("A new config has been generated, please configure it. The program will now shut down.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			config = GSON.fromJson(new FileReader(configFile), Config.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		CommandClientBuilder builder = new CommandClientBuilder();
		builder.setOwnerId("146310787441491968");
		builder.setCoOwnerIds("181765068625674240");
		builder.setPrefix("!");
		builder.setGame(Game.streaming("NMI BOT", "https://www.twitch.tv/gnacca_"));
		builder.useHelpBuilder(false);
		
		builder.addCommand(new SuccCommand());
		
		waiter = new EventWaiter();
		try {
			jda = new JDABuilder(AccountType.BOT)
					.setToken(config.token)
					.setGame(Game.playing("loading..."))
					.addEventListener(waiter)
					.addEventListener(builder.build())
					.buildBlocking();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {}
}
