package com.example.bradcampbell;

import com.example.bradcampbell.data.HelloService;
import dagger.Component;
import javax.inject.Singleton;
import rx.schedulers.TestScheduler;

@Singleton
@Component(modules = MockAppModule.class)
public interface MockAppComponent extends AppComponent {
  HelloService getHelloService();
  TestScheduler getTestScheduler();
}
