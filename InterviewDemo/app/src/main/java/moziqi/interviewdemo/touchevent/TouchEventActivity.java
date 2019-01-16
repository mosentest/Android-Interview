package moziqi.interviewdemo.touchevent;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.LogUtils;

public class TouchEventActivity extends AppCompatActivity implements ILog {

    // Content View Elements

    private moziqi.interviewdemo.touchevent.TouchEventView mToucheventview;
    private RadioGroup mRg_activity;
    private RadioButton mRb_activity_dispatch_true;
    private RadioButton mRb_activity_dispatch_false;
    private RadioButton mRb_activity_dispatch_super;
    private RadioGroup mRg_view;
    private RadioButton mRb_view_dispatch_true;
    private RadioButton mRb_view_dispatch_false;
    private RadioButton mRb_view_dispatch_super;
    private TextView mTv_result;

    // End Of Content View Elements

    private void bindViews() {

        mToucheventview = (moziqi.interviewdemo.touchevent.TouchEventView) findViewById(R.id.toucheventview);
        mRg_activity = (RadioGroup) findViewById(R.id.rg_activity);
        mRb_activity_dispatch_true = (RadioButton) findViewById(R.id.rb_activity_dispatch_true);
        mRb_activity_dispatch_false = (RadioButton) findViewById(R.id.rb_activity_dispatch_false);
        mRb_activity_dispatch_super = (RadioButton) findViewById(R.id.rb_activity_dispatch_super);
        mRg_view = (RadioGroup) findViewById(R.id.rg_view);
        mRb_view_dispatch_true = (RadioButton) findViewById(R.id.rb_view_dispatch_true);
        mRb_view_dispatch_false = (RadioButton) findViewById(R.id.rb_view_dispatch_false);
        mRb_view_dispatch_super = (RadioButton) findViewById(R.id.rb_view_dispatch_super);
        mTv_result = (TextView) findViewById(R.id.tv_result);
    }


    private int dispatch = 2;

    private int touchEvent = 2;


    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mTv_result.setText("");
                    break;
                case 1:
                    CharSequence text = mTv_result.getText();
                    String content = (String) msg.obj;
                    LogUtils.i("moziqi", content);
                    mTv_result.setText(text + "\n" + content);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setContentView(R.layout.activity_touch_event);
        bindViews();
        mToucheventview = findViewById(R.id.toucheventview);
        mToucheventview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = "toucheventview.setOnClickListener>onClick";
                handler.sendMessage(message);
            }
        });
        mToucheventview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Message message = handler.obtainMessage();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(getTAG());
                stringBuilder.append("===");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        stringBuilder.append("toucheventview.setOnTouchListener>ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        stringBuilder.append("toucheventview.setOnTouchListener>ACTION_UP");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        stringBuilder.append("toucheventview.setOnTouchListener>ACTION_MOVE");
                        break;
                    default:
                        stringBuilder.append("toucheventview.setOnTouchListener>default");
                }
                message.what = 1;
                message.obj = stringBuilder.toString();
                handler.sendMessage(message);
                return false;
            }
        });
        mToucheventview.setMainHandler(handler);
        mRg_activity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handler.sendEmptyMessage(0);
                switch (checkedId) {
                    case R.id.rb_activity_dispatch_true:
                        dispatch = 1;
                        break;
                    case R.id.rb_activity_dispatch_false:
                        dispatch = 0;
                        break;
                    case R.id.rb_activity_dispatch_super:
                        dispatch = 2;
                        break;

                }
            }
        });
        mRg_view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                handler.sendEmptyMessage(0);
                switch (checkedId) {
                    case R.id.rb_activity_dispatch_true:
                        mToucheventview.setDispatchTouchEvent(1);
                        break;
                    case R.id.rb_activity_dispatch_false:
                        mToucheventview.setDispatchTouchEvent(0);
                        break;
                    case R.id.rb_activity_dispatch_super:
                        mToucheventview.setDispatchTouchEvent(2);
                        break;

                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Message message = handler.obtainMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTAG());
        stringBuilder.append("===");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stringBuilder.append("dispatchTouchEvent>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                stringBuilder.append("dispatchTouchEvent>ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                stringBuilder.append("dispatchTouchEvent>ACTION_MOVE");
                break;
            default:
                stringBuilder.append("dispatchTouchEvent>default");
        }
        message.what = 1;
        message.obj = stringBuilder.toString();
        handler.sendMessage(message);
        if (dispatch == 0) {
            return false;
        } else if (dispatch == 1) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Message message = handler.obtainMessage();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTAG());
        stringBuilder.append("===");
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stringBuilder.append("onTouchEvent>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                stringBuilder.append("onTouchEvent>ACTION_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                stringBuilder.append("onTouchEvent>ACTION_MOVE");
                break;
            default:
                stringBuilder.append("onTouchEvent>default");
        }
        message.what = 1;
        message.obj = stringBuilder.toString();
        handler.sendMessage(message);
        if (touchEvent == 0) {
            return false;
        } else if (touchEvent == 1) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }


    @Override
    public String getTAG() {
        return "TouchEventActivity";
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
