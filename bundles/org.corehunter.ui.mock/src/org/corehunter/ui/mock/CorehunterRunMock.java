package org.corehunter.ui.mock;

import org.corehunter.services.CorehunterRun;
import org.corehunter.services.CorehunterRunStatus;
import org.joda.time.DateTime;

import uno.informatics.data.pojo.SimpleEntityPojo;

public class CorehunterRunMock extends SimpleEntityPojo implements CorehunterRun
{
  private DateTime startDate;
  private DateTime endDate;
  private CorehunterRunStatus status;

  public CorehunterRunMock(String name, DateTime startDate, DateTime endDate, CorehunterRunStatus status)
  {
    super(name);
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
  }

  @Override
  public DateTime getStartDate()
  {
    return startDate ;
  }

  @Override
  public DateTime getEndDate()
  {
    return endDate;
  }

  @Override
  public CorehunterRunStatus getStatus()
  {
    return status;
  }
  
}
