package Algorithms;

import Problems.SearchProblem;

import java.util.ArrayList;
import java.util.Random;

public class SimulatedAnnealing {
    public enum Strategy { ARITHMETIC, GEOMETRIC, RANDOM }
    private SearchProblem problem;
    private double temperature, coolingRate;
    private int createdNodes, expandedNodes;
    private Strategy strategy;


    public SimulatedAnnealing(SearchProblem p, Strategy strategy, double t, double rate) {
        this.problem = p;
        createdNodes = 1;
        expandedNodes = 1;
        this.strategy = strategy;

        temperature = t;
        coolingRate = rate;
    }

    public void solve() {
        Random random = new Random();
        while (temperature > 1) {
            int currentValue = problem.evaluate(problem.getCurrentState());
            ArrayList<BaseState> neighbours = problem.getNeighbours();
            createdNodes += neighbours.size();
            BaseState nextState = neighbours.get(random.nextInt(neighbours.size()));
            int nextValue = problem.evaluate(nextState);
            if (Math.exp((nextValue - currentValue) / temperature) > random.nextDouble()) {
                problem.setNextState(nextState);
                expandedNodes++;
            }

            switch (strategy) {
                case ARITHMETIC:
                    temperature -= coolingRate;
                    break;
                case GEOMETRIC:
                    temperature *= (1 - coolingRate);
                    break;
                case RANDOM:
                    temperature -= random.nextDouble();
                    break;
            }
        }
    }


    public void showSolution() {
        System.out.println("Solving with " + strategy + " strategy");
        problem.getCurrentState().show();
        System.out.println("Solution evaluation: " + problem.evaluate(problem.getCurrentState()));
        System.out.println("Number of nodes created (visited): " + createdNodes);
        System.out.println("Number of nodes expanded: " + expandedNodes);
    }
}
