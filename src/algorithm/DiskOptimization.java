package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class DiskOptimization {

    Properties p = new Properties();
    DiskParameter dp = null;

    public static void main(String args[]) {
        new DiskOptimization("diskNumbers.properties");
    }

    public DiskOptimization(String filename) {
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
        generateLOOK();
        generateSCAN();
        generateCSCAN();
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

            int current = location[i]; //loop every number in sequence to be current
            sequence += "," + current; // add on dp.getCurrent() to numbers in sorted sequence
            int d = Math.abs(previous - current); //get distance calculated

            working1 += "|" + previous + "-" + current + "|+"; // | a - b |
            working2 += d + " + "; // the distance to be calculated after | a - b |
            total += d; //get total distance
            previous = current; //change previous number to be next number to add on to distance of sequence

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

        int ii;
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


    public void generateLOOK(){
        int location[]=arrangeByLOOK(dp.getSequence());
        printSequence("LOOK",location);
    }

    private int[]arrangeByLOOK(int[] sequence){
        //find length of sequence
        int n=sequence.length;
        int look[]=new int[n];
        //put diskNumbers.properties into temp array
        List<Integer> temp=new ArrayList<>();

        for (int i:sequence){
            temp.add(i);
        }
        //create 2 arraylists less and more.Less indicates values less than current
        //And more indicates values more than current
        List<Integer> less=new ArrayList<>();
        List<Integer> more=new ArrayList<>();
        //if current more than previous sort the temp array
        if (dp.getCurrent()>dp.getPrevious()){
            Collections.sort(temp);
            for (int i=0;i<n;i++){
                //if item in temp more than current add it into arraylist 'less'
                if (temp.get(i)>=dp.getCurrent()){
                    less.add(temp.get(i));
                //else put into arraylist 'more'
                }else{
                    more.add(temp.get(i));
                }
            }
            //reverse items in more arraylist
            Collections.reverse(more);
        }else{
            Collections.sort(temp);
            for (int i=0;i<n;i++){
                //if item in temp less than current add that item into less
                if (temp.get(i)<=dp.getCurrent()){
                    less.add(temp.get(i));
                    //else put in more
                }else{
                    more.add(temp.get(i));
                }
            }
            //reverse items in less arraylist
            Collections.reverse(less);
        }
        //all items in less put into more
        System.out.println("less = " + less);
        System.out.println("MORE = " + more);
        less.addAll(more);
        System.out.println("COMBINED = " + less);
        for (int i=0; i<less.size();i++){
            look[i]=less.get(i);
        }
        return look;
    } //end of arrangeByLook()

    private void generateSCAN() {
        //sorted properly disks by SCAN method
        ArrayList<Integer> location = arrangeBySCAN(dp.getCylinders(),dp.getPrevious(), dp.getCurrent(), dp.getSequence());
        System.out.println("calling scan generation");
        printArrayListSequence("SCAN", location); // calculate the distance, print out
    }

    //Original Sequence= 86,1470,913,1774,948,1509,1022,1750,130
    private ArrayList<Integer> arrangeBySCAN(int totalCylinder, int previous, int current, int[] sequence) {

        System.out.println("inside arrange by scan");

        System.out.println("starting array unsorted " + Arrays.toString(sequence)+ "\n");

        ArrayList<Integer> emptyRight = new ArrayList<>();
        ArrayList<Integer> emptyLeft = new ArrayList<>();

        for (int i=0; i< sequence.length; i++) {
            // if distance is > 0, increasing to 4999
            if ( (current - previous) > 0) {
                if ( (current - sequence[i]) <= 0) { //143 - 1470 < 0, current = 143, second number is 1470
                    System.out.println("distance > 0 value = " + sequence[i]);
                    emptyRight.add(sequence[i]);
                    Collections.sort(emptyRight); //sort emptyRight array in ascending order
                    //empty.add(4999);
                }
                else{ //143 - 86 < 0, current = 143, first number is 86
                    System.out.println("distance > 0 left array = " + sequence[i]);
                    emptyLeft.add(sequence[i]);
                    Collections.sort(emptyLeft); //sort emptyLeft array in acsending order
                    Collections.reverse(emptyLeft); //reverse so that its descending [130,86] instead of [86,130]
                }
            }
            // else if distance is < 0, decreasing to 0
            else {
                //current = 125, previous = 143, going down to 0
                if ( (current - sequence[i]) >= 0) {
                    System.out.println("more than 0 value = " + sequence[i]);
                    emptyRight.add(sequence[i]);
                    Collections.sort(emptyRight); //sort emptyRight array in ascending order
                    Collections.reverse(emptyRight); //reverse so that its descending [86,0]
                }
                else{
                    System.out.println("lesser than 0 value =  " + sequence[i]);
                    emptyLeft.add(sequence[i]);
                    Collections.sort(emptyLeft); //sort emptyLeft array in ascending order
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

        emptyRight.addAll(emptyLeft); // combine the two sorted arrays together

        System.out.println("\n originally unsorted = " + Arrays.toString(sequence) );
        System.out.println("finally scan sorted = " + emptyRight  + "\n");

        // Original unsorted Sequence = 86,1470,913,1774,948,1509,1022,1750,130
        //End result (1) Scan going to 4999 = [913, 948, 1022, 1470, 1509, 1750, 1774, 4999, 130, 86]
        //End result (2) Scan going to 0 = [86, 0, 130, 913, 948, 1022, 1470, 1509, 1750, 1774]
        return emptyRight; //return sorted array scan
    } //end of arrangeByScan()

    public void printArrayListSequence(String name, ArrayList<Integer> location) {
        String sequence = "";
        String working1 = "";
        String working2 = "";
        int total = 0;
        sequence += dp.getCurrent();
        int previous = dp.getCurrent();
        System.out.println("name --> " + name);
        for (int i=0; i < location.size(); i++) {

            int current = location.get(i); //loop every number in the sequence to be current
            sequence += "," + current; //add on the dp.getCurrent() to the numbers in sorted sequence
            int d = Math.abs(previous-current); //get distance calculated

            working1 += "|" + previous + "-" + current + "|+"; // | a - b |
            working2 += d + " + "; // the distance to be calculated after | a - b |
            total += d; //get total distance
            previous = current; //change the previous number to be next current number to add on the distance of sequence
        }

        System.out.println(name + '\n' + "====");
        System.out.println("Order of Access: " + sequence);

        System.out.println("Total Distance = " + working1.substring(0,working1.length()-1));
        System.out.println("                 = " + working2.substring(0,working2.length()-2));
        System.out.println("                 = " + total + '\n');

    } //end of printArrayListSequence()

    public void generateCSCAN() {
        int location[] = arrangeByCScan(dp.getCurrent(), dp.getSequence());
        printSequence("C-Scan" , location);
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
            if (cscan[i] >= current) {
                cscanHigher.add(cscan[i]); // add numbers greater than current number to array
            }
            if (cscan[i] < current) {
                cscanLower.add((cscan[i])); // add numbers lower than current number to array
            }
        }


        if (current < previous) { // determine if distance is descending  //40, 10, 10,0,0,4999,1999,1500,80,45
            System.out.println("Beforesort Lower = " +cscanLower);
            System.out.println("Beforesort Higher = " +cscanHigher);
            cscanHigher.sort(Collections.reverseOrder()); // sort array list in descending order
            cscanHigher.add(0, 4999);
            cscanLower.sort(Collections.reverseOrder()); // sort array list in descending order
            cscanLower.add(0);
            System.out.println("Lower = " +cscanLower);
            System.out.println("Higher = " +cscanHigher);
            cscanLower.addAll(cscanHigher); // append the array list containing higher numbers to array list containing lower numbers
            System.out.println("AfterAdd" +cscanHigher);
            int[] newcscan = new int[cscanLower.size()];
            for (int i = 0; i < newcscan.length; i++) {
                newcscan[i] = cscanLower.get(i); // append array list to new array
            }
            return newcscan;
        }
        else { // determine if distance is ascending  //45,45,80,1500,1999,1999,0,0,10,10
            System.out.println("cscan lower before " + cscanLower);
            System.out.println("cscan higher before = " + cscanHigher);
            Collections.sort(cscanHigher); // sort array list in ascending order
            System.out.println("after sort cscan higher == " + cscanHigher);
            cscanHigher.add(dp.getCylinders() - 1);
            System.out.println("after adding total cylinder - 1 " + cscanHigher);
            Collections.sort(cscanLower); // sort array list in ascending order
            cscanLower.add(0, 0);
            System.out.println("after sorted and add 0 to index 0 " + cscanLower);
            cscanHigher.addAll(cscanLower); // append the array list containing lower numbers to array list containing higher numbers
            System.out.println("cscan add all done " + cscanHigher);
            int[] newcscan = new int[cscanHigher.size()];
            System.out.println("new cscan befroe" + newcscan.length);
            for (int i = 0; i < newcscan.length; i++) {
                newcscan[i] = cscanHigher.get(i); // append array list to new array
            }
            System.out.println("RETURNING NEW CSCAN   " + Arrays.toString(newcscan));
            return newcscan;

        }
        // return

    } //end of arrangeByCSCAN()

}
