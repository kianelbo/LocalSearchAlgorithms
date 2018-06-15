package Algorithms;

import Problems.GeneticProblem;
import Visualizations.Plotter;

import java.util.ArrayList;
import java.util.Random;

public class Genetic {
    private GeneticProblem problem;
    private ArrayList<ArrayList<BaseState>> generations;
    private int population, cuts;
    private double mutationChance;


    public Genetic(GeneticProblem p, int population, int n, double mChance) {
        this.problem = p;
        this.population = population;
        generations = new ArrayList<>();
        this.cuts = n;
        this.mutationChance = mChance;
    }

    public void solve() {
        Random random = new Random();

        //initial population
        generations.add(problem.getPopulation(population));
        if (population % 2 == 1) population++;

        for (int n = 0; n < 20; n++) {
            ArrayList<BaseState> currentGeneration = generations.get(generations.size() - 1);

            //calculate fitness and selection
            ArrayList<BaseState> chosenOnes = new ArrayList<>();
            int totalFitness = 0;
            for (BaseState sample : currentGeneration) totalFitness += problem.getFitness(sample);
            for (int i = 0; i < population; i++) {
                int position = random.nextInt(totalFitness);
                for (BaseState sample : currentGeneration)
                    if (position < problem.getFitness(sample)) {
                        chosenOnes.add(sample);
                        break;
                    } else position -= problem.getFitness(sample);
            }

            //crossover
            ArrayList<BaseState> children = new ArrayList<>();
            for (int i = 0; i < chosenOnes.size(); i += 2) {
                ArrayList<BaseState> parents = new ArrayList<>();
                parents.add(chosenOnes.get(i));
                parents.add(chosenOnes.get(i + 1));
                children.addAll(problem.crossover(parents, cuts));
            }

            //mutation of next generation
            ArrayList<BaseState> newGeneration = new ArrayList<>();
            for (BaseState child : children)
                if (mutationChance >= random.nextDouble()) newGeneration.add(problem.mutation(child));
                else newGeneration.add(child);

            generations.add(newGeneration);

            BaseState maxFitness = newGeneration.get(0);
                for (BaseState child : newGeneration)
                    if (problem.getFitness(child) > problem.getFitness(maxFitness)) maxFitness = child;
                maxFitness.show();
        }
    }


    public void showSolution() {
        ArrayList<Integer> bests = new ArrayList<>();
        ArrayList<Integer> worsts = new ArrayList<>();
        ArrayList<Float> avgs = new ArrayList<>();

        for (ArrayList<BaseState> generation : generations) {
            int totalFitness = 0;
            int best = -Integer.MAX_VALUE;
            int worst = Integer.MAX_VALUE;
            BaseState maxFitness = null;
            for (BaseState sample : generation) {
                int sampleFitness = problem.getFitness(sample);
                if (sampleFitness > best) {
                    best = sampleFitness;
                    maxFitness = sample;
                }
                if (sampleFitness < worst) worst = sampleFitness;
                totalFitness += sampleFitness;
            }
            maxFitness.show();
            bests.add(best);
            worsts.add(worst);
            avgs.add((float) (totalFitness / population));
        }

        new Plotter(bests, worsts, avgs);
    }
}
