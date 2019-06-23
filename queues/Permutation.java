/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);

        RandomizedQueue<String> array = new RandomizedQueue<>();
       while (!StdIn.isEmpty()) {
            array.enqueue(StdIn.readString());
        }

        for (int j = 0; j < n; j++) {
            StdOut.println(array.dequeue());
        }
    }
}
