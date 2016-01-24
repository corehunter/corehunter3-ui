package org.corehunter.ui.mock;

import java.util.LinkedList;
import java.util.List;

import org.corehunter.services.Upload;
import org.corehunter.services.UploadClient;

public class UploadClientMock implements UploadClient
{
  
  private static List<Upload> uploads = new LinkedList<Upload>() ;
  
  static
  {
    uploads.add(createTestUpload1()) ;
    uploads.add(createTestUpload2()) ;
    //uploads.add(createTestUpload3()) ;
    //uploads.add(createTestUpload4()) ;
  }
  
  @Override
  public List<Upload> getAllUploads()
  {
    return uploads;
  }

  private static Upload createTestUpload1()
  {
    return new UploadMock("Dataset1");
  }
  
  private static Upload createTestUpload2()
  {
    return new UploadMock("Dataset2");
  }
}
