/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solver {

    // find a solution to the initial board (using the A* algorithm)

    private Board board;
    private List<Board> boards;
    private boolean isSolvable = false;
    private Node deleted;
    private List<Board> selected;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        board = initial;
        boards = new ArrayList<>();
        //Comparator<Node> comparator = new NodeComparator();
        MinPQ<Node> queue = new MinPQ<>();

        Node initialNode = new Node(board, board.manhattan(), null);
        queue.insert(initialNode);
        while (!queue.isEmpty()) {
            deleted = queue.delMin();
            if (deleted.board.isGoal()) {
                isSolvable = true;
                break;
            }
            Iterable<Board> childs = deleted.board.neighbors();
            for (Board boar : childs) {
                if (!isInList(boar)) {
                    queue.insert(new Node(boar, boar.manhattan(), deleted));
                    boards.add(boar);
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
            if (selected == null)
                getList(deleted);
            return selected.size() - 1;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable) {
            getList(deleted);
            return selected;
        }
        else {
            return null;
        }
    }

    private boolean isInList(Board b) {
        for (Board n : boards) {
            if (n.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private void getList(Node node) {
        selected = new ArrayList<>();
        while (node.previous != null) {
            selected.add(node.board);
            node = node.previous;
        }
        selected.add(board);
        Collections.reverse(selected);
    }

    private class Node implements Comparable<Node> {

        private Board board;
        private int priority;
        private Node previous;
        private int moves;

        public Node(Board board, int manhatan, Node previous) {
            this.board = board;
            this.priority = manhatan + moves;
            this.previous = previous;
            if(previous != null)
                this.moves = previous.moves + 1;
            else
                this.moves = 0;
        }

        public int compareTo(Node node) {
            return (this.board.manhattan() - node.board.manhattan()) + (this.moves - node.moves);
        }
    }

    private class NodeComparator implements Comparator<Node> {

        public int compare(Node o1, Node o2) {
            if (o1.priority < o2.priority) {
                return -1;
            }

            if (o1.priority > o2.priority) {
                return 1;
            }

            return 0;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In("test.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        StdOut.println(initial.hamming());

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
