package app.freecalls.pinging;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class OrderDefaultTest {

    OrderDefault sut;

    @Before
    public void before() {
        sut = new OrderDefault();
    }

    @Test
    public void offered_3_numbers_so_3_orders_in_sut() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.callMe(new PhoneNumber("222555444"));
        sut.callMe(new PhoneNumber("333555444"));
        assertEquals(3L, sut.countOrders());
    }

    @Test
    public void no_executor_was_set_so_there_are_1_orders() {
        sut.callMe(new PhoneNumber("111555444"));
        assertEquals(1L, sut.countOrders());
    }

    @Test
    public void no_sessions_are_in_orders_because_executor_was_set_and_one_session_was_pended() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {

        });
        assertEquals(0L, sut.countOrders());
    }

    @Test
    public void no_sessions_are_in_orders_aftere_finishing_the_last_one() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne().ifPresent(OrderDefault.OrderExecutorDriver::finish);
        assertEquals(0L, sut.countOrders());
    }

    @Test
    public void executions_with_errors_affects_rollbacking_to_orders() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne().ifPresent(OrderDefault.OrderExecutorDriver::rollback);
        assertEquals(1L, sut.countOrders());
    }


    @Test
    public void only_one_order_is_executed_on_demand() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.callMe(new PhoneNumber("222555444"));
        sut.callMe(new PhoneNumber("333555444"));
        sut.executeOne().ifPresent(OrderDefault.OrderExecutorDriver::finish);
        assertEquals(2L, sut.countOrders());
    }

    @Test
    public void no_error_during_executing_when_there_are_no_orders() {
        Optional<OrderDefault.OrderExecutorDriver> driver = sut.executeOne();
        driver.ifPresent(OrderDefault.OrderExecutorDriver::finish);
        assertEquals(false, driver.isPresent());
        assertEquals(0L, sut.countOrders());
    }
}