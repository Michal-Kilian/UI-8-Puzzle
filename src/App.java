//Michal Kilian - UI - zadanie 1
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

    //declaration of starting and ending position as a 2d integer array
    int[][] startPos = {{0, 1, 2, 3, 4, 5}, {6, 7, 8, 9, 10, 11}};
    int[][] endPos = {{5, 10, 3, 2, 7, 1}, {11, 4, 9, 8, 6, 0}};
    int width = getWidth();
    int height = getHeight();
    boolean solutionFound = false;
    int totalNodesCreated = 0;
    //int to set up which heuristic value are we using (1 or 2)
    int heuristicValueUsed = 1;

    ArrayList<Node> createdNodes = new ArrayList<Node>();
    HashMap<String, Node> processedNodes = new HashMap<String, Node>();

    //main function to check the correctness of the input, create a root node and start the process
    public static void main(String[] args) {
        App app = new App();

        if (app.check() == 1) {
            System.out.println("Starting position and ending position don't have the same dimension!");
            return;
        }

        Node root = new Node(app.startPos, null, null);
        app.createdNodes.add(root);
        app.totalNodesCreated++;

        app.process(root);
    }

    //function containing a while loop with main application logic 
    void process(Node node) {
        while (!createdNodes.isEmpty()) {

            createdNodes.remove(node);

            if (node.heuristics == 0) {
                findSolution(node);
                solutionFound = true;
                break;
            }

            processedNodes.put(hash(node), node);
            calcPossibleMoves(node);
            Node bestNode = chooseBestNode();

            node = bestNode;
        }
        if (solutionFound == false) {
            System.out.println("Solution wasn't found");
        }
    }

    //function that is called if the solution was found, prints the correct way of moves
    void findSolution(Node node) {
        while (node.parent != null) {
            System.out.print(node.move + " -> ");
            node = node.parent;
        }
        System.out.print("END");
        System.out.print("\n");

        System.out.println("Total nodes created: " + totalNodesCreated);
        System.out.println("Total nodes processed: " + processedNodes.size());
    }

    //check function to quickly realize whether the input positions are incorrect
    int check() {
        if (startPos.length != endPos.length || startPos[0].length != endPos[0].length)
            return 1;
        else
            return 0;
    }

    //function to quickly set up the current heuristics that we are using
    int heuristics(Node node) {
        if (heuristicValueUsed == 1)
            return heuristics1(node);
        else
            return heuristics2(node);
    }

    //function to calculate the amount of boxes that are not in the correct place (heuristic value 1)
    int heuristics1(Node node) {
        int amount = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (node.position[j][i] == 0)
                    continue;
                if (node.position[j][i] != endPos[j][i])
                    amount++;
            }
        }
        return amount;
    }

    //function to calculate the total amount of moves needed to find a solution
    int heuristics2(Node node) {
        int totalMoves = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (node.position[j][i] == 0)
                    continue;
                for (int k = 0; k < width; k++) {
                    for (int l = 0; l < height; l++) {
                        if (node.position[j][i] == endPos[l][k])
                            totalMoves += calcDistance(j, l, i, k);
                    }
                }
            }
        }
        return totalMoves;
    }

    //function that returns the manhattan distance between 2 indexes
    int calcDistance(int firstIndexX, int secondIndexX, int firstIndexY, int secondIndexY) {
        return Math.abs(firstIndexX - secondIndexX) + Math.abs(firstIndexY - secondIndexY);
    }

    //function that calculates the next possible moves of a node
    void calcPossibleMoves(Node node) {
        ArrayList<Boolean> possibleDirections = directions(node);

        if (possibleDirections.get(0) == true) {
            Node newNode = new Node(swapValues(node, Move.UP), Move.UP, node);
            addToCreatedNodes(newNode);
        }
        if (possibleDirections.get(1) == true) {
            Node newNode = new Node(swapValues(node, Move.DOWN), Move.DOWN, node);
            addToCreatedNodes(newNode);
        }
        if (possibleDirections.get(2) == true) {
            Node newNode = new Node(swapValues(node, Move.RIGHT), Move.RIGHT, node);
            addToCreatedNodes(newNode);
        }
        if (possibleDirections.get(3) == true) {
            Node newNode = new Node(swapValues(node, Move.LEFT), Move.LEFT, node);
            addToCreatedNodes(newNode);
        }        
    }

    //function to add a node to arraylist of created nodes
    void addToCreatedNodes(Node newNode) {
        if (!processedNodes.containsKey(hash(newNode))) {
            createdNodes.add(newNode);
            totalNodesCreated++;
        }
    }

    //function to calculate possible directions which a node can move to
    ArrayList<Boolean> directions(Node node) {
        ArrayList<Boolean> directions = new ArrayList<Boolean>();
        Coordinates crdnts = find0(node);
        int index0 = (crdnts.y * width) + crdnts.x;

        //to determine if we can move up
        if (index0 - width >= 0)
            directions.add(true);
        else
            directions.add(false);
        
        //to determine if we can move down
        if (index0 + width < height * width)
            directions.add(true);
        else
            directions.add(false);

        //to determine if we can move right
        if (index0 % width + 1 < width)
            directions.add(true);
        else
            directions.add(false);

        //to determine if we can move left
        if (index0 % width - 1 >= 0)
            directions.add(true);
        else
            directions.add(false);

    
        return directions;
    }

    //function to calculate the second index after a respective move
    int[][] swapValues(Node node, Move move) {
        Coordinates index0coordinates = find0(node);
        int firstIndex = (index0coordinates.y * width) + index0coordinates.x;
        int secondIndex;

        switch(move) {
            case UP:
                secondIndex = firstIndex - width;
                return swap(node.position, firstIndex, secondIndex);
            case DOWN:
                secondIndex = firstIndex + width;
                return swap(node.position, firstIndex, secondIndex);
            case RIGHT:
                secondIndex = firstIndex + 1;
                return swap(node.position, firstIndex, secondIndex);
            case LEFT:
                secondIndex = firstIndex - 1;
                return swap(node.position, firstIndex, secondIndex);
        }
        return null;
    }

    //function to swap 2 elements in a position to get a new one, once we have both indexes
    int[][] swap(int[][] position, int firstIndex, int secondIndex) {

        int[] position1d = Stream.of(position).flatMapToInt(IntStream::of).toArray();

        int temporary = position1d[firstIndex];
        position1d[firstIndex] = position1d[secondIndex];
        position1d[secondIndex] = temporary;

        int[][] position2d = new int[height][width];
        for (int i = 0; i < height; i++)
            System.arraycopy(position1d, (i*width), position2d[i], 0, width);

        return position2d;
    }

    //function to calculate the best node to pick from created nodes so far, based on their heuristic value
    Node chooseBestNode() {
        int min = Integer.MAX_VALUE;
        Node bestNode = null;

        for (Node node : createdNodes) {
            if (node.heuristics < min)
                min = node.heuristics;
        }

        for (Node node : createdNodes) {
            if (min == node.heuristics)
                bestNode = node;
        }

        return bestNode;
    }

    //function to find out the width of the matrix, based on the input positions
    int getWidth() {
        return startPos[0].length;
    }

    //function to find out the height of the matrix, based on the input positions
    int getHeight() {
        return startPos.length;
    }

    //function to get the coordinates of a blank box (with a number 0 in my case)
    Coordinates find0(Node node) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (node.position[j][i] == 0) {
                    return new Coordinates(i, j);
                }
            }
        }
        return null;
    }

    //function to get the hash string to put new nodes into the hashmap of processed nodes
    String hash(Node node) {
        return Arrays.deepToString(node.position);
    }
}