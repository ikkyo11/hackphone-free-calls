package app.freecalls.pinging;

import java.util.Optional;

interface ThrottableOrder extends Order {

    Optional<OrderDefault.OrderExecutorDriver> executeOne(OrderDefault.OrderExecutor... executor);
}
