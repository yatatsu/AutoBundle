package com.yatatsu.autobundle.example;

import javax.inject.Singleton;

import dagger.Component;


@Singleton @Component(modules = AppModule.class) public interface AppComponent {
    void inject(ExampleActivity activity);
}
