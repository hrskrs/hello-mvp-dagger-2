package com.example.bradcampbell.presentation;

import com.example.bradcampbell.domain.HelloInteractor;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import javax.inject.Inject;

public class HelloPresenter implements Presenter<HelloView> {
  private Subscription subscription = Subscriptions.empty();
  private HelloView view;

  private final HelloInteractor interactor;

  @Inject HelloPresenter(HelloInteractor interactor) {
    this.interactor = interactor;
  }

  @Override public void setView(HelloView view) {
    this.view = view;
    if (view == null) {
      subscription.unsubscribe();
    } else {
      refresh();
    }
  }

  public void refresh() {
    view.showLoading();
    subscription = interactor.value().subscribe(
        helloEntity -> view.display("" + helloEntity.value()),
        err -> view.hideLoading(),
        view::hideLoading);
  }

  /**
   * This is for demo purposes only. It would be strange to expose cache clearing methods
   * to the UI. Typically caches would be cleared as a result of some action, not from
   * pressing a button.
   */
  public void clearMemoryCache() {
    interactor.clearMemoryCache();
  }

  /**
   * This is for demo purposes only. It would be strange to expose cache clearing methods
   * to the UI. Typically caches would be cleared as a result of some action, not from
   * pressing a button.
   */
  public void clearMemoryAndDiskCache() {
      interactor.clearMemoryAndDiskCache();
  }
}
