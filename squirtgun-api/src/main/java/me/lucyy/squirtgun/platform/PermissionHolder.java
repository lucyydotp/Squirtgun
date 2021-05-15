package me.lucyy.squirtgun.platform;

/**
 * An entity capable of holding permissions and executing commands.
 */
public interface PermissionHolder {

	/**
	 * Gets whether the target holds the provided permission.
	 *
	 * @param permission the permission to check
	 * @return if the target holds the given permission
	 */
	boolean hasPermission(String permission);
}
