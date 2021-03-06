/* Copyright (c) 2012 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.csw.store.simple;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geoserver.csw.feature.AbstractFeatureCollection;
import org.geoserver.csw.feature.MemoryFeatureCollection;
import org.geoserver.csw.records.CSWRecordDescriptor;
import org.geotools.data.store.FilteringFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * A feature collection reading record files from the specified directory
 * 
 * @author Andrea Aime - GeoSolutions
 */
class RecordsFeatureCollection extends AbstractFeatureCollection<FeatureType, Feature> {

    File root;

    int offset;

    public RecordsFeatureCollection(File root, int offset) {
        super(CSWRecordDescriptor.RECORD);
        this.root = root;
        this.offset = offset;
    }

    @Override
    protected Iterator<Feature> openIterator() {
        return new SimpleRecordIterator(root, offset);
    }

    @Override
    protected void closeIterator(Iterator<Feature> close) {
        // nothing to do, the SimpleRecordIterator does not keep any reference to streams and the
        // like
    }

    @Override
    public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
        return new FilteringFeatureCollection<FeatureType, Feature>(this, filter);
    }

    @Override
    public FeatureCollection<FeatureType, Feature> sort(SortBy order) {
        List<Feature> features = new ArrayList<Feature>();
        MemoryFeatureCollection memory = new MemoryFeatureCollection(getSchema(), features);
        return memory.sort(order);
    }

}
