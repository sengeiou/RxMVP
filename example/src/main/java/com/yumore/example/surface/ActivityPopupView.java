package com.yumore.example.surface;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.entity.ActionItem;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.RxToast;
import com.yumore.utility.widget.popupwindows.RxPopupImply;
import com.yumore.utility.widget.popupwindows.RxPopupSingleView;
import com.yumore.utility.widget.popupwindows.tools.RxPopupView;
import com.yumore.utility.widget.popupwindows.tools.RxPopupViewManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yumore
 */
public class ActivityPopupView extends BaseActivity implements RxPopupViewManager.TipListener {


    public static final String TIP_TEXT = "Tip";
    private static final String TAG = ActivityPopupView.class.getSimpleName();
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.tv_imply)
    TextView mTvImply;
    @BindView(R2.id.tv_definition)
    TextView mTvDefinition;
    @BindView(R2.id.text_input_edit_text)
    TextInputEditText mTextInputEditText;
    @BindView(R2.id.text_input_layout)
    TextInputLayout mTextInputLayout;
    @BindView(R2.id.text_view_buttons_label)
    TextView mTextViewButtonsLabel;
    @BindView(R2.id.button_above)
    Button mButtonAbove;
    @BindView(R2.id.button_below)
    Button mButtonBelow;
    @BindView(R2.id.button_left_to)
    Button mButtonLeftTo;
    @BindView(R2.id.button_right_to)
    Button mButtonRightTo;
    @BindView(R2.id.linear_layout_buttons_above_below)
    LinearLayout mLinearLayoutButtonsAboveBelow;
    @BindView(R2.id.button_align_left)
    RadioButton mButtonAlignLeft;
    @BindView(R2.id.button_align_center)
    RadioButton mButtonAlignCenter;
    @BindView(R2.id.button_align_right)
    RadioButton mButtonAlignRight;
    @BindView(R2.id.linear_layout_buttons_align)
    RadioGroup mLinearLayoutButtonsAlign;
    @BindView(R2.id.text_view)
    TextView mTextView;
    @BindView(R2.id.parent_layout)
    RelativeLayout mParentLayout;
    @BindView(R2.id.root_layout)
    RelativeLayout mRootLayout;
    @BindView(R2.id.activity_popup_view)
    LinearLayout mActivityPopupView;
    RxPopupViewManager mRxPopupViewManager;
    @RxPopupView.Align
    int mAlign = RxPopupView.ALIGN_CENTER;
    private RxPopupSingleView titlePopup;
    private RxPopupImply popupImply;//提示  一小时后有惊喜

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_view);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mRxTitle.setLeftFinish(baseActivity);

        mRxPopupViewManager = new RxPopupViewManager(this);

        mButtonAlignCenter.setChecked(true);

    }

    private void initPopupView() {
        titlePopup = new RxPopupSingleView(baseActivity, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.popupwindow_definition_layout);
        titlePopup.addAction(new ActionItem("标清"));
        titlePopup.addAction(new ActionItem("高清"));
        titlePopup.addAction(new ActionItem("超清"));
        titlePopup.setItemOnClickListener(new RxPopupSingleView.OnItemOnClickListener() {

            @Override
            public void onItemClick(ActionItem item, int position) {
                // TODO Auto-generated method stub
                if (titlePopup.getAction(position).mTitle.equals(mTvDefinition.getText())) {
                    RxToast.showToast(baseActivity, "当前已经为" + mTvDefinition.getText(), 500);
                } else {
                    if (position >= 0 && position < 3) {
                        mTvDefinition.setText(titlePopup.getAction(position).mTitle);
                    }
                }
            }
        });
    }

    @OnClick({R2.id.tv_imply,
            R2.id.tv_definition,
            R2.id.button_above,
            R2.id.button_below,
            R2.id.button_left_to,
            R2.id.button_right_to,
            R2.id.button_align_center,
            R2.id.button_align_left,
            R2.id.button_align_right})
    public void onClick(View view) {
        String text = TextUtils.isEmpty(mTextInputEditText.getText()) ? TIP_TEXT : mTextInputEditText.getText().toString();
        RxPopupView.Builder builder;
        View tipvView;
        int id = view.getId();
        if (id == R.id.tv_imply) {
            if (popupImply == null) {
                popupImply = new RxPopupImply(baseActivity);
            }
            popupImply.show(mTvImply);
        } else if (id == R.id.tv_definition) {
            initPopupView();
            titlePopup.show(mTvDefinition, 0);
        } else if (id == R.id.button_above) {
            mRxPopupViewManager.findAndDismiss(mTextView);
            builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_ABOVE);
            builder.setAlign(mAlign);
            tipvView = mRxPopupViewManager.show(builder.build());
        } else if (id == R.id.button_below) {
            mRxPopupViewManager.findAndDismiss(mTextView);
            builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_BELOW);
            builder.setAlign(mAlign);
            builder.setBackgroundColor(getResources().getColor(R.color.orange));
            tipvView = mRxPopupViewManager.show(builder.build());
        } else if (id == R.id.button_left_to) {
            mRxPopupViewManager.findAndDismiss(mTextView);
            builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_LEFT_TO);
            builder.setBackgroundColor(getResources().getColor(R.color.greenyellow));
            builder.setTextColor(getResources().getColor(R.color.black));
            builder.setGravity(RxPopupView.GRAVITY_CENTER);
            builder.setTextSize(12);
            tipvView = mRxPopupViewManager.show(builder.build());
        } else if (id == R.id.button_right_to) {
            mRxPopupViewManager.findAndDismiss(mTextView);
            builder = new RxPopupView.Builder(this, mTextView, mParentLayout, text, RxPopupView.POSITION_RIGHT_TO);
            builder.setBackgroundColor(getResources().getColor(R.color.paleturquoise));
            builder.setTextColor(getResources().getColor(android.R.color.black));
            tipvView = mRxPopupViewManager.show(builder.build());
        } else if (id == R.id.button_align_center) {
            mAlign = RxPopupView.ALIGN_CENTER;
        } else if (id == R.id.button_align_left) {
            mAlign = RxPopupView.ALIGN_LEFT;
        } else if (id == R.id.button_align_right) {
            mAlign = RxPopupView.ALIGN_RIGHT;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        RxPopupView.Builder builder = new RxPopupView.Builder(this, mTextView, mRootLayout, TIP_TEXT, RxPopupView.POSITION_ABOVE);
        builder.setAlign(mAlign);
        mRxPopupViewManager.show(builder.build());
    }


    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {
        Log.d(TAG, "tip near anchor view " + anchorViewId + " dismissed");

        if (anchorViewId == R.id.text_view) {
            // Do something when a tip near view with id "R.id.text_view" has been dismissed
        }
    }
}
