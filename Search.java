import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
public class Search {

    Puzzle puzzle;
    String heuristic;

    public Search(Puzzle initial){
        puzzle = initial;
    }

    public void aStarSearch(Puzzle initial, String method){
        if(initial.isSolvable()){
            PriorityQueue<Node> pq = new PriorityQueue<Node>(1000, new stateComparator1());
            Node start = new Node(initial, 0, initial.heuristics(method), null, null);
            pq.add(start);
            int count = 1;
            while(!pq.isEmpty()){
                Node min = pq.poll();
                if(min.heuristic == 0){
                    System.out.println("A-star Search Method: " + method + " Number of Total Steps: " + min.level);
                    min.printPath(min);
                    System.out.println();
                    System.out.println("Total Number of Nodes considered: " + count);
                    System.out.println();
                    return;
                }

                for(Puzzle state: min.puzzleState.addSuccessors()){
                    Node child = new Node(state, min.level + 1, state.heuristics(method), min, state.getDirection());
                    if(!pq.contains(child)){
                        pq.add(child);
                        count++;
                    }
                    //Whether maxnodes means number of nodes in priority queue or the total number of nodes that are being considered
                    if (pq.size() > initial.getMaxNodes()) {
                        throw new OutOfMemoryError("Too many nodes in priority queue");
                    }
                    if(count > initial.getMaxNodes()){
                        throw new OutOfMemoryError("Too many nodes considered");
                    }
                }
            }
        }
        else{
            System.out.println("Not solvable"); 
        }
    }

    public void localBeamSearch(Puzzle initial, int k){
        if(initial.isSolvable()){
            BeamNode start = new BeamNode(initial, 0, initial.heuristics("h2"), null, null);
            ArrayList<BeamNode> kBeamPaths = new ArrayList<BeamNode>();
            PriorityQueue<BeamNode> pq = new PriorityQueue<BeamNode>(1000, new stateComparator2());
            int totalNodes = 0;
            //for loop generates the first k paths
            for(Puzzle state : start.puzzleState.addSuccessors()){
                BeamNode child = new BeamNode(state, 1, state.heuristics("h2"), start, state.getDirection());
                pq.add(child);
            }
            int initialCount = 1;
            while(initialCount <= k & !pq.isEmpty()){
                BeamNode child = pq.poll();
                if(child.heuristic == 0){
                    System.out.println("Local Beam Search: Number of Total Steps: " + child.level);
                    child.printPath(child);
                    System.out.println("Total Number of Nodes considered: " + initialCount);
                    return;
                }
                kBeamPaths.add(child);
                initialCount++;
            }
            totalNodes += initialCount;
            boolean value = false;
            while(!value){
                //For loop goes through the k successive states
                for(int i=0; i<kBeamPaths.size(); i++){
                    pq.clear();
                    BeamNode hold = kBeamPaths.get(i);
                    //For loop generates the children of the k successive state
                    for(Puzzle puzzles : hold.puzzleState.addSuccessors()){
                        BeamNode children = new BeamNode(puzzles, hold.level + 1, puzzles.heuristics("h2"), hold, puzzles.getDirection());
                        pq.add(children);
                        totalNodes++;
                        //Whether maxnodes means number of nodes in priority queue or the total number of nodes that are being considered
                        if(totalNodes > initial.getMaxNodes()){
                            throw new OutOfMemoryError("Too many nodes considered");
                        }
                        if (pq.size() > initial.getMaxNodes()) {
                            throw new OutOfMemoryError("Too many nodes in priority queue");
                        }
                    }
                    //The temp beam node represents the best child/neighbor
                    BeamNode temp = pq.poll();
                    if(temp.heuristic == 0){
                        System.out.println("Local Beam Search: Solution Steps: " + temp.level);
                        temp.printPath(temp);
                        System.out.println();
                        System.out.println("Total Number of Nodes considered: " + totalNodes);
                        System.out.println();
                        return;
                    }
                    kBeamPaths.set(i, temp);
                }
            }
        }
        else{
            System.out.println("Not solvable");
        }
    }

    public static void main(String[] args) {
        Puzzle puzzle = new Puzzle();
        puzzle.randomizeState(60);
        puzzle.maxNodes(Integer.MAX_VALUE);
        Search search = new Search(puzzle);
        search.aStarSearch(puzzle,"h2");
        search.aStarSearch(puzzle,"h1");
        search.localBeamSearch(puzzle, 3);
        
    }
}

class stateComparator1 implements Comparator<Node> {
    public int compare(Node n1, Node n2){
        if(n1.cost < n2.cost){
            return -1;
        }
        else if(n1.cost > n2.cost){
            return 1;
        }
        else{
            return 0;
        }
    }
}

class stateComparator2 implements Comparator<BeamNode>{
    public int compare(BeamNode n1, BeamNode n2){
        if(n1.heuristic < n2.heuristic){
            return -1;
        }
        else if(n1.heuristic > n2.heuristic){
            return 1;
        }
        else{
            return 0;
        }
    }
}
