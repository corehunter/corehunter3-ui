package org.corehunter.services;

public enum DatasetType
{
  PHENOTYPIC ("Phenotypic"),
  BI_ALLELIC_GENOTYPIC ("Bi-allelic Genotypic"),
  MULTI_ALLELIC_GENOTYPIC ("Multi-allelic Genotypic") ;
  
  private String name;

  private DatasetType(String name)
  {
    this.name = name ;
  }

  public synchronized final String getName()
  {
    return name;
  }
}
