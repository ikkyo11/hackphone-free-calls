package app.freecalls.pinging;

import hackphone.phone.registering.stateMachine.InformationAboutCaller;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class Runs {

    final Trigger trigger;
    final Order order;

    public Runs(Trigger trigger, Order order) {
        this.trigger = trigger;
        this.order = order;
    }

    @PostConstruct
    void run() {
        trigger.waitForIncomingCalls(this::callback);
    }

    private void callback(InformationAboutCaller informationAboutCaller) {
        order.callMe(new PhoneNumber(informationAboutCaller.getPhoneNumber()));
    }
}
