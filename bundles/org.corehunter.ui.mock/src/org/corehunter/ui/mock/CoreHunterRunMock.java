package org.corehunter.ui.mock;

import org.corehunter.services.CoreHunterRun;
import org.corehunter.services.CoreHunterRunStatus;
import org.joda.time.DateTime;

import uno.informatics.data.pojo.SimpleEntityPojo;

public class CoreHunterRunMock extends SimpleEntityPojo implements CoreHunterRun
{
  private DateTime startDate;
  private DateTime endDate;
  private CoreHunterRunStatus status;

  public CoreHunterRunMock(String name, DateTime startDate, DateTime endDate, CoreHunterRunStatus status)
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
  public CoreHunterRunStatus getStatus()
  {
    return status;
  }
  
}
