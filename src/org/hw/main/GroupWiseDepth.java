package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class GroupWiseDepth extends Wise{

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
        return gskyline.size() == gk;
    }
    @Override
    public ArrayList<ArrayList<GraphPoint<TwoDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<TwoDim>>> dsg, int k) {
        ArrayList<ArrayList<GraphPoint<TwoDim>>> resultGroups = new ArrayList<>();
        int resultSize = 0;
        ArrayList<GraphPoint<TwoDim>> dgl = new ArrayList<>();
        for (ArrayList<GraphPoint<TwoDim>> graphPoints : dsg) {
            for (GraphPoint<TwoDim> graphPoint : graphPoints) {
                if (graphPoint.parents.size() + 1 > k) continue;
                else if (graphPoint.parents.size() + 1 == k) {
//                    output(resultGroups, graphPoint);
                    resultSize++;
                } else {
                    dgl.add(graphPoint);
                }
            }
        }

        for (int i = 0; i < dgl.size(); i++) dgl.get(i).index = i;
        System.out.println("after preprocessing, there are "+ dgl.size()+ " points");
        System.out.println("skyline points (first layer) number: "+ dsg.get(0).size());
//        for (GraphPoint<TwoDim> graphPoint : dgl) {
//            System.out.println(graphPoint.index +  " "+ graphPoint.p.layer + " " + graphPoint.p.x + " " + graphPoint.p.y + " ");
//        }


        HashSet<GraphPoint<TwoDim>> childrenSet = new HashSet<GraphPoint<TwoDim>>();
        Stack<GraphPoint<TwoDim>> points = new Stack<GraphPoint<TwoDim>>();
        
        for (GraphPoint<TwoDim> firstLayerPoint : dgl) {
			points.push(firstLayerPoint);
			int tail = firstLayerPoint.index + 1;
			while (points.size() != 0) {
				childrenSet.clear();
                for (GraphPoint<TwoDim> graphPoint : points) {
                    childrenSet.addAll(graphPoint.children);
                }

                for (int j = tail; j < dgl.size(); j++) {
                    GraphPoint<TwoDim> graphPoint = dgl.get(j);
                    if (childrenSet.contains(graphPoint)) continue;
                    HashSet<GraphPoint<TwoDim>> newGroup = new HashSet<>();
                    newGroup.addAll(points);
                    for (GraphPoint<TwoDim> point : points) {
                        newGroup.addAll(point.parents);
                    }
                    newGroup.add(graphPoint);
                    newGroup.addAll(graphPoint.parents);
                	if (newGroup.size() == k) {
                		/*for (GraphPoint<TwoDim> GPoint : newGroup) {
                    		System.out.print(GPoint.index + ",");
            			}
                    	System.out.print("||");*/
						resultSize++;
					} else if (newGroup.size() < k){
						points.push(graphPoint);
						childrenSet.addAll(graphPoint.children);
					}
                }
                tail = points.lastElement().index + 1;
                points.pop();
			}
		}
        //System.out.println("");
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

        GroupWise groupWise = new GroupWise();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> gskylines = groupWise.GSkyline(layers, k);
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
