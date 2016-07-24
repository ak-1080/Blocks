import java.util.*;
public class TrayConfig implements Comparable<TrayConfig> {
//Comparable is mainly for the possible implementation of the A* algorithm 

    private int heuristic = 0;//A*--attribute representing an integer estimate of each configuration
    private int cost = 0;//A*--total cost of each configuration from initial configuration
    private char [][] puzzle;//char(2 byte/char) array representing the location of the blocks within the grid
    private int row;//number of rows
    private int col;//number of columns
    private  HashSet< Block> blocks; // set of blocks in grid puzzle
	public ParentInfo parent = new ParentInfo();//Parent information-- holds the parent of each configuration and the corresponding move from the parent
    private enum Moves {LEFT, RIGHT, UP, DOWN}//enumeration--set of possible moves available for each block
	
	//Add a sorted list of blocks
   
   
  
   //int row-- number of rows
   //int col -- number of columns
   //Hashset<Blocks> list --list of each block in the grid
    public  TrayConfig(char [][] puzzle, int row, int col, HashSet<Block> list){
    
       this.puzzle = puzzle;
       this.row = row;
       this.col = col;
       blocks = list;
      
    }
    
    //returns cost--distance-- of each configuration
    public int getCost(){return cost;}
    //returns the estimate of each configuration
    public int getHeuristic(){return heuristic;}
    //returns number of rows
    public int getRow(){return row;}
    //returns number of columns
    public int getColumn(){return col;}
    
    //sets a new cost for each configuration
    public void setCost(int cost){this.cost= cost;}
    
    //sets a new estimate
    public void setHeuristic(int heuristic){this.heuristic = heuristic;}
    
    
    
    //successors method--returns a linkedList of all possible child 
    //configuration from the current configuration 
    public LinkedList<TrayConfig> successors(){ 
        LinkedList < TrayConfig > configs = new LinkedList<TrayConfig> ();//list to be returned
       
       
       //iterate over all blocks in the hashSet 
       for(Block b: blocks)
            //check, for each block, all the possible moves (up, down, left, right)
            for (Moves move : Moves.values()){
             
                //obtain new possible coordinates after every move and update the array
                Tuple newC = updateC(b, move, puzzle);
                
                //if newC is null--> block move is not possible
                //then no further action is taken 
                if( newC!=null){
                    //if move is possible
                    //generate a new set of block list with only ONE modified block (Block b)
					HashSet <Block> newHashSet = new HashSet<Block>(); 
                    for(Block oldBlock: blocks)
                        if(!oldBlock.equals(b))//add all other blocks but not b
							newHashSet.add((new Block(oldBlock.getX(), oldBlock.getY(), oldBlock)));
					
					//add the final modified block to the list
                     newHashSet.add(new Block(newC.x, newC.y, b));
					 char newArray[][] = newPuzzle(newHashSet); //generate new grid information 
                     
                     //finally, create the a configuration based on the new of blocks and grid information
                     TrayConfig c = new TrayConfig(newArray, row, col, newHashSet); //NEW TRAY PUZZLE FOR EVERY POSSIBLE MOVE
					 
                    //***Efficiency Improvement #1:
                    /*In order to produce the final output, a string representing had to be controlled from the beginning of the execution 
                    till the end of the program. However, Since String class is immutable, it can cause a large over head when it comes to concatenating different strings.
                    This results in massive overhaul of the system memory and can cause overflow in certain cases since a new string has to be created using the 
                    already large string information of the old one. Therefore, I ended up using a parent pointer to track 
                    the moves and the parent. 

                    */
                     
                    
                     //create parent information
                     c.parent.tray = this;//the new configuration's parent is the current config
					 c.parent.oldInfo = b.getX()+ " "+b.getY();//old information is block b's location
					 c.parent.newInfo = newC.x+" "+newC.y;//new information--move--updated block b's location
                     configs.add(c);//add the complete configuration to the return list
                  }
				  
                
            }
            
        return configs;//return all possible configuration for each block after one move
     }
     
     
    
