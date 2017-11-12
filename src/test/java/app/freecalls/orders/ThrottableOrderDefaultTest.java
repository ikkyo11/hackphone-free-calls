package app.freecalls.orders;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ThrottableOrderDefaultTest {

    ThrottableOrderDefault sut;

    @Before
    public void before() {
        sut = new ThrottableOrderDefault();
    }

    @Test
    public void no_session_is_in_pending_state_because_no_executor_was_set() {
        sut.callMe(new PhoneNumber("111555444"));
        assertEquals(false, sut.getPendingSession().isPresent());
    }

    @Test
    public void one_session_is_in_pending_state_because_executor_was_set() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {

        });
        assertEquals(true, sut.getPendingSession().isPresent());
    }

    @Test
    public void there_is_no_pending_session_after_finished_session() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {
            driver.finish();
        });
        assertEquals(false, sut.getPendingSession().isPresent());
    }

    @Test
    public void only_one_order_is_executed_on_demand() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.callMe(new PhoneNumber("222555444"));
        sut.callMe(new PhoneNumber("333555444"));
        sut.executeOne().ifPresent(OrderExecutor.OrderExecutorDriver::finish);
        assertEquals(false, sut.getPendingSession().isPresent());
    }

    @Test
    public void no_error_during_executing_when_there_are_no_orders() {
        Optional<OrderExecutor.OrderExecutorDriver> driver = sut.executeOne();
        driver.ifPresent(OrderExecutor.OrderExecutorDriver::finish);
        assertEquals(false, driver.isPresent());
        assertEquals(false, sut.getPendingSession().isPresent());
    }

    @Test
    public void next_executor_is_performed_after_finish_previous_session() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.callMe(new PhoneNumber("222555444"));
        sut.callMe(new PhoneNumber("333555444"));
        Optional<OrderExecutor.OrderExecutorDriver> driverFirst = sut.executeOne();
        Optional<OrderExecutor.OrderExecutorDriver> driverSecond = sut.executeOne();
        assertEquals(true, driverFirst.isPresent());
        assertEquals(false, driverSecond.isPresent());
        assertEquals(2L, sut.countOrders());
    }
}
