import java.awt.*;

public class Cell {
    private boolean isAlive;
    public int row, col;
    private int age;

    public static final int SIZE = World.pixelInterval;

    public Cell(int r, int c){
        isAlive = false;
        row = r;
        col = c;
        age = 0;
    }
    public void kill(){
        isAlive = false;
    }
    public void spawn(){
        isAlive = true;
        age = 0;
    }
    public void age() { age++; }

    public boolean getIsAlive() { return isAlive; }

    /*
    returns the number of living cells around this cell.
     */
    public int numNeighbors(Cell[][] cells){
        int numNeighbors = 0;
        for (int i = -1; i <= 1; i++) { // range is from [-1, 1] so that I can check one to the left and right of cell, plus diagonal
            for (int j = -1; j <= 1; j++) { // range is from [-1, 1] so that I can check one to the top and bottom of cell, plus diagonal
                if(i == 0 && j == 0) continue; // don't want to check the cell itself

                if((row + i) >= 0 // to make sure it doesn't check to the left of a cell if it's on left most edge, etc.
                        && (col + j) >= 0 // to make sure it doesn't check to the top of a cell if it's on top most edge, etc.
                        && (row + i) < 100 // to make sure it doesn't check to the right of a cell if it's on right most edge, etc.
                        && (col + j) < 100 // to make sure it doesn't check to the bottom of a cell if it's on the bottom most edge, etc.
                        && cells[row+i][col+j].isAlive) // checks if cell to the top ( @[row, col-1] ), top left ( @[row-1, col-1] ),
                    numNeighbors++; //                               bottom ( @[row, col+1] ), bottom right ( @[row+1, col+1] ), etc. is alive
            }
        }
        return numNeighbors;
    }

    /*
    returns true if this Cell should be alive next generation.
    returns false if this Cell should not be alive next generation.
     */
    public boolean nextFate(Cell[][] cells){
        boolean fate = false;
        if(isAlive && (numNeighbors(cells) == 2 || numNeighbors(cells) == 3)) fate = true; // if a LIVE CELL has two or three neighbors, keep it alive
        if(!isAlive && numNeighbors(cells) == 3) fate = true; // if a DEAD cell has three neighbors, spawn it
        return fate;
    }

    public void draw(Graphics2D g2){
        g2.setColor(Color.lightGray);
        g2.drawRect(col*SIZE, row*SIZE, SIZE, SIZE);
        if(isAlive) {
            switch (age) {
                case 0:
                    g2.setColor(Constants.newCell);
                    break;
                case 1:
                    g2.setColor(Constants.stage1Cell);
                    break;
                case 2:
                    g2.setColor(Constants.stage2Cell);
                    break;
                case 3:
                    g2.setColor(Constants.stage3Cell);
                    break;
                case 4:
                    g2.setColor(Constants.stage4Cell);
                    break;
                case 5:
                default:
                    g2.setColor(Constants.stage5Cell);
                    break;
            }
            g2.fillRect(col*SIZE, row*SIZE, SIZE, SIZE);
        }
    }
}