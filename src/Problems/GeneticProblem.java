package Problems;

import Algorithms.BaseState;

import java.util.ArrayList;

public interface GeneticProblem {

    ArrayList<BaseState> getPopulation(int k);

    int getFitness(BaseState state);

    BaseState mutation(BaseState state);

    ArrayList<BaseState> crossover(ArrayList<BaseState> parents, int cuts);
}
