package app.freecalls.executor;

import app.freecalls.orders.OrderExecutor;
import hackphone.media.io.MediaSender;
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

import java.nio.ByteBuffer;
import java.util.function.Consumer;

class GettingFriendNumberRegisteringEventsImpl implements RegisteringEvents {

    private static final Logger logger = LoggerFactory.getLogger(GettingFriendNumberRegisteringEventsImpl.class);

    private final CallingTo callingTo;
    private final OrderExecutor.OrderExecutorDriver driver;
    private final ConfiguredStrategies phoneDriverStrategies;
    private final Streaming streaming;
    private final Consumer<CallingTo> onFriendIsKnown;

    public GettingFriendNumberRegisteringEventsImpl(
            CallingTo callingTo,
            Streaming streaming,
            OrderExecutor.OrderExecutorDriver driver,
            ConfiguredStrategies phoneDriverStrategies,
            Consumer<CallingTo> onFriendIsKnown) {
        this.callingTo = callingTo;
        this.streaming = streaming;
        this.driver = driver;
        this.phoneDriverStrategies = phoneDriverStrategies;
        this.onFriendIsKnown = onFriendIsKnown;
    }

    @Override
    public State createCustomizedStateForRegisteredState(SignallingContext signallingContext, SignallingSender signallingSender) {
        signallingContext.getInviteingContext().setCallingTo(callingTo);



        GettingFriendNumberEvents gettingFriendNumberEvents = new GettingFriendNumberEvents() {
            @Override
            public void onDtmf(String number) {
                logger.info("Friend-s phone number|{}|", number);
                onFriendIsKnown.accept(new CallingTo(number));
            }

            @Override
            public void onAudio(ByteBuffer alaw_8000Hz_20ms) {
                streaming.fromFirst(alaw_8000Hz_20ms);
            }

            @Override
            public void onSenderAvailable(MediaSender sender_alaw_8000Hz_20ms) {
                streaming.firstSender(sender_alaw_8000Hz_20ms);
            }
        };


        return new InviteingStateMachine(
                signallingContext,
                signallingSender,
                phoneDriverStrategies,
                new GettingFriendNumber(
                        failureReason -> driver.rollback(),
                        bye -> driver.finish(),
                        phoneDriverStrategies.mediaStrategy(),
                        gettingFriendNumberEvents));
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
