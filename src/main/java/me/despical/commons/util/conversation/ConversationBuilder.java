package me.despical.commons.util.conversation;

import me.despical.commons.util.Strings;
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

	public ConversationBuilder(JavaPlugin plugin) {
		this(plugin, "&cOperation cancelled!", "&cOnly by players!", 30);
	}

	public ConversationBuilder(JavaPlugin plugin, String conversationAbandoned, String onlyPlayers, int timeout) {
		conversationFactory = new ConversationFactory(plugin)
			.withModality(true)
			.withLocalEcho(false)
			.withEscapeSequence("cancel")
			.withTimeout(timeout)
			.addConversationAbandonedListener(listener -> {
				if (listener.gracefulExit()) {
					return;
				}

				listener.getContext().getForWhom().sendRawMessage(Strings.format(conversationAbandoned));

			}).thatExcludesNonPlayersWithMessage(Strings.format(onlyPlayers));
	}

	public ConversationBuilder withPrompt(Prompt prompt) {
		conversationFactory.withFirstPrompt(prompt);
		return this;
	}

	public void buildFor(Conversable conversable) {
		conversationFactory.buildConversation(conversable).begin();
	}
}