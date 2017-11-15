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

class ReachingFriendRegisteringEventsImpl implements RegisteringEvents {

    private static final Logger logger = LoggerFactory.getLogger(ReachingFriendRegisteringEventsImpl.class);
    private final CallingTo callingTo;
    private final Streaming streaming;
    private final ConfiguredStrategies phoneDriverStrategies;
    private final OrderExecutor.OrderExecutorDriver driver;

    public ReachingFriendRegisteringEventsImpl(
            CallingTo callingTo,
            Streaming streaming,
            ConfiguredStrategies phoneDriverStrategies,
            OrderExecutor.OrderExecutorDriver driver) {
        this.callingTo = callingTo;
        this.streaming = streaming;
        this.phoneDriverStrategies = phoneDriverStrategies;
        this.driver = driver;
    }

    @Override
    public State createCustomizedStateForRegisteredState(SignallingContext signallingContext, SignallingSender signallingSender) {
        signallingContext.getInviteingContext().setCallingTo(callingTo);
        GettingFriendNumberEvents gettingFriendNumberEvents = new GettingFriendNumberEvents() {
            @Override
            public void onDtmf(String number) {
                logger.info("DTMF signal is ignored by Frind-s reacher");
                // logger.info("Friend-s phone number|{}|", number);
                // callYourFriend(new CallingTo(number));
            }

            @Override
            public void onAudio(ByteBuffer alaw_8000Hz_20ms) {
                streaming.fromSecond(alaw_8000Hz_20ms);
            }

            @Override
            public void onSenderAvailable(MediaSender sender_alaw_8000Hz_20ms) {
                streaming.secondSender(sender_alaw_8000Hz_20ms);
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
        // TODO: moze zaimast rollbacka powiadomic strone A ze polaczenie sie (dokladnie to rejestacja) nie udalo
    }

    @Override
    public void onIncomingCall(SignallingContext signallingContext, InformationAboutCaller informationAboutCaller) {
        logger.error("|Account {}|Nobody should call me but the number {} did it.", signallingContext.accountName(), informationAboutCaller.getPhoneNumber());
    }
}
