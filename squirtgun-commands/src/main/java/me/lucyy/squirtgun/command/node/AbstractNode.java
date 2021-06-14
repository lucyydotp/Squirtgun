package me.lucyy.squirtgun.command.node;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.platform.PermissionHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNode<T extends PermissionHolder> implements CommandNode<T> {

    private final String name;
    private final String description;
    private final @Nullable String permission;

    protected AbstractNode(@NotNull String name, @NotNull String description, @Nullable String permission) {
        Preconditions.checkNotNull(name, "Name must not be null");
        Preconditions.checkNotNull(description, "Description must not be null");
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public @Nullable String getPermission() {
        return permission;
    }
}