     //Block b-- the block that is being moved
     //Move move--the corresponding action, from the enumeration list, that is taken for that block
     //char [][] arr-- the grid information that contains the block information(in the form of empty or occupied space)
     //returns a Tuple (type Tuple) coordinates of the newly updated coordinates
     //if the move is not possible, null is returned
     public Tuple updateC ( Block b, Moves move, char [][] arr){
        
        Tuple newC = null;//Tuple that holds the information
        boolean possible = true;//checks the possiblity of a block move
       
       //switch for the move cases
       switch(move){
            
            //check if the required move is left move
            case LEFT: 
                
               
                if (b.getY() > 0){ // make sure we can move one step <-- 
                   
                    for (int i = 0; i< b.getLength(); i++)
                        if(arr[b.getX()+i][b.getY()-1] !='\u0000')
                            possible = false;//if there is occuped space to the left of the block, then the move is impossible and null is returned
                    
                    /
                    if(possible)
                         newC = new Tuple(b.getX(), b.getY()-1);
                    
                }
               
                break;
                
            //right move
            case RIGHT:
                
                if(b.getY()+b.getWidth()-1 < col-1){
                    
                     for (int i = 0; i< b.getLength(); i++)
                        if(arr[b.getX()+i][b.getY()+b.getWidth()] !='\u0000')//check if it's possible to shift block to the right
                            possible = false;
                    
                    
                    
                    if(possible)
                       newC = new Tuple(b.getX(), b.getY()+1);
                        
                    
      
                }
     
                break;
            
            //up move
            case UP:
                
                if(b.getX() >0){
                    
                    for(int i =0; i<b.getWidth(); i++)
                        if(arr[b.getX()-1][b.getY()+i] != '\u0000')
                            possible = false;
                    
                    
                    
                    if(possible)
                       newC = new Tuple(b.getX()-1, b.getY());

                    
               
                }
                break;
            case DOWN:
                
               
                if(b.getX()+b.getLength()-1<row-1){
                    
                  
                   for(int i = 0; i<b.getWidth(); i++)
                     if(arr[b.getX()+b.getLength()][b.getY()+i]!='\u0000')
                        possible = false;
                  
                    if(possible)
                        newC = new Tuple(b.getX()+1, b.getY());
                    

                }
                break;
        }
        
        return newC;//return the updated moves
     
     }
     
     //returns a new puzzle based on current dimensions of the original puzzle
     public char [][] newPuzzle (HashSet <Block> list){
        
        char [][] newP = new char [row][col];
		
       for(Block b: list){
               
                for(int i = b.getX();i<b.getLength()+b.getX();i++)
                    for(int j = b.getY() ; j<b.getWidth()+b.getY(); j++)
                        newP[i][j] = 'f';
                     
            }
        return newP;
     }
    
    
     //comparesTo method is used to compare to configuration based on the estimates in the priority queue
     //Mainly used if A* algorithm is used
     @Override
      public int compareTo(TrayConfig t){
            return (heuristic < t.heuristic) ? -1 : ((heuristic == t.heuristic) ? 0 : 1);
      }
        
