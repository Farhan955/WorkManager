package com.idealogics.workmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static final String MESSAGE_STATUS = "message_status";
    TextView tvStatus;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        tvStatus = findViewById(R.id.textView);
        btnSend = findViewById(R.id.button);


        final WorkManager mWorkManager = WorkManager.getInstance();
//        final OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
        PeriodicWorkRequest mRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 16, TimeUnit.HOURS)
                .setConstraints(Constraints.NONE)
                .build();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkManager.enqueue(mRequest);
            }
        });
        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(@Nullable WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    tvStatus.append(state.toString() + "\n");
                }
            }
        });
    }


    public static String getcurentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z", Locale.US);
        df.setTimeZone(TimeZone.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

}
