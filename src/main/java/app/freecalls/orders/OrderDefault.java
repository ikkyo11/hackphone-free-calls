package app.freecalls.orders;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class OrderDefault implements Order {

    protected final LinkedList<PhoneNumber> orders = new LinkedList<>();

    @Override
    public void callMe(PhoneNumber phoneNumber) {
        orders.offer(phoneNumber);
    }

    private Optional<PhoneNumber> nextOrder() {
        return Optional.ofNullable(orders.poll());
    }

    Optional<OrderExecutor.OrderExecutorDriver> executeOne(OrderExecutor... executor) {
        return nextOrder()
                .stream()
                .map(OrderDefault::createDriver)
                .map(fn->fn.apply(behaviourBasedOnState()))
                .map(fn->fn.apply(Arrays.asList(executor).stream()))
                .findFirst();
    }

    interface DriverBehaviour {
        void pending(Session session);
        void noPending();
        void rollback(PhoneNumber phoneNumber);
    }

    protected DriverBehaviour behaviourBasedOnState() {
        return behaviourBasedOn(orders);
    }

    private static DriverBehaviour behaviourBasedOn(LinkedList<PhoneNumber> orders) {
        return new DriverBehaviour() {
            @Override
            public void pending(Session session) {

            }

            @Override
            public void noPending() {

            }

            @Override
            public void rollback(PhoneNumber phoneNumber) {
                orders.offer(phoneNumber);
            }
        };
    }

    private static Function<DriverBehaviour, Function<Stream<OrderExecutor>, OrderExecutor.OrderExecutorDriver>> createDriver(PhoneNumber phoneNumber) {
        return behaviour -> executors -> {
            behaviour.pending(new Session(phoneNumber));
            OrderExecutor.OrderExecutorDriver driver = new OrderExecutor.OrderExecutorDriver() {
                @Override
                public void finish() {
                    behaviour.noPending();
                }

                @Override
                public void rollback() {
                    behaviour.rollback(phoneNumber);
                }
            };
            executors.forEach(e -> e.onStarted(driver, phoneNumber));
            return driver;
        };
    }

    int countOrders() {
        return orders.size();
    }

    static class Session {
        private final PhoneNumber by;

        public Session(PhoneNumber by) {
            this.by = by;
        }
    }

}
