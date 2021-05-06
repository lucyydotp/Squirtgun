package me.lucyy.squirtgun;

import com.google.common.base.Preconditions;
import me.lucyy.squirtgun.platform.Platform;

public class CommonLibVersion {
	public static final String VERSION = "@VERSION@" + getAuthMode();

	private static String getAuthMode() {
		Preconditions.checkNotNull(Platform.getInstance(), "Platform must be initialised before accessing version");
		return Platform.getInstance().getAuthMode().toString();
	}
}
