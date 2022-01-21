package net.lucypoulton.squirtgun.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.lucypoulton.squirtgun.command.PermissionHolder;
import net.lucypoulton.squirtgun.format.TextConsumer;
import net.lucypoulton.squirtgun.format.node.TextNode;
import net.lucypoulton.squirtgun.format.serialiser.MarkdownSerialiser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordUser implements PermissionHolder, TextConsumer {

    private final JDA jda;
    private final long guildId, id;

    public DiscordUser(Member member) {
        jda = member.getJDA();
        guildId = member.getGuild().getIdLong();
        id = member.getIdLong();
    }

    public DiscordUser(JDA jda, long guildId, long id) {
        this.jda = jda;
        this.guildId = guildId;
        this.id = id;

        getMember();
    }

    @NotNull
    public Member getMember() {
        //noinspection ConstantConditions - an npe is thrown regardless, the source is not that important
        return Objects.requireNonNull(jda.getGuildById(guildId).getMemberById(id));
    }

    @NotNull
    public User getUser() {
        return Objects.requireNonNull(jda.getUserById(id));
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {

        if (!permission.startsWith("discord")) return false;
        String[] split = permission.split("\\.");
        Member member = getMember();

        if (split.length == 1) return true;
        if (split.length != 3) return false;

        return switch (split[1]) {
            case "role" -> member.getRoles().stream().anyMatch(x -> x.getId().equals(split[2]));
            case "guild" -> member.getUser().getMutualGuilds().stream().anyMatch(x -> x.getId().equals(split[2]));
            case "perm" -> {
                long perms = Long.parseLong(split[2]);
                yield member.hasPermission(Permission.getPermissions(perms));
            }
            default -> false;
        };
    }

    @Override
    public void send(@NotNull TextNode node) {
        User user = getMember().getUser();
        user.openPrivateChannel().queue(chan ->
                chan.sendMessage(MarkdownSerialiser.markdown().serialise(node)).queue()
        );
    }
}
