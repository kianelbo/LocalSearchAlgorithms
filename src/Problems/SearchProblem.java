package Problems;

import Algorithms.BaseState;

import java.util.ArrayList;

public interface SearchProblem {

    ArrayList<BaseState> getNeighbours();

    int evaluate(BaseState bs);

    void setNextState(BaseState nextState);

    BaseState getCurrentState();

    void reinitialize();
}
