package me.lucyy.common.format;

public interface FormatPattern {
	default String process(String in) {
		return process(in, null);
	}

	String process(String in, String formattersOverride);
}
