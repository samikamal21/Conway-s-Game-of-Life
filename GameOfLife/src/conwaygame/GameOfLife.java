package conwaygame;
import java.util.ArrayList;

/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    private int ROW;
    private int COL;

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        ROW = 5;
        COL = 5;
        grid = new boolean[ROW][COL];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
   
    public GameOfLife (String file) {
        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        ROW = StdIn.readInt();
        COL = StdIn.readInt();
        grid = new boolean[ROW][COL];

        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[0].length; c++) {
                grid[r][c] = StdIn.readBoolean();
            }
        }
        getTotalAliveCells();
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid() {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[0].length; c++) {
                if(grid[r][c] == ALIVE)
                    totalAliveCells++;
            }
        }
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {
        if(grid[row][col] == ALIVE)
            return ALIVE;
        else
            return DEAD;
    }
    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        // WRITE YOUR CODE HERE
        for(int r = 0; r < ROW; r++) {
            for(int c = 0; c < COL; c++) {
                if(grid[r][c] == ALIVE)
                    return true;
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        // WRITE YOUR CODE HERE
        int numOfNeighbors = 0;
        int startOfRow = Math.max(row-1,0);
        int endOfRow = Math.min(row+1,grid.length - 1);

        int startOfCol = Math.max(col-1,0);
        int endOfCol = Math.min(col+1,grid[0].length - 1);

        for(int r = startOfRow; r <= endOfRow; r++) {
            for(int c = startOfCol; c <= endOfCol; c++) {
                if(r == row && c == col) continue;
                if(grid[r][c] == ALIVE) numOfNeighbors++;
            }
        }
        return numOfNeighbors; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        // WRITE YOUR CODE HERE
        boolean[][] newGrid = new boolean[ROW][COL];
        int rows = grid.length;
        int cols = grid[0].length;
       
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                int numOfNeighbors = numOfAliveNeighbors(r,c);
                if(grid[r][c] == ALIVE) {
                    if(numOfNeighbors == 2 || numOfNeighbors == 3) {
                        newGrid[r][c] = ALIVE;
                    }
                }
                else {
                    if(numOfNeighbors == 3) 
                        newGrid[r][c] = ALIVE;
                    else
                        newGrid[r][c] = DEAD;
                }
            }
        }
        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                grid[r][c] = newGrid[r][c];
            }
        }
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        // WRITE YOUR CODE HERE
        computeNewGrid();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        // WRITE YOUR CODE HERE
        for(int i = 0; i < n; i++) {
            computeNewGrid();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        // WRITE YOUR CODE HERE
        WeightedQuickUnionUF U = new WeightedQuickUnionUF(ROW, COL);
        int count = 0;

        for(int row = 0; row < grid.length; row++) {
            for(int col = 0; col < grid[0].length; col++) {

                int startOfRow = Math.max(row-1,0);
                int endOfRow = Math.min(row+1,grid.length - 1);

                int startOfCol = Math.max(col-1,0);
                int endOfCol = Math.min(col+1,grid[0].length - 1);

                for(int r = startOfRow; r <= endOfRow; r++) {
                    for(int c = startOfCol; c <= endOfCol; c++) {
                        if(r == row && c == col) continue;

                        if(startOfRow == U.find(r,c) || col == U.find(row,col) && grid[r][c] == ALIVE) {
                            U.union(r, col, row, c);
                            count++;
                        }
                        if(isAlive() == false) count = 0;
                    }
                }
            }
        } 
        
        return count; // update this line, provided so that code compiles
    }
}
