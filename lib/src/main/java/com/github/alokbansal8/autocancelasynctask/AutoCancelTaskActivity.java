package com.github.alokbansal8.autocancelasynctask;

import android.support.v7.app.AppCompatActivity;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused") public abstract class AutoCancelTaskActivity extends AppCompatActivity {

  private final CopyOnWriteArrayList<AutoCancelAsyncTask> tasks = new CopyOnWriteArrayList<>();

  void addLifecycleListener(AutoCancelAsyncTask task) {
    tasks.add(task);
  }

  void removeLifecycleListener(AutoCancelAsyncTask task) {
    tasks.remove(task);
  }

  @Override protected void onStop() {
    super.onStop();
    final int size = tasks.size();
    if (size == 0) return;
    for (int i = 0; i < size; i++) {
      AutoCancelAsyncTask task = tasks.get(i);
      if (task == null) continue;
      task.onActivityStopped();
    }
    tasks.clear();
  }
}
