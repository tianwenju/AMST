package com.delta.ameslibs.di.component;

import com.delta.ameslibs.di.module.MangerModule;
import com.delta.ameslibs.di.scope.AScope;
import com.delta.commonlibs.di.component.BaseAppComponent;
import com.delta.ttsmanager.TextToSpeechManager;

import dagger.Component;

/**
 * Created by V.Wenju.Tian on 2016/11/22.
 */
@AScope
@Component(modules = {MangerModule.class},dependencies = {BaseAppComponent.class})
public interface AppComponent {

    TextToSpeechManager TEXT_TO_SPEECH_MANAGER();

}
