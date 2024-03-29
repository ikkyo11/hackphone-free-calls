package app.freecalls.orders;

import hackphone.phone.PhoneDriver;
import hackphone.phone.configuration.SignallingContext;
import hackphone.phone.registering.RegisteringEvents;
import hackphone.phone.registering.stateMachine.InformationAboutCaller;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TriggerTest {

    @Mock
    PhoneDriver pinger;

    @Mock
    SignallingContext signallingContext;

    @Mock
    Order order;

    @Mock
    InformationAboutCaller informationAboutCaller;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        doAnswer(invocationOnMock -> {
            RegisteringEvents events = invocationOnMock.getArgument(0);
            events.onRegisteringSucceeded(signallingContext);
            events.onIncomingCall(signallingContext, informationAboutCaller);
            return null;
        }).when(pinger).register(any(RegisteringEvents.class));
    }

    @Test
    public void pingingLeg_is_registered() {
        Trigger sut = new Trigger(()->pinger);
        sut.waitForIncomingCalls(order::callMe);
        verify(pinger, times(1)).register(any(RegisteringEvents.class));
    }


    @Test
    public void call_me_is_called_after_incoming_call() {
        Trigger sut = new Trigger(()->pinger);
        sut.waitForIncomingCalls(order::callMe);
        verify(order, times(1)).callMe(any(PhoneNumber.class));
    }

}