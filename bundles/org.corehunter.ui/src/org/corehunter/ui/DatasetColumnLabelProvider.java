/*******************************************************************************
 * Copyright 2016 Guy Davenport
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.corehunter.ui;


import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerColumn;

import uno.informatics.data.Feature;
import uno.informatics.data.FeatureDatasetRow;

/**
 * @author Guy Davenport
 *
 */
public class DatasetColumnLabelProvider extends ColumnLabelProvider
{
	private Feature feature ;
  private int columnIndex;
  
	public DatasetColumnLabelProvider(Feature feature, int columnIndex)
  {
	  super();
	  this.columnIndex = columnIndex;
	  this.feature = feature;
  }
	
  public final Feature getFeature()
	{
		return feature;
	}

	public final int getColumnIndex()
	{
		return columnIndex;
	}

	@Override
  public String getText(Object element) 
  {
    return super.getText(((FeatureDatasetRow)element).getValue(columnIndex));
  }

	@Override
  protected void initialize(ColumnViewer viewer, ViewerColumn column) 
  {

  }
}
