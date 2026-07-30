/**
 * Copyright (c) 2017 Jakub Sapalski
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package pl.betoncraft.flier.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.betoncraft.flier.api.Flier;
import pl.betoncraft.flier.api.core.InGamePlayer;
import pl.betoncraft.flier.api.core.LoadingException;

/**
 * Manages languages. Single-language mode: the language is defined globally
 * in config.yml ("language" option) and used for all players.
 *
 * @author Jakub Sapalski
 */
public class LangManager {

	private static LangManager instance;
	private String lang;
	private ConfigurationSection messages;
	private final Flier flier = Flier.getInstance();

	/**
	 * Creates new language manager. Needs to be reloaded before use.
	 */
	public LangManager() {
		instance = this;
	}
	
	/**
	 * Reloads the messages.
	 */
	public static void reload() throws LoadingException {
		File file = new File(instance.flier.getDataFolder(), "messages.yml");
		if (!file.exists()) {
			try (InputStream in = instance.flier.getResource("messages.yml")) {
				if (in == null) {
					throw new LoadingException("Could not find default messages.yml inside the plugin jar.");
				}
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				throw new LoadingException("Could not save default messages.yml: " + e.getMessage());
			}
		} else {
			// update new strings
			YamlConfiguration def;
			try (InputStream in = instance.flier.getResource("messages.yml")) {
				if (in == null) {
					throw new LoadingException("Could not find default messages.yml inside the plugin jar.");
				}
				def = YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new LoadingException("Could not read default messages.yml: " + e.getMessage());
			}
			YamlConfiguration cur = YamlConfiguration.loadConfiguration(file);
			boolean changed = false;
			for (String key : def.getKeys(true)) {
				if (!cur.contains(key)) {
					changed = true;
					cur.set(key, def.get(key));
				}
			}
			if (changed) {
				try {
					cur.save(file);
				} catch (IOException e) {
					instance.flier.getLogger().warning("Could not update messages.yml: " + e.getMessage());
				}
			}
		}
		instance.lang = instance.flier.getConfig().getString("language", "en");
		ConfigurationSection section = YamlConfiguration.loadConfiguration(file).getConfigurationSection(instance.lang);
		if (section == null) {
			throw new LoadingException(String.format("Language '%s' is not defined in messages.yml.", instance.lang));
		}
		instance.messages = section;
	}
	
	/**
	 * Gets the language used by the CommandSender. In single-language mode
	 * this is always the language from config.yml.
	 * 
	 * @param player
	 *            CommandSender
	 * @return the language used
	 */
	public static String getLanguage(CommandSender player) {
		return instance.lang;
	}

	/**
	 * Returns the message for this player.
	 * 
	 * @param player
	 *            player for whom the message needs to be translated
	 * @param message
	 *            message name
	 * @param variables
	 *            array of variables
	 * @return the message string
	 */
	public static String getMessage(InGamePlayer player, String message, Object... variables) {
		return getMessage(player.getLanguage(), message, variables);
	}
	
	/**
	 * Returns the message for this CommandSender.
	 * 
	 * @param player
	 *            CommandSender for whom the message needs to be translated
	 * @param message
	 *            message name
	 * @param variables
	 *            array of variables
	 * @return the message string
	 */
	public static String getMessage(CommandSender player, String message, Object... variables) {
		return getMessage(getLanguage(player), message, variables);
	}
	
	/**
	 * Returns the message in the specified language. In single-language mode
	 * the lang argument is only used for logging, the configured language is
	 * always used.
	 * 
	 * @param lang
	 *            language name (used for logging only)
	 * @param message
	 *            message name
	 * @param variables
	 *            array of variables
	 * @return the message string
	 */
	public static String getMessage(String lang, String message, Object... variables) {
		if (instance.messages == null) {
			instance.flier.getLogger().warning("LangManager used before it was reloaded.");
			return "";
		}
		String string = instance.messages.getString(message);
		if (string == null) {
			instance.flier.getLogger()
					.warning(String.format("Message '%s' in language '%s' is not defined.", message, lang));
			return "";
		}
		for (int i = 0; i < variables.length; i++) {
			String value = (variables[i] instanceof Float || variables[i] instanceof Double) ?
					String.format("%.0f", variables[i]) :
					variables[i].toString();
			string = string.replace(String.format("{%d}", i + 1), value);
		}
		return string.replace('&', ChatColor.COLOR_CHAR);
	}

	/**
	 * Sends specified message to specified player, optionally inserting
	 * variables.
	 * 
	 * @param player
	 *            the player to whom the message will be sent
	 * @param message
	 *            message name
	 * @param variables
	 *            array of variables
	 */
	public static void sendMessage(InGamePlayer player, String message, Object... variables) {
		send(player.getPlayer(), getMessage(player, message, variables));
	}
	
	/**
	 * Sends specified message to specified CommandSender, optionally inserting
	 * variables.
	 * 
	 * @param player
	 *            the CommandSender to whom the message will be sent
	 * @param message
	 *            message name
	 * @param variables
	 *            array of variables
	 */
	public static void sendMessage(CommandSender player, String message, Object... variables) {
		send(player, getMessage(player, message, variables));
	}
	
	private static void send(CommandSender player, String translated) {
		if (translated != null && !translated.isEmpty()) {
			player.sendMessage(translated);
		}
	}

}
