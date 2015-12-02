import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private SearchNode goalNode;
    private boolean isSolvable;
    public Solver(Board initial) {
        MinPQ<SearchNode> minpq1  = new MinPQ<>();
        MinPQ<SearchNode> minpq2 = new MinPQ<>(); //minPQ for twin
        SearchNode searchNode = new SearchNode(initial, null, 0);
        SearchNode twinSearchNode = new SearchNode(initial.twin(), null, 0);
        minpq1.insert(searchNode);
        minpq2.insert(twinSearchNode);
        while (true) {
            searchNode = solveStep(minpq1);
            if (searchNode.board.isGoal()) { isSolvable = true; break; }
            searchNode = solveStep(minpq2);
            if (searchNode.board.isGoal()) { isSolvable = false; break; }
        }
        this.goalNode = searchNode;
    }
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previous;
        private int moves;
        private int priority;
        SearchNode(Board board, SearchNode previous, int moves) {
            this.board = board;
            this.previous = previous;
            this.moves = moves;
            this.priority = this.board.manhattan() + moves;
        }
        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return +1;
            return 0;
        }
    }
    private SearchNode solveStep(MinPQ<SearchNode> minPQ) {
        SearchNode searchNode = minPQ.delMin();
        Iterable<Board> neighboringBoards = searchNode.board.neighbors();
        for (Board neighbor: neighboringBoards) {
            if (searchNode.previous == null || !neighbor.equals(searchNode.previous.board)) {
                SearchNode nextSearchNode = new SearchNode(neighbor, searchNode, searchNode.moves + 1);
                minPQ.insert(nextSearchNode);
            }
        }
        return searchNode;
    }
    public boolean isSolvable() {
        return this.isSolvable;
    }
    public int moves() {
        if (!isSolvable)
            return -1;
        return goalNode.moves;
    }
    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        Stack<Board> solution = new Stack<>();
        SearchNode currentNode = goalNode;
        solution.push(currentNode.board);
        while (currentNode.previous != null) {
            solution.push(currentNode.previous.board);
            currentNode = currentNode.previous;
        }
        return solution;
    }
    public static void main(String[] args) {
        int[][] blocks = { {1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15}, {16, 17,18, 19, 20}, {21, 22, 23, 24, 0}};
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        System.out.println(solver.isSolvable);
        System.out.println(solver.moves());
        for (Board board: solver.solution()) {
            System.out.println(board.manhattan());
            System.out.println(board.toString());
        }
    }
}