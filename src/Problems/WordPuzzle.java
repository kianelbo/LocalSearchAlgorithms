package Problems;

import Algorithms.BaseState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WordPuzzle implements SearchProblem {
    private ArrayList<String> dictionary;
    private int rows, cols;
    private PuzzleState initialTable, currentTable;

    public WordPuzzle(String filename) {
        Scanner input = null;
        try {
            input = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rows = input.nextInt();
        cols = input.nextInt();
        initialTable = new PuzzleState();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                initialTable.table[i][j] = input.next().charAt(0);

        dictionary = new ArrayList<>();
        while (input.hasNext())
            dictionary.add(input.next().replace(",", ""));

        char[][] currentMatrix = new char[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                currentMatrix[i][j] = initialTable.table[i][j];
        currentTable = new PuzzleState(currentMatrix);
    }


    @Override
    public void setNextState(BaseState nextState) {
        currentTable = (PuzzleState) nextState;
    }

    @Override
    public BaseState getCurrentState() {
        return currentTable;
    }

    @Override
    public void reinitialize() {
        currentTable = initialTable;
    }

    @Override
    public ArrayList<BaseState> getNeighbours() {
        ArrayList<BaseState> neighbours = new ArrayList<>();

        int tableSize = rows * cols;
        for (int a = 0; a < tableSize; a++)
            for (int b = a + 1; b < tableSize; b++) {
                int i0 = a / cols, j0 = a % cols;
                int i1 = b / cols, j1 = b % cols;
                char[][] newTable = new char[rows][cols];
                if (currentTable.table[i0][j0] == currentTable.table[i1][j1]) continue;
                for (int m = 0; m < rows; m++) for (int n = 0; n < cols; n++) newTable[m][n] = currentTable.table[m][n];
                newTable[i0][j0] = newTable[i1][j1];
                newTable[i1][j1] = currentTable.table[i0][j0];
                neighbours.add(new PuzzleState(newTable));
            }

        return neighbours;
    }

    @Override
    public int evaluate(BaseState bs) {
        int score = 0;

        char[][] table = ((PuzzleState) bs).table;
        for (String word : dictionary) {
            int wordScore = 0;
            char[] wordChars = word.toCharArray();
            int x, y;
            for (x = 0; x < rows; x++) for (y = 0; y < cols; y++)
                if (table[x][y] == wordChars[0])
                    wordScore = Math.max(wordScore, wordEvaluate(wordChars, x, y, 1));

            score += wordScore;
        }
        return score;
    }

    private int wordEvaluate(char[] word, int x, int y, int index) {
        if (word.length <= index) return index;

        int score = index;

        if (x > 0) if (currentTable.table[x - 1][y] == word[index])
            score = Math.max(score, wordEvaluate(word, x - 1, y, index + 1));
        if (y > 0) if (currentTable.table[x][y - 1] == word[index])
            score = Math.max(score, wordEvaluate(word, x, y - 1, index + 1));
        if (x < rows - 1) if (currentTable.table[x + 1][y] == word[index])
            score = Math.max(score, wordEvaluate(word, x + 1, y, index + 1));
        if (y < cols - 1) if (currentTable.table[x][y + 1] == word[index])
            score = Math.max(score, wordEvaluate(word, x, y + 1, index + 1));

        return score;
    }


    private class PuzzleState implements BaseState {
        private char[][] table;

        private PuzzleState() {
            table = new char[rows][cols];
        }

        private PuzzleState(char[][] newTable) {
            this.table = newTable;
        }

        @Override
        public void show() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++)
                    System.out.print(currentTable.table[i][j] + " ");
                System.out.print("\n");
            }
        }
    }
}
