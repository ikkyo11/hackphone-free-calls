package app.freecalls.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsteriskConfigurationIntegrationTest {

    @Autowired
    AsteriskConfiguration asteriskConfiguration;

    @Test
    public void hostname() {
        assertEquals("asterisk.softwaremind.pl", asteriskConfiguration.getHost());
    }

    @Test
    public void port() {
        assertEquals(5060, asteriskConfiguration.getPort());
    }

}