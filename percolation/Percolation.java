/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int[][] grid;
    private int sideLength;
    private int virtualTop;
    private int virtualBot;
    private int openSites;
    private WeightedQuickUnionUF unionFind;

    public Percolation(int n) {                // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException();

        openSites = 0;
        sideLength = n;
        virtualTop = 0;
        virtualBot = (n * n) + 1;
        unionFind = new WeightedQuickUnionUF((n * n) + 2);

        grid = new int[n][n];
        // 0 represents a blocked site whereas 1 represents an open site
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0;
            }
        }
    }

    public void open(int row, int col) {       // open site (row, col) if it is not open already
        if (row < 1 || col < 1 || row > sideLength || col > sideLength)
            throw new IllegalArgumentException();

        int referenceNum = ufReference(row, col);

        if (grid[row - 1][col - 1] != 1) {
            grid[row - 1][col - 1] = 1;
            openSites++;
        }

        if (row == 1) {
            unionFind.union(virtualTop, referenceNum);
        }
        else if (row == sideLength) {
            unionFind.union(virtualBot, referenceNum);
        }


        // Connect the surrouding open sites to the one that was just open
        if (row - 2 >= 0) {
            if (grid[row - 2][col - 1] == 1) {
                unionFind.union(referenceNum, ufReference(row - 1, col));
            }
        }
        if (row <= sideLength - 1) {
            if (grid[row][col - 1] == 1) {
                unionFind.union(referenceNum, ufReference(row + 1, col));
            }
        }
        if (col - 2 >= 0) {
            if (grid[row - 1][col - 2] == 1) {
                unionFind.union(referenceNum, ufReference(row, col - 1));
            }
        }
        if (col <= sideLength - 1) {
            if (grid[row - 1][col] == 1) {
                unionFind.union(referenceNum, ufReference(row, col + 1));
            }
        }

    }

    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        if (row < 1 || col < 1 || row > sideLength || col > sideLength)
            throw new IllegalArgumentException();

        if (grid[row - 1][col - 1] == 1)
            return true;
        else
            return false;
    }

    public boolean isFull(int row, int col) {  // is site (row, col) full?
        if (row < 1 || col < 1 || row > sideLength || col > sideLength)
            throw new IllegalArgumentException();

        return unionFind.connected(ufReference(row, col), virtualTop);
    }

    public int numberOfOpenSites() {       // number of open sites
        return openSites;
    }

    public boolean percolates() {              // does the system percolate?
        return unionFind.connected(virtualTop, virtualBot);
    }

    // Finding the uf reference number from the row and col argument
    private int ufReference(int row, int col) {
        return ((row - 1) * sideLength) + col;
    }

    public static void main(String[] args) {   // test client (optional)

    }
}
