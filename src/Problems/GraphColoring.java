package Problems;

import Algorithms.BaseState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GraphColoring implements SearchProblem {
    private GraphState currentGraph;
    private int[][] structure;
    private int maxColors, graphSize;


    public GraphColoring(String filename) {
        Scanner input = null;
        try {
            input = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        maxColors = input.nextInt();
        graphSize = input.nextInt();
        structure = new int[graphSize][graphSize];
        for (int i = 0; i < graphSize; i++)
            for (int j = 0; j < graphSize; j++)
                structure[i][j] = input.nextInt();

        currentGraph = new GraphState();
    }

    @Override
    public ArrayList<BaseState> getNeighbours() {
        ArrayList<BaseState> neighbours = new ArrayList<>();

        for (int i = 0; i < graphSize; i++) {
            int[] newColors = new int[graphSize];
            System.arraycopy(currentGraph.colors, 0, newColors, 0, graphSize);
            for (int color = 0; color < maxColors; color++) {
                if (newColors[i] == color) continue;
                newColors[i] = color;
                neighbours.add(new GraphState(newColors));
            }
        }

        return neighbours;
    }

    @Override
    public int evaluate(BaseState bs) {
        int[] colorArray = ((GraphState) bs).colors;
        int sameColors = 0;
        for (int i = 0; i < graphSize; i++)
            for (int j = 0; j < graphSize; j++)
                if (structure[i][j] == 1 && colorArray[i] == colorArray[j]) sameColors++;

        return sameColors / -2;
    }

    @Override
    public void setNextState(BaseState nextState) {
        currentGraph = (GraphState) nextState;
    }

    @Override
    public BaseState getCurrentState() {
        return currentGraph;
    }

    @Override
    public void reinitialize() {
        currentGraph = new GraphState();
    }


    private class GraphState implements BaseState {
        private int[] colors;

        public GraphState() {
            colors = new int[graphSize];
            Random random = new Random();
            for (int i = 0; i < graphSize; i++) colors[i] = random.nextInt(maxColors);
        }

        public GraphState(int[] newColors) {
            this.colors = newColors;
        }

        @Override
        public void show() {
            for (int i = 0; i < graphSize; i++)
                System.out.println("Node " + i + " -> color " + colors[i]);
        }
    }
}
