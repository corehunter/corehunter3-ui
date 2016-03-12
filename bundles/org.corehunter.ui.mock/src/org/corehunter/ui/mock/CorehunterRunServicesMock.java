
package org.corehunter.ui.mock;

import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.CorehunterRun;
import org.corehunter.services.CorehunterRunArguments;
import org.corehunter.services.CorehunterRunServices;
import org.corehunter.services.CorehunterRunStatus;
import org.jamesframework.core.subset.SubsetSolution;
import org.joda.time.DateTime;

public class CorehunterRunServicesMock implements CorehunterRunServices {

    private static List<CorehunterRun> runs = new LinkedList<CorehunterRun>();

    static {
        for (int i = 0; i < 10; ++i) {
            runs.add(createTestCorehunterRunMock1(i));
            runs.add(createTestCorehunterRunMock2(i));
            runs.add(createTestCorehunterRunMock3(i));
            runs.add(createTestCorehunterRunMock4(i));
        }
    }

    @Override
    public List<CorehunterRun> getAllCorehunterRuns() {
        return runs;
    }

    private static CorehunterRun createTestCorehunterRunMock1(int index) {
        return new CorehunterRunMock("Result1" + index, new DateTime(), new DateTime(), CorehunterRunStatus.FAILED);
    }

    private static CorehunterRun createTestCorehunterRunMock2(int index) {
        return new CorehunterRunMock("Result2" + index, new DateTime(), new DateTime(), CorehunterRunStatus.FINISHED);
    }

    private static CorehunterRun createTestCorehunterRunMock3(int index) {
        return new CorehunterRunMock("Result3" + index, new DateTime(), new DateTime(), CorehunterRunStatus.RUNNING);
    }

    private static CorehunterRun createTestCorehunterRunMock4(int index) {
        return new CorehunterRunMock("Result4" + index, new DateTime(), new DateTime(), CorehunterRunStatus.FINISHED);
    }

    @Override
    public CorehunterRun executeCorehunter(CorehunterRunArguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CorehunterRun getCorehunterRun(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getErrorMessage(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getErrorStream(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getOutputStream(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SubsetSolution getSubsetSolution(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
