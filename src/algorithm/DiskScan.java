package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class DiskScan {
    Properties p = new Properties();
    DiskParameter dp = null;

    public static void main(String args[]) {
        new DiskScan("diskNumbers.properties");
    }

    public DiskScan(String filename) {
        try {
            p.load(new BufferedReader(new FileReader(filename)));
            dp = new DiskParameter(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        generateScan();// call scanning method of disk
    }


    private void generateScan() {
        //sorted properly disks by SCAN method
        ArrayList<Integer> location = arrangeBySCAN(dp.getCylinders(),dp.getPrevious(), dp.getCurrent(), dp.getSequence());
        System.out.println("calling scan generation");
        printSequence("SCAN", location); // calculate the distance, print out
    }

    //Sequence= 86,1470,913,1774,948,1509,1022,1750,130
    private ArrayList<Integer> arrangeBySCAN(int totalCylinder, int previous, int current, int[] sequence) {
        // current is 143, previous 125, so increasing 143, 913, 948, 1022, 1470, 1509, 1750, 1774, 5000, 130, 86
        System.out.println("inside arrange by scan");

        System.out.println("starting array unsorted " + Arrays.toString(sequence)+ "\n");

        ArrayList<Integer> emptyRight = new ArrayList<>();
        ArrayList<Integer> emptyLeft = new ArrayList<>();

        for (int i=0; i< sequence.length; i++) {
            // if distance is > 0, increasing to 4999
            if ( (current - previous) > 0) {
                if ( (current - sequence[i]) < 0) { //143 - 1470 < 0, current = 143, second number is 1470
                    System.out.println("distance > 0 value = " + sequence[i]);
                    emptyRight.add(sequence[i]);
                    Collections.sort(emptyRight);
                    //empty.add(4999);
                }
                else{ //143 - 86 < 0, current = 143, first number is 86
                    System.out.println("distance > 0 left array = " + sequence[i]);
                    emptyLeft.add(sequence[i]);
                    Collections.sort(emptyLeft);
                    Collections.reverse(emptyLeft); //reverse so that its [130,86] instead of [86,130]
                }
            }
            // else if distance is < 0, decreasing to 0
            else {
                //current = 125, previous = 143, going down to 0
                if ( (current - sequence[i]) > 0) {
                    System.out.println("more than 0 value = " + sequence[i]);
                    emptyRight.add(sequence[i]);
                    Collections.sort(emptyRight);
                    Collections.reverse(emptyRight);
                }
                else{
                    System.out.println("lesser than 0 value =  " + sequence[i]);
                    emptyLeft.add(sequence[i]);
                    Collections.sort(emptyLeft);
                }
            }

        }
        if ((current-previous) > 0) {
            emptyRight.add(totalCylinder - 1); //add 4999 to the end of the right array
        }
        else{
            emptyRight.add(0); //add 0 to the end of the right array
        }
        System.out.println("\n END OF FOR LOOP RIGHT ARRAY " + emptyRight);
        System.out.println("END OF FOR LOOP LEFT ARRAY " + emptyLeft);

        emptyRight.addAll(emptyLeft); // combine the two arrays together

        System.out.println("\n originally unsorted = " + Arrays.toString(sequence) );
        System.out.println("finally scan sorted = " + emptyRight  + "\n");

        // Sequence= 86,1470,913,1774,948,1509,1022,1750,130
        //End result (1) Scan going to 4999 = [913, 948, 1022, 1470, 1509, 1750, 1774, 4999, 130, 86]
        //End result (2) Scan going to 0 = [130, 86, 0, 948, 1022, 1470, 1509, 1750, 1774, 913]
        return emptyRight; //return sorted array scan
    } //end of arrangeByScan()

    public void printSequence(String name, ArrayList<Integer> location) {
        String sequence = "";
        String working1 = "";
        String working2 = "";
        int total = 0;
        sequence += dp.getCurrent();
        int previous = dp.getCurrent();
        System.out.println("name --> " + name);
        for (int i=0; i < location.size(); i++) {

            int current = location.get(i);
            sequence += "," + current;
            int d = Math.abs(previous-current);

            working1 += "|" + previous + "-" + current + "|+";
            working2 += d + " + ";
            total += d;
            previous = current;
        }

        System.out.println(name + '\n' + "====");
        System.out.println("Order of Access: " + sequence);

        System.out.println("Total Distance = " + working1.substring(0,working1.length()-1));
        System.out.println("                 = " + working2.substring(0,working2.length()-2));
        System.out.println("                 = " + total + '\n');

    }
}
