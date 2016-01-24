package org.corehunter.services;

import java.util.Date;

public interface CorehunterRun
{
  public String getName();

  public Date getStartDate();

  public Date getEndDate();
  
  public String getStatus();
}
