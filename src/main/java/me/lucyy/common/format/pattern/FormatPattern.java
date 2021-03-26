package me.lucyy.common.format.pattern;

public interface FormatPattern {
	default String process(String in) {
		return process(in, null);
	}

	String process(String in, String formattersOverride);
}
