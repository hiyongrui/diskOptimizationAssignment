package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class DiskFCFS {

    Properties p = new Properties();
    DiskParameter dp = null;

    public static void main(String args[]) {
        new DiskFCFS("diskNumbers.properties");
    }

    public DiskFCFS(String filename) {
        try {
            p.load(new BufferedReader(new FileReader(filename)));
            dp = new DiskParameter(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        generateAnalysis();
    }

    public void generateAnalysis() {
        generateFCFS();
    }

    public void printSequence(String name, int location[]) {
        String sequence = "";
        String working1 = "";
        String working2 = "";
        int total = 0;
        sequence += dp.getCurrent();
        int previous = dp.getCurrent();
        System.out.println("name --> " + name);
        for (int i = 0; i < location.length; i++) {

            int current = location[i];
            sequence += "," + current;
            int d = Math.abs(previous - current);

            working1 += "|" + previous + "-" + current + "|+";
            working2 += d + " + ";
            total += d;
            previous = current;
        }

        System.out.println(name + '\n' + "====");
        System.out.println("Order of Access: " + sequence);

        System.out.println("Total Distance = " + working1.substring(0, working1.length() - 1));
        System.out.println("                 = " + working2.substring(0, working2.length() - 2));
        System.out.println("                 = " + total + '\n');

    }

    public void generateFCFS() {
        int location[] = dp.getSequence();
        printSequence("FCFS", location);
    }

}