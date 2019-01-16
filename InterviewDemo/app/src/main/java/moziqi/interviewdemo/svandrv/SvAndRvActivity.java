package moziqi.interviewdemo.svandrv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.ILog;
import moziqi.interviewdemo.util.LogUtils;

public class SvAndRvActivity extends AppCompatActivity implements ILog {


    private LinearLayoutCompat sv_ff;
    private NestedScrollViewCompat sv_cc;

    private View vOne;
    private View vTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sv_and_rv);

        vOne = findViewById(R.id.vOne);
        vTwo = findViewById(R.id.vTwo);
        sv_ff = findViewById(R.id.sv_ff);
        sv_cc = findViewById(R.id.sv_cc);

        sv_cc.setOnScrollChanged(new NestedScrollViewCompat.OnScrollChanged() {
            @Override
            public void onScroll(int x, int y, int oldx, int oldy) {
                int top = vTwo.getTop();
                //LogUtils.i(getTAG(), "vTwo.top>>" + top);
                //LogUtils.i(getTAG(), "y>>" + y);
                if (y >= top) {
                    sv_cc.getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    sv_cc.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        sv_ff.setTopY(vTwo.getTop());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getTAG() {
        return "SvAndRvActivity";
    }
}
