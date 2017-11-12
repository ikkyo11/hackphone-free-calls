package app.freecalls.pinging;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class Runs {

    final Trigger trigger;
    final ThrottableOrder order;

    public Runs(Trigger trigger, ThrottableOrder order) {
        this.trigger = trigger;
        this.order = order;
    }

    @PostConstruct
    void run() {
        trigger.waitForIncomingCalls(order::callMe);
    }
}
