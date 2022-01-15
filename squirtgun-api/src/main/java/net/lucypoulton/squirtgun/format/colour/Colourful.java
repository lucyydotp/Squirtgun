package net.lucypoulton.squirtgun.format.colour;

import java.util.Map;

/**
 * A method of colouring text.
 */
@FunctionalInterface
public interface Colourful {
    Map<String, Colour> interpolate(String input);
}
