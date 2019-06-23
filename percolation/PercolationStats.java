/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] threshold;
    private int numExperiments;

    public PercolationStats(int n,
                            int trials) {    // perform trials independent experiments on an n-by-n grid

        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        Percolation testGrid;
        numExperiments = trials;
        threshold = new double[trials];

        for (int i = 0; i < trials; i++) {
            testGrid = new Percolation(n);
            int randomRow;
            int randomCol;
            while (!testGrid.percolates()) {
                randomRow = StdRandom.uniform(1, n + 1);
                randomCol = StdRandom.uniform(1, n + 1);
                if (!testGrid.isOpen(randomRow, randomCol))
                    testGrid.open(randomRow, randomCol);
            }
            threshold[i] = ((double) testGrid.numberOfOpenSites()) / (n * n);
        }
    }

    public double mean() {                          // sample mean of percolation threshold
        return StdStats.mean(threshold);
    }

    public double stddev() {                        // sample standard deviation of percolation threshold
        return StdStats.stddev(threshold);
    }

    public double confidenceLo() {                  // low  endpoint of 95% confidence interval
        return this.mean() - ((this.stddev() * 1.96) / Math.sqrt(numExperiments));
    }

    public double confidenceHi() {                   // high endpoint of 95% confidence interval
        return this.mean() + ((this.stddev() * 1.96) / Math.sqrt(numExperiments));
    }

    public static void main(String[] args) {        // test client (described below)
        // Need to parse the string and convert it into int
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats test = new PercolationStats(n, t);
        StdOut.println("mean                    = " + test.mean());
        StdOut.println("stddev                  = " + test.stddev());
        StdOut.println(
                "95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi()
                        + "]");
    }
}
