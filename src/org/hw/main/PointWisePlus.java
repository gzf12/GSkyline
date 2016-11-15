package org.hw.main;

import com.sun.java.accessibility.util.TopLevelWindowListener;
import com.sun.tracing.dtrace.ArgsAttributes;
import org.hw.struct.GraphPoint;
import org.hw.struct.TwoDim;
import org.hw.util.ReadData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by DamonLiu on 2016/11/15.
 */
public class PointWisePlus extends Wise {


    private int k;
    private ArrayList<ArrayList<GraphPoint<TwoDim>>> DSG;
    private ArrayList<ArrayList<GraphPoint<TwoDim>>> simpleDSG;
    private ArrayList<ArrayList<GraphPoint<TwoDim>>> GSkyline = new ArrayList<>();


    public PointWisePlus() {
        k = -1;
        DSG = new ArrayList<>();
        simpleDSG = new ArrayList<>();
        GSkyline = new ArrayList<>();
    }

    /**
     * this method simplify the DSG to a forest with multiple trees. each point have only one parent.
     *
     * @return the simplified DSG
     */
    private void getSimplifyDSG() {
        for (int i = 0; i < k; i++) {
            simpleDSG.add(new ArrayList<>());
        }
        for (ArrayList<GraphPoint<TwoDim>> layer : DSG) {
            for (GraphPoint<TwoDim> point : layer) {
                if (point.parents.size() + 1 > k) {
                    continue;
                } else if (point.parents.size() + 1 == k) {//find a group with exactly k points and is a unit group
                    output(point);
                } else {
                    point.children = new ArrayList<>();
                    if (point.parents.size() > 0) {
                        ArrayList<GraphPoint<TwoDim>> tmp = new ArrayList<>();
                        tmp.add(point.parents.get(point.parents.size() - 1));
                        point.parents = tmp;
                        point.parents.get(0).children.add(point);
                    }
                    simpleDSG.get(point.p.layer - 1).add(point);
                }
            }
        }
    }

    @Override
    public ArrayList<ArrayList<GraphPoint<TwoDim>>> GSkyline(ArrayList<ArrayList<GraphPoint<TwoDim>>> dsg, int k) {
        this.k = k;
        this.DSG = dsg;
        getSimplifyDSG();  //at this point, simpleDSG contains all the points with one parents and multiple children.

        GraphPoint<TwoDim> virtualRoot = new GraphPoint<>(new TwoDim(Float.MIN_VALUE, Float.MIN_VALUE));
        virtualRoot.children.addAll(simpleDSG.get(0));
        ArrayList<ArrayList<GraphPoint<TwoDim>>> groups = new ArrayList<>();
        for (GraphPoint<TwoDim> firstLayerPoint : simpleDSG.get(0)) {
            firstLayerPoint.parents.add(virtualRoot);
        }

        Stack<GraphPoint<TwoDim>> stack = new Stack<>();
        stack.push(virtualRoot);
        System.out.println("Push " + virtualRoot);
        pointWisePlus(stack, k+1);
        virtualRoot = stack.pop();
        System.out.println("pop" + virtualRoot);

//        for (GraphPoint<TwoDim> firstLayerPoint : simpleDSG.get(0)) {
//            Stack<GraphPoint<TwoDim>> stack = new Stack<>();
//            stack.push(firstLayerPoint);
//            System.out.println("Push " + firstLayerPoint);
//            pointWisePlus(stack, k, groups);
//
//            firstLayerPoint = stack.pop();
//            System.out.println("pop" + firstLayerPoint);
//        }
        return this.GSkyline;
    }

    private ArrayList<ArrayList<GraphPoint<TwoDim>>> pointWisePlus(Stack<GraphPoint<TwoDim>> stack, int k) {


        GraphPoint<TwoDim> currentPoint = stack.peek();
        for (GraphPoint<TwoDim> child : currentPoint.children) {
            stack.push(child);
            System.out.println("push " + child);
            if (stack.size() < k) {
                pointWisePlus(stack, k);
            } else {
                output(stack,k);
            }
            child = stack.pop();
            System.out.println("pop  " + child);
        }


        currentPoint = stack.peek();
        if (currentPoint.parents.size() > 0) {
            GraphPoint<TwoDim> parentPoint = currentPoint.parents.get(0);
            while (parentPoint != null) {
                int indexForCurrentPoint = parentPoint.children.indexOf(currentPoint);
                for (int i = indexForCurrentPoint + 1; i < parentPoint.children.size(); i++) {
                    GraphPoint<TwoDim> tmpPoint = parentPoint.children.get(i);
                    stack.push(tmpPoint);
                    System.out.println("push " + tmpPoint);
                    if (stack.size() < k) {
                        pointWisePlus(stack, k);
                    } else {
                        output(stack, k);
                    }
                    tmpPoint = stack.pop();
                    System.out.println("pop  " + tmpPoint);
                }
                if (stack.size() < k){
                    if (parentPoint.parents.size() == 0) {
                        int nextSiblingIndex = parentPoint.children.indexOf(currentPoint)+1;
                        if (nextSiblingIndex >= parentPoint.children.size()){
                            break;
                        }
                        currentPoint = parentPoint.children.get(nextSiblingIndex);
                    } else {
                        currentPoint = parentPoint;
                        parentPoint = parentPoint.parents.get(0);
                    }
                }
            }
        }

        return this.GSkyline;
    }

    private void output(Stack<GraphPoint<TwoDim>> s, int k) {
        assert (s.size() == k);
        ArrayList<GraphPoint<TwoDim>> group = new ArrayList<>();
        group.addAll(s);
        String tmp= "";
        for (GraphPoint<TwoDim> point : group){
            tmp+=point.p.label+",";
        }
        System.out.println("output " + tmp);
        if (!GSkyline.contains(group))
            GSkyline.add(group);
    }

    private void output(GraphPoint<TwoDim> point) {
        assert (point.parents.size() + 1 == k);
        ArrayList<GraphPoint<TwoDim>> group = new ArrayList<>(k);
        group.addAll(point.parents);
        group.add(point);
        GSkyline.add(group);

    }


    public static void main(String[] args) {
        Layer2D layer = new Layer2D();
        ArrayList<TwoDim> ps = ReadData.read2DData("hotel.txt");
        layer.getKSkylineLayers(ps, ps.size());

        int k = 4;
        DSG dsg = new DSG();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> layers = dsg.sortByLayer(ps, k);
        layers = dsg.getKDSG(layers, k); //the simplified DSG

        Wise pointWisePlus = new PointWisePlus();
        ArrayList<ArrayList<GraphPoint<TwoDim>>> gskylines = pointWisePlus.GSkyline(layers, k);
        System.out.println("------------");
        for (ArrayList<GraphPoint<TwoDim>> gskyline : gskylines) {
            for (GraphPoint<TwoDim> graphPoint : gskyline) {
                System.out.print(graphPoint.p.label + " ");
            }
            System.out.println("\n------------");
        }
        System.out.println(gskylines.size());
    }

}
