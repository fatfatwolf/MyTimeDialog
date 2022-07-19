package com.example.timeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.timeapplication.timeview.MyDateTimePickDialog;
import com.example.timeapplication.timeview.TimeUtils;

public class MainActivity extends AppCompatActivity {

    Button button;
    // 选择时间
    MyDateTimePickDialog mPickDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartEndTime();
            }
        });
    }

    public void setStartEndTime() {
                if (mPickDialog == null)
                    mPickDialog = new MyDateTimePickDialog(MainActivity.this);
                // 此处设置一个默认值
                int[] startDateAndTime = TimeUtils.getDateAndTime("2022-7-19 13:23:44");
                if (startDateAndTime.length == 6) {
                    mPickDialog.setDateAndTime(startDateAndTime[0], startDateAndTime[1], startDateAndTime[2], startDateAndTime[3], startDateAndTime[4], startDateAndTime[5]);
                }
                mPickDialog.showThisDialog(new MyDateTimePickDialog.OnPickViewOkListener() {
                    @Override
                    public void ok(String year, String month, String date, String hour, String minute, String second) {
                        Log.i("xjz111",year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second);
                        mPickDialog.dismiss();
                    }
                }, new MyDateTimePickDialog.OnPickViewCancelListener() {
                    @Override
                    public void cancel() {
                        mPickDialog.dismiss();
                    }
                });
    }
}