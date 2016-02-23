package org.corehunter.ui.mock;

import java.util.Date;

import org.corehunter.services.Upload;

import uno.informatics.data.pojo.SimpleEntityPojo;

public class UploadMock extends SimpleEntityPojo implements Upload
{
  private Date date;

  public UploadMock(String name)
  {
    super(name);
    
    date = new Date() ;
  }

  @Override
  public Date getDate()
  {
    return date;
  }
}
