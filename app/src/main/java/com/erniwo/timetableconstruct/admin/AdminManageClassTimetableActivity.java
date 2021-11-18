package com.erniwo.timetableconstruct.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erniwo.timetableconstruct.Config;
import com.erniwo.timetableconstruct.model.Course;
import com.erniwo.timetableconstruct.R;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

//import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
//import com.bigkoo.pickerview.view.OptionsPickerView;

public class AdminManageClassTimetableActivity extends AppCompatActivity {

    private TextView nameOfClass;

    private FrameLayout mFrameLayout;
    private TextView mWeekOfTermTextView;
    private ImageView mBgImageView;
    private ImageButton mAddImgBtn;
    private Button courseButton1;
    private Button courseButton2;
    private Button courseButton3;
    private LinearLayout headerClassNumLl;
    private boolean flagUpdateCalendar = false;

    public static List<Course> sCourseList;
    public static Time[] sTimes;

    private final List<TextView> mClassTableTvList = new ArrayList<>();
    private TextView[] mClassNumHeaders = null;


    private static final int REQUEST_CODE_COURSE_DETAILS = 0;
    private static final int REQUEST_CODE_COURSE_EDIT = 1;
    private static final int REQUEST_CODE_FILE_CHOOSE = 2;
    private static final int REQUEST_CODE_CONFIG = 3;
    private static final int REQUEST_CODE_LOGIN = 4;
    private static final int REQUEST_CODE_SCAN = 5;
    private static final int REQUEST_CODE_SET_TIME = 6;

    private static final int REQ_PER_CALENDAR = 0x11;//日历权限申请

//    private OptionsPickerView<String> mOptionsPv;

    public static float VALUE_1DP;//1dp的值

    private static float sCellWidthPx;//课程视图的宽度(px)
    private static float sCellHeightPx;//课程视图的高度;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_class_timetable);

        getWritePermission();//得到读写权限用于保存课表信息


        mWeekOfTermTextView = findViewById(R.id.tv_week_of_term);
        mAddImgBtn = findViewById(R.id.img_btn_add);
        courseButton1 = findViewById(R.id.course_single1);
        courseButton2 = findViewById(R.id.course_single2);
        courseButton3 = findViewById(R.id.course_single3);
        mBgImageView = findViewById(R.id.iv_bg_main);
        mFrameLayout = findViewById(R.id.fl_timetable);
        headerClassNumLl = findViewById(R.id.ll_header_class_num);


