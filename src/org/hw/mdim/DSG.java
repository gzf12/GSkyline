package org.hw.mdim;

import org.hw.struct.GraphPoint;
import org.hw.struct.MultiDim;
import org.hw.util.ReadData;

import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/26.
 * 生成有向图
 */
public class DSG {
    public ArrayList<ArrayList<GraphPoint<MultiDim>>> sortByLayer(ArrayList<MultiDim> ps, int k){
        ArrayList<ArrayList<GraphPoint<MultiDim>>> layers = new ArrayList<>();
        for (int i = 0; i < k; i++) layers.add(new ArrayList<>());
        for (MultiDim p : ps) if (p.layer <= k) layers.get(p.layer-1).add(new GraphPoint<>(p));
        return layers;
    }
    public ArrayList<ArrayList<GraphPoint<MultiDim>>> getKDSG(ArrayList<ArrayList<GraphPoint<MultiDim>>> layers, int k){
        for (int i = 0; i < layers.size(); i++) {
            for (int j = 0; j < layers.get(i).size(); j++) {
                GraphPoint<MultiDim> p = layers.get(i).get(j);
                // 可以将线性搜索改成二分查找
                for (int m = 0; m < i; m++) {
                    for (GraphPoint<MultiDim> gp : layers.get(m)) {
                        if (isDominate(gp.p, p.p)) p.parents.add(gp);
                    }
                }
                for (int m = i+1; m < layers.size(); m++) {
                    for (GraphPoint<MultiDim> gp : layers.get(m)) {
                        if (isDominate(p.p, gp.p)) p.children.add(gp);
                    }
                }
            }
        }
        return layers;
    }


    private static boolean isDominate(MultiDim p1, MultiDim p2){
        for (int i = 0; i < p1.eles.length; i++) if (p1.eles[i] > p2.eles[i]) return false;
        return true;
    }

    public static void main(String[] args) {
        LayerMD layer = new LayerMD();
        ArrayList<MultiDim> ps = ReadData.readData("hotel_4.txt", 4);
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;

        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> layers = dsg.sortByLayer(ps, k);
//        for (ArrayList<GraphPoint<MultiDim>> twoDims : layers) {
//            for (GraphPoint<MultiDim> p : twoDims) {
//                System.out.println(p.p.layer+ " " + p.p.x + " " + p.p.y);
//            }
//        }
        dsg.getKDSG(layers, k);
        int count = 0;
        for (ArrayList<GraphPoint<MultiDim>> twoDims : layers) {
            for (GraphPoint<MultiDim> p : twoDims) {
                System.out.print(count++ + " " );
                for (float ele : p.p.eles) {
                    System.out.print(ele + " " );
                }
                System.out.println(p.p.layer);

                for (GraphPoint<MultiDim> child : p.children) {
                    for (float ele : child.p.eles) {
                        System.out.print(ele + " " );
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
    }
}
