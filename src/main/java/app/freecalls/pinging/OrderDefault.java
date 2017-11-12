package app.freecalls.pinging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

    Optional<OrderExecutorDriver> executeOne(OrderExecutor... executor) {
        PhoneNumber number = orders.poll();
        if(number!=null) {
            pending.set(new Session(number));
            OrderExecutorDriver driver = new OrderExecutorDriver() {
                @Override
                public void finish() {
                    pending.set(null);
                }

                @Override
                public void rollback() {
                    orders.offer(number);
                }
            };
            Arrays.asList(executor).forEach(e-> e.onStarted(driver));
            return Optional.of(driver);
        } else {
            return Optional.empty();
        }
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
