package Problems;

import Algorithms.BaseState;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class KeyboardMaker implements GeneticProblem {
    private HashMap<Character, Integer> mostUsed;
    private HashMap<String, Integer> couples;

    public KeyboardMaker(String filename) {
        Scanner input = null;
        try {
            input = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        mostUsed = new HashMap<>();
        couples = new HashMap<>();

        String[] mostUsedChars = input.nextLine().split(" ");
        for (int i = 0; i < mostUsedChars.length; i++)
            mostUsed.put(mostUsedChars[i].charAt(0), 26 - i);

        String[] mostUsedCouples = input.nextLine().split(" ");
        for (int i = 0; i < mostUsedCouples.length; i++)
            couples.put(mostUsedCouples[i], 10 - i);
    }

    @Override
    public ArrayList<BaseState> getPopulation(int k) {
        ArrayList<BaseState> population = new ArrayList<>();

        ArrayList<Character> list = new ArrayList<>();
        for (int n = 0; n < 26; n++) list.add((char) (n + 65));

        for (int i = 0; i < k; i++) {
            Collections.shuffle(list);
            char[] newChars = new char[26];
            for (int j = 0; j < 26; j++) newChars[j] = list.get(j);
            population.add(new KeyboardState(newChars));
        }
        return population;
    }

    @Override
    public int getFitness(BaseState state) {
        KeyboardState keyboardState = (KeyboardState) state;
        if (keyboardState.fitness != 0) return keyboardState.fitness;

        char[] ks = ((KeyboardState) state).keys;
        int score = 100;

        int totalScoreL = 0;
        for (int i = 0; i < 13; i++) if (mostUsed.containsKey(ks[i])) totalScoreL += mostUsed.get(ks[i]);
        int totalScoreR = 0;
        for (int i = 13; i < 26; i++) if (mostUsed.containsKey(ks[i])) totalScoreR += mostUsed.get(ks[i]);
        score -= Math.abs(totalScoreL - totalScoreR);

        ArrayList<Character> stateKeys = new ArrayList<>();
        for (int i = 0; i < 26; i++) stateKeys.add(keyboardState.keys[i]);
        int coupleScore = 0;
        for (String couple : couples.keySet())
            if ((stateKeys.indexOf(couple.charAt(0)) < 13 && stateKeys.indexOf(couple.charAt(1)) < 13) ||
                    (stateKeys.indexOf(couple.charAt(0)) > 13 && stateKeys.indexOf(couple.charAt(1)) > 13))
                coupleScore += couples.get(couple);

        score -= coupleScore;

        keyboardState.fitness = score;
        return score;
    }

    @Override
    public BaseState mutation(BaseState state) {
        char[] ks = ((KeyboardState) state).keys;
        char[] newState = new char[26];
        for (int i = 0; i < 26; i++) newState[i] = ks[i];

        Random random = new Random();
        int a = random.nextInt(25), b = random.nextInt(25);
        newState[a] = ks[b];
        newState[b] = ks[a];

        return new KeyboardState(newState);
    }

    @Override
    public ArrayList<BaseState> crossover(ArrayList<BaseState> parents, int cuts) {
        Random random = new Random();
        ArrayList<BaseState> children = new ArrayList<>();
        children.add(new KeyboardState());
        children.add(new KeyboardState());

        ArrayList<Integer> cutPositions = new ArrayList<>();
        for (int i = 0; i < cuts; i++) {
            int selected = 1 + random.nextInt(25);
            if (!cutPositions.contains(selected)) cutPositions.add(selected);
            else i--;
        }
        Collections.sort(cutPositions);
        cutPositions.add(26);

        int parentTurn = 0;
        int fromMark = 0, toMark;

        for (int i = 0; i < cuts + 1; i++) {
            toMark = cutPositions.get(i);
            for (int j = 0; j < 2; j++) {
                char[] childKeys = ((KeyboardState) children.get(j)).keys;
                System.arraycopy(((KeyboardState) parents.get(parentTurn)).keys, fromMark, childKeys, fromMark, toMark - fromMark);
                parentTurn ^= 1;
            }
            parentTurn ^= 1;
            fromMark = toMark;
        }

        for (BaseState child : children) removeDuplicates((KeyboardState) child);

        return children;
    }


    private void removeDuplicates(KeyboardState state) {
        ArrayList<Character> unused = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            boolean isUnused = true;
            for (int j = 0; j < 26; j++)
                if (state.keys[j] == (char) (i + 65)) {
                    isUnused = false;
                    break;
                }
            if (isUnused) unused.add((char) (i + 65));
        }

        for (int i = 0; i < 26; i++) for (int j = i + 1; j < 26; j++)
                if (state.keys[i] == state.keys[j]) state.keys[j] = unused.remove(0);
    }


    private class KeyboardState implements BaseState {
        private char[] keys;
        private int fitness;

        public KeyboardState() {
            keys = new char[26];
            fitness = 0;
        }

        private KeyboardState(char[] keys) {
            this.keys = keys;
            fitness = 0;
        }

        @Override
        public void show() {
            System.out.println("\n\n*********************");
            System.out.println("Fitness = " + fitness);
            for (int i = 0; i < 5; i++) System.out.print(keys[i] + " ");
            System.out.print("| ");
            for (int i = 13; i < 18; i++) System.out.print(keys[i] + " ");
            System.out.print("\n  ");
            for (int i = 5; i < 9; i++) System.out.print(keys[i] + " ");
            System.out.print("| ");
            for (int i = 18; i < 22; i++) System.out.print(keys[i] + " ");
            System.out.print("\n  ");
            for (int i = 9; i < 13; i++) System.out.print(keys[i] + " ");
            System.out.print("| ");
            for (int i = 22; i < 26; i++) System.out.print(keys[i] + " ");
        }
    }
}
