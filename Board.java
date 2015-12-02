import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int N;
    private int[][] tiles;
    private int i0;
    private int j0;
    public Board(int[][] blocks) {
        N = blocks.length;
        tiles = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                checkInput(blocks[i][j]);
                if (blocks[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                }
                tiles[i][j] = blocks[i][j];
            }
    }
    private boolean exch(Board a, int i1, int j1, int i2, int j2) {
        if (i2 < 0 || i2 >= N || j2 < 0 || j2 >= N) return false;
        int swap = a.tiles[i1][j1];
        a.tiles[i1][j1] = a.tiles[i2][j2];
        a.tiles[i2][j2] = swap;
        if (a.tiles[i2][j2] == 0) { //if blank tile, change it's coordinates
            if (j1 == j2) a.i0 = i2; //doesn't work for twin method
            if (i1 == i2) a.j0 = j2;
        }
        return true;
    }
    private boolean tilesEquals(Board w, Board v) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (w.tiles[i][j] != v.tiles[i][j])
                    return false;
        return true;
    }
    private void checkInput(int element) {
        if (element < 0 || element >= N * N) throw new IllegalArgumentException("Elements out of bounds");
    }
    public int dimension() {
        return N;
    }
    public int hamming() {
        int hammingCount = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (i == (N - 1) && j == (N - 1)) break;
                if (tiles[i][j] != i * N + (j + 1)) hammingCount++;
            }
        return hammingCount;
        }
    public int manhattan() {
        int currentManhattan;
        int manhattanCount = 0;
        int iOfGoalPosition, jOfGoalPosition;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != i * N + (j + 1) && tiles[i][j] != 0) {
                    iOfGoalPosition = (tiles[i][j] - 1)/N;
                    if (tiles[i][j] % N == 0)
                        jOfGoalPosition = N - 1;
                    else
                        jOfGoalPosition = tiles[i][j] % N - 1;
                    currentManhattan = Math.abs(i - iOfGoalPosition) + Math.abs(j - jOfGoalPosition);
                    manhattanCount += currentManhattan;
                }
            }
        return manhattanCount;
    }
    public boolean isGoal() {
        if (hamming() == 0) return true;
        return false;
    }
    public Board twin() {
        Board twin = new Board(tiles);
        int i1, j1, i2, j2;
        boolean repeating = true;
        do {
            i1 = StdRandom.uniform(0, N);
            j1 = StdRandom.uniform(0, N);
            i2 = StdRandom.uniform(0, N);
            j2 = StdRandom.uniform(0, N);
            if (twin.tiles[i1][j1] != 0 && twin.tiles[i2][j2] != 0 && twin.tiles[i1][j1] != twin.tiles[i2][j2])
                repeating = false;
        } while (repeating);
            exch(twin, i1, j1, i2, j2);
        return twin;
    }
    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.N == that.N && tilesEquals(this, that));
    }
    public Iterable<Board> neighbors() {
        Stack<Board> neighboringBoards = new Stack<>();
        Board board = new Board(tiles);
        if (exch(board, i0, j0, i0, j0 - 1)) neighboringBoards.push(board);
        board = new Board(tiles);
        if (exch(board, i0, j0, i0, j0 + 1)) neighboringBoards.push(board);
        board = new Board(tiles);
        if (exch(board, i0, j0, i0 - 1, j0)) neighboringBoards.push(board);
        board = new Board(tiles);
        if (exch(board, i0, j0, i0 + 1, j0)) neighboringBoards.push(board);
        return neighboringBoards;
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%3d", tiles[i][j]));
            }
            s.append("\n");
        }
    return s.toString();
    }
    public static void main(String[] args) {

    }
}