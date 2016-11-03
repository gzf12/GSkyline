package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gzf on 2016/10/27.
 */
public class PointWise extends Wise{

    @Override
    public ArrayList<ArrayList<GraphPoint<TwoDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<TwoDim>>> dsg, int k) {
        // preprogressing
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

        for (int i = 0; i < dgl.size(); i++) dgl.get(i).index = i;
        System.out.println("already get group size "+ resultGroups.size()+ " points");
        System.out.println("after preprocessing, there are "+ dgl.size()+ " points");
        // 预处理之后所有的元素
//        for (GraphPoint<TwoDim> graphPoint : dgl) {
//            System.out.println(graphPoint.index +  " "+ graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y + " ");
//        }

        HashSet<GraphPoint<TwoDim>> skylineGPoints = new HashSet<>();
        skylineGPoints.addAll(dsg.get(0));
        System.out.println("skyline points (first layer) number: "+ skylineGPoints.size());

//----------------------
        ArrayList<ArrayList<ArrayList<GraphPoint<TwoDim>>>> gskyline = new ArrayList<>(k+1);
        HashSet<GraphPoint<TwoDim>> childrenSet = null;
//
        // 1.
        ArrayList<ArrayList<GraphPoint<TwoDim>>> levelFirst = new ArrayList<>();
        for (GraphPoint<TwoDim> graphPoint : dsg.get(0)) {
            ArrayList<GraphPoint<TwoDim>> group = new ArrayList<>();
            group.add(graphPoint);
            levelFirst.add(group);
        }
        gskyline.add(levelFirst);
        // 2. layers

        for (int i = 1; i < k; i++) {
            gskyline.add(new ArrayList<>());
            //3. group
            ArrayList<ArrayList<GraphPoint<TwoDim>>> groups = gskyline.get(i - 1);
            for (ArrayList<GraphPoint<TwoDim>> group : groups) {
                //4. graph point
                childrenSet = new HashSet<>();
                for (GraphPoint<TwoDim> graphPoint : group) {
                    //5.
                    childrenSet.addAll(graphPoint.children);
                }
                // 6 - 14
                int tail = 0;
                if (group.size() > 0) tail = group.get(group.size()-1).index + 1;

                for (int j = tail; j < dgl.size(); j++) {
                    //7.- 10
                    GraphPoint<TwoDim> graphPoint = dgl.get(j);
                    if (!childrenSet.contains(graphPoint) && !skylineGPoints.contains(graphPoint)) continue;
                    if (graphPoint.p.layer - group.get(group.size()-1).p.layer >= 2) continue;
                    ArrayList<GraphPoint<TwoDim>> newGroup = new ArrayList<>();
                    newGroup.addAll(group);
                    newGroup.add(graphPoint);
                    // 13 -14
                    if (isGSkyline(newGroup,i + 1)) gskyline.get(i).add(newGroup);
                }
            }
        }
//        resultGroups.addAll(gskyline.get(k-1));
        resultSize += gskyline.get(k-1).size();
        System.out.println("G-Skyline siez: " + resultSize);
        return resultGroups;


    }

    private static boolean isGSkyline(ArrayList<GraphPoint<TwoDim>> group, int gk){
        Set gskyline = new HashSet<GraphPoint<TwoDim>>();
        for (GraphPoint<TwoDim> graphPoint : group) {
            gskyline.addAll(graphPoint.parents);
            gskyline.add(graphPoint);
        }
        return gskyline.size() == gk;
    }

    public static void output(ArrayList<ArrayList<GraphPoint<TwoDim>>> resultGroups, GraphPoint<TwoDim> graphPoint){
        ArrayList<GraphPoint<TwoDim>> group = new ArrayList<>();
        group.addAll(graphPoint.parents);
        group.add(graphPoint);
        resultGroups.add(group);
//        outputG(group);
    }
    public static void outputG(ArrayList<GraphPoint<TwoDim>> group){
        for (GraphPoint<TwoDim> graphPoint : group) {
            System.out.print(graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y + " ");
        }
        System.out.println("#");

    }

    // collection 删除元素比较麻烦。
    private static void deletGraphPoint(ArrayList<ArrayList<GraphPoint<TwoDim>>> dsg, GraphPoint<TwoDim> graphPoint){
        for (GraphPoint<TwoDim> parent : graphPoint.parents) {
            parent.children.remove(graphPoint);
        }
        for (GraphPoint<TwoDim> child : graphPoint.children) {
            deletGraphPoint(dsg,child);
        }
        dsg.get(graphPoint.p.layer-1).remove(graphPoint);
        System.out.println(graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y);
    }
    public static void main(String[] args) {
        Layer2D layer = new Layer2D();
        ArrayList<TwoDim> ps = ReadData.read2DData("hotel.txt");
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;
        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);

        PointWise pointWise = new PointWise();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> gskylines = pointWise.GSkyline(layers, k);
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
