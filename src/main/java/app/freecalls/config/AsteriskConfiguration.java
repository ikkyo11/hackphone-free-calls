package app.freecalls.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.StringJoiner;

@Configuration
@ConfigurationProperties(prefix = "asterisk")
class AsteriskConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AsteriskConfiguration.class);

    private String host;
    private int port;

    @PostConstruct
    void print() {
        StringJoiner out = new StringJoiner(";");
        out.add("Configuration Asterisk");
        out.add(host);
        out.add(port+"");
        logger.info(out.toString());
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
