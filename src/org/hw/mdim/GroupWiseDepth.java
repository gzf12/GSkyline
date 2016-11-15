package org.hw.mdim;

import org.hw.struct.GraphPoint;
import org.hw.struct.MultiDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class GroupWiseDepth extends Wise{

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
        return gskyline.size() == gk;
    }

    public ArrayList<ArrayList<GraphPoint<MultiDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<MultiDim>>> dsg, int k) {
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
        System.out.println("after preprocessing, there are "+ dgl.size()+ " points");
        System.out.println("skyline points (first layer) number: "+ dsg.get(0).size());
//        for (GraphPoint<MultiDim> graphPoint : dgl) {
//            for (float ele : graphPoint.p.eles) {
//                System.out.print(ele + " " );
//            }
//            System.out.println(graphPoint.p.layer);
//        }

        HashSet<GraphPoint<MultiDim>> childrenSet = new HashSet<GraphPoint<MultiDim>>();
        Stack<GraphPoint<MultiDim>> points = new Stack<GraphPoint<MultiDim>>();
        
        for (GraphPoint<MultiDim> firstLayerPoint : dgl) {
			points.push(firstLayerPoint);
			int tail = firstLayerPoint.index + 1;
			while (points.size() != 0) {
				childrenSet.clear();
                for (GraphPoint<MultiDim> graphPoint : points) {
                    childrenSet.addAll(graphPoint.children);
                }

                for (int j = tail; j < dgl.size(); j++) {
                    GraphPoint<MultiDim> graphPoint = dgl.get(j);
                    if (childrenSet.contains(graphPoint)) continue;
                    HashSet<GraphPoint<MultiDim>> newGroup = new HashSet<>();
                    newGroup.addAll(points);
                    for (GraphPoint<MultiDim> point : points) {
                        newGroup.addAll(point.parents);
                    }
                    newGroup.add(graphPoint);
                    newGroup.addAll(graphPoint.parents);
                	if (newGroup.size() == k) {
                		/*for (GraphPoint<MultiDim> GPoint : newGroup) {
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
        LayerMD layer = new LayerMD();
        ArrayList<MultiDim> ps = ReadData.readData("hotel_4.txt", 4);
        layer.getKSkylineLayers(ps,ps.size());

        int k = 4;
        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k);

        GroupWise groupWise = new GroupWise();
        ArrayList<ArrayList<GraphPoint<MultiDim>>> gskylines = groupWise.GSkyline(layers, k);
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

