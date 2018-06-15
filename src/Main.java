import Algorithms.Genetic;
import Algorithms.HillClimbing;
import Algorithms.SimulatedAnnealing;
import Problems.GraphColoring;
import Problems.KeyboardMaker;
import Problems.WordPuzzle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        GraphColoring graphColoring = new GraphColoring("GraphColoringInput.txt");
//        HillClimbing hc = new HillClimbing(graphColoring, HillClimbing.Strategy.RANDOM_RESTART);
//        hc.solve();
//        hc.showSolution();

//        WordPuzzle wordPuzzle = new WordPuzzle("WordPuzzleInput.txt");
//        SimulatedAnnealing sa = new SimulatedAnnealing(wordPuzzle, SimulatedAnnealing.Strategy.RANDOM, 1000, 0.005);
//        sa.solve();
//        sa.showSolution();

        KeyboardMaker keyboardMaker = new KeyboardMaker("KeyboardMakerInput.txt");
        Genetic g = new Genetic(keyboardMaker, 4, 1, 0.5);
        g.solve();
        g.showSolution();
    }
}
