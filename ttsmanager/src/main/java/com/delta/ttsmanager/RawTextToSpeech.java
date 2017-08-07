package com.delta.ttsmanager;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Shaoqiang.Zhang on 2017/2/22.
 */

public class RawTextToSpeech implements TextToSpeech.OnInitListener, OnSpeakListener {
    private final TextToSpeech mTextToSpeech;//TTS对象
    private final ConcurrentLinkedQueue<String> mBufferedMessages;//消息队列
    private Context mContext;
    private boolean mIsReady;//标识符

    public RawTextToSpeech(Context context) {
        this.mContext = context;//获取上下文
        this.mBufferedMessages = new ConcurrentLinkedQueue<String>();//实例化队列
        this.mTextToSpeech = new TextToSpeech(this.mContext, this);//实例化TTS
    }

    //初始化TTS引擎
    @Override
    public void onInit(int status) {
        Log.i("RawTextToSpeech", String.valueOf(status));

        List<TextToSpeech.EngineInfo> engines = mTextToSpeech.getEngines();
        for (TextToSpeech.EngineInfo engine : engines) {
            Log.e("sss", "onInit: " + engine.name);
        }

        if (status == TextToSpeech.SUCCESS) {
            int result = this.mTextToSpeech.setLanguage(Locale.CHINA);//设置识别语音为中文
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Toast.makeText(mContext, "语音数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
            synchronized (this) {
                this.mIsReady = true;//设置标识符为true
                for (String bufferedMessage : this.mBufferedMessages) {
                    speakText(bufferedMessage);//读语音
                }
                this.mBufferedMessages.clear();//读完后清空队列
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void readMessage(String message) {
        read(message);
    }

    //释放资源
    public void freeSource() {
        synchronized (this) {
            this.mTextToSpeech.shutdown();
            // this.mIsReady=false;
        }
    }

    //更新消息队列，或者读语音
    public void read(String lanaugh) {
        String message = lanaugh;
        synchronized (this) {
            if (this.mIsReady) {
                speakText(message);
            } else {
                this.mBufferedMessages.add(message);
            }
        }
    }

    //读语音处理
    private void speakText(String message) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, "STREAM_NOTIFICATION");//设置播放类型（音频流类型）
        this.mTextToSpeech.speak(message, TextToSpeech.QUEUE_ADD, params);//将这个发音任务添加当前任务之后
        this.mTextToSpeech.playSilence(100, TextToSpeech.QUEUE_ADD, params);//间隔多长时间
    }

    public void stop() {

        mTextToSpeech.stop();

    }
}
