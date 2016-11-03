package org.hw;

/**
 * Created by gzf on 2016/10/31.
 * jar包运行的主类
 */
public class Run {
    public static void main(String[] args) {
        try {
            if (args.length != 4){
                usage();
                return;
            }
            int dim = Integer.valueOf(args[2]);
            int k = Integer.valueOf(args[3]);
            String filename = args[1];
            if (dim == 2){
                org.hw.main.Wise wise = new org.hw.main.GroupWisePlus();
                if (args[0].endsWith("pwise")) wise = new org.hw.main.PointWise();
                else if (args[0].endsWith("uwise")) wise = new org.hw.main.GroupWise();
                org.hw.main.Test.test(filename,wise,k,false);
            } else {
                org.hw.mdim.Wise wise = new org.hw.mdim.GroupWisePlus();
                if (args[0].endsWith("pwise")) wise = new org.hw.mdim.PointWise();
                else if (args[0].endsWith("uwise")) wise = new org.hw.mdim.GroupWise();
                org.hw.mdim.Test.test(filename,wise,k,false,dim);
            }

        } catch (Exception e){
            usage();
        }

    }
    private static void usage(){
        System.out.println("usage: algorithm dataname dim k");
        System.out.println("java -jar **.jar uwise+ corr_6.txt 6 4");
        System.out.println("    algorithm: pwise, uwise, uwise+");
        System.out.println("    dataname: anti_2.txt, anti_4.txt, corr_2.txt ...");
        System.out.println("    dim: 维度，跟数据名要对应");
        System.out.println("    k: group的大小");
    }

}
