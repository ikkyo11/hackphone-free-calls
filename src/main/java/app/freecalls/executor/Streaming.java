package app.freecalls.executor;

import hackphone.media.PackagingAlaw;
import hackphone.media.io.MediaSender;

import java.nio.ByteBuffer;

class Streaming {
    final PackagingAlaw firstPackaging = new PackagingAlaw();
    final PackagingAlaw secondPackaging = new PackagingAlaw();

    MediaSender first;
    MediaSender second;

    synchronized void firstSender(MediaSender first) {
        this.first = first;
    }

    synchronized void fromFirst(ByteBuffer alaw_8000Hz_20ms) {
        if(second!=null) {
            second.accept(secondPackaging.packageAlaw(alaw_8000Hz_20ms));
        }
    }

    synchronized void secondSender(MediaSender second) {
        this.second = second;
    }

    synchronized void fromSecond(ByteBuffer alaw_8000Hz_20ms) {
        if(first!=null) {
            first.accept(firstPackaging.packageAlaw(alaw_8000Hz_20ms));
        }
    }
}
