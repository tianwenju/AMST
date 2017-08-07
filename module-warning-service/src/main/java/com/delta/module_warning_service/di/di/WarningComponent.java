package com.delta.module_warning_service.di.di;

import com.delta.module_warning_service.di.WarningService;

import dagger.Component;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/1/11 17:06
 */
@ServiceScope
@Component(modules = {WarningSocketPresenterModule.class})
public interface WarningComponent {

    void inject(WarningService warningService);
}
