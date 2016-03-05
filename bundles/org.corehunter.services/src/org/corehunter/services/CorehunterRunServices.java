package org.corehunter.services;

import java.util.List;

public interface CorehunterRunServices
{  
  public CorehunterRun executeCorehunter(CorehunterRunArguments arguments) ;
  
  public List<CorehunterRun> getAllCorehunterRuns();

}
