package app.freecalls.executor;

import app.freecalls.orders.ThrottableOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
class ExecutingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ExecutingStrategy.class);

    final ThrottableOrder orders;

    public ExecutingStrategy(ThrottableOrder orders) {
        this.orders = orders;
    }

    @Scheduled(initialDelayString = "${executing.strategy.initialDelay:5000}", fixedRateString = "${executing.strategy.rate:5000}")
    void scheduled() {
        orders.executeOne(new OrderExecutorScenario())
                .ifPresentOrElse(
                        drv -> logger.info("Driver {}", drv),
                        ()-> logger.debug("No order to execute.")
                );
    }
}
