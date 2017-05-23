package cores.logback;

import net.logstash.logback.composite.AbstractPatternJsonProvider;
import net.logstash.logback.pattern.AbstractJsonPatternParser;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * https://github.com/logstash/logstash-logback-encoder/blob/ae1e3d91137d69b327f9977f096c145fbc893004/src/main/java/net/logstash/logback/composite/loggingevent/LoggingEventPatternJsonProvider.java
 */
public class PatchedLoggingEventPatternJsonProvider extends AbstractPatternJsonProvider<ILoggingEvent> {

    @Override
    protected AbstractJsonPatternParser<ILoggingEvent> createParser() {
        return new PatchedLoggingEventJsonPatternParser(this, this.jsonFactory);
    }

}
