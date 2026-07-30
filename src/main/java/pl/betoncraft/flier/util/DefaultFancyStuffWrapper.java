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

import java.time.Duration;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;

import pl.betoncraft.flier.api.core.FancyStuffWrapper;

/**
 * Default implementation of FancyStuffWrapper. Since the migration to Paper,
 * titles, action bar and tab list are handled natively through the Adventure
 * API - no external plugins are required.
 *
 * @author Jakub Sapalski
 */
public class DefaultFancyStuffWrapper implements FancyStuffWrapper {
	
	private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

	public DefaultFancyStuffWrapper() {
	}
	
	@Override
	public void sendTitle(Player player, String title, String sub, int fadeIn, int stay, int fadeOut) {
		if (fadeIn + stay + fadeOut <= 0) {
			fadeIn = 20;
			stay = 100;
			fadeOut = 20;
		}
		Component titleComponent = LEGACY.deserialize(title);
		Component subComponent = sub == null ? Component.empty() : LEGACY.deserialize(sub);
		Times times = Times.times(
				Duration.ofMillis(fadeIn * 50L),
				Duration.ofMillis(stay * 50L),
				Duration.ofMillis(fadeOut * 50L));
		player.showTitle(Title.title(titleComponent, subComponent, times));
	}
	
	@Override
	public void sendActionBar(Player player, String message) {
		player.sendActionBar(LEGACY.deserialize(message));
	}
	
	@Override
	public void setTabList(Player player, String header, String footer) {
		Component headerComponent = header == null ? Component.empty() : LEGACY.deserialize(header);
		Component footerComponent = footer == null ? Component.empty() : LEGACY.deserialize(footer);
		player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
	}
	
	@Override
	public boolean hasTitleHandler() {
		return true;
	}
	
	@Override
	public boolean hasActionBarHandler() {
		return true;
	}
	
	@Override
	public boolean hasTabListHandler() {
		return true;
	}
	
}
