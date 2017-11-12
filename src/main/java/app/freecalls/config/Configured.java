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
    final PhoneDriverFactory phoneDriverFactory;

    Configured(AccountsConfiguration accountsConfiguration, PhoneDriverFactory phoneDriverFactory) {
        this.accountsConfiguration = accountsConfiguration;
        this.phoneDriverFactory = phoneDriverFactory;
    }

    @Override
    public PhoneDriver pingingDriver() {
        try {
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getPinging().getUsername(), accountsConfiguration.getPinging().getPassword());
            return phoneDriverFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PhoneDriver firstLegDriver() {
        try {
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getFirstLeg().getUsername(), accountsConfiguration.getFirstLeg().getPassword());
            return phoneDriverFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PhoneDriver secondLegDriver() {
        try {
            final PhoneAccount account = new PhoneAccount(accountsConfiguration.getSecondLeg().getUsername(), accountsConfiguration.getSecondLeg().getPassword());
            return phoneDriverFactory.phone(account);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}


