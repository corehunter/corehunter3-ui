/*******************************************************************************
 * Copyright 2014 Guy Davenport
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

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import uno.informatics.data.FeatureDatasetRow;

/**
 * @author Guy Davenport
 *
 */
public class RowHeaderLabelProvider extends CellLabelProvider {

    @Override
    public void update(ViewerCell cell) {
        if (((FeatureDatasetRow) cell.getElement()).getHeader() != null)
            cell.setText(((FeatureDatasetRow) cell.getElement()).getHeader().getName());

    }

}
