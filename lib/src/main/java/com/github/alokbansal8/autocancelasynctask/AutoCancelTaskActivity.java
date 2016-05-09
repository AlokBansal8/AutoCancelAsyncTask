/*
  Copyright [2016] [Alok Bansal]

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

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
