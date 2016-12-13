package com.example.bradcampbell.presentation;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rx.Observable.just;

import com.example.bradcampbell.domain.HelloEntity;
import com.example.bradcampbell.domain.HelloInteractor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import rx.Observable;
import rx.schedulers.TestScheduler;

@RunWith(RobolectricTestRunner.class)
public class HelloPresenterTests {
  @Mock HelloInteractor interactor;
  @Mock HelloView view;

  private HelloPresenter helloPresenter;

  @Before public void setup() {
    MockitoAnnotations.initMocks(this);

    helloPresenter = new HelloPresenter(interactor);
  }

  @Test public void testLoadingIsCalledCorrectly() {
    TestScheduler testScheduler = new TestScheduler();
    Observable<HelloEntity> result = just(HelloEntity.create(0, 0)).subscribeOn(testScheduler);
    when(interactor.value()).thenReturn(result);

    helloPresenter.setView(view);

    verify(view, times(1)).showLoading();
    verify(view, never()).hideLoading();
    verify(view, never()).display(anyString());

    testScheduler.triggerActions();

    verify(view, times(1)).display("0");
    verify(view, times(1)).hideLoading();
  }
}
