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
