package net.lucypoulton.squirtgun.format.colour;

/**
 * A gradient that fades smoothly between two colours.
 */
public abstract class Gradient implements Colourful {

    // todo - assert correct range of values
    static float[] lerp(int one, int two, int steps) {
        float step = (two - one) / (float) steps;
        float[] out = new float[steps];
        out[0] = one;
        for (int i = 1; i < steps; i++) {
            out[i] = out[i - 1] + step;
        }
        return out;
    }

    private final Colour one, two;

    public Colour one() {
        return one;
    };
    Colour two() {
        return two;
    };

    protected Gradient(Colour one, Colour two) {
        this.one = one;
        this.two = two;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Gradient grad &&
                grad.one() == one() &&
                grad.two() == two();
    }
}
