package net.lucypoulton.squirtgun.discord;

import net.dv8tion.jda.api.entities.TextChannel;
import net.lucypoulton.squirtgun.format.TextConsumer;
import net.lucypoulton.squirtgun.format.node.TextNode;
import net.lucypoulton.squirtgun.format.serialiser.MarkdownSerialiser;

public class DiscordTextConsumers {

    public static TextConsumer channel(TextChannel channel) {
        return new Channel(channel);
    }

    private static class Channel implements TextConsumer {
        private final TextChannel channel;

        Channel(TextChannel channel) {
            this.channel = channel;
        }

        @Override
        public void send(TextNode node) {
            channel.sendMessage(MarkdownSerialiser.markdown().serialise(node)).queue();
        }
    }
}
