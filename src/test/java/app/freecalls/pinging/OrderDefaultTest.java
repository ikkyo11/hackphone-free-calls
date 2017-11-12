package app.freecalls.pinging;

import org.junit.Before;
import org.junit.Test;

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
    public void no_session_is_in_pending_state_because_no_executor_was_set() {
        sut.callMe(new PhoneNumber("111555444"));
        assertEquals(false, sut.getPendingSession().isPresent());
    }

    @Test
    public void no_executor_was_set_so_there_are_1_orders() {
        sut.callMe(new PhoneNumber("111555444"));
        assertEquals(1L, sut.countOrders());
    }

    @Test
    public void one_session_is_in_pending_state_because_executor_was_set() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {

        });
        assertEquals(true, sut.getPendingSession().isPresent());
    }

    @Test
    public void no_sessions_are_in_orders_because_executor_was_set_and_one_session_was_pended() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {

        });
        assertEquals(0L, sut.countOrders());
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
    public void no_sessions_are_in_orders_aftere_finishing_the_last_one() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {
            driver.finish();
        });
        assertEquals(0L, sut.countOrders());
    }

    @Test
    public void executions_with_errors_affects_rollbacking_to_orders() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.executeOne(driver -> {
            driver.rollback();
        });
        assertEquals(1L, sut.countOrders());
    }


    @Test
    public void only_one_order_is_executed_on_demand() {
        sut.callMe(new PhoneNumber("111555444"));
        sut.callMe(new PhoneNumber("222555444"));
        sut.callMe(new PhoneNumber("333555444"));
        sut.executeOne(driver -> {
            driver.finish();
        });
        assertEquals(false, sut.getPendingSession().isPresent());
        assertEquals(2L, sut.countOrders());
    }
}