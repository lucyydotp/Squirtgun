/*
 * Copyright © 2021 Lucy Poulton
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
 */

package me.lucyy.squirtgun.fabric;

import com.google.common.collect.Lists;
import me.lucyy.squirtgun.fabric.task.FabricTaskScheduler;
import me.lucyy.squirtgun.platform.AuthMode;
import me.lucyy.squirtgun.platform.EventListener;
import me.lucyy.squirtgun.platform.Platform;
import me.lucyy.squirtgun.platform.audience.SquirtgunUser;
import me.lucyy.squirtgun.plugin.SquirtgunPlugin;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Squirtgun Platform implementation for the Fabric mod loader.
 */
public class FabricPlatform implements Platform {

	private static final Logger LOGGER = Logger.getLogger(FabricPlatform.class.getSimpleName());
	private static final List<String> PROXY_BRIDGING_MODS = List.of("fabricproxy", "fabricproxy-lite");

	private MinecraftDedicatedServer server;
	private FabricServerAudiences audiences;
	private List<FabricPlayer> onlinePlayers = List.of();  // default to empty list until server has started
	private final FabricTaskScheduler taskScheduler;
	private final FabricConsoleWrapper consoleWrapper;
	private final FabricListenerAdapter listenerAdapter = new FabricListenerAdapter();

	/**
	 * Create the Squirtgun's Fabric "platform" for the server.
	 *
	 * <p>The server instance passed <b>must</b> be a {@link MinecraftDedicatedServer}.</p>
	 *
	 * @param server server instance to work with
	 */
	public FabricPlatform(final @NotNull MinecraftServer server) {
		this.server = validateState(MinecraftDedicatedServer.class::isInstance, server, MinecraftDedicatedServer.class::cast, "Squirtgun cannot be used on the client!");
		this.audiences = FabricServerAudiences.of(server);
		this.taskScheduler = new FabricTaskScheduler(this);
		this.consoleWrapper = new FabricConsoleWrapper(this);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::serverStopping);
		ServerLifecycleEvents.SERVER_STARTED.register(this::serverStarted);
	}

	private void serverStarted(final MinecraftServer server) {
		this.onlinePlayers = Lists.transform(getServer().getPlayerManager().getPlayerList(), this::asFabricPlayerOrNull);
	}

	private void serverStopping(final MinecraftServer server) {
		this.taskScheduler.shutdown();
		this.server = null;
		this.audiences = null;
	}

	/**
	 * Get the server instance this platform is working with.
	 *
	 * @return the server instance
	 */
	public MinecraftDedicatedServer getServer() {
		return validateState(Objects::nonNull, this.server, "Cannot access the server without a server running");
	}

	/**
	 * Get Adventure's audience provider for this server.
	 *
	 * @return the server audience provider
	 */
	public FabricServerAudiences getAudienceProvider() {
		return validateState(Objects::nonNull, this.audiences, "Cannot access the audience provider without a server running");
	}

	@Override
	public String name() {
		return "Fabric";
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public void log(final Component component) {
		getAudienceProvider().console().sendMessage(component);
	}

	@Override
	public AuthMode getAuthMode() {
		if (PROXY_BRIDGING_MODS.stream().anyMatch(FabricLoader.getInstance()::isModLoaded)) {
			return AuthMode.BUNGEE;
		} else {
			return getServer().getProperties().onlineMode ? AuthMode.ONLINE : AuthMode.OFFLINE;
		}
	}

	@Override
	public FabricTaskScheduler getTaskScheduler() {
		return this.taskScheduler;
	}

	@Override
	public void registerEventListener(final EventListener listener) {
		this.listenerAdapter.addListener(listener);
	}

	@Override
	public void unregisterEventListener(final EventListener listener) {
		this.listenerAdapter.removeListener(listener);
	}

	/**
	 *
	 *
	 * @param commandSource {@link ServerCommandSource} to adapt
	 * @return corresponding {@link SquirtgunUser}
	 */
	public SquirtgunUser fromCommandSource(final ServerCommandSource commandSource) {
		final var entity = commandSource.getEntity();
		if (entity instanceof ServerPlayerEntity) {
			return getPlayer((ServerPlayerEntity) entity);
		} else {
			return getConsole();
		}
	}

	@Override
	public FabricConsoleWrapper getConsole() {
		return this.consoleWrapper;
	}

	@Override
	public FabricPlayer getPlayer(final UUID uuid) {
		return asFabricPlayerOrNull(getServer().getPlayerManager().getPlayer(uuid));
	}

	@Override
	public @Nullable FabricPlayer getPlayer(final String name) {
		return asFabricPlayerOrNull(getServer().getPlayerManager().getPlayer(name));
	}

	/**
	 *
	 * @param player player entity to wrap
	 * @return Squirtgun's Fabric player wrapper
	 */
	public FabricPlayer getPlayer(final ServerPlayerEntity player) {
		return asFabricPlayerOrNull(player);
	}

	@Override
	public List<FabricPlayer> getOnlinePlayers() {
		return this.onlinePlayers;
	}

	@Override
	public Path getConfigPath(final SquirtgunPlugin<?> plugin) {
		return FabricLoader.getInstance().getConfigDir().resolve(plugin.getPluginName());
	}

	private FabricPlayer asFabricPlayerOrNull(final ServerPlayerEntity player) {
		return player == null ? null : new FabricPlayer(player, getAudienceProvider().audience(player));
	}

	private <T> T validateState(final Predicate<T> validation, final T value, final String message) {
		return validateState(validation, value, Function.identity(), message);
	}

	private <T, U> U validateState(final Predicate<T> validation, final T value, final Function<T, U> post, final String message) {
		if (validation.test(value)) {
			return post.apply(value);
		} else {
			throw new IllegalStateException(message);
		}
	}
}
