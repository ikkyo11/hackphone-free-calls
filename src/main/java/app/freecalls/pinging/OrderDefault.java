package app.freecalls.pinging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sdp.Phone;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class OrderDefault implements Order {

    private static final Logger logger = LoggerFactory.getLogger(OrderDefault.class);

    final LinkedList<PhoneNumber> orders = new LinkedList<>();

    @Override
    public void callMe(PhoneNumber phoneNumber) {
        logger.info("I will call {} to get phone number of friend.", phoneNumber);
        orders.offer(phoneNumber);
    }

    Optional<Session> getPendingSession() {
        return Optional.ofNullable(pending.get());
    }

    private final AtomicReference<Session> pending = new AtomicReference<>();

    void executeOne(OrderExecutor executor) {
        PhoneNumber number = orders.poll();
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
        executor.onStarted(driver);
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
