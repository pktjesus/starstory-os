package org.opensearch.index.analysis.jamo;

import org.opensearch.common.settings.Settings;
import org.opensearch.env.Environment;
import org.opensearch.index.IndexSettings;
import org.opensearch.index.analysis.AbstractCharFilterFactory;

import java.io.Reader;

public class JamoCharFilterFactory extends AbstractCharFilterFactory {

    public JamoCharFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name);
    }

    @Override
    public Reader create(Reader in) {
        return new JamoCharFilter(in);
    }
}

