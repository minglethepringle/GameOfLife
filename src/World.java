import javax.swing.*;
import java.awt.*;

public class World {

    private int rows, cols;
    public static int pixelInterval;
    private Cell[][] cells;
    public static double spawnChance = 0.5;

    /*
    Creates a world of cells, devoid of life.
     */
    public World(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        pixelInterval = LifePanel.width / cols; // Used for calculating cell width/height, not necessary
        randomizeWorld();
    }

    /*
    Sets every cell to be alive or not alive with a 50% chance.
     */
    public void randomizeWorld(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(i, j);
                if(Math.random() >= 1 - spawnChance) cells[i][j].spawn();
            }
        }
    }

    public void drawWorld(Graphics2D g2){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].draw(g2);
            }
        }
    }

    public void resetWorld() {
        clearWorld();
        randomizeWorld();
    }

    public void clearWorld() {
        this.cells = new Cell[rows][cols];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    public void nextGeneration(){
        boolean[][] nextGenFate = new boolean[cells.length][cells[0].length];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                nextGenFate[i][j] = cells[i][j].nextFate(cells);
            }
        }

        for (int i = 0; i < nextGenFate.length; i++) {
            for (int j = 0; j < nextGenFate[i].length; j++) {
                if(nextGenFate[i][j]) { //logic of the cell; if the cell's corresponding value in the nextGenFate array is true, that means
                    if(cells[i][j].getIsAlive()) {
                        cells[i][j].age();
                    } else {
                        cells[i][j].spawn();
                    }
                } else {
                    cells[i][j].kill(); // otherwise the cell should be killed
                }
            }
        }
    }

    public void drawIn(int r, int c) {
        if(cells[r][c].getIsAlive()) {
            cells[r][c].kill();
        } else {
            cells[r][c].spawn();
        }
    }

    public boolean drawIn(int r, int c, char tag) {
        try {
            if(tag == 'b') {
                cells[r][c].kill();
            } else {
                cells[r][c].spawn();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null,
                    "Pattern is out of screen!",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;

    }
}