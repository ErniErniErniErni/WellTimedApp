package com.erniwo.timetableconstruct.admin;

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
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erniwo.timetableconstruct.Config;
import com.erniwo.timetableconstruct.R;
import com.erniwo.timetableconstruct.model.Course;
//import com.erniwo.timetableconstruct.util.MyRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class AdminManageClassTimetableActivity extends AppCompatActivity {

//    RecyclerView recyclerView;
//    MyRecyclerViewAdapter myRecyclerAdapter;
//    GridLayout grid;

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    private String currentClassName;

    private TextView nameOfClass;
    private Button tryButton;
    private TextView addButton;

    private FrameLayout mFrameLayout;
    private TableLayout mTableLayout;
    private TextView mWeekOfTermTextView;
    private ImageView mBgImageView;
    private ImageButton mAddImgBtn;
    private Button courseButton1;
    private Button courseButton2;
    private Button courseButton3;
    private LinearLayout headerClassNumLl;
    private boolean flagUpdateCalendar = false;
//    private GridLayout
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
//    private String currentClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_class_timetable);

        getWritePermission();//得到读写权限用于保存课表信息

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String currClassName = (String) bundle.get("ClickedClassName");
            setCurrentClassName(currClassName);
        }

//        setCurrentClassName(intent.getStringExtra("ClickedClassName"));


        headerClassNumLl = findViewById(R.id.ll_header_class_num);
        nameOfClass = (TextView) findViewById(R.id.name_of_class) ;
//        setCurrentClassName(AdminManageListOfClassesActivity.getClickedClassName());
        nameOfClass.setText("Timetable of " + getCurrentClassName());

        tryButton = (Button) findViewById(R.id.course11);
        addButton = (TextView) findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminManageClassTimetableActivity.this, AdminEditClassTimeTableActivity.class);
                startActivity(intent);
            }
        });



        //计算1dp的数值方便接下来设置元素尺寸,提高效率
        VALUE_1DP = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getResources().getDisplayMetrics());

        //获取课程节数表头的宽度
        float headerClassNumWidth = getResources().getDimension(R.dimen.table_header_class_width);
        //设置课程格子高度和宽度
        setTableCellDimens(headerClassNumWidth);

//        initTimetable();
        pullExistingClasses();


    }

    private void pullExistingClasses() {
        //Pull timetable from backend
        String classID = AdminManageListOfClassesActivity.getClickedClassID();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Classes")
                .child(classID).child("Timetable");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                for(DataSnapshot child2 : snapshot2.getChildren()){

                    String subject = child2.child("Subject").getValue().toString();
                    String location = child2.child("Location").getValue().toString();
                    String teacher = child2.child("Teacher").getValue().toString();
                    String dayOfWeek = child2.child("DayOfWeek").getValue().toString();
                    String period = child2.child("Period").getValue().toString();

                    String textOnCourseCard = subject + "\n" + location + "\n" + teacher;
                    tryButton.setText(textOnCourseCard);
                    tryButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * 初始化课表
     */
//    private void initTimetable()//根据保存的信息，创建课程表
//    {
//        //初始化设置按钮
//        initAddBtn();
//        //设置标题中显示的当前周数
////        mWeekOfTermTextView.setText(String.format(getString(R.string.day_of_week), Config.getCurrentWeek()));
//        //初始化课程表视图
//        initFrameLayout();
//
//        //读取时间数据
////        sTimes = new FileUtils<Time[]>().readFromJson(this, FileUtils.TIME_FILE_NAME, Time[].class);
//
//        //读取课程数据
////        sCourseList = new FileUtils<ArrayList<Course>>().readFromJson(
////                this,
////                FileUtils.TIMETABLE_FILE_NAME,
////                new TypeToken<ArrayList<Course>>() {
////                }.getType());
//
//        //更新节数表头
////        updateClassNumHeader();
//        //读取失败返回
//        if (sCourseList == null) {
//            sCourseList = new ArrayList<>();
//            return;
//        }
//
//        //Log.d("courseNum",String.valueOf(sCourseList.size()));
//
//        int size = sCourseList.size();
//        if (size != 0) {
////            updateTimetable();
//        }
//
//        flagUpdateCalendar = false;
//
//    }

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

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTableLayout.getLayoutParams();
        //设置课程表高度
        layoutParams.height = (int) sCellHeightPx * Config.getMaxClassNum();
        //设置课程表宽度
        layoutParams.width = (int) sCellWidthPx * 7;
        setCourseCardButton(40, 50);
        mAddImgBtn.getLayoutParams().height = (int) sCellHeightPx;

        mTableLayout.performClick();
        mTableLayout.setOnTouchListener((view, motionEvent) -> {
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
//        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        final TableLayout.LayoutParams layoutParams = (TableLayout.LayoutParams) mAddImgBtn.getLayoutParams();
        layoutParams.width = (int) sCellWidthPx;
        layoutParams.height = (int) sCellHeightPx;

        mAddImgBtn.setOnClickListener(view -> {
            //点击后隐藏按钮，否则可能会被新建的课程覆盖
            mAddImgBtn.setVisibility(View.GONE);
            Intent intent = new Intent(AdminManageClassTimetableActivity.this, AdminEditClassTimeTableActivity.class);
            int dayOfWeek = layoutParams.leftMargin / (int) sCellWidthPx;
            int classStart = layoutParams.topMargin / (int) sCellHeightPx;
            mAddImgBtn.setVisibility(View.INVISIBLE);
//            intent.putExtra(AdminEditClassTimeTableActivity.EXTRA_Day_OF_WEEK, dayOfWeek + 1);
//            intent.putExtra(AdminEditClassTimeTableActivity.EXTRA_CLASS_START, classStart + 1);
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
     * get reading and writing permissions
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
     * 设置课程视图的监听
     *
     * @param textView
     * @param index
     */
    private void setTableClickListener(TextView textView, final int index)//设置课程视图的监听
    {
//        textView.setOnClickListener(new View.OnClickListener {
//        public void onClick(View v) {
//                Intent intent = new Intent(AdminManageClassTimetableActivity.this, AdminCourseDetailsActivity.class);
////            intent.putExtra(AdminCourseDetailsActivity.KEY_COURSE_INDEX, index);
//            startActivityForResult(intent, REQUEST_CODE_COURSE_DETAILS);
//            }
//        });
//    }

    /**
     * 设置课程格
     *
     * @param textView
     * @param class_num 节数
     * @param left      距左边界的格数
     * @param top       距上边界的格数
     */
//    private void setTableCellTextView(TextView textView, int class_num, final int left,
//                                      final int top) {
//
//        //Log.d("tablecell", left + "," + top);
//        float leftMargin = left * sCellWidthPx;
//        float topMargin = top * sCellHeightPx;
//
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
//                (int) (sCellWidthPx - 6 * VALUE_1DP),
//                (int) (class_num * sCellHeightPx - 6 * VALUE_1DP));
//
//        layoutParams.topMargin = (int) (topMargin + 3 * VALUE_1DP);
//        layoutParams.leftMargin = (int) (leftMargin + 3 * VALUE_1DP);
//
//        //设置对齐方式
//        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
//        //设置文本颜色为白色
//        textView.setTextColor(getResources().getColor(R.color.white));
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.timetable_cell_text_size));
//
//        textView.setLayoutParams(layoutParams);
    }
}