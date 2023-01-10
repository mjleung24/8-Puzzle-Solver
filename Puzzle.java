import java.lang.Math;
import java.util.*;

public class Puzzle {

    private char[][] puzzle;
    //Curent position of the blank char
    private static int[] indexB;

    //{{'b',1,2},{3,4,5},{6,7,8}}
    private static int[][] solutionIndex;
    //Used to calculate Manhattan distance, where the first row represents the row index of the desired value 
    //and the second row represents the column index of the desired value

    private int maxNodes;
    private String direction;

    public Puzzle(){
        puzzle = new char[][] {{'b','1','2'},{'3','4','5'},{'6','7','8'}};
        solutionIndex = new int[][] {{0,0,1,1,1,2,2,2},{1,2,0,1,2,0,1,2}};
        indexB = new int[] {0,0};
        maxNodes = 0;
        direction = null;
    }

    public Puzzle(char[][] puzzle){
        this.puzzle = puzzle;
        setIndex();
    }

    public char[][] getCurrentState(){
        return puzzle;
    }

    public int[] getIndex(){
        return indexB;
    }

    public int getMaxNodes(){
        return maxNodes;
    }

    public void setMaxNodes(int n){
        maxNodes = n;
    }

    public String getDirection(){
        return direction;
    }

    public void setDirection(String direction){
        this.direction = direction; 
    }

    public void maxNodes(int n){
        this.maxNodes = n;
    }

    public void setIndex(){
        int[] index = new int[2];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(getCurrentState()[i][j]=='b'){
                    index[0]=i;
                    index[1]=j;
                }
            }
        }
        indexB = index;
    }

    public void setState(String state){
        String  newState = state.replaceAll("\\s", "");
        if(newState.length() != 9){
            throw new IllegalArgumentException("Not a legal state");
        }

        StringBuilder finalState = new StringBuilder(newState);

        for(int i=0; i<newState.length(); i++){
            finalState.setCharAt(i, newState.charAt(i));
        }

        for(int j = 0; j < 3; j++){
            for(int k = 0; k < 3; k++){
                puzzle[j][k] = finalState.charAt(k + j*3);
            }
        }
        setIndex();
    }

    public char[][] copyState(char[][] state){
        char[][] copy = new char[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                copy[i][j] = state[i][j];
            }
        }
        return copy;
    }

    // blank = [0,0] target = [1,0]
    //First index is the row, second index is the column
    //Swap method will only be used to switch the blank with a number, we don't need to have the parameter of where the index B is because it's constantly being updated
    private void swap(int target[]){
        int row = target[0];
        int column = target[1];
        getCurrentState()[indexB[0]][indexB[1]] = getCurrentState()[row][column];
        getCurrentState()[row][column] = 'b';
        indexB[0] = row;
        indexB[1] = column;
    }

    public String printState(){
        String state = "";
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                state = state + getCurrentState()[i][j];
            }
        }
        return state;
    }


    
    private void moveUp(){
        int row = indexB[0];
        int column = indexB[1];
        int[] target = new int[] {row-1, column};
        swap(target);
    }

    private void moveDown(){
        int row = indexB[0];
        int column = indexB[1];
        int[] target = new int[] {row+1, column};
        swap(target);
    }

    private void moveRight(){
        int row = indexB[0];
        int column = indexB[1];
        int[] target = new int[] {row, column+1};
        swap(target);
    }

    private void moveLeft(){
        int row = indexB[0];
        int column = indexB[1];
        int[] target = new int[] {row, column-1};
        swap(target);
    }

    public void move(String direction){
        if(direction.equals("up") & indexB[0]!=0){
            moveUp();
        }
        else if(direction.equals("down") & indexB[0] != 2){
            moveDown();
        }
        else if(direction.equals("right") & indexB[1] != 2){
            moveRight();
        }
        else if(direction.equals("left") & indexB[1] != 0){
            moveLeft();
        }
    }

    public int heuristicOne(){
        int misplaced = 0;
        int counter = 48;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int value = puzzle[i][j];
                if(i==0 & j==0){
                    if(puzzle[i][j] != 'b'){
                        misplaced = 0;
                    }
                }
                else if(value != counter){
                    misplaced++;
                }
                counter++;
            }
        }
        return misplaced;
    }

    public int heuristicTwo(){
        int count = 0;
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                if(puzzle[i][j] != 'b'){
                    int x = puzzle[i][j] - '0';
                    count = count + Math.abs(i-solutionIndex[0][x-1]) + Math.abs(j-solutionIndex[1][x-1]);
                }
            }
        }
        return count;
    }

    public int heuristics(String method){
        int heuristic = 0;
        if(method.equals("h1")){
            heuristic = heuristicOne();
        }
        else if(method.equals("h2")){
            heuristic = heuristicTwo();
        }
        return heuristic;
    }

    public void randomizeState(int n){
        int count = 0;
        Random randomSeed = new Random(420);
        while(count<n){
            
            //int random = (int) (Math.random()*4);
            int random = randomSeed.nextInt(4); 
            if(random == 0 & indexB[0]!=0){
                move("up");
                count++;
                System.out.println("Move " + count +  ": up" + " " + printState());
            }
            else if(random == 1 & indexB[0] != 2){
                move("down");
                count++;
                System.out.println("Move " + count +  ": down" + " " + printState());
            }
            else if(random == 2 & indexB[1] != 0){
                move("left");
                count++;
                System.out.println("Move " + count +  ": left" + " " + printState());
            }
            else if(random == 3 & indexB[1] != 2){
                move("right");
                count++;
                System.out.println("Move " + count +  ": right" + " " + printState());
            }
        }
        System.out.println("Randomized State: " + printState());
    }

    public boolean isSolvable(){
        int count = 0;
        ArrayList<Character> array = new ArrayList<Character>();

        for(int i = 0; i < puzzle.length; i++){
            for(int j = 0; j<puzzle.length; j++){
                array.add(puzzle[i][j]);
            }
        }
        Character[] compare = new Character[array.size()];
        array.toArray(compare);
        for(int i = 0; i<compare.length-1; i++){
            for(int j = i+1; j<compare.length; j++){
                if(compare[i] != 'b' && compare[j] != 'b' && compare[i] > compare[j]){
                    count++;
                }
            }
        }
        return count % 2 == 0;
    }

    public ArrayList<Puzzle> addSuccessors(){
        ArrayList<Puzzle> children = new ArrayList<Puzzle>();

        if(indexB[0] != 0){
            Puzzle child = new Puzzle(copyState(getCurrentState()));
            child.move("up");
            child.setDirection("Up");
            children.add(child);
        }
        if(indexB[0] != 2){
            Puzzle child = new Puzzle(copyState(getCurrentState()));
            child.move("down");
            child.setDirection("Down");
            children.add(child);
        }
        if(indexB[1] != 0){
            Puzzle child = new Puzzle(copyState(getCurrentState()));
            child.move("left");
            child.setDirection("Left");
            children.add(child);
        }
        if(indexB[1] != 2){
            Puzzle child = new Puzzle(copyState(getCurrentState()));
            child.move("right");
            child.setDirection("Right");
            children.add(child);
        }
        return children;
    }
}
