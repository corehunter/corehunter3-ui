package org.corehunter.ui.mock;

import org.corehunter.data.CoreHunterData;
import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.problems.sol.Solution;
import org.jamesframework.core.subset.SubsetSolution;

public class MockObjective implements Objective<SubsetSolution, CoreHunterData> {

    @Override
    public Evaluation evaluate(SubsetSolution solution, CoreHunterData data) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isMinimizing() {
        // TODO Auto-generated method stub
        return false;
    }
}
