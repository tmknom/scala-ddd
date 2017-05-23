package cores.logback;

import com.fasterxml.jackson.core.JsonFactory;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.spi.ContextAware;

/**
 * https://github.com/logstash/logstash-logback-encoder/blob/ae1e3d91137d69b327f9977f096c145fbc893004/src/main/java/net/logstash/logback/pattern/LoggingEventJsonPatternParser.java
 */
public class PatchedLoggingEventJsonPatternParser extends PatchedAbstractJsonPatternParser<ILoggingEvent> {

    public PatchedLoggingEventJsonPatternParser(final ContextAware contextAware, final JsonFactory jsonFactory) {
        super(contextAware, jsonFactory);
    }

    @Override
    protected PatternLayoutBase<ILoggingEvent> createLayout() {
        return new PatternLayout();
    }
}
