package app.freecalls.executor;

import hackphone.media.AudioDetector;
import hackphone.media.DtmfDetector;
import hackphone.media.io.InputOutputMediaStrategy;
import hackphone.media.io.MediaReceiver;
import hackphone.media.io.MediaSender;
import hackphone.media.io.MediaTrafficContext;
import hackphone.phone.configuration.SignallingContext;
import hackphone.phone.inviteing.InviteingEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

class GettingFriendNumber implements InviteingEvents {

    private static final Logger logger = LoggerFactory.getLogger(GettingFriendNumber.class);

    final Consumer<String> onFailure;
    final Consumer<String> onBye;

    final GettingFriendNumberEvents events;
    final InputOutputMediaStrategy mediaStrategy;

    public GettingFriendNumber(
            Consumer<String> onFailure,
            Consumer<String> onBye,
            InputOutputMediaStrategy mediaStrategy,
            GettingFriendNumberEvents events) {
        this.mediaStrategy = mediaStrategy;
        this.onFailure = onFailure;
        this.onBye = onBye;
        this.events = events;
    }

    @Override
    public void onSuccess(SignallingContext signallingContext, InetSocketAddress rtpPeer) {
        signallingContext.getInviteingContext().setPeerRtp(rtpPeer);
        int myPort = signallingContext.trafficable_myRtpPort();
        logger.info("Getting Friend Number|IN|INVITE|Success|RTP Peer {}:{}", rtpPeer, myPort);
        final DtmfDetector dtmfDetector = new DtmfDetector(events::onDtmf, (key) -> { });
        final AudioDetector audioDetector = new AudioDetector((audioPacket) -> events.onAudio(audioPacket));
        final InetSocketAddress me = new InetSocketAddress(signallingContext.myIp(), signallingContext.trafficable_myRtpPort());
        final MediaTrafficContext trafficContext = new MediaTrafficContext(me, rtpPeer);
        final MediaSender sender = mediaStrategy.connect(new MediaReceiver() {
            @Override
            public void disconnected() {
                signallingContext.logger().info("Media traffic disconnected");
            }

            @Override
            public void accept(ByteBuffer byteBuffer) {
                dtmfDetector.accept(byteBuffer);
                audioDetector.accept(byteBuffer);
            }
        }, trafficContext);
        events.onSenderAvailable(sender);
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
