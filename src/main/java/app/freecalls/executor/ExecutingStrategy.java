package app.freecalls.executor;

import app.freecalls.orders.OrderExecutor;
import app.freecalls.orders.ThrottableOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
class ExecutingStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ExecutingStrategy.class);

    final ThrottableOrder orders;
    final ObjectFactory<OrderExecutor> executorFactory;

    public ExecutingStrategy(
            ThrottableOrder orders,
            @Qualifier("postponed") ObjectFactory<OrderExecutor> executorFactory) {
        this.orders = orders;
        this.executorFactory = executorFactory;
    }

    @Scheduled(initialDelayString = "${executing.strategy.initialDelay:5000}", fixedRateString = "${executing.strategy.rate:5000}")
    void scheduled() {
        orders.executeOne(executorFactory.getObject())
                .ifPresentOrElse(
                        drv -> logger.debug("Driver {}", drv),
                        ()-> logger.debug("No order to execute.")
                );
    }
}
