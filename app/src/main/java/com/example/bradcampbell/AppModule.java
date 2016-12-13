package com.example.bradcampbell;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterFactory;

import com.example.bradcampbell.data.HelloService;
import com.example.bradcampbell.domain.Clock;
import com.example.bradcampbell.domain.SchedulerProvider;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {
  private App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Provides @Singleton SharedPreferences provideSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(app);
  }

  @Provides @Singleton HelloService provideHelloService() {
    return new HelloService();
  }

  @Provides @Singleton SchedulerProvider provideSchedulerProvider() {
    return SchedulerProvider.DEFAULT;
  }

  @Provides @Singleton Clock provideClock() {
    return Clock.REAL;
  }

  @Provides @Nullable LayoutInflaterFactory provideLayoutInflaterFactory() {
      return null;
  }
}
