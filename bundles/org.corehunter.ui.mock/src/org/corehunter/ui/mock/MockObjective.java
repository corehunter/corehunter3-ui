package org.corehunter.ui.mock;

import org.corehunter.data.CoreHunterData;
import org.jamesframework.core.problems.objectives.Objective;
import org.jamesframework.core.problems.objectives.evaluations.Evaluation;
import org.jamesframework.core.subset.SubsetSolution;

/**
 * Empty objective only for testing 
 * @author daveneti
 *
 */
public class MockObjective implements Objective<SubsetSolution, CoreHunterData> {

    @Override
    public Evaluation evaluate(SubsetSolution solution, CoreHunterData data) {

        return null;
    }

    @Override
    public boolean isMinimizing() {

        return false;
    }
}
