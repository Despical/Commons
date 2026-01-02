/*
 * Commons - Box of the common utilities
 * Copyright (C) 2026  Berke Akçen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.despical.commons.util.conversation;

import dev.despical.commons.util.Strings;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * <p>
 * Created at 22.05.2021
 */
public class ConversationBuilder {

	private final ConversationFactory conversationFactory;
	private String cancelMessage = Strings.format("&cOnly by players!");
	private String onlyByPlayersMessage = Strings.format("&cOperation cancelled!");

	public ConversationBuilder(JavaPlugin plugin) {
		this(plugin, 30);
	}

	public ConversationBuilder(JavaPlugin plugin, int timeout) {
		conversationFactory = new ConversationFactory(plugin)
			.withModality(true)
			.withLocalEcho(false)
			.withEscapeSequence("cancel")
			.withTimeout(timeout)
			.addConversationAbandonedListener(listener -> {
				if (listener.gracefulExit()) {
					return;
				}

				listener.getContext().getForWhom().sendRawMessage(onlyByPlayersMessage);

			}).thatExcludesNonPlayersWithMessage(cancelMessage);
	}

	public ConversationBuilder cancelMessage(String message) {
		this.cancelMessage = message;
		return this;
	}

	public ConversationBuilder onlyByPlayersMessage(String message) {
		this.onlyByPlayersMessage = message;
		return this;
	}

	public ConversationBuilder withPrompt(Prompt prompt) {
		conversationFactory.withFirstPrompt(prompt);
		return this;
	}

	public void buildFor(Conversable conversable) {
		conversationFactory.buildConversation(conversable).begin();
	}
}
