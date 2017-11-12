package app.freecalls.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsConfigurationIntegrationTest {

    @Autowired
    AccountsConfiguration accountsConfiguration;

    @Test
    public void account_pinging_with_username() {
        assertEquals("orders-test-user", accountsConfiguration.getPinging().getUsername());
    }

    @Test
    public void account_pinging_with_password() {
        assertEquals("orders-test-user-password", accountsConfiguration.getPinging().getPassword());
    }

    @Test
    public void account_firstLeg_with_username() {
        assertEquals("first-test-user", accountsConfiguration.getFirstLeg().getUsername());
    }

    @Test
    public void account_firstLeg_with_password() {
        assertEquals("first-test-user-password", accountsConfiguration.getFirstLeg().getPassword());
    }

    @Test
    public void account_secondLeg_with_username() {
        assertEquals("second-test-user", accountsConfiguration.getSecondLeg().getUsername());
    }

    @Test
    public void account_secondLeg_with_password() {
        assertEquals("second-test-user-password", accountsConfiguration.getSecondLeg().getPassword());
    }


}