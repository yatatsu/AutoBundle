package com.yatatsu.autobundle.example;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Typical module for application
 */
@Module class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Singleton @Provides Context context() {
        return application;
    }
}
