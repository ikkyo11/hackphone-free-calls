package app.freecalls.orders;

public interface OrderExecutor {
    void onStarted(OrderExecutorDriver driver, PhoneNumber caller);

    interface OrderExecutorDriver {
        void finish();
        void rollback();
    }
}
