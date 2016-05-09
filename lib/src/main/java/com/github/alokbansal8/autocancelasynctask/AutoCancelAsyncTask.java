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

import android.os.AsyncTask;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import java.lang.ref.WeakReference;

@SuppressWarnings("unused") public abstract class AutoCancelAsyncTask<Params, Result> extends AsyncTask<Params, Void, Result> {

  private final WeakReference<AutoCancelTaskActivity> activityRef;

  public AutoCancelAsyncTask(@NonNull AutoCancelTaskActivity activity) {
    activity.addLifecycleListener(this);
    activityRef = new WeakReference<>(activity);
  }

  final void onActivityStopped() {
    cancel(false);
  }

  @Nullable @SafeVarargs @Override protected final Result doInBackground(Params... params) {
    if (isCancelled()) {
      stopListening();
      return null;
    }
    try {
      return onDoInBackground(params);
    } catch (Throwable t) {
      stopListening();
      throw t;
    }
  }

  @Override protected final void onPostExecute(@Nullable Result result) {
    stopListening();
    if (isCancelled()) return;
    onResult(result);
  }

  @Override protected final void onCancelled(@Nullable Result result) {
    stopListening();
    onCancel();
  }

  private void stopListening() {
    AutoCancelTaskActivity activity = activityRef.get();
    if (activity != null) activity.removeLifecycleListener(this);
  }

  @SuppressWarnings("unchecked") @WorkerThread @Nullable
  protected abstract Result onDoInBackground(Params... params);

  @UiThread protected abstract void onResult(@Nullable Result result);

  @SuppressWarnings("unused") @CallSuper protected void onCancel() {
  }
}
