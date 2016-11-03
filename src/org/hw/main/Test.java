package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/25.
 * 测试
 */
public class Test {
    public static void main(String[] args) {
        String fileName = "anti_2.txt";

        Wise wise = new GroupWisePlus();

        int k = 4;
        boolean isOutput = false;

        test(fileName, wise, k, isOutput);
    }

    public static void test(String fileName, Wise wise, int k, boolean isOutput) {
        Layer2D layer = new Layer2D();
        ArrayList<TwoDim> ps = ReadData.read2DData(fileName);
        long start = System.nanoTime();
        layer.getKSkylineLayers(ps,ps.size());

        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);
        System.out.println(wise.getClass().toString());
        ArrayList<ArrayList<GraphPoint<TwoDim>>> gskylines = wise.GSkyline(layers, k);
        long end = System.nanoTime();
        System.out.println("cost time: " + ((end-start)/1000));
        if (isOutput) {
            System.out.println("------------");
            for (ArrayList<GraphPoint<TwoDim>> gskyline : gskylines) {
                for (GraphPoint<TwoDim> graphPoint : gskyline) {
                    System.out.println(graphPoint.index + " # " + graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y);
                }
                System.out.println("------------");
            }
        }

//        System.out.println("G-Skyline size: "+gskylines.size());
    }

}
