package ru.jtconsulting.voicerecognition;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
/**
 * Created by www on 11.01.2016.
 */
public class Handl extends Handler {
    public Handl() {
        super();
        Log.d(LOG_TAG, "handleMessage:START");
    }

    final String LOG_TAG = "Handl";
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Log.d(LOG_TAG, "handleMessage:"+msg.what);

    }
}
