package org.hw.struct;


import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/26.
 * 图中的点
 */
public class GraphPoint<T> {
    public final T p;
    public ArrayList<GraphPoint<T>> parents = new ArrayList<>() ;
    public ArrayList<GraphPoint<T>> children = new ArrayList<>();
    public int index = Integer.MAX_VALUE;

    public GraphPoint(T p) {
        this.p = p;
    }

}
