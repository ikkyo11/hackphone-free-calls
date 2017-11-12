package app.freecalls.executor;

import app.freecalls.orders.OrderExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OrderExecutorScenario implements OrderExecutor {

    private static final Logger logger = LoggerFactory.getLogger(OrderExecutorScenario.class);

    @Override
    public void onStarted(OrderExecutorDriver driver) {
        logger.info("Executing ..."+driver);
    }
}
