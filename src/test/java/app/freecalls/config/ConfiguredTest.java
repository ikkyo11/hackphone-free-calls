package app.freecalls.config;

import hackphone.phone.PhoneDriver;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class ConfiguredTest {

    @Test
    public void pinging_phoneDriver_is_produced() {
        Configured configured = configured();
        PhoneDriver driver = configured.pingingDriver();
        assertNotNull(driver);
    }

    @Test
    public void firstLeg_phoneDriver_is_produced() {
        Configured configured = configured();
        PhoneDriver driver = configured.firstLegDriver();
        assertNotNull(driver);
    }

    @Test
    public void secondLeg_phoneDriver_is_produced() {
        Configured configured = configured();
        PhoneDriver driver = configured.secondLegDriver();
        assertNotNull(driver);
    }

    private Configured configured() {

        AccountsConfiguration accountsConfiguration = new AccountsConfiguration();
        accountsConfiguration.setPinging(new AccountsConfiguration.Account("pUser", "pPassword"));
        accountsConfiguration.setFirstLeg(new AccountsConfiguration.Account("fUser", "fPassword"));
        accountsConfiguration.setSecondLeg(new AccountsConfiguration.Account("sUser", "sPassword"));

        AsteriskConfiguration asteriskConfiguration = new AsteriskConfiguration();
        asteriskConfiguration.setHost("snmill.com");
        asteriskConfiguration.setPort(5060);

        return new Configured(accountsConfiguration, asteriskConfiguration);
    }
}