package org.hw.mdim;

import org.hw.struct.GraphPoint;
import org.hw.struct.MultiDim;
import org.hw.util.ReadData;

import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/25.
 * 测试
 */
public class Test {
    public static void main(String[] args) {
        String fileName = "inde_8.txt";

        Wise wise = new PointWise();

        int k = 4;
        boolean isOutput = false;
        int dim = 4;

        test(fileName, wise, k, isOutput, dim);
    }

    public static void test(String fileName, Wise wise, int k, boolean isOutput, int dim) {
        LayerMD layer = new LayerMD();
        ArrayList<MultiDim> ps = ReadData.readData(fileName,dim);
        long start = System.nanoTime();
        layer.getKSkylineLayers(ps,ps.size());

        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);
        System.out.println(wise.getClass().toString());
        ArrayList<ArrayList<GraphPoint<MultiDim>>> gskylines = wise.GSkyline(layers, k);
        long end = System.nanoTime();
        System.out.println("cost time: " + ((end-start)/1000));
        if (isOutput) {
            System.out.println("------------");
            for (ArrayList<GraphPoint<MultiDim>> gskyline : gskylines) {
                for (GraphPoint<MultiDim> graphPoint : gskyline) {
                    System.out.print(graphPoint.index + " # " + graphPoint.p.layer + " " );
                    for (float ele : graphPoint.p.eles) {
                        System.out.print(ele + " ");
                    }
                    System.out.println();
                }
                System.out.println("------------");
            }
        }

//        System.out.println("G-Skyline size: "+gskylines.size());
    }

}
