package app.freecalls.orders;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
class ThrottableOrderDefault extends OrderDefault implements ThrottableOrder {

    private final AtomicReference<Session> pending = new AtomicReference<>();

    Optional<Session> getPendingSession() {
        return Optional.ofNullable(pending.get());
    }

    @Override
    protected DriverBehaviour behaviourBasedOnState() {
        return behaviourBasedOn(pending, orders);
    }

    @Override
    public Optional<OrderExecutor.OrderExecutorDriver> executeOne(OrderExecutor... executor) {
        if(getPendingSession().isPresent()) {
            return Optional.empty();
        } else {
            return super.executeOne(executor);
        }
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
}
