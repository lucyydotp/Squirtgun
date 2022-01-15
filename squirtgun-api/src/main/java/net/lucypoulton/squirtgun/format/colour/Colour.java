package net.lucypoulton.squirtgun.format.colour;

import java.util.Map;

/**
 * A single RGB colour.
 */
public class Colour implements Colourful {

    private final int red, green, blue;

    public Colour(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int red() {
        return red;
    }

    public int green() {
        return green;
    }

    public int blue() {
        return blue;
    }

    @Override
    public String toString() {
        return String.format("#%2x%2x%2x", red(), green(), blue());
    }

    @Override
    public Map<String, Colour> interpolate(String input) {
        return Map.of(input, this);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Colour col &&
                col.red() == red() &&
                col.green() == green() &&
                col.blue() == blue();
    }
}
