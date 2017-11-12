package app.freecalls.pinging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class OrderDefault implements Order {

    private static final Logger logger = LoggerFactory.getLogger(OrderDefault.class);

    private final LinkedList<PhoneNumber> orders = new LinkedList<>();
    private final AtomicReference<Session> pending = new AtomicReference<>();

    @Override
    public void callMe(PhoneNumber phoneNumber) {
        logger.info("I will call {} to get phone number of friend.", phoneNumber);
        orders.offer(phoneNumber);
    }

    Optional<Session> getPendingSession() {
        return Optional.ofNullable(pending.get());
    }

    private Optional<PhoneNumber> nextOrder() {
        return Optional.ofNullable(orders.poll());
    }

    Optional<OrderExecutorDriver> executeOne(OrderExecutor... executor) {
        return nextOrder()
                .stream()
                .map(OrderDefault::createDriver)
                .map(fn->fn.apply(behaviourBasedOn(pending, orders)))
                .map(fn->fn.apply(Arrays.asList(executor).stream()))
                .findFirst();
    }

    interface DriverBehaviour {
        void pending(Session session);
        void noPending();
        void rollback(PhoneNumber phoneNumber);
    }

    private static DriverBehaviour behaviourBasedOn(AtomicReference pending, LinkedList<PhoneNumber> orders) {
        return new DriverBehaviour() {
            @Override
            public void pending(Session session) {
                pending.set(session);
            }

            @Override
            public void noPending() {
                pending.set(null);
            }

            @Override
            public void rollback(PhoneNumber phoneNumber) {
                orders.offer(phoneNumber);
            }
        };
    }

    private static Function<DriverBehaviour, Function<Stream<OrderExecutor>, OrderExecutorDriver>> createDriver(PhoneNumber phoneNumber) {
        return behaviour -> executors -> {
            behaviour.pending(new Session(phoneNumber));
            OrderExecutorDriver driver = new OrderExecutorDriver() {
                @Override
                public void finish() {
                    behaviour.noPending();
                }

                @Override
                public void rollback() {
                    behaviour.rollback(phoneNumber);
                }
            };
            executors.forEach(e -> e.onStarted(driver));
            return driver;
        };
    }

    int countOrders() {
        return orders.size();
    }

    public interface OrderExecutorDriver {
        void finish();
        void rollback();
    }

    public interface OrderExecutor {
        void onStarted(OrderExecutorDriver driver);
    }

    static class Session {
        private final PhoneNumber by;

        public Session(PhoneNumber by) {
            this.by = by;
        }
    }

}
