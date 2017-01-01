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

/**
 * @author Guy Davenport
 *
 */
public class DataGridLabelProvider<ValueType extends Object> extends ColumnLabelProvider {
    private int columnIndex;

    public DataGridLabelProvider(int columnIndex) {
        super();
        
        this.columnIndex = columnIndex;
    }

    public final int getColumnIndex() {
        return columnIndex;
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public String getText(Object element) {
		return element == null ? "" : getElementText((DataGridViewerRow<ValueType>)element, columnIndex);
	}
	
	protected String getElementText(DataGridViewerRow<ValueType> element, int columnIndex) {
		return element.getElements()[columnIndex] == null ? "" : element.getElements()[columnIndex].toString();
	}
}
