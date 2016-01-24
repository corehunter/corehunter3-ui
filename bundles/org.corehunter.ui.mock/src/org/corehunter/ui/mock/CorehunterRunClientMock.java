package org.corehunter.ui.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.CorehunterRun;
import org.corehunter.services.CorehunterRunClient;

public class CorehunterRunClientMock implements CorehunterRunClient
{
  
  private static List<CorehunterRun> runs = new LinkedList<CorehunterRun>() ;
  
  static
  {
    runs.add(createTestCorehunterRunMock1()) ;
    runs.add(createTestCorehunterRunMock2()) ;
    //runs.add(createTestCorehunterRunMock3()) ;
    //runs.add(createTestCorehunterRunMock4()) ;
  }
  
  @Override
  public List<CorehunterRun> getAllCorehunterRuns()
  {
    return runs;
  }

  private static CorehunterRun createTestCorehunterRunMock1()
  {
    return new CorehunterRunMock("Result1", new Date(), new Date(), "started");
  }
  
  private static CorehunterRun createTestCorehunterRunMock2()
  {
    return new CorehunterRunMock("Result2", new Date(), new Date(), "finished");
  }
}
