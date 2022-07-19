package com.example.timeapplication.timeview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.timeapplication.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 自定义时间日期选择控件（选择年月日时分秒）
 * create by xjz on 2022/7/19
 */

public class MyDateTimePickDialog extends Dialog {
    private Context mContext;
    private OnPickViewCancelListener mCancelListener;
    private OnPickViewOkListener mOkListener;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private String mYearStr = "", mMonthStr = "", mDayStr = "", mHourStr = "", mMinuteStr = "", mSecondStr = "";
    private PickerView mPickViewYear, mPickViewMonth, mPickViewDay, mPickViewHour, mPickViewMinute, mPickViewSecond;
    private Calendar mCalendar;

    /**
     *   0 - 筛选 年/月/日/时/分/秒  默认
     *   1 - 筛选 年/月/日
     *   2 - 筛选 时/分
     **/
    private int mType; // 筛选类型

    public MyDateTimePickDialog(Context context) {
        super(context);
        mContext = context;

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_my_date_time_pick_view, null);
        setContentView(view);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawableResource(R.drawable.shape_my_bottom_dialog);
        window.setWindowAnimations(R.style.dialog_anim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        TextView tvCancel = view.findViewById(R.id.cancel_my_date_time_pick_view);
        TextView tvOk = view.findViewById(R.id.ok_my_date_time_pick_view);
        mPickViewYear = view.findViewById(R.id.myPickerView_my_date_time_pick_view);
        mPickViewMonth = view.findViewById(R.id.myPickerView_my_date_time_pick_view1);
        mPickViewDay = view.findViewById(R.id.myPickerView_my_date_time_pick_view2);
        mPickViewHour = view.findViewById(R.id.myPickerView_my_date_time_pick_view3);
        mPickViewMinute = view.findViewById(R.id.myPickerView_my_date_time_pick_view4);
        mPickViewSecond = view.findViewById(R.id.myPickerView_my_date_time_pick_view5);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;   // 获取的 月 从 0 开始
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mSecond = mCalendar.get(Calendar.SECOND);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelListener != null)
                    mCancelListener.cancel();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYearStr = mYear + "";
                mMonthStr = formatTime(mMonth);
                mDayStr = formatTime(mDay);
                mHourStr = formatTime(mHour);
                mMinuteStr = formatTime(mMinute);
                mSecondStr = formatTime(mSecond);

