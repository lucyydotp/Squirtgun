package net.lucypoulton.squirtgun.format.colour;

import java.util.HashMap;
import java.util.Map;

/**
 * A gradient that fades the red, green and blue components of two colours.
 */
public class RgbGradient extends Gradient {

    public RgbGradient(Colour one, Colour two) {
        super(one, two);
    }

    @Override
    public Map<String, Colour> interpolate(String content) {
        float[] reds = lerp(one().red(), two().red(), content.length());
        float[] greens = lerp(one().green(), two().green(), content.length());
        float[] blues = lerp(one().blue(), two().blue(), content.length());

        String[] split = content.split("");

        Map<String, Colour> out = new HashMap<>();
        for (int i = 0; i < content.length(); i++) {
            out.put(split[i],
                    new Colour(
                            Math.round(reds[i]),
                            Math.round(greens[i]),
                            Math.round(blues[i])
                    )
            );
        }
        return out;
    }
}