      //isGoal method is a static method that checks if any configuration is a goal state 
      //based on the set of goal blocks (the blocks we want in the final position)
      //TrayConfig t--a configuration that we want to check for its goalness
      //Precondition: number of blocks in t and in the goalList must be equal
      //HashSet<Block> goalList--the set of gaol Blocks
      //returns true if configuration matches the goal configuration, false otherwise
      public static boolean isGoal(TrayConfig t, HashSet<Block> goalList)
      {
        //checking is preformed by comparing the list in t
        //and whether or not they match the blocks in the goal list
		for (Block goal: goalList)
            if(!t.blocks.contains(goal))
                return false;
                
                
        //***Efficiency Improvement:#2
        /*Since the comparison is preformed with an O(N^2) implementation,
         comparing a large number of blocks can take a lot of time and be more expensive (big.Tray.3 and 4)
         so in order to reduce the runtime, I used a hashSet which for both the goalList and the list of blocks in
         a configuration in order to to reduce the implementation ~O(N). This will make the program solver execute much faster
         in order to find a solution more quickly

        */
        return true;
      }
      
	 
      //Maps each configuration to a unique integer( 2^32 possible mappings)
      //Assumption that the state space of each puzzle is less than 2^32 in order for the solver to work
	  public int integerLayout(){
	  
        //the integer to be returned
		int hash = 0;
            
            //add all blocks information into a single integer
           for(Block b: blocks){   
                //In order to reduce the possiblity of different configuration to be mappend to the same integer
                //double is used to generate non-integer total based on each block's attributes
				Double row =(double)b.getX()*2.232;
				Double col =(double) b.getY()*5.787;
				Double length = (double)b.getLength()*8.84;
				Double width = (double)b.getWidth()*1.423;
				Double total = row+col+length+width;

				 //hash += total.hashCode()-340366;
                 hash += total.hashCode();//add the hashcode of the total to the return integer
			}
            
        //***Efficiency Improvement:#3
        /*Since I wanted to minimize the runtime of this method and the program overall, an O(N) implementation is used instead of other 
        more complex implementations that requite the traversal of the array and checking whether a block's location matches that location in the array
        O(N * (row*column)). Also, there are no string manipulation in order to avoid memory overhead. 
      
        */
		
					
            
          
		  return hash;//return that unique integer
		  
      
      }
	  
        
	   public boolean equals(Object o){
        
		TrayConfig c = (TrayConfig) o;//case the object to trayConfig
		
		//Checks if all the blocks in this hashSet matches the blocks in c's set
		for(Block myBlock: blocks)
				if (!c.getBlocks().contains(myBlock))
					return false;
		
		return true;
        

    }
    
    
    //isOk method tests whether a block is occupying someone else's location
    //or is out of bounds.
    //if the blocks are all in their proper place, true is returned and false otherwise.
    public boolean isOk() throws IllegalStateException{//checks that all blocks are in proper positions. 
    
        
        //check if each block is occupying a different block in the same set
        for(Block b1: blocks)
            for (Block b2: blocks){//second block in the same set different than b1
                if(!b1.equals(b2))
                    //if blocks occupy same location then an exception is thrown
                    if(b2.getX() == b1.getX()&&b2.getY() == b1.getY())
                        throw new IllegalStateException("Different blocks occupying same space!!");
                
                //if blocks are out bounds on all edges, an exception is also thrown.
                if (b2.getY() < 0||b2.getY()+b2.getWidth()-1 > col-1)    
                    throw new IllegalStateException("Block out of bounds--horizontal!!");
                
                 if(b2.getX() <0||b2.getX()+b2.getLength()-1>row-1)
                    throw new IllegalStateException("Block out of bounds--vertical!!");
                 
                
                }
          
            
            
        //returns true if the configuration has all blocks in their proper place within the grid.
        return true;
    
    }
    
    //main method for testing
    public static void main(String [] args){
        
        Block b1 = new Block (0,0, 1, 2);//1x2 at 0,0
        Block b2 = new Block (0,2, 1, 2);//1x2 at 0,0
        Block conflic = new Block (0,0, 1, 3);//1x2 at 0,0
        char [][] p = new char [5][4];//puzzle grid 5x4
        HashSet<Block> lis = new HashSet<Block>();
        lis.add(b1);
        lis.add(b2);
        
        
         for(Block b: lis){
               
                for(int i = b.getX();i<b.getLength()+b.getX();i++)
                    for(int j = b.getY() ; j<b.getWidth()+b.getY(); j++)
                        p[i][j] = 'f';
                     
            }
        TrayConfig c = new TrayConfig(p, 5, 4, lis);
        lis.add(conflic);
        TrayConfig conflict = new TrayConfig(p, 5, 4, lis);
        
        System.out.println("Hello\n"+c.isOk());
       // System.out.println("Hello\n"+conflict.isOk());
      
    
    
    }
      
      //returns the set of blocks
      public HashSet<Block> getBlocks(){
      
        return blocks;
      }
      
      //Returns a String representation of the grid
      public String toString(){
      
        String out="";
        
        for(int i = 0 ; i < row; i++){
            for (int j = 0; j < col; j++)
                out+=""+puzzle[i][j];
            out+="\n";
        }
            
        return out;
      
      }
      
      
}

//*** BASIC HELPER CLASSES***
//Not really needed, but I kept them anyway 

//Tuple class that holds two integrs (the row (x) and column (y) locations of each block)
class Tuple { 
  public final int x; 
  public final int y; 
  public Tuple(int x, int y) { 
    this.x = x; 
    this.y = y; 
  } 
  
  
  public Tuple(Tuple t) { 
    this.x = t.x; 
    this.y = t.y; 
  } 
} 

//ParenInfo class that has a parent pointer as well as a String containing information
//the old and new move
class ParentInfo{
	
	public TrayConfig tray;
	public String newInfo;
	public String oldInfo;
	
	// public ParentInfo(Tratray, tup) { 
    
  // } 

}

