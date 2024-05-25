package info.mrprogrammer.mrpillstracker.core.WorkManager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        CheckForMedicineReminder.INSTANCE.startBackgroundWork(this.getApplicationContext());
        callForWorkManger();
        return Result.success();
    }

    private void callForWorkManger() {
        OneTimeWorkRequest myWork = new OneTimeWorkRequest.Builder(MyWorker.class).setInitialDelay(1, TimeUnit.MINUTES).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(myWork);
    }
}
