package Algorithms;

import Problems.SearchProblem;

import java.util.ArrayList;
import java.util.Random;

public class HillClimbing {
    public enum Strategy  { SIMPLE, STOCHASTIC, FIRST_CHOICE, RANDOM_RESTART }
    private SearchProblem problem;
    private int createdNodes, expandedNodes;
    private Strategy strategy;

    public HillClimbing(SearchProblem p, Strategy strategy) {
        this.problem = p;
        createdNodes = 1;
        expandedNodes = 1;
        this.strategy = strategy;
    }

    public void solve() {
        switch (strategy) {
            case SIMPLE:
                solveSimple();
                return;
            case STOCHASTIC:
                solveStochastic();
                return;
            case FIRST_CHOICE:
                solveFirstChoice();
                return;
            case RANDOM_RESTART:
                solveRandomRestart();
        }
    }

    private void solveSimple() {
        int currentEval = -Integer.MAX_VALUE;
        BaseState nextState = null;
        boolean ending = false;
        while (!ending) {
            ending = true;
            for (BaseState state : problem.getNeighbours()) {
                createdNodes++;
                if (problem.evaluate(state) > currentEval) {
                    nextState = state;
                    currentEval = problem.evaluate(state);
                    ending = false;
                }
            }
            problem.setNextState(nextState);
            expandedNodes++;
        }
    }

    private void solveStochastic() {
        int currentEval = -Integer.MAX_VALUE;
        Random random = new Random();
        while (true) {
            ArrayList<BaseState> betterNeighbours = new ArrayList<>();
            for (BaseState state : problem.getNeighbours()) {
                createdNodes++;
                if (problem.evaluate(state) > currentEval) betterNeighbours.add(state);
            }
            if (betterNeighbours.isEmpty()) return;
            BaseState nextState = betterNeighbours.get(random.nextInt(betterNeighbours.size()));
            currentEval = problem.evaluate(nextState);
            problem.setNextState(nextState);
            expandedNodes++;
        }
    }

    private void solveFirstChoice() {
        int currentEval = -Integer.MAX_VALUE;
        BaseState nextState = null;
        boolean ending = false;
        while (!ending) {
            ending = true;
            for (BaseState state : problem.getNeighbours()) {
                createdNodes++;
                if (problem.evaluate(state) > currentEval) {
                    nextState = state;
                    currentEval = problem.evaluate(state);
                    ending = false;
                    break;
                }
            }
            problem.setNextState(nextState);
            expandedNodes++;
        }
    }

    private void solveRandomRestart() {
        BaseState bestSolution = problem.getCurrentState();
        for (int i = 0; i < 15; i++) {
            problem.reinitialize();
            solveSimple();
            if (problem.evaluate(problem.getCurrentState()) > problem.evaluate(bestSolution)) bestSolution = problem.getCurrentState();
        }
        problem.setNextState(bestSolution);
    }

    public void showSolution() {
        System.out.println("Solving in " + strategy + " mode");
        problem.getCurrentState().show();
        System.out.println("Solution evaluation: " + problem.evaluate(problem.getCurrentState()));
        System.out.println("Number of nodes created (visited): " + createdNodes);
        System.out.println("Number of nodes expanded: " + expandedNodes);
    }
}
