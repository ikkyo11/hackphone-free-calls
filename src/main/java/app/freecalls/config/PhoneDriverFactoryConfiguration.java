package app.freecalls.config;

import hackphone.phone.PhoneDriverFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PhoneDriverFactoryConfiguration {

    final AsteriskConfiguration asteriskConfiguration;

    public PhoneDriverFactoryConfiguration(AsteriskConfiguration asteriskConfiguration) {
        this.asteriskConfiguration = asteriskConfiguration;
    }

    @Bean
    public PhoneDriverFactory phoneFactory() {
        try {
            return new PhoneDriverFactory(asteriskConfiguration.getHost(), asteriskConfiguration.getPort());
        } catch(Exception ex) {
            throw new IllegalStateException("Invalid configuration for creating phone factory");
        }
    }
}