                if (mOkListener != null)
                    mOkListener.ok(mYearStr, mMonthStr, mDayStr, mHourStr, mMinuteStr, mSecondStr);
            }
        });

    }
    /**
     * 不传时间进来，默认是现在的年月日时分秒
     * type 不传，默认 type = 1  ，筛选 年/月/日/时/分/秒
     *
     * @param okListener     确定按钮监听
     * @param cancelListener 取消按钮监听
     */
    public void showThisDialog(OnPickViewOkListener okListener, OnPickViewCancelListener cancelListener) {
        mOkListener = okListener;
        mCancelListener = cancelListener;
        initPickView();
        this.show();
    }

    public void setType(int type){
        mType = type;

        if (mType == 1) {  // 只筛选 年月日，把 时分秒 控件 隐掉
            mPickViewHour.setVisibility(View.GONE);
            mPickViewMinute.setVisibility(View.GONE);
            mPickViewSecond.setVisibility(View.GONE);
        }
        if (mType == 2) {  // 只筛选 时分，把 年月日 秒 控件隐掉
            mPickViewYear.setVisibility(View.GONE);
            mPickViewMonth.setVisibility(View.GONE);
            mPickViewDay.setVisibility(View.GONE);
            mPickViewSecond.setVisibility(View.GONE);
        }


    }

    // 外界设置 初始时间
    public void setDateAndTime(int year, int month, int day, int hour, int minute, int second) {
        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;
        mSecond = second;

    }

    private void initPickView() {
        // ============= 年 数据 =============
        if (mType != 2) {
            List<String> listYear = new ArrayList<>();
            int index = 0;
            for (int i = 0; i < 20; i++) {
                String year = (mCalendar.get(Calendar.YEAR) - 10 + i) + "年";
                listYear.add(year);
                if (year.equals(mYear + "年"))
                    index = i;

            }
            mPickViewYear.setData(listYear, false);
            mPickViewYear.setSelected(index);
            mPickViewYear.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    String yearStr = text.replace("年", "");
                    if (yearStr.matches("[0-9]+"))
                        mYear = Integer.parseInt(yearStr);
                    setDayData();
                }
            });
        }

        // =============== 设置 日 数据 =================
        if (mType != 2)
            setDayData();

        // ==================== 设置 月 数据 ======================
        if (mType != 2) {
            List<String> listMonth = new ArrayList<>();
            for (int j = 1; j <= 12; j++) {
                if (j < 10)
                    listMonth.add("0" + j + "月");
                else
                    listMonth.add(j + "月");
            }
            mPickViewMonth.setData(listMonth);
            mPickViewMonth.setSelected(mMonth - 1);
            mPickViewMonth.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    String monthStr = text.replace("月", "");
                    if (monthStr.matches("[0-9]+"))
                        mMonth = Integer.parseInt(monthStr);
                    setDayData();
                }
            });
        }

        // ================= 设置 时 数据 ====================
        if (mType != 1) {
            List<String> listHour = new ArrayList<>();
            for (int i = 0; i <= 23; i++) {
                if (i < 10) {
                    listHour.add("0" + i + "时");
                } else
                    listHour.add(i + "时");
            }
            mPickViewHour.setData(listHour);
            mPickViewHour.setSelected(mHour);
            mPickViewHour.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    String hourStr = text.replace("时", "");
                    if (hourStr.matches("[0-9]+"))
                        mHour = Integer.parseInt(hourStr);
                }
            });
        }


        // ================  设置 分 数据 =================
        if (mType != 1) {
            List<String> listMinute = new ArrayList<>();
            for (int i = 0; i <= 59; i++) {
                if (i < 10)
                    listMinute.add("0" + i + "分");
                else
                    listMinute.add(i + "分");
            }
            mPickViewMinute.setData(listMinute);
            mPickViewMinute.setSelected(mMinute);
            mPickViewMinute.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    String minuteStr = text.replace("分", "");
                    if (minuteStr.matches("[0-9]+"))
                        mMinute = Integer.parseInt(minuteStr);
                }
            });
        }

        // ================ 设置 秒 数据 ==================
        if (mType != 1 && mType != 2) {
            List<String> listSecond = new ArrayList<>();
            for (int i = 0; i <= 59; i++) {
                if (i < 10)
                    listSecond.add("0" + i + "秒");
                else
                    listSecond.add(i + "秒");
            }
            mPickViewSecond.setData(listSecond);
            mPickViewSecond.setSelected(mSecond);

            mPickViewSecond.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    String secondStr = text.replace("秒", "");
                    if (secondStr.matches("[0-9]+"))
                        mSecond = Integer.parseInt(secondStr);
                }
            });
        }

    }

    // 设置 日 数据
    private void setDayData() {
        List<String> listDay = new ArrayList<>();
        int dayNum = 31;
        if (mMonth == 4 || mMonth == 6 || mMonth == 9 || mMonth == 11) {
            dayNum = 30;
        }
        if (mMonth == 2) {
            if (mYear % 400 == 0 || mYear % 4 == 0) {  // 闰年
                dayNum = 29;
            } else {
                dayNum = 28;
            }
        }

        for (int i = 1; i <= dayNum; i++) {
            if (i < 10)
                listDay.add("0" + i + "日");
            else
                listDay.add(i + "日");
        }
        mPickViewDay.setData(listDay);
        mPickViewDay.setSelected(mDay - 1);
        mPickViewDay.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String dayStr = text.replace("日", "");
                if (dayStr.matches("[0-9]+"))
                    mDay = Integer.parseInt(dayStr);
            }
        });

    }


    public MyDateTimePickDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public interface OnPickViewCancelListener {
        void cancel();
    }

    public interface OnPickViewOkListener {
        void ok(String year, String month, String date, String hour, String minute, String second);
    }

    // 小于 10 的数字前面加上 0 ，例如：01、02
    private String formatTime(int dateOrTime) {
        if (dateOrTime < 10)
            return "0" + dateOrTime;
        else
            return dateOrTime + "";


    }


}
