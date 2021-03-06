package com.yumore.example.surface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxFileTool;
import com.yumore.utility.utility.RxZipTool;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.RxToast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yumore
 */
public class ActivityZipEncrypt extends BaseActivity {

    @BindView(R2.id.btn_create_folder)
    Button mBtnCreateFolder;
    @BindView(R2.id.btn_zip)
    Button mBtnZip;
    @BindView(R2.id.tv_state)
    TextView mTvState;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.btn_upzip)
    Button mBtnUpzip;
    @BindView(R2.id.btn_zip_delete_dir)
    Button mBtnZipDeleteDir;
    @BindView(R2.id.Progress)
    ProgressBar mProgress;
    @SuppressLint("HandlerLeak")
    private final Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RxZipTool.CompressStatus.START: {
                    mTvState.setText("Start...");
                    mProgress.setVisibility(View.VISIBLE);
                    break;
                }
                case RxZipTool.CompressStatus.HANDLING: {
                    Bundle bundle = msg.getData();
                    int percent = bundle.getInt(RxZipTool.CompressKeys.PERCENT);
                    mTvState.setText(percent + "%");
                    mProgress.setProgress(percent);
                    break;
                }
                case RxZipTool.CompressStatus.ERROR: {
                    Bundle bundle = msg.getData();
                    String error = bundle.getString(RxZipTool.CompressKeys.ERROR);
                    mTvState.setText(error);
                    break;
                }
                case RxZipTool.CompressStatus.COMPLETED: {
                    mTvState.setText("Completed");
                    mProgress.setVisibility(View.INVISIBLE);
                    break;
                }
                default:
                    break;
            }
        }
    };
    private File fileDir;
    private File fileTempDir;
    private File unZipDirFile;
    private File fileZip;
    private String zipPath;
    private String zipParentPath;
    private String zipTempDeletePath;
    private String unzipPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip_encrypt);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(baseActivity);
        zipParentPath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RxTool";
        zipTempDeletePath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "RxTool" + File.separator + "RxTempTool";
        unzipPath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "??????????????????";
        zipPath = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "Rxtool.zip";

        unZipDirFile = new File(unzipPath);
        if (!unZipDirFile.exists()) {
            unZipDirFile.mkdirs();
        }
    }

    @OnClick({R2.id.btn_create_folder,
            R2.id.btn_zip,
            R2.id.btn_upzip,
            R2.id.btn_zip_delete_dir})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn_create_folder) {
            fileDir = new File(zipParentPath);
            fileTempDir = new File(zipTempDeletePath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            if (!fileTempDir.exists()) {
                fileTempDir.mkdirs();
            }

            try {
                File file = File.createTempFile("?????????????????????", ".txt", fileDir);
                File file1 = File.createTempFile("???????????????o(?????????)o", ".txt", fileTempDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mTvState.setText("???????????? ????????????,????????????????????????RxTool???(????????)");
        } else if (id == R.id.btn_zip) {
            fileZip = new File(zipPath);
            if (fileZip.exists()) {
                RxFileTool.deleteFile(fileZip);
                Logger.d("????????????????????????????????????");
            }

            if (fileDir != null) {
                if (fileDir.exists()) {
                    String result = RxZipTool.zipEncrypt(fileDir.getAbsolutePath(), fileZip.getAbsolutePath(), true, "123456");
                    mTvState.setText("?????????????????????,??????" + result);
                } else {
                    RxToast.error("????????????????????????");
                }
            } else {
                RxToast.error("????????????????????????");
            }
        } else if (id == R.id.btn_upzip) {
            List<File> zipFiles = RxZipTool.unzipFileByKeyword(fileZip, unZipDirFile, "123456");
            String str = "??????????????????(*???*)\n";
            if (zipFiles != null) {
                for (File zipFile : zipFiles) {
                    str += zipFile.getAbsolutePath() + "\n\n";
                }
            }
            mTvState.setText(str);

//                RxZipTool.Unzip(fileZip, unZipDirFile.getAbsolutePath(), "123456", "GBK", _handler, false);
        } else if (id == R.id.btn_zip_delete_dir) {
            if (RxZipTool.removeDirFromZipArchive(zipPath, "RxTool" + File.separator + "RxTempTool")) {
                mTvState.setText("RxTempTool ????????????");
            } else {
                mTvState.setText("RxTempTool ????????????");
            }
        }
    }
}
