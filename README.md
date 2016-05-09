# AutoCancelAsyncTask
This library allow you to use Android's [AsyncTask](http://developer.android.com/reference/android/os/AsyncTask.html) such
that it gets cancelled at [onStop](http://developer.android.com/reference/android/app/Activity.html#onStop()) event of
Activity's [lifecycle](http://developer.android.com/guide/components/activities.html#Lifecycle).

## Usage
### Gradle
Add the following line to your build.gradle file.
```groovy
compile ''
```

### Description
Extend the Activity, whose lifecycle you would like to link to AsyncTask, from
[AutoCancelTaskActivity](https://github.com/AlokBansal8/AutoCancelAsyncTask/blob/master/lib/src/main/java/com/github/alokbansal8/autocancelasynctask/AutoCancelTaskActivity.java)
and define your task using [AutoCancelAsyncTask](https://github.com/AlokBansal8/AutoCancelAsyncTask/blob/master/lib/src/main/java/com/github/alokbansal8/autocancelasynctask/AutoCancelAsyncTask.java).

### Show me the Code
Example code:
``` java
public class SampleActivity extends AutoCancelTaskActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ...
  }

  @Override protected void onStart() {
    super.onStart();
    doAsyncWork();
    ...
  }

  private void doAsyncWork() {
    new AutoCancelAsyncTask<Param, Result>(this) {
      @Nullable @Override protected Result onDoInBackground(Param... params) {
        //Background operations
        return result;
      }

      @Override protected void onResult(@Nullable Result result) {
        //UI operations
      }
    }.execute(params);
  }
}
```

