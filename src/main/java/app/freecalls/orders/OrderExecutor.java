package app.freecalls.orders;

public interface OrderExecutor {
    void onStarted(OrderExecutorDriver driver);

    interface OrderExecutorDriver {
        void finish();
        void rollback();
    }
}
