package app.freecalls.executor;

import app.freecalls.orders.OrderExecutor;
import hackphone.phone.ConfiguredStrategies;
import hackphone.phone.configuration.SignallingContext;
import hackphone.phone.inviteing.CallingTo;
import hackphone.phone.inviteing.InviteingStateMachine;
import hackphone.phone.io.SignallingSender;
import hackphone.phone.registering.RegisteringEvents;
import hackphone.phone.registering.stateMachine.InformationAboutCaller;
import hackphone.phone.registering.stateMachine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RegisteringEventsImpl implements RegisteringEvents {

    private static final Logger logger = LoggerFactory.getLogger(RegisteringEventsImpl.class);

    private final CallingTo callingTo;
    private final OrderExecutor.OrderExecutorDriver driver;
    private final ConfiguredStrategies phoneDriverStrategies;

    public RegisteringEventsImpl(
            CallingTo callingTo,
            OrderExecutor.OrderExecutorDriver driver,
            ConfiguredStrategies phoneDriverStrategies) {
        this.callingTo = callingTo;
        this.driver = driver;
        this.phoneDriverStrategies = phoneDriverStrategies;
    }

    @Override
    public State createCustomizedStateForRegisteredState(SignallingContext signallingContext, SignallingSender signallingSender) {
        signallingContext.getInviteingContext().setCallingTo(callingTo);
        return new InviteingStateMachine(
                signallingContext,
                signallingSender,
                phoneDriverStrategies,
                new GettingFriendNumber(failureReason -> driver.rollback(), bye -> driver.finish()));
    }

    @Override
    public void onRegisteringSucceeded(SignallingContext signallingContext) {

    }

    @Override
    public void onRegisteringFailed(SignallingContext signallingContext, String reason) {
        logger.error("|Account {}|Registration filed due to reason {}.", signallingContext.accountName(), reason);
        driver.rollback();
    }

    @Override
    public void onIncomingCall(SignallingContext signallingContext, InformationAboutCaller informationAboutCaller) {
        logger.error("|Account {}|Nobody should call me but the number {} did it.", signallingContext.accountName(), informationAboutCaller.getPhoneNumber());
    }
}
