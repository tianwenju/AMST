package com.delta.ameslibs.di.module;

import android.content.Context;

import com.delta.ameslibs.di.scope.AScope;
import com.delta.ttsmanager.RawTextToSpeech;
import com.delta.ttsmanager.TextToSpeechManager;

import dagger.Module;
import dagger.Provides;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/1/4 9:28
 */

@Module
public class MangerModule {



    @AScope
    @Provides
    TextToSpeechManager provderTextSpeechManager(Context context) {
        TextToSpeechManager textToSpeechManager = new TextToSpeechManager();
        textToSpeechManager.setOnSpeakListener(new RawTextToSpeech(context));
        return textToSpeechManager;
    }


}
