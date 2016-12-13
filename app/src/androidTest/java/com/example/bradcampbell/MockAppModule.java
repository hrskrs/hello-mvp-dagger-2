package com.example.bradcampbell;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import com.example.bradcampbell.data.HelloService;
import com.example.bradcampbell.domain.Clock;
import com.example.bradcampbell.domain.SchedulerProvider;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.mock;

@Module
public class MockAppModule {
    private final SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
    private final Clock mockClock = mock(Clock.class);
    private final HelloService mockHelloService = mock(HelloService.class);
    private final TestScheduler testScheduler = new TestScheduler();

    @Provides SharedPreferences provideSharedPreferences() {
        return mockSharedPreferences;
    }

    @Provides @Singleton SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider() {
            @Override public <T> Observable.Transformer<T, T> applySchedulers() {
                return tObservable -> tObservable
                    .subscribeOn(testScheduler)
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Provides TestScheduler provideTestScheduler() {
        return testScheduler;
    }

    @Provides HelloService provideHelloService() {
        return mockHelloService;
    }

    @Provides Clock provideClock() {
        return mockClock;
    }

    @Provides @Nullable LayoutInflaterFactory provideLayoutInflaterFactory() {
        return (parent, name, context, attrs) -> {
            View result = null;
            // ProgressBar animates forever which keeps the main thread from being idle
            // and hence blocks Espresso from working. This is true even if you disable
            // animations on the device (testing on Android L and above it seems). I have
            // added a hook to replace any ProgressBar with a plain old View instance
            // to avoid the animation issues.
            if ("ProgressBar".equals(name)) {
                result = new View(context, attrs);
            }
            // We have overridden the Activity Delegate LayoutFactory, so we need to call
            // back into it
            if (result == null && context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                AppCompatDelegate delegate = activity.getDelegate();
                result = delegate.createView(parent, name, context, attrs);
            }
            return result;
        };
    }
}
