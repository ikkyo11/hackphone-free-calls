package app.freecalls.executor;

import app.freecalls.config.FirstLegDriver;
import app.freecalls.orders.OrderExecutor;
import app.freecalls.orders.PhoneNumber;
import hackphone.phone.PhoneDriver;
import hackphone.phone.PhoneDriverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderExecutorScenario implements OrderExecutor {

    private static final Logger logger = LoggerFactory.getLogger(OrderExecutorScenario.class);

    final FirstLegDriver firstLegDriver;
    final PhoneDriverFactory phoneDriverFactory;

    public OrderExecutorScenario(
            FirstLegDriver firstLegDriver,
            PhoneDriverFactory phoneDriverFactory) {
        this.firstLegDriver = firstLegDriver;
        this.phoneDriverFactory = phoneDriverFactory;
    }

    @Override
    public void onStarted(OrderExecutorDriver driver, PhoneNumber caller) {
        PhoneDriver phone = firstLegDriver.firstLegDriver();
        logger.info("Calling to {} for friend-s contact. Using signalization {}.", caller.toString(), phone.toString());
        phone.register(new RegisteringEventsImpl(caller.callingTo(), driver, phoneDriverFactory.getStrategies()));
    }
}
