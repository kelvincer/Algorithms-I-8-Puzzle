/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)

    private boolean isSolvable = true;
    private Stack<Board> selected;
    private Node initialTwinNode;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Node deleted;
        MinPQ<Node> queue = new MinPQ<>();
        Node initialNode = new Node(initial, null);
        queue.insert(initialNode);
        initialTwinNode = new Node(initial.twin(), null);
        queue.insert(initialTwinNode);
        while (!queue.isEmpty()) {
            deleted = queue.delMin();
            if (deleted.board.isGoal()) {
                setSolution(deleted);
                break;
            }
            for (Board neighborBoard : deleted.board.neighbors()) {
                if (deleted.previous == null || !neighborBoard.equals(deleted.previous.board)) {
                    Node searchNode = new Node(neighborBoard, deleted);
                    queue.insert(searchNode);
                }
            }
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable) {
            return selected.size() - 1;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable) {
            return selected;
        }
        else {
            return null;
        }
    }

    private void setSolution(Node node) {
        selected = new Stack<>();
        while (node != null) {
            selected.push(node.board);
            if (node.board == initialTwinNode.board) {
                isSolvable = false;
                break;
            }
            node = node.previous;
        }
    }

    private class Node implements Comparable<Node> {

        private Board board;
        private int priority;
        private Node previous;
        private int moves;

        public Node(Board board, Node previous) {
            this.board = board;
            this.previous = previous;
            if (previous != null) {
                moves = previous.moves + 1;
            }
            else {
                moves = 0;
            }
            this.priority = board.manhattan() + moves;
        }

        public int compareTo(Node node) {
            return this.priority - node.priority;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In("puzzle2x2-unsolvable1.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
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
