package app.freecalls.pinging;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
class Runs {

    final Trigger trigger;

    public Runs(Trigger trigger) {
        this.trigger = trigger;
    }

    @PostConstruct
    void run() {
        trigger.waitForIncomingCalls();
    }


}
