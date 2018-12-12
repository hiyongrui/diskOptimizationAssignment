package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class DiskCSCAN {
    Properties p = new Properties();
    DiskParameter dp = null;

    public static void main(String args[]) {
        new DiskCSCAN("diskq1.properties");
    }

    public DiskCSCAN(String filename) {
        try
        {
            p.load(new BufferedReader (new FileReader (filename)));
            dp = new DiskParameter(p);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        generateAnalysisCScan();
    }
    public void generateAnalysisCScan()
    {
        generatecScan();
    }
    public void printSequenceCScan(String name, int location[])
    {
        String sequence = "";
        String working1 = "";
        String working2 = "";
        int total = 0;
        sequence += dp.getCurrent();
        int previous = dp.getCurrent();
        for (int i = 0; i < location.length; i++) {
            int current = location[i];
            sequence += "," + current;
            int d = Math.abs(previous-current);

            working1 += "|" + previous + "-" + current + "|+";
            working2 += d + " + ";
            total += d;
            previous = current;
        }
        System.out.println(name+'\n'+"====");
        System.out.println("Order of Access: "+sequence);
        System.out.println("Total Distance = "+working1.substring(0, working1.length()-1));
        System.out.println("               = "+working2.substring(0, working2.length()-2));
        System.out.println("               = "+total + '\n');
    }
    public void generatecScan() {
        int location[] = arrangeByCScan(dp.getCurrent(), dp.getSequence());
        printSequenceCScan("C-Scan" , location);
    }
    private int[] arrangeByCScan(int current, int sequence[])
    {
        int n = sequence.length;
        int cscan[] = new int[n];
        for (int i = 0 ; i < n; i++) {
            cscan[i] = sequence[i];
        }
        int previous = dp.getPrevious(); // get previous
        List<Integer> cscanHigher = new ArrayList<Integer>(); // create array to contain numbers greater than current number
        List<Integer> cscanLower = new ArrayList<Integer>(); // create array to contain numbers lower than current number
        for (int i = 0; i < cscan.length; i++) {
            if (cscan[i] > current) {
                cscanHigher.add(cscan[i]); // add numbers greater than current number to array
            }
            if (cscan[i] < current) {
                cscanLower.add((cscan[i])); // add numbers lower than current number to array
            }
        }
        
        
        if (current < previous) { // determine if distance is descending
            System.out.println("Beforesort Lower = " +cscanLower);
            System.out.println("Beforesort Higher = " +cscanHigher);
            Collections.sort(cscanHigher, Collections.reverseOrder()); // sort array list in descending order
            cscanHigher.add(0, 4999);
            Collections.sort(cscanLower, Collections.reverseOrder()); // sort array list in descending order
            cscanLower.add(0);
            System.out.println("Lower = " +cscanLower);
            System.out.println("Higher = " +cscanHigher);
            cscanLower.addAll(cscanHigher); // append the array list containing higher numbers to array list containing lower numbers
            System.out.println("AfterAdd" +cscanHigher);
            int[] newcscan = new int[cscanLower.size()];
            for (int i = 0; i < newcscan.length; i++) {
                newcscan[i] = cscanLower.get(i).intValue(); // append array list to new array
            }
            return newcscan;
        }
        else { // determine if distance is ascending
            Collections.sort(cscanHigher); // sort array list in ascending order
            cscanHigher.add(dp.getCylinders() - 1);
            Collections.sort(cscanLower); // sort array list in ascending order
            cscanLower.add(0, 0);
            cscanHigher.addAll(cscanLower); // append the array list containing lower numbers to array list containing higher numbers
            
            int[] newcscan = new int[cscanLower.size()];

	        for (int i = 0; i < newcscan.length; i++) {
	             newcscan[i] = cscanHigher.get(i).intValue(); // append array list to new array
	         }
	        return newcscan;
	
	        }
         // return

    }

}
