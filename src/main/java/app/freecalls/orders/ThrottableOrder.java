package app.freecalls.orders;

import java.util.Optional;

public interface ThrottableOrder extends Order {

    Optional<OrderExecutor.OrderExecutorDriver> executeOne(OrderExecutor... executor);
}
