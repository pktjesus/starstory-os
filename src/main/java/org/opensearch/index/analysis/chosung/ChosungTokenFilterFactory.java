package org.opensearch.index.analysis.chosung;

import org.apache.lucene.analysis.TokenStream;
import org.opensearch.common.settings.Settings;
import org.opensearch.env.Environment;
import org.opensearch.index.IndexSettings;
import org.opensearch.index.analysis.AbstractTokenFilterFactory;

public class ChosungTokenFilterFactory extends AbstractTokenFilterFactory {

//    public ChosungTokenFilterFactory(IndexSettings indexSettings, String name, Settings settings) {
//        super(indexSettings, name, settings);
//    }
    public ChosungTokenFilterFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);
    }

    @Override
    public TokenStream create(TokenStream stream) {
        return new ChosungTokenFilter(stream);
    }

}
