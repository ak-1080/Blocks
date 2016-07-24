public class Block{
    
   // public int x,y,length,width;
    private int x;//upper right corner representing the row
    private int y;//upper right corner representing the column
    private int length; //integer length of the block
    private int width;//integer width 
    public boolean goal = false; // separate variable used primarily by the A* algorithm
    
    
   
    public Block(int x1, int y1, int length1, int width1){
        
        x = x1;
        y = y1;
        length = length1;
        width = width1;
    
    }
    
   
    public Block( int x2,int y2, Block b){ // new block with updated coordinates x2, y2
    
        x = x2;
        y  = y2;
        length = b.length;
        width = b.width;
        goal = b.goal;
    
    }
    
   
	public boolean equals( Object o ){
        Block b = (Block) o;
        // System.out.println("Goal: "+b);
        // System.out.println("THIS: "+this);
    
        return  (x==b.x&&y == b.y&&length == b.length&&width == b.width);
    
    }
	
	@Override
    public int hashCode(){
        
		//System.out.println("in here");
        return this.toString().hashCode();

    }

    public int getX(){return x;}
    public int getY(){return y;}
    public int getLength(){return length;}
    public int getWidth(){return width;}
    
   
    public void setX(int x){
        this.x = x;
    }
   
    public void setY(int y){
        this.y = y;
    }
    
    
    
    public void setLength(int length){
        this.length = length;
    }
    
    
   
    public void setWidth(int width){
        this.width = width;
    }
    
    //toString method--returns a clear string representation of the block's information
    public String toString(){
        
       
        return "["+length+"x"+width+"--"+"("+ x+","+y+")]";

    }
    
    
    public static void main(String [] args){
    
        Block b = new Block(2,2,5,5);
        System.out.println(b);
    
    
    }

}