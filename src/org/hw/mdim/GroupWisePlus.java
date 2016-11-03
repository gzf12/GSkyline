package org.hw.mdim;

import org.hw.struct.GraphPoint;
import org.hw.struct.MultiDim;
import org.hw.util.ReadData;

import java.util.*;

/**
 * Created by gzf on 2016/10/27.
 */
public class GroupWisePlus extends Wise{

    public static void output(ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups, GraphPoint<MultiDim> graphPoint){
        ArrayList<GraphPoint<MultiDim>> group = new ArrayList<>();
        group.addAll(graphPoint.parents);
        group.add(graphPoint);
        resultGroups.add(group);
//        outputG(group);
    }
    public static void output(ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups, HashSet<GraphPoint<MultiDim>> groupSet){
        ArrayList<GraphPoint<MultiDim>> group = new ArrayList<>(groupSet);
        resultGroups.add(group);
//        outputG(group);
    }
    public static void outputG(ArrayList<GraphPoint<MultiDim>> group){
        for (GraphPoint<MultiDim> graphPoint : group) {
            for (float ele : graphPoint.p.eles) {
                System.out.print(ele + " " );
            }
            System.out.println(graphPoint.p.layer);
        }
        System.out.println("#");

    }

    public static boolean isGSkyline(ArrayList<GraphPoint<MultiDim>> group, int gk){
        Set gskyline = new HashSet<GraphPoint<MultiDim>>();
        for (GraphPoint<MultiDim> graphPoint : group) {
            gskyline.addAll(graphPoint.parents);
            gskyline.add(graphPoint);
        }
        if (gskyline.size() == gk) return true;
        return false;
    }


//    public ArrayList<ArrayList<GraphPoint<MultiDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<MultiDim>>> dsg, int k) {
//        ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups = new ArrayList<>();
//
//        ArrayList<GraphPoint<MultiDim>> dgl = new ArrayList<>();
//        for (ArrayList<GraphPoint<MultiDim>> graphPoints : dsg) {
//            for (GraphPoint<MultiDim> graphPoint : graphPoints) {
//                if (graphPoint.parents.size() + 1 > k) {
//                } else if (graphPoint.parents.size() + 1 == k) {
//                    output(resultGroups, graphPoint);
//                } else {
//                    dgl.add(graphPoint);
//                }
//            }
//        }
//
//        Collections.reverse(dgl);
//        for (int i = 0; i < dgl.size(); i++) dgl.get(i).index = i;
//
//        ArrayList<ArrayList<ArrayList<GraphPoint<MultiDim>>>> gskyline = new ArrayList<ArrayList<ArrayList<GraphPoint<MultiDim>>>>(k+1);
//        for (int i = 0; i < k; i++) {
//            gskyline.add(new ArrayList<>());
//        }
//        for (int i = 0; i < dgl.size(); i++) {
//            HashSet<GraphPoint<MultiDim>> group = new HashSet<>();
//            for (int j = i; j < dgl.size(); j++){
//                GraphPoint<MultiDim> graphPoint = dgl.get(i);
//                group.add(graphPoint);
//                group.addAll(graphPoint.parents);
//            }
//            if (group.size() == k) {
//                output(resultGroups,group);
//                break;
//            } else if (group.size() < k) break;
//            ArrayList<GraphPoint<MultiDim>> g = new ArrayList<>();
//            g.add(dgl.get(i));
//            gskyline.get(0).add(g);
//            //8
//            int j = 2;
//            HashSet<GraphPoint<MultiDim>> parentsSet = null;
//            //9
//            Queue<ArrayList<GraphPoint<MultiDim>>> queue = new LinkedList<>();
//            queue.offer(g);
//            while (!queue.isEmpty()) {
//                ArrayList<GraphPoint<MultiDim>> graphPoints = queue.poll();
//                parentsSet = new HashSet<>();
//                //11 - 12
//                for (GraphPoint<MultiDim> graphPoint : graphPoints) {
//                    parentsSet.addAll(graphPoint.parents);
//                }
//                // 13 - 16
//                int tail = graphPoints.get(graphPoints.size()-1).index + 1;
//                for (int t = tail; t < dgl.size(); t++) {
//                    if (parentsSet.contains(dgl.get(t))) continue;
//                    ArrayList<GraphPoint<MultiDim>> tempg = new ArrayList<>();
//                    tempg.add(dgl.get(t));
//                    tempg.addAll(graphPoints);
//                    HashSet<GraphPoint<MultiDim>> groupSet = new HashSet();
//                    for (GraphPoint<MultiDim> graphPoint : group) {
//                        groupSet.addAll(graphPoint.parents);
//                        groupSet.add(graphPoint);
//                    }
//                    if (groupSet.size() == k){
//                        output(resultGroups, groupSet);
//                    }
//                    if (groupSet.size() < k) {
//                        queue.offer(tempg);
//                    }
//
//                }
//            }
////            while (g.size()>0) {
////                //10
////                for (ArrayList<GraphPoint<MultiDim>> graphPoints : gskyline.get(j - 1)) {
////                    parentsSet = new HashSet<>();
////                    //11 - 12
////                    for (GraphPoint<MultiDim> graphPoint : graphPoints) {
////                        parentsSet.addAll(graphPoint.parents);
////                    }
////                    // 13 - 16
////                    int tail = graphPoints.get(graphPoints.size()-1).index + 1;
////                    for (int t = tail; t < dgl.size(); t++) {
////                        if (parentsSet.contains(dgl.get(t))) continue;
////                        ArrayList<GraphPoint<MultiDim>> tempg = new ArrayList<>();
////                        tempg.add(dgl.get(t));
////                        tempg.addAll(graphPoints);
////                        gskyline.get(j).add(tempg);
////                    }
////
////                }
////
////
////            }
//
//        }
//
//        return resultGroups;
//    }

