package app.freecalls.orders;

import app.freecalls.config.PingingDriver;
import hackphone.phone.PhoneDriver;
import hackphone.phone.configuration.SignallingContext;
import hackphone.phone.io.SignallingSender;
import hackphone.phone.registering.RegisteringEvents;
import hackphone.phone.registering.stateMachine.InformationAboutCaller;
import hackphone.phone.registering.stateMachine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
class Trigger {

    private static final Logger logger = LoggerFactory.getLogger(Trigger.class);

    final PingingDriver pingingDriver;

    public Trigger(PingingDriver pingingDriver) {
        this.pingingDriver = pingingDriver;
    }

    void waitForIncomingCalls(Consumer<PhoneNumber> incomingCall) {
        PhoneDriver driver = pingingDriver.pingingDriver();
        driver.register(new RegisteringEvents() {

            @Override
            public State createCustomizedStateForRegisteredState(SignallingContext signallingContext, SignallingSender signallingSender) {
                return null;
            }

            @Override
            public void onRegisteringSucceeded(SignallingContext signallingContext) {
                logger.info("REGISTRATION|SUCCESS|{}", signallingContext.accountName());
            }

            @Override
            public void onRegisteringFailed(SignallingContext signallingContext, String reason) {
                logger.info("REGISTRATION|FAILURE|{}|{}", signallingContext.accountName(), reason);
            }

            @Override
            public void onIncomingCall(SignallingContext signallingContext, InformationAboutCaller informationAboutCaller) {
                logger.info("EVENT|INCOMING_CALL|{}|{}|", signallingContext.accountName(), informationAboutCaller);
                incomingCall.accept(new PhoneNumber(informationAboutCaller.getPhoneNumber()));
            }
        });
    }
}
