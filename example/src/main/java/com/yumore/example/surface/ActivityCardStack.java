package com.yumore.example.surface;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.example.adapter.AdapterStackTest;
import com.yumore.rxui.interfaces.OnSimpleListener;
import com.yumore.rxui.tool.RxTool;
import com.yumore.rxui.view.cardstack.RxCardStackView;
import com.yumore.rxui.view.cardstack.tools.RxAdapterAllMoveDownAnimator;
import com.yumore.rxui.view.cardstack.tools.RxAdapterUpDownAnimator;
import com.yumore.rxui.view.cardstack.tools.RxAdapterUpDownStackAnimator;

import java.util.Arrays;

/**
 * @author yumore
 */
public class ActivityCardStack extends AppCompatActivity implements RxCardStackView.ItemExpendListener {

    public static Integer[] TEST_DATAS = new Integer[]{
            R.color.custom_progress_green_header,
            R.color.custom_progress_green_progress,
            R.color.background_content,
            R.color.custom_progress_orange_header,
            R.color.custom_progress_orange_progress,
            R.color.darkslategrey,
            R.color.forestgreen,
            R.color.custom_progress_blue_header,
            R.color.cadetblue,
            R.color.custom_progress_purple_header,
            R.color.mediumaquamarine,
            R.color.mediumseagreen,
            R.color.custom_progress_red_header,
            R.color.custom_progress_red_progress,
            R.color.coral,
            R.color.WARNING_COLOR,
            R.color.aqua,
            R.color.blue_shadow_50,
            R.color.cadetblue,
            R.color.custom_progress_red_progress_half,
            R.color.brown,
            R.color.brown1,
            R.color.brown2,
            R.color.brown3,
            R.color.orange,
            R.color.custom_progress_orange_progress_half
    };
    @BindView(R2.id.stackview_main)
    RxCardStackView mStackView;
    @BindView(R2.id.button_container)
    LinearLayout mButtonContainer;
    private AdapterStackTest mTestStackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_stack);
        ButterKnife.bind(this);

        mStackView.setItemExpendListener(this);
        mTestStackAdapter = new AdapterStackTest(this);
        mStackView.setAdapter(mTestStackAdapter);

        RxTool.delayToDo(200, new OnSimpleListener() {
            @Override
            public void doSomething() {
                mTestStackAdapter.updateData(Arrays.asList(TEST_DATAS));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_all_down) {
            mStackView.setRxAdapterAnimator(new RxAdapterAllMoveDownAnimator(mStackView));
        } else if (itemId == R.id.menu_up_down) {
            mStackView.setRxAdapterAnimator(new RxAdapterUpDownAnimator(mStackView));
        } else if (itemId == R.id.menu_up_down_stack) {
            mStackView.setRxAdapterAnimator(new RxAdapterUpDownStackAnimator(mStackView));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPreClick(View view) {
        mStackView.pre();
    }

    public void onNextClick(View view) {
        mStackView.next();
    }

    @Override
    public void onItemExpend(boolean expend) {
        mButtonContainer.setVisibility(expend ? View.VISIBLE : View.GONE);
    }
}
