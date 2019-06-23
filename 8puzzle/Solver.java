/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private int totalMoves;
    private boolean solvable;
    private final Stack<Board> solution;

    private class Node {
        private final Board board;
        private final int numMoves;
        private final Node prev;

        public Node(Board board, int numMoves, Node prev) {
            this.board = board;
            this.numMoves = numMoves;
            this.prev = prev;
        }

    }

    private class ByManhattan implements Comparator<Node> {
        public int compare(Node i, Node j) {
            int priority1 = i.board.manhattan() + i.numMoves;
            int priority2 = j.board.manhattan() + j.numMoves;
            if (priority1 > priority2) { return 1; }
            else if (priority1 < priority2) { return -1; }
            else { return 0; }
        }
    }

    public Solver(Board initial) {         // find a solution to the initial board (using the A* algorithm)
        ByManhattan manhattanPriority = new ByManhattan();
        MinPQ<Node> originalBoard = new MinPQ<Node>(manhattanPriority);
        MinPQ<Node> twinBoard = new MinPQ<Node>(manhattanPriority);
        solution = new Stack<Board>();

        originalBoard.insert(new Node(initial, 0, null));

        twinBoard.insert(new Node(initial.twin(), 0, null));

        Node originalTemp = originalBoard.delMin();
        Node twinTemp = twinBoard.delMin();

        if (originalTemp.board.isGoal()) {
            solvable = true;
            totalMoves = 0;
            solution.push(originalTemp.board);
        }

        int movesCounter;

        while (!originalTemp.board.isGoal() && !twinTemp.board.isGoal()) {

            if (originalTemp.prev == null)
                movesCounter = 1;
            else
                movesCounter = originalTemp.numMoves + 1;

            for (Board neighbor: originalTemp.board.neighbors()) {
                if (originalTemp.prev == null || !neighbor.equals(originalTemp.prev.board))
                    originalBoard.insert(new Node(neighbor, movesCounter, originalTemp));

            }

            for (Board twinNeighbor: twinTemp.board.neighbors()) {
                if (twinTemp.prev == null || !twinNeighbor.equals(twinTemp.prev.board))
                    twinBoard.insert(new Node(twinNeighbor, movesCounter, twinTemp));
            }

            originalTemp = originalBoard.delMin();
            twinTemp = twinBoard.delMin();

            if (originalTemp.board.isGoal()) {
                this.totalMoves = originalTemp.numMoves;
                solvable = true;
                Node nodeIterator = originalTemp;
                while (nodeIterator != null) {
                    solution.push(nodeIterator.board);
                    nodeIterator = nodeIterator.prev;
                }
                break;
            } else if (twinTemp.board.isGoal()) {
                solvable = false;
                break;
            }
        }

    }

    public boolean isSolvable() {          // is the initial board solvable?
        return solvable;
    }

    public int moves() {                   // min number of moves to solve initial board; -1 if unsolvable
        if (isSolvable())
            return totalMoves;
        else
            return -1;
    }

    public Iterable<Board> solution() {    // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable())
            return solution;
        else
            return null;
    }

    public static void main(String[] args) { // solve a slider puzzle (given below)

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
