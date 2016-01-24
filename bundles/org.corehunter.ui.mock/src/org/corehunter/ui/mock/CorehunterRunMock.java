package org.corehunter.ui.mock;

import java.util.Date;

import org.corehunter.services.CorehunterRun;

import uno.informatics.common.model.impl.SimpleIdentifierImpl;

public class CorehunterRunMock extends SimpleIdentifierImpl implements CorehunterRun
{
  private Date startDate;
  private Date endDate;
  private String status;

  public CorehunterRunMock(String name, Date startDate, Date endDate, String status)
  {
    super(name);
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = status;
  }

  @Override
  public Date getStartDate()
  {
    return startDate ;
  }

  @Override
  public Date getEndDate()
  {
    return endDate;
  }

  @Override
  public String getStatus()
  {
    return status;
  }
  
}
