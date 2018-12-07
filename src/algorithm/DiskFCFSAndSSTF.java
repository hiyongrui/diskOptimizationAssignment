package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class DiskFCFSAndSSTF {

    Properties p = new Properties();
    DiskParameter dp = null;

    public static void main(String args[]) {
        new DiskFCFSAndSSTF("diskNumbers.properties");
    }

    public DiskFCFSAndSSTF(String filename) {
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
        generateSSTF();
    }

    public void printSequence(String name, int location[]) {
        String sequence = "";
        String working1 = "";
        String working2 = "";
        int total = 0;
        sequence += dp.getCurrent();
        int previous = dp.getCurrent();
        System.out.println("calculating --> " + name + " optimisation");
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

    public void generateSSTF() {
        System.out.println("generateSSTF() is called! ");
        int location[] = arrangeBySSTF(dp.getCurrent(), dp.getSequence());
        printSequence("SSTF", location);
    }

    //Original Sequence= 86,1470,913,1774,948,1509,1022,1750,130
    private int[] arrangeBySSTF(int current, int sequence[]) {
        // current is 143, so nearest 130, 86, 913, 948, 1022, 1470, 1509, 1750, 1774
        System.out.println("inside arrange by sstf");
        int n = sequence.length;
        int sstf[] = new int[n];
        for (int i = 0; i < n; i++) {
            sstf[i] = sequence[i];
        }
        System.out.println("starting array unsorted " + Arrays.toString(sstf) + "\n");

        int ii = -1;
        for (int i = 0; i < n; i++) {
            int minimum = Integer.MAX_VALUE;
            ii = i;
            System.out.println("ii value @@ " + ii);

            for (int j = i; j < n; j++) { //read from unsorted array
                int distance = Math.abs(current - sstf[j]);
                if (distance < minimum) { //if distance between current and next is less than the minimum , e.g. if 13 < 57, 13 is new minimum.
                    System.out.println("minimum before @@@ " + minimum);
                    ii = j;
                    minimum = distance; //replace MAX value to be distance
                    System.out.println("minimum after changed from distance -- " + minimum);
                }
            } //end of 2nd for loop

            int tmp = sstf[i]; // sequential order from the unsorted array 86,1470,9139,1774,948,1509,1022,1750,130
            System.out.println("\n tmp = sstf[i] i value = " + i + " = " + tmp); // 86
            sstf[i] = sstf[ii]; //sorted number found,
            System.out.println("sstf[i]  = sstf[ii] ii value = " + ii + " = " + sstf[i]);
            sstf[ii] = tmp;
            current = sstf[i]; // swop number to put in front with the other number from unsorted
            System.out.println("current = sstf[i] ==  " + current);
            System.out.println("sstf[ii] = tmp == " + sstf[ii]);
            System.out.println("last value of sstf[i] ==   " + sstf[i]);
            System.out.println("added to sstf array = " + Arrays.toString(sstf) + "\n");
        } //end of first for loop

        // Sequence= 86,1470,9139,1774,948,1509,1022,1750,130
        //finally sstf sorted = [86, 1470, 913, 1774, 948, 1509, 1022, 1750, 130]
        return sstf;
    } //end of arrangeBySSTF()

}