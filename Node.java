
public class Node {
    Node parentState;
    Puzzle puzzleState;
    String direction;
    int heuristic; //h(n)
    int level; //g(n)
    int cost; //f(n)
    
    public Node(Puzzle puzzle, int level, int heuristic, Node parent, String direction){
        parentState = parent;
        puzzleState = puzzle;
        cost = level + heuristic;
        this.level = level;
        this.heuristic = heuristic;
        this.direction = direction;
    }

    public String printNode(){
        return puzzleState.printState();
    }

    public void printPath(Node root){
        if(root == null){
            return;
        }
        printPath(root.parentState);
        System.out.print("Move " + root.level + " " + root.puzzleState.getDirection() + ": "+ root.printNode());
        System.out.println();
    }
}