    @Override
    public ArrayList<ArrayList<GraphPoint<MultiDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<MultiDim>>> dsg, int k) {
        ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups = new ArrayList<>();
        int resultSize = 0;
        ArrayList<GraphPoint<MultiDim>> dgl = new ArrayList<>();
        for (ArrayList<GraphPoint<MultiDim>> graphPoints : dsg) {
            for (GraphPoint<MultiDim> graphPoint : graphPoints) {
                if (graphPoint.parents.size() + 1 > k) {
                } else if (graphPoint.parents.size() + 1 == k) {
//                    output(resultGroups, graphPoint);
                    resultSize ++;
                } else {
                    dgl.add(graphPoint);
                }
            }
        }

        Collections.reverse(dgl);
        for (int i = 0; i < dgl.size(); i++) dgl.get(i).index = i;
        System.out.println("after preprocessing, there are "+ dgl.size()+ " points");
        System.out.println("skyline points (first layer) number: "+ dsg.get(0).size());
//        for (GraphPoint<MultiDim> graphPoint : dgl) {
//            System.out.println(graphPoint.index +  " "+ graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y + " ");
//        }

        for (int i = 0; i < dgl.size(); i++) {
            HashSet<GraphPoint<MultiDim>> group = new HashSet<>();
            for (int j = i; j < dgl.size(); j++){
                GraphPoint<MultiDim> graphPoint = dgl.get(j);
                group.add(graphPoint);
                group.addAll(graphPoint.parents);
            }
            if (group.size() == k) {
//                output(resultGroups,group);
                resultSize++;
                break;
            } else if (group.size() < k) break;
            ArrayList<GraphPoint<MultiDim>> g = new ArrayList<>();
            g.add(dgl.get(i));
//            System.out.println("one unit " + g.get(0).index);

            //8
            int j = 2;
            HashSet<GraphPoint<MultiDim>> parentsSet = null;
            //9
            Queue<ArrayList<GraphPoint<MultiDim>>> queue = new LinkedList<>();
            queue.offer(g);
            while (!queue.isEmpty()) {
                ArrayList<GraphPoint<MultiDim>> graphPoints = queue.poll();
//                for (GraphPoint<MultiDim> graphPoint : graphPoints) {
//                    System.out.print(graphPoint.index + " ");
//                }
//                System.out.println();
                parentsSet = new HashSet<>();
                //11 - 12
                for (GraphPoint<MultiDim> graphPoint : graphPoints) {
                    parentsSet.addAll(graphPoint.parents);
                }
                // 13 - 16
                int tail = graphPoints.get(graphPoints.size()-1).index + 1;
                for (int t = tail; t < dgl.size(); t++) {
                    GraphPoint<MultiDim> graphPoint = dgl.get(t);
                    if (parentsSet.contains(graphPoint)) continue;
                    // 构造一个set
                    HashSet<GraphPoint<MultiDim>> groupSet = new HashSet();
                    groupSet.addAll(graphPoints);
                    for (GraphPoint<MultiDim> point : graphPoints) {
                        groupSet.addAll(point.parents);
                    }
                    groupSet.add(graphPoint);
                    groupSet.addAll(graphPoint.parents);

                    if (groupSet.size() == k){
//                        output(resultGroups, groupSet);
                        resultSize++;
                    }
                    if (groupSet.size() < k) {
                        ArrayList<GraphPoint<MultiDim>> nextGroup = new ArrayList<>();
                        nextGroup.addAll(graphPoints);
                        nextGroup.add(graphPoint);
                        queue.offer(nextGroup);
                    }
                }
            }

        }
        System.out.println("G-skyline size: " + resultSize );
        return resultGroups;
    }

    public static void main(String[] args) {
        LayerMD layer = new LayerMD();
        ArrayList<MultiDim> ps = ReadData.readData("hotel_4.txt", 4);
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;
        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);

        GroupWisePlus groupWisePlus = new GroupWisePlus();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> gskylines = groupWisePlus.GSkyline(layers, k);
        System.out.println("------------");
        int count = 0;
        for (ArrayList<GraphPoint<MultiDim>> gskyline : gskylines) {
            for (GraphPoint<MultiDim> graphPoint : gskyline) {
                System.out.print(count++ + " " );
                for (float ele : graphPoint.p.eles) {
                    System.out.print(ele + " " );
                }
                System.out.println(graphPoint.p.layer);
            }
            System.out.println("------------");
        }
        System.out.println(gskylines.size());
    }
}
