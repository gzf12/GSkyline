package org.hw.main;

import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by gzf on 2016/10/25.
 * 二维分层
 */
public class Layer2D {

    public ArrayList<TwoDim> getKSkylineLayers(ArrayList<TwoDim> ps, int k){
        // 1.对输入的二维点按照第一维属性进行非降序排序
        ps.sort(new MyComparator());
        // 2.
        ps.get(0).layer = 1;
        // 3.
        int maxLayer = 1;
        // 4.
        ArrayList<TwoDim> tailPs = new ArrayList<>();
        for (int i = 0; i < k; i++) tailPs.add(ps.get(0));
        // 5.
        for (int i = 1; i < ps.size(); i++) {
            TwoDim pui = ps.get(i);
            // 6.- 8.
            if (!isDominate(tailPs.get(0),pui)){
                pui.layer = 1;
                tailPs.set(0,pui);
            } // 9. -11.
            else if (isDominate(tailPs.get(maxLayer - 1),pui)) {
                pui.layer = ++maxLayer;
                tailPs.set(maxLayer - 1, pui);
            } else {// 12. - 15.

//                for (int j = 1; j <= maxLayer; j++){
//                    if ( isDominate(tailPs.get(j-1),pui) && !isDominate(tailPs.get(j-1+1),pui) ){
//                        pui.layer = j+1;
//                        break;
//                    }
//                }
                pui.layer = binarySearch(tailPs,maxLayer,pui) + 1;
                tailPs.set(pui.layer - 1,pui);
            }
        }
        return ps;
    }

    private static int binarySearch(ArrayList<TwoDim> arr, int maxLayer, TwoDim value) {

        int min =0;
        int max = maxLayer-1;
        while(min<max){
            //采用无符号右移一位，即可以表示除以2
            int mid = (min+max)>>>1;
            TwoDim midValue = arr.get(mid);
            if (!isDominate(midValue,value)){
                max = mid;
            }else if(isDominate(midValue,value)){
                min = mid+1;
            }
        }
//        System.out.println(value.x + " " + value.y + " ## " + min);
        return min;

    }

    private static boolean isDominate(TwoDim p1, TwoDim p2){
        if ((p1.y - p2.y <= 0) && (p1.x - p2.x < 0)) return true;
        if ((p1.y - p2.y < 0) && (p1.x - p2.x <= 0)) return true;
        return false;
    }

    private class MyComparator implements Comparator<TwoDim>{
        @Override
        public int compare(TwoDim p1, TwoDim p2) {
            if (p1.x > p2.x) return 1;
            else if (p1.x == p2.x){
                if (p1.y > p2.y) {
//                    System.out.println("small");
                    return 1;
                }
                else{
//                    System.out.println("big");
                    return -1;
                }
            }
            else return -1;
        }
    }

    public static void main(String[] args){

        Layer2D layer = new Layer2D();
        ArrayList<TwoDim> ps = ReadData.read2DData("anti_2.txt");
        layer.getKSkylineLayers(ps,ps.size());
//        System.out.println("-------------------");
//        ps.sort((p1, p2) -> {
//            if (p1.x > p2.x) return 1;
//            else if (p1.x == p2.x){
//                if (p1.y > p2.y) {
//                    System.out.println("small " + p1.x);
//                    return 1;
//                }
//                else{
//                    System.out.println("big");
//                    return -1;
//                }
//            }
//            else return -1;
//        });
        for (int i1 = 0; i1 < ps.size() - 1; i1++) {
            if(ps.get(i1).x == ps.get(i1+1).x) System.out.println(i1+" " +ps.get(i1).x+" " + ps.get(i1+1).x + " " + ps.get(i1).layer + " " + ps.get(i1+1).layer);
        }
        int i = 0;
        for (TwoDim tu : ps) {

            System.out.println((i++) + " "+tu.x+" "+tu.y+" ##" + tu.layer);
        }
        System.out.println("test");
    }
}
