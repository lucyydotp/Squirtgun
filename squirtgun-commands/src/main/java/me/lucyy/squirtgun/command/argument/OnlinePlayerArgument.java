package me.lucyy.squirtgun.command.argument;

import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.SquirtgunPlayer;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class OnlinePlayerArgument extends AbstractArgument<SquirtgunPlayer> {
	private final Platform platform;

	public OnlinePlayerArgument(String name, String description, boolean isOptional, Platform platform) {
		super(name, description, isOptional);
		this.platform = platform;
	}

	@Override
	public SquirtgunPlayer getValue(Queue<String> args) {
		return null;
	}

	@Override
	public @Nullable List<String> tabComplete(Queue<String> args) {
		String name = args.peek();
		return name == null ? null : platform.getOnlinePlayers()
				.stream()
				.map(SquirtgunPlayer::getUsername)
				.filter(username -> username.startsWith(name))
				.collect(Collectors.toList());
	}
}
