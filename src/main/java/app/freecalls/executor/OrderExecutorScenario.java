package app.freecalls.executor;

import app.freecalls.config.FirstLegDriver;
import app.freecalls.config.SecondLegDriver;
import app.freecalls.orders.OrderExecutor;
import app.freecalls.orders.PhoneNumber;
import hackphone.phone.PhoneDriver;
import hackphone.phone.PhoneDriverFactory;
import hackphone.phone.inviteing.CallingTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class OrderExecutorScenario implements OrderExecutor {

    private static final Logger logger = LoggerFactory.getLogger(OrderExecutorScenario.class);

    final FirstLegDriver firstLegDriver;
    final SecondLegDriver secondLegDriver;
    final PhoneDriverFactory phoneDriverFactory;
    final Streaming streaming = new Streaming();

    public OrderExecutorScenario(
            FirstLegDriver firstLegDriver,
            SecondLegDriver secondLegDriver,
            PhoneDriverFactory phoneDriverFactory) {
        this.firstLegDriver = firstLegDriver;
        this.secondLegDriver = secondLegDriver;
        this.phoneDriverFactory = phoneDriverFactory;
    }

    private AtomicReference<OrderExecutorDriver> driverReference = new AtomicReference<>();

    @Override
    public void onStarted(OrderExecutorDriver driver, PhoneNumber caller) {
        driverReference.set(driver);
        final PhoneDriver phone = firstLegDriver.firstLegDriver();
        logger.info("Calling to {} for friend-s contact. Using signalization {}.", caller.toString(), phone.toString());
        phone.register(new GettingFriendNumberRegisteringEventsImpl(
                caller.callingTo(),
                streaming,
                driver,
                phoneDriverFactory.getStrategies(),
                this::callYourFriend));
    }

    private void callYourFriend(CallingTo callingTo) {
        final OrderExecutorDriver driver = driverReference.get();
        final PhoneDriver phone = secondLegDriver.secondLegDriver();
        logger.info("Calling to {}. It is your friend.", callingTo.getPhoneNumber());
        CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(()->{
            phone.register(new ReachingFriendRegisteringEventsImpl(
                    callingTo,
                    streaming,
                    phoneDriverFactory.getStrategies(),
                    driver
            ));
        });
    }
}
