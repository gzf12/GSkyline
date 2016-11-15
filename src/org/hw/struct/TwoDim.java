package org.hw.struct;

/**
 * Created by gzf on 2016/10/25.
 * 二维元素
 */
public class TwoDim {
    public final float x;
    public final float y;
    public int layer = Integer.MAX_VALUE;
    public int label;

    public TwoDim(float x, float y,int label) {
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public TwoDim(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "TwoDim{" +
                "x=" + x +
                ", y=" + y +
                ", layer=" + layer +
                ", label=" + label +
                '}';
    }
}
