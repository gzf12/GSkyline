package org.hw.mdim;

import org.hw.struct.GraphPoint;
import org.hw.struct.MultiDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by gzf on 2016/10/27.
 */
public class PointWise extends Wise{
    public ArrayList<ArrayList<GraphPoint<MultiDim>>> testPreprocessing(ArrayList<ArrayList<GraphPoint<MultiDim>>> dsg, int k) {
        // preprogressing
        ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups = new ArrayList<>();

        ArrayList<GraphPoint<MultiDim>> dgl = new ArrayList<>();
        for (ArrayList<GraphPoint<MultiDim>> graphPoints : dsg) {
            for (GraphPoint<MultiDim> graphPoint : graphPoints) {
                if (graphPoint.parents.size() + 1 > k) {
                } else if (graphPoint.parents.size() + 1 == k) {
                    output(resultGroups, graphPoint);
                } else {
                    dgl.add(graphPoint);
                }
            }
        }

        for (int i = 0; i < dgl.size(); i++) dgl.get(i).index = i;
        for (GraphPoint<MultiDim> graphPoint : dgl) {
            for (float ele : graphPoint.p.eles) {
                System.out.print(ele + " " );
            }
            System.out.println(graphPoint.p.layer);
        }
        return resultGroups;
    }

    public ArrayList<ArrayList<GraphPoint<MultiDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<MultiDim>>> dsg, int k) {
        // preprogressing
        ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups = new ArrayList<>();
        int resultSize = 0;
        ArrayList<GraphPoint<MultiDim>> dgl = new ArrayList<>();
        for (ArrayList<GraphPoint<MultiDim>> graphPoints : dsg) {
            for (GraphPoint<MultiDim> graphPoint : graphPoints) {
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
//        for (GraphPoint<MultiDim> graphPoint : dgl) {
//            for (float ele : graphPoint.p.eles) {
//                System.out.print(ele + " " );
//            }
//            System.out.println(graphPoint.p.layer);
//        }
        System.out.println("after preprocessing, there are "+ dgl.size()+ " points");
        System.out.println("skyline points (first layer) number: "+ dsg.get(0).size());
        HashSet<GraphPoint<MultiDim>> skylineGPoints = new HashSet<>();
        skylineGPoints.addAll(dsg.get(0));

//----------------------
        ArrayList<ArrayList<ArrayList<GraphPoint<MultiDim>>>> gskyline = new ArrayList<>(k+1);
        HashSet<GraphPoint<MultiDim>> childrenSet = null;
//
        // 1.
        ArrayList<ArrayList<GraphPoint<MultiDim>>> levelFirst = new ArrayList<>();
        for (GraphPoint<MultiDim> graphPoint : dsg.get(0)) {
            ArrayList<GraphPoint<MultiDim>> group = new ArrayList<>();
            group.add(graphPoint);
            levelFirst.add(group);
        }
        gskyline.add(levelFirst);
        // 2. layers

        for (int i = 1; i < k; i++) {
            gskyline.add(new ArrayList<>());
            //3. group
            ArrayList<ArrayList<GraphPoint<MultiDim>>> groups = gskyline.get(i - 1);
            for (ArrayList<GraphPoint<MultiDim>> group : groups) {
                //4. graph point
                childrenSet = new HashSet<>();
                for (GraphPoint<MultiDim> graphPoint : group) {
                    //5.
                    childrenSet.addAll(graphPoint.children);
                }
                // 6 - 14
                int tail = 0;
                if (group.size() > 0) tail = group.get(group.size()-1).index + 1;

                for (int j = tail; j < dgl.size(); j++) {
                    //7.- 10
                    GraphPoint<MultiDim> graphPoint = dgl.get(j);
                    if (!childrenSet.contains(graphPoint) && !skylineGPoints.contains(graphPoint)) continue;
                    if (graphPoint.p.layer - group.get(group.size()-1).p.layer >= 2) continue;
                    ArrayList<GraphPoint<MultiDim>> newGroup = new ArrayList<>();
                    newGroup.addAll(group);
                    newGroup.add(graphPoint);
                    // 13 -14
                    if (isGSkyline(newGroup,i + 1)) gskyline.get(i).add(newGroup);
                }
            }
        }
//        resultGroups.addAll(gskyline.get(k-1));
        resultSize += gskyline.get(k-1).size();
        System.out.println("G-Skyline size: " + resultSize);
        return resultGroups;
    }

    private static boolean isGSkyline(ArrayList<GraphPoint<MultiDim>> group, int gk){
        Set gskyline = new HashSet<GraphPoint<MultiDim>>();
        for (GraphPoint<MultiDim> graphPoint : group) {
            gskyline.addAll(graphPoint.parents);
            gskyline.add(graphPoint);
        }
        if (gskyline.size() == gk) return true;
        return false;
    }

    private static void output(ArrayList<ArrayList<GraphPoint<MultiDim>>> resultGroups, GraphPoint<MultiDim> graphPoint){
        ArrayList<GraphPoint<MultiDim>> group = new ArrayList<>();
        group.addAll(graphPoint.parents);
        group.add(graphPoint);
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
    public static void main(String[] args) {
        LayerMD layer = new LayerMD();
        ArrayList<MultiDim> ps = ReadData.readData("hotel_4.txt", 4);
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;
        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);

        PointWise pointWise = new PointWise();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> gskylines = pointWise.GSkyline(layers, k);
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
