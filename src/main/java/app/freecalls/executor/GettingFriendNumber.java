package app.freecalls.executor;

import hackphone.phone.configuration.SignallingContext;
import hackphone.phone.inviteing.InviteingEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

class GettingFriendNumber implements InviteingEvents {

    private static final Logger logger = LoggerFactory.getLogger(GettingFriendNumber.class);

    final Consumer<String> onFailure;
    final Consumer<String> onBye;

    public GettingFriendNumber(Consumer<String> onFailure, Consumer<String> onBye) {
        this.onFailure = onFailure;
        this.onBye = onBye;
    }

    @Override
    public void onSuccess(SignallingContext signallingContext, InetSocketAddress rtpPeer) {
        signallingContext.getInviteingContext().setPeerRtp(rtpPeer);
        int myPort = signallingContext.trafficable_myRtpPort();
        logger.info("Getting Friend Number|IN|INVITE|Success|RTP Peer {}:{}", rtpPeer, myPort);
    }

    @Override
    public void onFailure(SignallingContext signallingContext, String reason) {
        logger.info("Getting Friend Number|IN|INVITE|Failure|{}", reason);
        onFailure.accept(reason);
        // TODO: jesli bedzie ciagle Busy Here to ciagle
        // bedzie na ten numer dzwonic, az do bolu, dlatego
        // trzeba zrobic jakas max ilosc wydzwonien albo
        // schemat kolejki powtorzeniowej
    }

    @Override
    public void onBye(SignallingContext signallingContext) {
        logger.info("Getting Friend Number|IN|BYE");
        onBye.accept("BYE");
    }

    @Override
    public void onRinging(SignallingContext signallingContext) {
        logger.info("|Getting Friend Number|IN|INVITE|Ringing");
    }
}
