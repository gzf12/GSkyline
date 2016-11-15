package org.hw.util;

import org.hw.struct.MultiDim;
import org.hw.struct.TwoDim;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by gzf on 2016/10/25.
 * 读取数据
 */
public class ReadData {
    public static ArrayList<TwoDim> read2DData(String fileName){
        ArrayList<TwoDim> p = new ArrayList<>();
//        System.out.println(fileName);
//        fileName = ReadData.class.getClassLoader().getResource(fileName).getFile();
        InputStream inputStream = ReadData.class.getResourceAsStream("/" + fileName);

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            float x, y;
            int counter = 0;
            while((line = bufferedReader.readLine()) != null){
                counter++;
                String[] numstr = line.split(" ");
                x = Float.valueOf(numstr[0]);
                y = Float.valueOf(numstr[1]);
                TwoDim tmp = new TwoDim(x,y,counter);
                p.add(tmp);

            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }

    public static ArrayList<MultiDim> readData(String fileName, int dim){
        ArrayList<MultiDim> p = new ArrayList<>();
//        fileName = ReadData.class.getClassLoader().getResource(fileName).getFile();
//        File file = new File(fileName);
        InputStream inputStream = ReadData.class.getResourceAsStream("/" + fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line ;
            while ((line = reader.readLine()) != null) {
                String[] numstr = line.split(" ");
                float[] eles = new float[dim];
                for (int i = 0 ; i < dim; i ++) {
                    eles[i] = Float.valueOf(numstr[i]);
                }
                p.add(new MultiDim(eles));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return p;
    }

    public static void testm () {
        ArrayList<MultiDim> ps = readData("hotel_4.txt", 4);
        int i = 0;
        for (MultiDim p : ps) {
            System.out.print(i++ + " ");
            for (float ele : p.eles) {
                System.out.print(ele + " ");
            }
            System.out.println();
        }
    }
    public static void test2 () {
        ArrayList<TwoDim> ps = read2DData("hotel.txt");
        System.out.println(ps.size());
    }
    public static void main(String[] args){
        test2();
    }
}
