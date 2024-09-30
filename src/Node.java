import java.util.Arrays;

public class Node {

    //node struct, containing the position, heuristic value, last move used and a pointer to a parent node
    int[][] position;
    int heuristics;
    Move move;
    Node parent;

    App app = new App();
    
    //constructor of a new node
    public Node(int[][] position, Move move, Node parent) {
        this.position = position;
        this.heuristics = app.heuristics(this);
        this.move = move;
        this.parent = parent;
    }

    //help function to print a node
    public void printNode(Node this) {
        System.out.println("Position: " + Arrays.deepToString(this.position));
        System.out.println("Heuristics: " + this.heuristics);
        if (move == null && parent == null) {
            System.out.println("ROOT");
        }
        else {
            System.out.println("Last move: " + this.move);
            System.out.println("Parent: " + Arrays.deepToString(this.parent.position));
        }
        System.out.println("-------------------------");
    }

}
