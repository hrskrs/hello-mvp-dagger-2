package com.example.bradcampbell;

import com.example.bradcampbell.ui.HelloFragment;
import com.example.bradcampbell.ui.MainActivity;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  MainActivity inject(MainActivity activity);
  HelloFragment inject(HelloFragment fragment);
}
