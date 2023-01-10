public class BeamNode {
    BeamNode parentState;
    Puzzle puzzleState;
    String direction;
    int heuristic; //h(n)
    int level; //g(n)
    
    public BeamNode(Puzzle puzzle, int level, int heuristic, BeamNode parent, String direction){
        parentState = parent;
        puzzleState = puzzle;
        this.level = level;
        this.heuristic = heuristic;
        this.direction = direction;
    }

    public String printBeamNode(){
        return puzzleState.printState();
    }

    public void printPath(BeamNode root){
        if(root == null){
            return;
        }
        printPath(root.parentState);
        System.out.print("Move " + root.level + " " + root.puzzleState.getDirection() + ": "+ root.printBeamNode());
        System.out.println();
    }
}

