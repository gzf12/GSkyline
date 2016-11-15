package org.hw.main;

import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;


public class PointWiseDepth extends Wise{

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

        HashSet<GraphPoint<TwoDim>> skylineGPoints = new HashSet<>();
        skylineGPoints.addAll(dsg.get(0));
        System.out.println("skyline points (first layer) number: "+ skylineGPoints.size());

        HashSet<GraphPoint<TwoDim>> childrenSet = new HashSet<GraphPoint<TwoDim>>();
        Stack<GraphPoint<TwoDim>> points = new Stack<GraphPoint<TwoDim>>();
        
        for (GraphPoint<TwoDim> skylinePoint : dsg.get(0)) {
			points.push(skylinePoint);
			int tail = skylinePoint.index + 1;
			while (points.size() != 0) {
				childrenSet.clear();
                for (GraphPoint<TwoDim> graphPoint : points) {
                    childrenSet.addAll(graphPoint.children);
                }

                for (int j = tail; j < dgl.size(); j++) {
                    GraphPoint<TwoDim> graphPoint = dgl.get(j);
                    if (!childrenSet.contains(graphPoint) && !skylineGPoints.contains(graphPoint)) continue;
                    if (graphPoint.p.layer - points.lastElement().p.layer >= 2) continue;
                    ArrayList<GraphPoint<TwoDim>> newGroup = new ArrayList<>();
                    newGroup.addAll(points);
                    newGroup.add(graphPoint);
                    if (isGSkyline(newGroup,points.size() + 1)) {
                    	if (points.size() + 1 == k) {
                    		/*for (GraphPoint<TwoDim> GPoint : newGroup) {
                        		System.out.print(GPoint.index + ",");
                			}
                        	System.out.print("||");*/
							resultSize++;
						} else {
							points.push(graphPoint);
							childrenSet.addAll(graphPoint.children);
						}
                    }
                }
                tail = points.lastElement().index + 1;
                points.pop();
			}
		}
        //System.out.println("");
        System.out.println("G-Skyline size: " + resultSize);
        return resultGroups;


    }

    private static boolean isGSkyline(ArrayList<GraphPoint<TwoDim>> group, int gk){
        Set<GraphPoint<TwoDim>> gskyline = new HashSet<GraphPoint<TwoDim>>();
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
