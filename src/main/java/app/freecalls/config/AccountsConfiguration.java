package app.freecalls.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.StringJoiner;

@Configuration
@ConfigurationProperties(prefix = "accounts")
class AccountsConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AccountsConfiguration.class);

    private Account pinging;
    private Account firstLeg;
    private Account secondLeg;

    @PostConstruct
    void print() {
        StringJoiner out = new StringJoiner(";");
        out.add("Configuration Accounts");
        out.add("Pinging");
        out.add(pinging.username);
        out.add("First leg");
        out.add(firstLeg.username);
        out.add("Second leg");
        out.add(secondLeg.username);
        logger.info(out.toString());
    }

    public Account getPinging() {
        return pinging;
    }

    public void setPinging(Account pinging) {
        this.pinging = pinging;
    }

    public Account getFirstLeg() {
        return firstLeg;
    }

    public void setFirstLeg(Account firstLeg) {
        this.firstLeg = firstLeg;
    }

    public Account getSecondLeg() {
        return secondLeg;
    }

    public void setSecondLeg(Account secondLeg) {
        this.secondLeg = secondLeg;
    }

    static class Account {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
