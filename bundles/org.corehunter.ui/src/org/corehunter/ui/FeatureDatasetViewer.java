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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import uno.informatics.data.Feature;
import uno.informatics.data.FeatureDataset;

/**
 * @author Guy Davenport
 *
 */
public class FeatureDatasetViewer {
    private static final int MIM_COLUMN_SIZE = 5;
    private GridTableViewer gridViewer;

    private FeatureDataset value;
    private Map<Integer, GridViewerColumn> viewerColumns;

    /**
     * @param parent
     * @param configuration
     */
    public FeatureDatasetViewer() {
        viewerColumns = new TreeMap<Integer, GridViewerColumn>();
    }

    public void setValue(FeatureDataset value) {
        if (!ObjectUtils.equals(this.value, value)) {
            this.value = value;
        }
    }

    public void createPartControl(Composite parent) {
        gridViewer = new GridTableViewer(parent);

        gridViewer.setContentProvider(new FeatureDatasetContentProvider());
        // gridViewer.setLabelProvider(new DatasetLabelProvider()) ;
        gridViewer.getGrid().setHeaderVisible(true);

        gridViewer.getGrid().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        updateGridViewer();

        gridViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleViewerSelectionChanged();
            }
        });

    }

    protected void handleViewerSelectionChanged() {

    }

    private void updateGridViewer() {
        if (value != null && !value.getFeatures().isEmpty()) {
            if (gridViewer != null) {
                int oldColumnCount = viewerColumns.size();
                int newColumnCount = value.getFeatures().size();

                // create new columns
                if (newColumnCount > oldColumnCount) {
                    for (int i = oldColumnCount; i < newColumnCount; ++i) {
                        viewerColumns.put(i, new GridViewerColumn(gridViewer, SWT.NONE, i));
                    }
                } else {
                    // remove columns
                    if (newColumnCount < oldColumnCount) {
                        for (int i = newColumnCount; i > oldColumnCount; --i) {
                            GridViewerColumn gridViewerColumn = viewerColumns.get(i - 1);

                            if (gridViewerColumn != null)
                                gridViewerColumn.getColumn().dispose();
                        }
                    }
                }

                Iterator<Integer> iterator = viewerColumns.keySet().iterator();
                GridViewerColumn gridViewerColumn;

                int index = 0;
                Feature feature;

                while (iterator.hasNext()) {
                    index = iterator.next();

                    feature = value.getFeatures().get(index);

                    gridViewerColumn = viewerColumns.get(index);

                    gridViewerColumn.getColumn().setText(feature.getName());

                    gridViewerColumn.setLabelProvider(new DatasetColumnLabelProvider(feature, index));

                    gridViewerColumn.getColumn().setWidth(guessColumnWidth(feature.getName()));
                }

                gridViewer.getGrid().setRowHeaderVisible(value.hasRowHeaders());

                gridViewer.setRowHeaderLabelProvider(new RowHeaderLabelProvider());

                gridViewer.setInput(value);
            }
        } else {
            if (gridViewer != null) {
                Iterator<GridViewerColumn> iterator = viewerColumns.values().iterator();

                while (iterator.hasNext())
                    iterator.next().getColumn().dispose();

                viewerColumns.clear();

                gridViewer.setInput(null);
            }
        }
    }

    private int guessColumnWidth(String columnHeader) {
        int size = 0;

        if (columnHeader != null && columnHeader.length() > 0)
            size = (columnHeader.length() * 10) + 10;

        if (size < MIM_COLUMN_SIZE)
            size = MIM_COLUMN_SIZE;

        return size;
    }
}
