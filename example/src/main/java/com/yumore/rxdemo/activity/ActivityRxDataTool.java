package com.yumore.rxdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.rxdemo.R;
import com.yumore.rxdemo.R2;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.tool.RxDataTool;
import com.yumore.rxui.view.RxTitle;

/**
 * @author yumore
 */
public class ActivityRxDataTool extends ActivityBase {

    @BindView(R2.id.editText)
    EditText mEditText;
    @BindView(R2.id.btn_null)
    Button mBtnNull;
    @BindView(R2.id.tv_null)
    TextView mTvNull;
    @BindView(R2.id.btn_null_obj)
    Button mBtnNullObj;
    @BindView(R2.id.tv_null_obj)
    TextView mTvNullObj;
    @BindView(R2.id.btn_is_integer)
    Button mBtnIsInteger;
    @BindView(R2.id.tv_is_integer)
    TextView mTvIsInteger;
    @BindView(R2.id.btn_is_double)
    Button mBtnIsDouble;
    @BindView(R2.id.tv_is_double)
    TextView mTvIsDouble;
    @BindView(R2.id.btn_is_number)
    Button mBtnIsNumber;
    @BindView(R2.id.tv_is_number)
    TextView mTvIsNumber;
    @BindView(R2.id.ed_month)
    EditText mEdMonth;
    @BindView(R2.id.ed_day)
    EditText mEdDay;
    @BindView(R2.id.btn_astro)
    Button mBtnAstro;
    @BindView(R2.id.tv_astro)
    TextView mTvAstro;
    @BindView(R2.id.ed_mobile)
    EditText mEdMobile;
    @BindView(R2.id.btn_hind_mobile)
    Button mBtnHindMobile;
    @BindView(R2.id.tv_hind_mobile)
    TextView mTvHindMobile;
    @BindView(R2.id.ed_bank_card)
    EditText mEdBankCard;
    @BindView(R2.id.btn_format_bank_card)
    Button mBtnFormatBankCard;
    @BindView(R2.id.tv_format_bank_card)
    TextView mTvFormatBankCard;
    @BindView(R2.id.btn_format_bank_card_4)
    Button mBtnFormatBankCard4;
    @BindView(R2.id.tv_format_bank_card_4)
    TextView mTvFormatBankCard4;
    @BindView(R2.id.btn_getAmountValue)
    Button mBtnGetAmountValue;
    @BindView(R2.id.tv_getAmountValue)
    TextView mTvGetAmountValue;
    @BindView(R2.id.btn_getRoundUp)
    Button mBtnGetRoundUp;
    @BindView(R2.id.tv_getRoundUp)
    TextView mTvGetRoundUp;
    @BindView(R2.id.ed_text)
    EditText mEdText;
    @BindView(R2.id.btn_string_to_int)
    Button mBtnStringToInt;
    @BindView(R2.id.tv_string_to_int)
    TextView mTvStringToInt;
    @BindView(R2.id.btn_string_to_long)
    Button mBtnStringToLong;
    @BindView(R2.id.tv_string_to_long)
    TextView mTvStringToLong;
    @BindView(R2.id.btn_string_to_double)
    Button mBtnStringToDouble;
    @BindView(R2.id.tv_string_to_double)
    TextView mTvStringToDouble;
    @BindView(R2.id.btn_string_to_float)
    Button mBtnStringToFloat;
    @BindView(R2.id.tv_string_to_float)
    TextView mTvStringToFloat;
    @BindView(R2.id.btn_string_to_two_number)
    Button mBtnStringToTwoNumber;
    @BindView(R2.id.tv_string_to_two_number)
    TextView mTvStringToTwoNumber;
    @BindView(R2.id.btn_upperFirstLetter)
    Button mBtnUpperFirstLetter;
    @BindView(R2.id.tv_upperFirstLetter)
    TextView mTvUpperFirstLetter;
    @BindView(R2.id.btn_lowerFirstLetter)
    Button mBtnLowerFirstLetter;
    @BindView(R2.id.tv_lowerFirstLetter)
    TextView mTvLowerFirstLetter;
    @BindView(R2.id.btn_reverse)
    Button mBtnReverse;
    @BindView(R2.id.tv_reverse)
    TextView mTvReverse;
    @BindView(R2.id.btn_toDBC)
    Button mBtnToDBC;
    @BindView(R2.id.tv_toDBC)
    TextView mTvToDBC;
    @BindView(R2.id.btn_toSBC)
    Button mBtnToSBC;
    @BindView(R2.id.tv_toSBC)
    TextView mTvToSBC;
    @BindView(R2.id.btn_oneCn2ASCII)
    Button mBtnOneCn2ASCII;
    @BindView(R2.id.tv_oneCn2ASCII)
    TextView mTvOneCn2ASCII;
    @BindView(R2.id.btn_oneCn2PY)
    Button mBtnOneCn2PY;
    @BindView(R2.id.tv_oneCn2PY)
    TextView mTvOneCn2PY;
    @BindView(R2.id.btn_getPYFirstLetter)
    Button mBtnGetPYFirstLetter;
    @BindView(R2.id.tv_getPYFirstLetter)
    TextView mTvGetPYFirstLetter;
    @BindView(R2.id.btn_cn2PY)
    Button mBtnCn2PY;
    @BindView(R2.id.tv_cn2PY)
    TextView mTvCn2PY;
    @BindView(R2.id.ed_money)
    EditText mEdMoney;
    @BindView(R2.id.ed_string)
    EditText mEdString;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.btn_getPYAllFirstLetter)
    Button mBtnGetPYAllFirstLetter;
    @BindView(R2.id.tv_getPYAllFirstLetter)
    TextView mTvGetPYAllFirstLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_data_utils);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
    }

    @OnClick({R2.id.btn_null,
            R2.id.btn_null_obj,
            R2.id.btn_is_integer,
            R2.id.btn_is_double,
            R2.id.btn_is_number,
            R2.id.btn_astro,
            R2.id.btn_hind_mobile,
            R2.id.btn_format_bank_card,
            R2.id.btn_format_bank_card_4,
            R2.id.btn_getAmountValue,
            R2.id.btn_getRoundUp,
            R2.id.btn_string_to_int,
            R2.id.btn_string_to_long,
            R2.id.btn_string_to_double,
            R2.id.btn_string_to_float,
            R2.id.btn_string_to_two_number,
            R2.id.btn_upperFirstLetter,
            R2.id.btn_lowerFirstLetter,
            R2.id.btn_reverse,
            R2.id.btn_toDBC,
            R2.id.btn_toSBC,
            R2.id.btn_oneCn2ASCII,
            R2.id.btn_oneCn2PY,
            R2.id.btn_getPYFirstLetter,
            R2.id.btn_getPYAllFirstLetter,
            R2.id.btn_cn2PY})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_null) {
            mTvNull.setText(RxDataTool.isNullString(mEditText.getText().toString()) + "");
        } else if (id == R.id.btn_null_obj) {
            mTvNullObj.setText(RxDataTool.isNullString(mEditText.getText().toString()) + "");
        } else if (id == R.id.btn_is_integer) {
            mTvIsInteger.setText(RxDataTool.isInteger(mEditText.getText().toString()) + "");
        } else if (id == R.id.btn_is_double) {
            mTvIsDouble.setText(RxDataTool.isDouble(mEditText.getText().toString()) + "");
        } else if (id == R.id.btn_is_number) {
            mTvIsNumber.setText(RxDataTool.isNumber(mEditText.getText().toString()) + "");
        } else if (id == R.id.btn_astro) {
            mTvAstro.setText(RxDataTool.getAstro(RxDataTool.stringToInt(mEdMonth.getText().toString()), RxDataTool.stringToInt(mEdDay.getText().toString())));
        } else if (id == R.id.btn_hind_mobile) {
            mTvHindMobile.setText(RxDataTool.hideMobilePhone4(mEdMobile.getText().toString()));
        } else if (id == R.id.btn_format_bank_card) {
            mTvFormatBankCard.setText(RxDataTool.formatCard(mEdBankCard.getText().toString()));
        } else if (id == R.id.btn_format_bank_card_4) {
            mTvFormatBankCard4.setText(RxDataTool.formatCardEnd4(mEdBankCard.getText().toString()));
        } else if (id == R.id.btn_getAmountValue) {
            mTvGetAmountValue.setText(RxDataTool.getAmountValue(mEdMoney.getText().toString()));
        } else if (id == R.id.btn_getRoundUp) {
            mTvGetRoundUp.setText(RxDataTool.getRoundUp(mEdMoney.getText().toString(), 2));
        } else if (id == R.id.btn_string_to_int) {
            mTvStringToInt.setText(RxDataTool.stringToInt(mEdText.getText().toString()) + "");
        } else if (id == R.id.btn_string_to_long) {
            mTvStringToLong.setText(RxDataTool.stringToLong(mEdText.getText().toString()) + "");
        } else if (id == R.id.btn_string_to_double) {
            mTvStringToDouble.setText(RxDataTool.stringToDouble(mEdText.getText().toString()) + "");
        } else if (id == R.id.btn_string_to_float) {
            mTvStringToFloat.setText(RxDataTool.stringToFloat(mEdText.getText().toString()) + "");
        } else if (id == R.id.btn_string_to_two_number) {
            mTvStringToTwoNumber.setText(RxDataTool.format2Decimals(mEdText.getText().toString()) + "");
        } else if (id == R.id.btn_upperFirstLetter) {
            mTvUpperFirstLetter.setText(RxDataTool.upperFirstLetter(mEdString.getText().toString()));
        } else if (id == R.id.btn_lowerFirstLetter) {
            mTvLowerFirstLetter.setText(RxDataTool.lowerFirstLetter(mEdString.getText().toString()));
        } else if (id == R.id.btn_reverse) {
            mTvReverse.setText(RxDataTool.reverse(mEdString.getText().toString()));
        } else if (id == R.id.btn_toDBC) {
            mTvToDBC.setText(RxDataTool.toDBC(mEdString.getText().toString()));
        } else if (id == R.id.btn_toSBC) {
            mTvToSBC.setText(RxDataTool.toSBC(mEdString.getText().toString()));
        } else if (id == R.id.btn_oneCn2ASCII) {
            mTvOneCn2ASCII.setText(RxDataTool.oneCn2ASCII(mEdString.getText().toString()) + "");
        } else if (id == R.id.btn_oneCn2PY) {
            mTvOneCn2PY.setText(RxDataTool.oneCn2PY(mEdString.getText().toString()));
        } else if (id == R.id.btn_getPYFirstLetter) {
            mTvGetPYFirstLetter.setText(RxDataTool.getPYFirstLetter(mEdString.getText().toString()));
        } else if (id == R.id.btn_getPYAllFirstLetter) {
            mTvGetPYAllFirstLetter.setText(RxDataTool.getPYAllFirstLetter(mEdString.getText().toString()));
        } else if (id == R.id.btn_cn2PY) {
            mTvCn2PY.setText(RxDataTool.cn2PY(mEdString.getText().toString()));
        }
    }
}
