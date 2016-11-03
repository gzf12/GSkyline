package org.hw.mdim;

import org.hw.struct.GraphPoint;
import org.hw.struct.MultiDim;

import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/28.
 * Wise算法
 */
public abstract class Wise {
    public abstract ArrayList<ArrayList<GraphPoint<MultiDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<MultiDim>>> dsg, int k);
}
