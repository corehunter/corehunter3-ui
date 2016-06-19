
package org.corehunter.ui.mock;

import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunArguments;
import org.corehunter.services.CoreHunterRunServices;
import org.corehunter.services.CoreHunterRunStatus;
import org.jamesframework.core.subset.SubsetSolution;
import org.joda.time.DateTime;

public class CoreHunterRunServicesMock implements CoreHunterRunServices {

    private static List<CoreHunterRun> runs = new LinkedList<CoreHunterRun>();

    static {
        for (int i = 0; i < 10; ++i) {
            runs.add(createTestCoreHunterRunMock1(i));
            runs.add(createTestCoreHunterRunMock2(i));
            runs.add(createTestCoreHunterRunMock3(i));
            runs.add(createTestCoreHunterRunMock4(i));
        }
    }

    @Override
    public List<CoreHunterRun> getAllCoreHunterRuns() {
        return runs;
    }

    private static CoreHunterRun createTestCoreHunterRunMock1(int index) {
        return new CoreHunterRunMock("Result1" + index, new DateTime(), new DateTime(), CoreHunterRunStatus.FAILED);
    }

    private static CoreHunterRun createTestCoreHunterRunMock2(int index) {
        return new CoreHunterRunMock("Result2" + index, new DateTime(), new DateTime(), CoreHunterRunStatus.FINISHED);
    }

    private static CoreHunterRun createTestCoreHunterRunMock3(int index) {
        return new CoreHunterRunMock("Result3" + index, new DateTime(), new DateTime(), CoreHunterRunStatus.RUNNING);
    }

    private static CoreHunterRun createTestCoreHunterRunMock4(int index) {
        return new CoreHunterRunMock("Result4" + index, new DateTime(), new DateTime(), CoreHunterRunStatus.FINISHED);
    }

    @Override
    public CoreHunterRun executeCoreHunter(CoreHunterRunArguments arguments) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CoreHunterRun getCoreHunterRun(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeCoreHunterRun(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void deleteCoreHunterRun(String arg0) {
        // TODO Auto-generated method stub
        
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
