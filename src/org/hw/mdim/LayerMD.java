package org.hw.mdim;

import org.hw.struct.MultiDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by gzf on 2016/10/25.
 * 多维分层
 */
public class LayerMD {

    public ArrayList<MultiDim> getKSkylineLayers(ArrayList<MultiDim> ps, int k){
        // 1.对输入的多维点按照第一维属性进行非降序排序
        ps.sort(new MyComparator());
        // 2.
        ps.get(0).layer = 1;
        // 3.
        int maxLayer = 1;
        // 4.
//        ArrayList<MultiDim> tailPs = new ArrayList<>();
//        for (int i = 0; i < k; i++) tailPs.add(ps.get(0));
        ArrayList<ArrayList<MultiDim>> layers= new ArrayList<>();
        for (int i = 0; i < k; i++) layers.add(new ArrayList<>());
        layers.get(0).add(ps.get(0));
        // 5.
        for (int i = 1; i < ps.size(); i++) {
            MultiDim pui = ps.get(i);
            // 6.- 8.
            int lastDominate = -1;
            boolean cantDominate = false;
            for (MultiDim point : layers.get(0)) {
                if (isDominate(point,pui)) cantDominate = true;
            }
            if (!cantDominate) {
                pui.layer = 1;
                layers.get(0).add(pui);
                continue;
            }
            for (int i1 = 1; i1 < layers.size(); i1++) {
                boolean flag = false;
                for (MultiDim point : layers.get(i1)) {
                    if (isDominate(point,pui)) flag = true;
                }
                if (!flag) {
                    pui.layer = i1+1;
                    layers.get(i1).add(pui);
                    break;
                }
            }
        }
//        System.out.println(layers.get(0).size());
        return ps;
    }

    public static int binarySearch(ArrayList<MultiDim> arr,int maxLayer,MultiDim value) {

        int min =0;
        int max = maxLayer-1;
        while(min<max){
            //采用无符号右移一位，即可以表示除以2
            int mid = (min+max)>>>1;
            MultiDim midValue = arr.get(mid);
            if (!isDominate(midValue,value)){
                max = mid;
            }else if(isDominate(midValue,value)){
                min = mid+1;
            }
        }
//        System.out.println(value.x + " " + value.y + " ## " + min);
        return min;

    }

    private static boolean isDominate(MultiDim p1, MultiDim p2){
        for (int i = 0; i < p1.eles.length; i++) if (p1.eles[i] > p2.eles[i]) return false;
        return true;
    }

    private class MyComparator implements Comparator<MultiDim>{
        @Override
        public int compare(MultiDim p1, MultiDim p2) {
            for (int i = 0; i < p1.eles.length; i++) {
                if (p1.eles[i] == p2.eles[i]) continue;
                else if (p1.eles[i] >= p2.eles[i]) return 1;
                else return -1;
            }
            return -1;
        }
    }

    public static void main(String[] args){

        LayerMD layer = new LayerMD();
        ArrayList<MultiDim> ps = ReadData.readData("inde_4.txt", 4);
        ps = layer.getKSkylineLayers(ps,ps.size());
//        ps.sort(new Comparator<MultiDim>() {
//            @Override
//            public int compare(MultiDim p1, MultiDim p2) {
//                if (p1.eles[0] >= p2.eles[0]) return 1;
//                return -1;
//            }
//        });
        for (int i1 = 0; i1 < ps.size() - 1; i1++) {
            if(ps.get(i1).eles[0] == ps.get(i1+1).eles[0]) System.out.println(i1+"\t" +ps.get(i1).eles[0]+"\t" + ps.get(i1+1).eles[0] + "\t" + ps.get(i1).layer + "\t" + ps.get(i1+1).layer);
        }
        int i = 0;
        for (MultiDim tu : ps) {
//            if(tu.layer != 1) continue;
            System.out.print(i++ + " " );
            for (float ele : tu.eles) {
                System.out.print(ele + " " );
            }
            System.out.println(tu.layer);
        }
        System.out.println("test");


    }
}
