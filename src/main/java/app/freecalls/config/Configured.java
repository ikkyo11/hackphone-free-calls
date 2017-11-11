package app.freecalls.config;

import hackphone.phone.PhoneDriver;
import hackphone.phone.PhoneDriverFactory;
import hackphone.phone.configuration.PhoneAccount;
import org.springframework.stereotype.Component;

@Component
public class Configured {

    final AccountsConfiguration accountsConfiguration;
    final AsteriskConfiguration asteriskConfiguration;

    public Configured(AccountsConfiguration accountsConfiguration, AsteriskConfiguration asteriskConfiguration) {
        this.accountsConfiguration = accountsConfiguration;
        this.asteriskConfiguration = asteriskConfiguration;
    }

    PhoneDriver pingingDriver() {
        try {
            final PhoneDriverFactory phoneFactory = new PhoneDriverFactory(asteriskConfiguration.getHost(), asteriskConfiguration.getPort());
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getPinging().getUsername(), accountsConfiguration.getPinging().getPassword());
            return phoneFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

