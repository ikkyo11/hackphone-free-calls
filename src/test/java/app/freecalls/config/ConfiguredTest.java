package app.freecalls.config;

import hackphone.phone.PhoneDriver;
import hackphone.phone.configuration.SignallingContext;
import hackphone.phone.registering.RegisteringEventsSupport;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class ConfiguredTest {

    @Test
    public void pinging_phoneDriver() {
        final AtomicBoolean success = new AtomicBoolean(false);
        final AtomicBoolean failure = new AtomicBoolean(false);

        Configured configured = configured();
        PhoneDriver driver = configured.pingingDriver();
        driver.register(new RegisteringEventsSupport() {

            @Override
            public void onRegisteringSucceeded(SignallingContext signallingContext) {
                success.set(true);
            }

            @Override
            public void onRegisteringFailed(SignallingContext signallingContext, String s) {
                failure.set(true);
            }
        });

        assertEquals(true, failure.get());
        assertEquals(false, success.get());
    }



    private Configured configured() {

        AccountsConfiguration accountsConfiguration = new AccountsConfiguration();
        accountsConfiguration.setPinging(new AccountsConfiguration.Account("pUser", "pPassword"));
        accountsConfiguration.setFirstLeg(new AccountsConfiguration.Account("fUser", "fPassword"));
        accountsConfiguration.setFirstLeg(new AccountsConfiguration.Account("sUser", "sPassword"));

        AsteriskConfiguration asteriskConfiguration = new AsteriskConfiguration();
        asteriskConfiguration.setHost("snmill.com");
        asteriskConfiguration.setPort(5060);

        return new Configured(accountsConfiguration, asteriskConfiguration);
    }
}