        courseButton1.setText("Math \n R3");
        courseButton2.setText("French \n R8");
        courseButton3.setText("Geo \n R6");
        //计算1dp的数值方便接下来设置元素尺寸,提高效率
        VALUE_1DP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getResources().getDisplayMetrics());

        //获取课程节数表头的宽度
        float headerClassNumWidth = getResources().getDimension(R.dimen.table_header_class_width);
        //设置课程格子高度和宽度
        setTableCellDimens(headerClassNumWidth);

        updateDayOfWeekHeader();
        initToolbar();

        initTimetable();

    }

    /**
     * 初始化课表
     */
    private void initTimetable()//根据保存的信息，创建课程表
    {
        //初始化设置按钮
        initAddBtn();
        //设置标题中显示的当前周数
//        mWeekOfTermTextView.setText(String.format(getString(R.string.day_of_week), Config.getCurrentWeek()));
        //初始化课程表视图
        initFrameLayout();

        //读取时间数据
//        sTimes = new FileUtils<Time[]>().readFromJson(this, FileUtils.TIME_FILE_NAME, Time[].class);

        //读取课程数据
//        sCourseList = new FileUtils<ArrayList<Course>>().readFromJson(
//                this,
//                FileUtils.TIMETABLE_FILE_NAME,
//                new TypeToken<ArrayList<Course>>() {
//                }.getType());

        //更新节数表头
//        updateClassNumHeader();
        //读取失败返回
        if (sCourseList == null) {
            sCourseList = new ArrayList<>();
            return;
        }

        //Log.d("courseNum",String.valueOf(sCourseList.size()));

        int size = sCourseList.size();
        if (size != 0) {
//            updateTimetable();
        }

        flagUpdateCalendar = false;
    }
    private void initToolbar() {
        //设置标题为自定义toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    /**
     * 更新周数表头
     */
    private void updateDayOfWeekHeader() {
        int[] weekTextView = new int[]{//储存周几表头
                R.id.tv_sun,
                R.id.tv_mon,
                R.id.tv_tues,
                R.id.tv_wed,
                R.id.tv_thur,
                R.id.tv_fri,
                R.id.tv_sat
        };

    }


    /**
     * 计算课程格子的长宽
     *
     * @param headerWidth
     */
    private void setTableCellDimens(float headerWidth) {
        //获取屏幕宽度，用于设置课程视图的宽度
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

        Resources resources = getResources();
        int toolbarHeight = resources.getDimensionPixelSize(R.dimen.toolbar_height);
        int headerWeekHeight = resources.getDimensionPixelSize(R.dimen.header_week_height);

        //课程视图宽度
        sCellWidthPx = (displayWidth - headerWidth) / 7.0f;

        sCellHeightPx = Math.max(sCellWidthPx,
                (displayHeight - toolbarHeight - headerWeekHeight) / (float) Config.getMaxClassNum());
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initFrameLayout() {

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mFrameLayout.getLayoutParams();
        //设置课程表高度
        layoutParams.height = (int) sCellHeightPx * Config.getMaxClassNum();
        //设置课程表宽度
        layoutParams.width = (int) sCellWidthPx * 7;
        setCourseCardButton(40, 50);
        mAddImgBtn.getLayoutParams().height = (int) sCellHeightPx;

        mFrameLayout.performClick();
        mFrameLayout.setOnTouchListener((view, motionEvent) -> {
            int event = motionEvent.getAction();
            if (event == MotionEvent.ACTION_UP) {
                if (mAddImgBtn.getVisibility() == View.VISIBLE) {
                    mAddImgBtn.setVisibility(View.GONE);
                } else {
                    int x = (int) (motionEvent.getX() / sCellWidthPx);
                    int y = (int) (motionEvent.getY() / sCellHeightPx);
                    x = (int) (x * sCellWidthPx);
                    y = (int) (y * sCellHeightPx);
                    setAddImgBtn(x, y);
                }
            }
            return true;
        });
    }

    private void initAddBtn() {
        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.width = (int) sCellWidthPx;
        layoutParams.height = (int) sCellHeightPx;

        mAddImgBtn.setOnClickListener(view -> {
            //点击后隐藏按钮，否则可能会被新建的课程覆盖
            mAddImgBtn.setVisibility(View.GONE);
            Intent intent = new Intent(AdminManageClassTimetableActivity.this, AdminEditClassTimeTableActivity.class);
            int dayOfWeek = layoutParams.leftMargin / (int) sCellWidthPx;
            int classStart = layoutParams.topMargin / (int) sCellHeightPx;
            mAddImgBtn.setVisibility(View.INVISIBLE);
            intent.putExtra(AdminEditClassTimeTableActivity.EXTRA_Day_OF_WEEK, dayOfWeek + 1);
            intent.putExtra(AdminEditClassTimeTableActivity.EXTRA_CLASS_START, classStart + 1);
            startActivityForResult(intent, REQUEST_CODE_COURSE_EDIT);
        });
    }

    private void setAddImgBtn(int left, int top) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        mAddImgBtn.setVisibility(View.VISIBLE);
    }
    private void setCourseCardButton(int left, int top) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.leftMargin = left;
        layoutParams.topMargin = top;
        courseButton1.setVisibility(View.VISIBLE);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        Intent intent;
//
//        if (id == R.id.menu_config) {//菜单设置
//            intent = new Intent(this, ConfigActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_CONFIG);
//        } else if (id == R.id.menu_set_week) {//菜单设置当前周
//            showSelectCurrentWeekDialog();
//        } else if (id == R.id.menu_import) {//菜单登录教务系统
//            intent = new Intent(this, LoginActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_LOGIN);
//        } else if (id == R.id.menu_append) {//菜单导入Excel
//            intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("application/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, REQUEST_CODE_FILE_CHOOSE);
//        } else if (id == R.id.menu_append_class) {//菜单添加课程
//            intent = new Intent(this, EditActivity.class);
//            startActivityForResult(intent, REQUEST_CODE_COURSE_EDIT);
//        } else if (id == R.id.menu_share_timetable) {//分享
//            shareTimetable();
//        } else if (id == R.id.menu_set_time) {//设置上课时间
//            startActivityForResult(
//                    new Intent(this, SetTimeActivity.class),
//                    REQUEST_CODE_SET_TIME);
//        } else if (id == R.id.menu_update) {//菜单检查更新
//            checkUpdate();
//        }
//        return super.onOptionsItemSelected(item);
//    }



    private boolean hasAllPermissions() {
        for (String item : PERMISSIONS_STORAGE) {
            int permission = ActivityCompat.checkSelfPermission(this, item);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取读写权限
     */

    private void getWritePermission() {
        //检测是否有写的权限
        if (hasAllPermissions()) {
//            setCalendarEvent();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 初始化课表
     */



    /**
     * 设置课程视图的监听
     *
     * @param textView
     * @param index
     */
    private void setTableClickListener(TextView textView, final int index)//设置课程视图的监听
    {
//        textView.setOnClickListener(new OneClickListener(view -> {
//            Intent intent = new Intent(AdminManageClassTimetableActivity.this, CourseDetailsActivity.class);
//            intent.putExtra(CourseDetailsActivity.KEY_COURSE_INDEX, index);
//            startActivityForResult(intent, REQUEST_CODE_COURSE_DETAILS);
//        }));
    }

    /**
     * 设置课程格
     *
     * @param textView
     * @param class_num 节数
     * @param left      距左边界的格数
     * @param top       距上边界的格数
     */
    private void setTableCellTextView(TextView textView, int class_num, final int left,
                                      final int top) {

        //Log.d("tablecell", left + "," + top);
        float leftMargin = left * sCellWidthPx;
        float topMargin = top * sCellHeightPx;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                (int) (sCellWidthPx - 6 * VALUE_1DP),
                (int) (class_num * sCellHeightPx - 6 * VALUE_1DP));

        layoutParams.topMargin = (int) (topMargin + 3 * VALUE_1DP);
        layoutParams.leftMargin = (int) (leftMargin + 3 * VALUE_1DP);

        //设置对齐方式
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        //设置文本颜色为白色
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.timetable_cell_text_size));

        textView.setLayoutParams(layoutParams);
    }





    @Override
    protected void onDestroy() {
        if (flagUpdateCalendar) {
//            updateCalendarEvent();
        }
        super.onDestroy();
    }
}