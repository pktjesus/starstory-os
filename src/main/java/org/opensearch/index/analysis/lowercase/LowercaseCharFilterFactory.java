package org.opensearch.index.analysis.lowercase;

import java.io.Reader;

import org.opensearch.common.settings.Settings;
import org.opensearch.env.Environment;
import org.opensearch.index.IndexSettings;
import org.opensearch.index.analysis.AbstractCharFilterFactory;

public class LowercaseCharFilterFactory extends AbstractCharFilterFactory {

    public LowercaseCharFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name);
    }

    @Override
    public Reader create(Reader reader) {
        return new LowercaseCharFilter(reader);
    }
}
