/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int[][] tiles;
    private int[][] goal;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }

        goal = new int[dimension()][dimension()];
        int g = 1;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                goal[i][j] = g++;
            }
        }
        goal[dimension() - 1][dimension() - 1] = 0;
    }

    // string representation of this board
    public String toString() {
        String out = dimension() + "";
        for (int i = 0; i < dimension(); i++) {
            out += "\n";
            for (int j = 0; j < dimension(); j++) {
                out += tiles[i][j] + " ";
            }
        }
        return out;
    }

    // board dimension n
    public int dimension() {
        return tiles[0].length;
    }

    // number of tiles out of place
    public int hamming() {
        int op = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] == 0)
                    continue;
                if (tiles[i][j] != goal[i][j]) {
                    op++;
                }
            }
        }
        return op;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {

        int manDistance = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                Index index = findIndexes(tiles[i][j]);
                manDistance += abs(i - index.i) + abs(j - index.j);
            }
        }

        return manDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {

        //if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;

        Board by = (Board) y;

        if (by.dimension() != tiles[0].length)
            return false;

        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (by.tiles[i][j] != tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        int dimen = dimension();
        List<Board> neighbors = new ArrayList<>();

        if (tiles[0][0] == 0) {
            neighbors.add(newBoard(0, 0, 0, 1));
            neighbors.add(newBoard(0, 0, 1, 0));
            return neighbors;
        }

        if (tiles[0][dimen - 1] == 0) {
            neighbors.add(newBoard(0, dimen - 1, 0, dimen - 2));
            neighbors.add(newBoard(0, dimen - 1, 1, dimen - 1));
            return neighbors;
        }

        if (tiles[dimen - 1][0] == 0) {
            neighbors.add(newBoard(dimen - 1, 0, dimen - 2, 0));
            neighbors.add(newBoard(dimen - 1, 0, dimen - 1, 1));
            return neighbors;
        }

        if (tiles[dimen - 1][dimen - 1] == 0) {
            neighbors.add(newBoard(dimen - 1, dimen - 1, dimen - 2, dimen - 1));
            neighbors.add(newBoard(dimen - 1, dimen - 1, dimen - 1, dimen - 2));
            return neighbors;
        }

        for (int j = 1; j < dimen - 1; j++) {
            if (tiles[0][j] == 0) {
                neighbors.add(newBoard(0, j, 0, j - 1));
                neighbors.add(newBoard(0, j, 0, j + 1));
                neighbors.add(newBoard(0, j, 1, j));
                return neighbors;
            }
        }

        for (int i = 1; i < dimen - 1; i++) {
            if (tiles[i][0] == 0) {
                neighbors.add(newBoard(i, 0, i - 1, 0));
                neighbors.add(newBoard(i, 0, i, 1));
                neighbors.add(newBoard(i, 0, i + 1, 0));
                return neighbors;
            }
        }

        for (int i = 1; i < dimen - 1; i++) {
            if (tiles[i][dimen - 1] == 0) {
                neighbors.add(newBoard(i, dimen - 1, i - 1, dimen - 1));
                neighbors.add(newBoard(i, dimen - 1, i, dimen - 2));
                neighbors.add(newBoard(i, dimen - 1, i + 1, dimen - 1));
                return neighbors;
            }
        }

        for (int j = 1; j < dimen - 1; j++) {
            if (tiles[dimen - 1][j] == 0) {
                neighbors.add(newBoard(dimen - 1, j, dimen - 2, j));
                neighbors.add(newBoard(dimen - 1, j, dimen - 1, j - 1));
                neighbors.add(newBoard(dimen - 1, j, dimen - 1, j + 1));
                return neighbors;
            }
        }

        for (int i = 1; i < dimen - 1; i++) {
            for (int j = 1; j < dimen - 1; j++) {
                if (tiles[i][j] == 0) {
                    neighbors.add(newBoard(i, j, i - 1, j));
                    neighbors.add(newBoard(i, j, i, j - 1));
                    neighbors.add(newBoard(i, j, i, j + 1));
                    neighbors.add(newBoard(i, j, i + 1, j));
                    return neighbors;
                }
            }
        }

        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] t = arrayCopy(tiles);
        int dimen = dimension();
        Index index = null;
        for (int i = 0; i < dimen; i++) {
            for (int j = 0; j < dimen; j++) {
                if (tiles[i][j] == 0) {
                    index = new Index(i, j);
                    break;
                }
            }
        }
        if (index.i == 0) {
            int temp = t[1][0];
            t[1][0] = t[1][1];
            t[1][1] = temp;
        }
        else {
            int temp = t[0][0];
            t[0][0] = t[0][1];
            t[0][1] = temp;
        }
        return new Board(t);
    }

    private int abs(int x) {
        return Math.abs(x);
    }

    private Board newBoard(int a, int b, int c, int d) {
        int[][] tilesNei = new int[dimension()][dimension()];
        tilesNei[a][b] = tiles[c][d];
        tilesNei[c][d] = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if ((i == a && j == b) || (i == c && j == d)) {
                    continue;
                }
                tilesNei[i][j] = tiles[i][j];
            }

        }

        Board board = new Board(tilesNei);
        return board;
    }

    private Index findIndexes(int n) {
        for (int i = 0; i < dimension(); i++)
            for (int j = 0; j < dimension(); j++) {
                if (goal[i][j] == n) {
                    return new Index(i, j);
                }
            }

        return null;
    }

    private class Index {
        private int i;
        private int j;

        public Index(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    private int[][] arrayCopy(int[][] blocks) {
        int[][] copy = new int[blocks.length][blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                copy[i][j] = blocks[i][j];
            }
        }
        return copy;
    }
}
