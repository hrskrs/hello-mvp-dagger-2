# hello-mvp-dagger-2

MVP example code using RxJava 1, Retrolambda, Dagger 2, and more.

The app demonstrates all parts of MVP (Model, View, and Presenter). It demonstrates getting data from a fake service and caching it in memory and to disk. The cached data is valid for 5 seconds, or until manually cleared by the user. 

The project demonstrates techniques from Dan Lew's blog such as loading data from multiple sources (http://blog.danlew.net/2015/06/22/loading-data-from-multiple-sources-with-rxjava/), using compose (http://blog.danlew.net/2015/03/02/dont-break-the-chain/), and deferring code until subscription (http://blog.danlew.net/2015/07/23/deferring-observable-code-until-subscription-in-rxjava/) to name a few.

The project also deomnstrates an Espresso integration test, as well as a few unit tests (that run with Robolectric). 
