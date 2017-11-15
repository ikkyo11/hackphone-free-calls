package app.freecalls.executor;

import app.freecalls.config.FirstLegDriver;
import app.freecalls.config.SecondLegDriver;
import app.freecalls.orders.PhoneNumber;
import hackphone.phone.PhoneDriverFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@Qualifier("postponed")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class PostponedOrderExecutorScenario extends OrderExecutorScenario {

    private final long postponingMillis;

    public PostponedOrderExecutorScenario(
            @Value("${executing.strategy.postponing:6000}") long postponingMillis,
            FirstLegDriver firstLegDriver,
            SecondLegDriver secondLegDriver,
            PhoneDriverFactory phoneDriverFactory) {
        super(firstLegDriver, secondLegDriver, phoneDriverFactory);
        this.postponingMillis = postponingMillis;
    }

    @Override
    public void onStarted(OrderExecutorDriver driver, PhoneNumber caller) {
        CompletableFuture
                .delayedExecutor(postponingMillis, TimeUnit.MILLISECONDS)
                .execute(()-> {
                    super.onStarted(driver, caller);
                });
    }
}
