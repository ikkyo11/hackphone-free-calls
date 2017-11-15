package app.freecalls.executor;

import hackphone.media.io.MediaSender;

import java.nio.ByteBuffer;

interface GettingFriendNumberEvents {

    void onDtmf(String number);

    void onAudio(ByteBuffer alaw_8000Hz_20ms);

    void onSenderAvailable(MediaSender sender_alaw_8000Hz_20ms);
}
