package app.freecalls.pinging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderDefault implements Order {

    private static final Logger logger = LoggerFactory.getLogger(OrderDefault.class);

    @Override
    public void callMe(PhoneNumber phoneNumber) {
        logger.info("I will call {} to get phone number of friend.", phoneNumber);
    }
}
