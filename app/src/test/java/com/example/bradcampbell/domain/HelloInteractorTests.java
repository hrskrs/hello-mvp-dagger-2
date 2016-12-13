package com.example.bradcampbell.domain;

import static java.util.Collections.singletonList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.example.bradcampbell.data.HelloDiskCache;
import com.example.bradcampbell.data.HelloService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import rx.Observable;
import rx.observers.TestSubscriber;

@RunWith(RobolectricTestRunner.class)
public class HelloInteractorTests {
  @Mock HelloService service;
  @Mock HelloDiskCache cache;
  @Mock Clock clock;

  private HelloInteractor interactor;

  @Before public void setup() {
    MockitoAnnotations.initMocks(this);

    SchedulerProvider schedulerProvider = new SchedulerProvider() {
      @Override public <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable;
      }
    };

    when(cache.saveEntity(any())).thenReturn(Observable.just(null));

    interactor = new HelloInteractor(schedulerProvider, cache, service, clock);
  }

  @Test public void testHitsMemoryCache() {
    HelloEntity expectedResult = HelloEntity.create(1, 0L);
    HelloEntity nonExpectedResult = HelloEntity.create(2, 0L);

    when(service.getValue()).thenReturn(Observable.just(1));
    when(cache.getEntity()).thenReturn(Observable.just(null));
    when(clock.millis()).thenReturn(0L);

    TestSubscriber<HelloEntity> testSubscriberFirst = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberFirst);
    testSubscriberFirst.assertNoErrors();
    testSubscriberFirst.assertReceivedOnNext(singletonList(expectedResult));

    when(cache.getEntity()).thenReturn(Observable.just(nonExpectedResult));
    when(service.getValue()).thenReturn(Observable.just(2));

    TestSubscriber<HelloEntity> testSubscriberSecond = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberSecond);
    testSubscriberSecond.assertNoErrors();
    testSubscriberSecond.assertReceivedOnNext(singletonList(expectedResult));
  }

  @Test public void testHitsDiskCache() {
    HelloEntity expectedResult = HelloEntity.create(1, 0L);

    when(service.getValue()).thenReturn(Observable.just(1));
    when(cache.getEntity()).thenReturn(Observable.just(null));
    when(clock.millis()).thenReturn(0L);

    TestSubscriber<HelloEntity> testSubscriberFirst = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberFirst);
    testSubscriberFirst.assertNoErrors();
    testSubscriberFirst.assertReceivedOnNext(singletonList(expectedResult));

    interactor.clearMemoryCache();
    when(cache.getEntity()).thenReturn(Observable.just(expectedResult));
    when(service.getValue()).thenReturn(Observable.just(2));

    TestSubscriber<HelloEntity> testSubscriberSecond = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberSecond);
    testSubscriberSecond.assertNoErrors();
    testSubscriberSecond.assertReceivedOnNext(singletonList(expectedResult));
  }

  @Test public void testCacheExpiry() {
    HelloEntity expectedResultFirst = HelloEntity.create(1, 0L);
    when(service.getValue()).thenReturn(Observable.empty());
    when(cache.getEntity()).thenReturn(Observable.just(expectedResultFirst));
    when(clock.millis()).thenReturn(0L);

    TestSubscriber<HelloEntity> testSubscriberFirst = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberFirst);
    testSubscriberFirst.assertNoErrors();
    testSubscriberFirst.assertReceivedOnNext(singletonList(expectedResultFirst));

    when(clock.millis()).thenReturn(4999L);
    TestSubscriber<HelloEntity> testSubscriberSecond = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberSecond);
    testSubscriberSecond.assertNoErrors();
    testSubscriberSecond.assertReceivedOnNext(singletonList(expectedResultFirst));

    when(clock.millis()).thenReturn(5000L);
    when(service.getValue()).thenReturn(Observable.just(2));

    TestSubscriber<HelloEntity> testSubscriberThird = new TestSubscriber<>();
    interactor.value().subscribe(testSubscriberThird);
    testSubscriberThird.assertNoErrors();
    testSubscriberThird.assertReceivedOnNext(singletonList(HelloEntity.create(2, 5000L)));
  }
}