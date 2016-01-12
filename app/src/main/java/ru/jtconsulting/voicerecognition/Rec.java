package ru.jtconsulting.voicerecognition;


import android.util.Log;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
/**
 * Запись звука
 */
public class Rec{
    private    int myBufferSize = 8192;
    private int sampleRate = 8000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private    AudioRecord audioRecord;

    public boolean isReading() {
        return isReading;
    }

    public void setReading(boolean isReading) {
        this.isReading = isReading;
    }

    public boolean isReading;
    final String TAG = "AudioRecord";
    public Rec() {
        Log.d(TAG, "Constructor");
        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;
        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + myBufferSize);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
    }
    public void  Start(){
        if (audioRecord == null)
            return;
        isReading = true;
        byte[] myBuffer = new byte[myBufferSize];
        int readCount = 0;
        int totalCount = 0;
        while (isReading) {
            readCount = audioRecord.read(myBuffer, 0, myBufferSize);
            totalCount += readCount;
            Log.d(TAG, "readCount = " + readCount + ", totalCount = "
                    + totalCount);
        }
        Log.d(TAG, "FINITA");
    }


}


