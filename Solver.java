import java.io.*;
import java.util.*;
import java.lang.*;
public class Solver{


    public static void main(String [] args){
    
    
        try
    	{ 
            
            //Debugging information 
            boolean debug = false;//checks if the debugging is enabled
            Scanner input = new Scanner(System.in);//input scanner for the input
            
            //defualt command line positions without debugging options
            //init represent the initial configuration index
            //goalPos represents the index of the goal configuration
            int init = 0;
            int goalPos = 1;
            int in = 0;
            String out = "";
            //if the user selects the debugging option
            if(args[0].equals("-ooptions")){
                debug = true;
                init++;//both init and goalPos are incremented
                goalPos++;
                //debuggin menu
                out="1-Print current puzzle\n";
                out +="2-Display Parent and successors\n";
                out+="3-Display initial block list\n";
                System.out.println(out);
                in = input.nextInt();
                
            
            }
                
             //File reader for the initial configuration text file   
    	    FileReader reader = new FileReader(args[init]);
	        BufferedReader inFile = new BufferedReader( reader );
            
            //File reader for the goal position 
            FileReader reader2 = new FileReader(args[goalPos]);
	        BufferedReader inFile2 = new BufferedReader( reader2 );
            
            
            //set to store the list of blocks
            HashSet<Block> goalList = new HashSet<Block> ();
            
            String goal = inFile2.readLine();//read the first line in the goal Text file
            
            //continue reading goal configuration till the end
            while ( goal != null )
		    {
                
                //Split the string of line by spaces
                String [] num = goal.split(" ");
                //convert the information into integers
                int length = Integer.parseInt(num[0]);
                int width = Integer.parseInt(num[1]);
                int x =Integer.parseInt(num[2]);
                int y = Integer.parseInt(num[3]); 
                
                //create a new goal block
                Block b = new Block(x, y, length, width);
                //add the block to the goal list
                goalList.add(b);
                goal = inFile2.readLine();
			}
            
          //****NOW Parsing Initial configuration text File
            
	        //reading initial configuration
            String line = inFile.readLine();//read first line which contains only the dimensions of the grid
            String[] firstLine  = line.split(" ");
            int row = Integer.parseInt(firstLine[0]);//#number of rows
            int col = Integer.parseInt(firstLine[1]);//#number of columns
          
          
            
            char [][] puzzle = new char [row][col];//create an empty array with the given dimensions
	        HashSet<Block> blocks = new HashSet<Block>();//empty hashSet to store the blocks
           
            line = inFile.readLine();//read second line
	        
	        while ( line != null )
		    {
                //Split the string of line by spaces
                String [] num = line.split(" ");
                int length = Integer.parseInt(num[0]);
                int width = Integer.parseInt(num[1]);
                int x =Integer.parseInt(num[2]);
                int y = Integer.parseInt(num[3]); 
                
                
                //create a new goal block
                Block b = new Block(x, y, length, width);
                
                //if the generated block is a goal block, that block's goal Attribute is assigned to true--A* search
                for(Block goalBlock: goalList){
                    if(b.getLength() == goalBlock.getLength() && b.getWidth() == goalBlock.getWidth()){
                        b.goal = true;
                        }
               }
                blocks.add(b);//add block to the set
                line = inFile.readLine();
			}
            
            //close both input buffers
			inFile.close();
            inFile2.close();
            
            //Assign block dimensions in the final array
            for(Block b: blocks){
                for(int i = b.getX();i<b.getLength()+b.getX();i++)
                    for(int j = b.getY() ; j<b.getWidth()+b.getY(); j++){
                      
                        puzzle[i][j] = 'f';
                     }
            }
		
            
            //create initial TrayConfig object
            TrayConfig initialConfig = new TrayConfig(puzzle,row, col, blocks);
            
            //for debugging output only
            if(debug&&in == 1)
                System.out.println("Initial Puzzle Information:\n"+initialConfig);
            
            if(debug&&in == 3){
                System.out.println("Block List:");
                for(Block b: blocks)
                    System.out.println(b);
            }
           
           //calcute the run time duration of the program

           boolean steps = solveDFS(initialConfig, goalList, debug, in);
           
            if(!steps)
                     System.exit(1);//exit if there's no solution
            
       
          
       
    	}
    	catch(FileNotFoundException ex){}//catch a bunch of exceptions 
    	catch(IOException ex){}
  
    }
    
    //Method that seaches for the solution using DFS
    //TrayConfig mainConfig--the initial tray configuration
    //HashSet<Block> goalList--the set of blocks that each child configuration will be matched against
    //boolean debug--debug on or off
    //int in--debug option
    public static boolean solveDFS(TrayConfig mainConfig, HashSet<Block> goalList, boolean debug, int in){
    
        ArrayDeque <TrayConfig> stack = new ArrayDeque<TrayConfig>();//created a new data structure that represents the stack
        stack.push(mainConfig);//add the first configuration to the stack
       
		HashSet<Integer> set = new HashSet<Integer>();//THE main set to store the unique integer layouts
         
        //LinkedList to store parent-child moves
        LinkedList<ParentInfo> info= new LinkedList<ParentInfo>();
        
        
        
        //DFS algorithm
        //runs untill the stack is empty
        while (!stack.isEmpty()){
        
            
            TrayConfig removed = stack.removeLast();//Removes the last node aded (DFS)
           
            if(TrayConfig.isGoal(removed, goalList)){//if that node/tray matches goal, then we're done
                 
                   //if true:
                   //store all the parent-child information in the linkedList
				   while (removed.parent.tray!=null){
					
					info.add(removed.parent);
					removed = removed.parent.tray;
				   
				   }
				   
                   //iterate backwards to output first to last actions
				   for(int i = info.size()-1; i >=0; i--)
						System.out.println(info.get(i).oldInfo+" "+info.get(i).newInfo);
                    
                    if(in == 4)
                        System.out.println("Number of steps:"+info.size());
         
                return true;
            }
            
				//generate the unique integer for each layout
				int uniqueLayout = removed.integerLayout();
                
                //if we havent seen this layout before, then
                //we add it to the set and its children to the stack
                if (!set.contains(uniqueLayout)){
                    
						set.add(uniqueLayout);//add to the hashSet

                        //print debuggin information
						if(debug&&in==2){
							System.out.println("Parent");
							System.out.println(removed);
							System.out.println("Successors");
						 
						}
						
                        //iterate over all possible children 
						for(TrayConfig config: removed.successors()){
							
								if(debug&&in==2)
									System.out.println(config);
							
                                config.setCost(removed.getCost()+1);
                              
							   //add child to the stack
								stack.add(config);
                             
					 
						}
				
                }
        }
        
        
       return false;
    
    }
    
 

}