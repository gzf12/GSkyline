package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.*;

/**
 * Created by gzf on 2016/10/27.
 */
public class GroupWisePlus extends Wise{

    public static void output(ArrayList<ArrayList<GraphPoint<TwoDim>>> resultGroups, GraphPoint<TwoDim> graphPoint){
        ArrayList<GraphPoint<TwoDim>> group = new ArrayList<>();
        group.addAll(graphPoint.parents);
        group.add(graphPoint);
        resultGroups.add(group);
//        outputG(group);
    }
    public static void output(ArrayList<ArrayList<GraphPoint<TwoDim>>> resultGroups, HashSet<GraphPoint<TwoDim>> groupSet){
        ArrayList<GraphPoint<TwoDim>> group = new ArrayList<>(groupSet);
        resultGroups.add(group);
//        outputG(group);
    }
    public static void outputG(ArrayList<GraphPoint<TwoDim>> group){
        for (GraphPoint<TwoDim> graphPoint : group) {
            System.out.print(graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y + " ");
        }
        System.out.println("#");

    }

    public static boolean isGSkyline(ArrayList<GraphPoint<TwoDim>> group, int gk){
        Set gskyline = new HashSet<GraphPoint<TwoDim>>();
        for (GraphPoint<TwoDim> graphPoint : group) {
            gskyline.addAll(graphPoint.parents);
            gskyline.add(graphPoint);
        }
        if (gskyline.size() == gk) return true;
        return false;
    }

    @Override
    public ArrayList<ArrayList<GraphPoint<TwoDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<TwoDim>>> dsg, int k) {
        ArrayList<ArrayList<GraphPoint<TwoDim>>> resultGroups = new ArrayList<>();
        int resultSize = 0;
        ArrayList<GraphPoint<TwoDim>> dgl = new ArrayList<>();
        for (ArrayList<GraphPoint<TwoDim>> graphPoints : dsg) {
            for (GraphPoint<TwoDim> graphPoint : graphPoints) {
                if (graphPoint.parents.size() + 1 > k) {
                } else if (graphPoint.parents.size() + 1 == k) {
//                    output(resultGroups, graphPoint);
                    resultSize++;
                } else {
                    dgl.add(graphPoint);
                }
            }
        }

        Collections.reverse(dgl);
        for (int i = 0; i < dgl.size(); i++) dgl.get(i).index = i;
        System.out.println("after preprocessing, there are "+ dgl.size()+ " points");
        System.out.println("skyline points (first layer) number: "+ dsg.get(0).size());
//        for (GraphPoint<TwoDim> graphPoint : dgl) {
//            System.out.println(graphPoint.index +  " "+ graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y + " ");
//        }

//        ArrayList<ArrayList<ArrayList<GraphPoint<TwoDim>>>> gskyline = new ArrayList<>(k+1);
//        for (int i = 0; i < k; i++) {
//            gskyline.add(new ArrayList<>());
//        }
        for (int i = 0; i < dgl.size(); i++) {
            HashSet<GraphPoint<TwoDim>> group = new HashSet<>();
            for (int j = i; j < dgl.size(); j++){
                GraphPoint<TwoDim> graphPoint = dgl.get(j);
                group.add(graphPoint);
                group.addAll(graphPoint.parents);
            }
            if (group.size() == k) {
//                output(resultGroups,group);
                resultSize++;
                break;
            } else if (group.size() < k) break;
            ArrayList<GraphPoint<TwoDim>> g = new ArrayList<>();
            g.add(dgl.get(i));
//            System.out.println("one unit " + g.get(0).index);
//            gskyline.get(0).add(g);
            //8
            int j = 2;
            HashSet<GraphPoint<TwoDim>> parentsSet = null;
            //9
            Queue<ArrayList<GraphPoint<TwoDim>>> queue = new LinkedList<>();
            queue.offer(g);
            while (!queue.isEmpty()) {
                ArrayList<GraphPoint<TwoDim>> graphPoints = queue.poll();
//                for (GraphPoint<TwoDim> graphPoint : graphPoints) {
//                    System.out.print(graphPoint.index + " ");
//                }
//                System.out.println();
                parentsSet = new HashSet<>();
                //11 - 12
                for (GraphPoint<TwoDim> graphPoint : graphPoints) {
                    parentsSet.addAll(graphPoint.parents);
                }
                // 13 - 16
                int tail = graphPoints.get(graphPoints.size()-1).index + 1;
                for (int t = tail; t < dgl.size(); t++) {
                    GraphPoint<TwoDim> graphPoint = dgl.get(t);
                    if (parentsSet.contains(graphPoint)) continue;
                    // 构造一个set
                    HashSet<GraphPoint<TwoDim>> groupSet = new HashSet();
                    groupSet.addAll(graphPoints);
                    for (GraphPoint<TwoDim> point : graphPoints) {
                        groupSet.addAll(point.parents);
                    }
                    groupSet.add(graphPoint);
                    groupSet.addAll(graphPoint.parents);

                    if (groupSet.size() == k){
//                        output(resultGroups, groupSet);
                        resultSize++;
                    }
                    if (groupSet.size() < k) {
                        ArrayList<GraphPoint<TwoDim>> nextGroup = new ArrayList<>();
                        nextGroup.addAll(graphPoints);
                        nextGroup.add(graphPoint);
                        queue.offer(nextGroup);
                    }
                }
            }
//            while (g.size()>0) {
//                //10
//                for (ArrayList<GraphPoint<TwoDim>> graphPoints : gskyline.get(j - 1)) {
//                    parentsSet = new HashSet<>();
//                    //11 - 12
//                    for (GraphPoint<TwoDim> graphPoint : graphPoints) {
//                        parentsSet.addAll(graphPoint.parents);
//                    }
//                    // 13 - 16
//                    int tail = graphPoints.get(graphPoints.size()-1).index + 1;
//                    for (int t = tail; t < dgl.size(); t++) {
//                        if (parentsSet.contains(dgl.get(t))) continue;
//                        ArrayList<GraphPoint<TwoDim>> tempg = new ArrayList<>();
//                        tempg.add(dgl.get(t));
//                        tempg.addAll(graphPoints);
//                        gskyline.get(j).add(tempg);
//                    }
//
//                }
//
//
//            }

        }
        System.out.println("G-skyline size: " + resultSize);
        return resultGroups;
    }

    public static void main(String[] args) {
        Layer2D layer = new Layer2D();
        ArrayList<TwoDim> ps = ReadData.read2DData("hotel.txt");
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;
        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);

//        for (GraphPoint<TwoDim> graphPoint : layers.get(0)) {
//            System.out.println( " # " + graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y);
//        }
//        System.out.println(layers.get(0).size());

        GroupWisePlus groupWisePlus = new GroupWisePlus();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> gskylines = groupWisePlus.GSkyline(layers, k);
        System.out.println("------------");
        for (ArrayList<GraphPoint<TwoDim>> gskyline : gskylines) {
            for (GraphPoint<TwoDim> graphPoint : gskyline) {
                System.out.println(graphPoint.index + " # " + graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y);
            }
            System.out.println("------------");
        }
        System.out.println(gskylines.size());
    }
}
