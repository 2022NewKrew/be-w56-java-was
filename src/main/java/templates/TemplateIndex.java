package templates;

import util.Checker;

import java.util.Objects;

public class TemplateIndex {
    private final String tag;
    private final int frontOut;
    private final int frontIn;
    private final int rearIn;
    private final int rearOut;

    public TemplateIndex(
            final String tag,
            final int frontOut,
            final int frontIn,
            final int rearIn,
            final int rearOut
    ) {
        Checker.checkString(tag);
        Checker.checkIntMinMax(frontOut, frontIn);
        Checker.checkIntMinMax(frontIn, rearIn);
        Checker.checkIntMinMax(rearIn, rearOut);
        this.tag = Objects.requireNonNull(tag);
        this.frontOut = frontOut;
        this.frontIn = frontIn;
        this.rearIn = rearIn;
        this.rearOut = rearOut;
    }

    public String getTag() {
        return tag;
    }

    public int getFrontOut() {
        return frontOut;
    }

    public int getFrontIn() {
        return frontIn;
    }

    public int getRearIn() {
        return rearIn;
    }

    public int getRearOut() {
        return rearOut;
    }
}
