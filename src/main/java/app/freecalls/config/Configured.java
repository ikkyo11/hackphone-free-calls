package app.freecalls.config;

import hackphone.phone.PhoneDriver;
import hackphone.phone.PhoneDriverFactory;
import hackphone.phone.configuration.PhoneAccount;
import org.springframework.stereotype.Component;

@Component
class Configured implements
        PingingDriver,
        FirstLegDriver,
        SecondLegDriver
{

    final AccountsConfiguration accountsConfiguration;
    final AsteriskConfiguration asteriskConfiguration;

    Configured(AccountsConfiguration accountsConfiguration, AsteriskConfiguration asteriskConfiguration) {
        this.accountsConfiguration = accountsConfiguration;
        this.asteriskConfiguration = asteriskConfiguration;
    }

    @Override
    public PhoneDriver pingingDriver() {
        try {
            final PhoneDriverFactory phoneFactory = new PhoneDriverFactory(asteriskConfiguration.getHost(), asteriskConfiguration.getPort());
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getPinging().getUsername(), accountsConfiguration.getPinging().getPassword());
            return phoneFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PhoneDriver firstLegDriver() {
        try {
            final PhoneDriverFactory phoneFactory = new PhoneDriverFactory(asteriskConfiguration.getHost(), asteriskConfiguration.getPort());
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getFirstLeg().getUsername(), accountsConfiguration.getFirstLeg().getPassword());
            return phoneFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PhoneDriver secondLegDriver() {
        try {
            final PhoneDriverFactory phoneFactory = new PhoneDriverFactory(asteriskConfiguration.getHost(), asteriskConfiguration.getPort());
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getSecondLeg().getUsername(), accountsConfiguration.getSecondLeg().getPassword());
            return phoneFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}


