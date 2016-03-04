package org.corehunter.ui.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.CorehunterRun;
import org.corehunter.services.CorehunterRunArguments;
import org.corehunter.services.CorehunterRunClient;

public class CorehunterRunClientMock implements CorehunterRunClient
{
  
  private static List<CorehunterRun> runs = new LinkedList<CorehunterRun>() ;
  
  static
  {
    for (int i = 0 ; i < 10 ; ++i)
    {
      runs.add(createTestCorehunterRunMock1(i)) ;
      runs.add(createTestCorehunterRunMock2(i)) ;
      runs.add(createTestCorehunterRunMock3(i)) ;
      runs.add(createTestCorehunterRunMock4(i)) ;
    }
  }
  
  @Override
  public List<CorehunterRun> getAllCorehunterRuns()
  {
    return runs;
  }

  private static CorehunterRun createTestCorehunterRunMock1(int index)
  {
    return new CorehunterRunMock("Result1" + index, new Date(), new Date(), "started");
  }
  
  private static CorehunterRun createTestCorehunterRunMock2(int index)
  {
    return new CorehunterRunMock("Result2" + index, new Date(), new Date(), "finished");
  }
  
  private static CorehunterRun createTestCorehunterRunMock3(int index)
  {
    return new CorehunterRunMock("Result3" + index, new Date(), new Date(), "started");
  }
  
  private static CorehunterRun createTestCorehunterRunMock4(int index)
  {
    return new CorehunterRunMock("Result4" + index, new Date(), new Date(), "finished");
  }

  @Override
  public CorehunterRun executeCorehunter(CorehunterRunArguments arguments)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
