package app.freecalls.executor;

import app.freecalls.orders.OrderExecutor;
import app.freecalls.orders.ThrottableOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class ExecutingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ExecutingStrategy.class);

    final ThrottableOrder orders;

    public ExecutingStrategy(ThrottableOrder orders) {
        this.orders = orders;
    }

    @Scheduled(initialDelayString = "${executing.strategy.initialDelay:5000}", fixedRateString = "${executing.strategy.rate:5000}")
    void scheduled() {
        Optional<OrderExecutor.OrderExecutorDriver> driver = orders.executeOne(new OrderExecutorScenario());
        driver.ifPresentOrElse(drv -> logger.info("Driver {}", drv), ()-> {
            logger.debug("No order to execute.");
        });
    }
}
