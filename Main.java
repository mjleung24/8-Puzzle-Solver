import java.io.*;  // Import the Scanner class
public class Main {
    
    public static void main(String[] args) throws IOException{

        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("TestCode.txt"));
            String line = reader.readLine();
            Puzzle puzzle = new Puzzle();
            while(line != null){
                System.out.println("Command: " + line);
                if(line.contains("end")){
                    System.out.println();
                }
                else if(line.contains("setState")){
                    String[] split = line.split(" ", 2);
                    puzzle.setState(split[1]);
                    System.out.println("Puzzle in this position: " + puzzle.printState());
                }
                else if(line.contains("printState")){
                    System.out.println(puzzle.printState());
                }
                else if(line.contains("move")){
                    String[] split = line.split(" ", 2);
                    String direction = split[1];
                    puzzle.move(direction);
                    System.out.println(puzzle.printState());
                }
                else if(line.contains("randomizeState")){
                    String[] split = line.split(" ", 2);
                    String str = split[1];
                    int number = Integer.parseInt(str);
                    int maxnodes = puzzle.getMaxNodes();
                    puzzle = new Puzzle();
                    puzzle.randomizeState(number);
                    puzzle.maxNodes(maxnodes);              
                }
                else if(line.trim().contains("solve A-Star")){
                    String[] split = line.split(" ",3);
                    String heuristic = split[2];
                    if(puzzle.getMaxNodes() <= 0){
                        System.out.println("No available nodes to operate search");
                    }
                    Search search = new Search(puzzle);
                    if(heuristic.equals("h1")){
                        search.aStarSearch(puzzle, "h1");
                    }
                    else if(heuristic.equals("h2")){
                        search.aStarSearch(puzzle, "h2"); 
                    }
                    else{
                        System.out.println("Not a valid heuristic");
                    }
                }
                else if(line.trim().contains("solve beam")){
                    String[] split = line.split(" ", 3);
                    String kValue = split[2];
                    int k = Integer.parseInt(kValue);
                    Search search = new Search(puzzle);
                    search.localBeamSearch(puzzle, k);
                }
                else if(line.trim().contains("maxNodes")){
                    String[] split = line.split(" ", 2);
                    String str = split[1];
                    int n = Integer.parseInt(str);
                    puzzle.maxNodes(n);
                }

                line = reader.readLine();
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}