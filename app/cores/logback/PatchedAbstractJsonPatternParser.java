package cores.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.spi.ContextAware;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.logstash.logback.pattern.AbstractJsonPatternParser;
import net.logstash.logback.pattern.ValueGetter;

import java.io.IOException;

/**
 * https://github.com/tetv/logstash-logback-encoder/blob/b182099cc0064ca60e849e6f5560b8b5c326763b/src/main/java/net/logstash/logback/pattern/AbstractJsonPatternParser.java
 */
public abstract class PatchedAbstractJsonPatternParser<Event> extends AbstractJsonPatternParser<Event> {

    public PatchedAbstractJsonPatternParser(final ContextAware contextAware, final JsonFactory jsonFactory) {
        super(contextAware, jsonFactory);
        super.addOperation(new AsJsonOperation());
    }

    protected class AsJsonOperation extends Operation {

        public AsJsonOperation() {
            super("asJson", true);
        }

        @Override
        public ValueGetter<?, Event> createValueGetter(String data) {
            return new AsJsonValueTransformer<Event>(makeLayoutValueGetter(data));
        }
    }

    protected static abstract class AbstractAsObjectTransformer<T, Event> implements ValueGetter<T, Event> {

        private final ValueGetter<String, Event> generator;

        AbstractAsObjectTransformer(final ValueGetter<String, Event> generator) {
            this.generator = generator;
        }

        @Override
        public T getValue(final Event event) {
            final String value = generator.getValue(event);
            if (value == null || value.isEmpty()) {
                return null;
            }
            try {
                return transform(value);
            } catch (Exception e) {
                return null;
            }
        }

        abstract protected T transform(final String value) throws NumberFormatException, IOException;
    }

    protected static class AsJsonValueTransformer<Event> extends AbstractAsObjectTransformer<JsonNode, Event> {
        private static final ObjectMapper mapper = new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

        public AsJsonValueTransformer(final ValueGetter<String, Event> generator) {
            super(generator);
        }

        protected JsonNode transform(final String value) throws IOException {
            return mapper.readTree(value);
        }
    }
}


