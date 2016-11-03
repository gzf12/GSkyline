package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;

import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/28.
 * Wise方法抽象类
 */
public abstract class Wise {
    public abstract ArrayList<ArrayList<GraphPoint<TwoDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<TwoDim>>> dsg, int k);
}
