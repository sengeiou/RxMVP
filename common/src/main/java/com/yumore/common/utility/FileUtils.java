package com.yumore.common.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import com.yumore.common.BuildConfig;
import com.yumore.common.R;
import com.yumore.common.common.utility.LoggerUtils;
import okhttp3.MediaType;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Nathaniel
 */
public class FileUtils {
    public static final int TYPE_DOC = 0;
    public static final int TYPE_PIC = 1;
    public static final int TYPE_AUD = 2;
    public static final int TYPE_VID = 3;
    public static final int TYPE_APK = 4;
    public static final int TYPE_ZIP = 5;
    private static final String TAG = FileUtils.class.getSimpleName();
    private static final String ROOT_PATH = "backstage";
    private static final String IMAGE_PATH = "image";
    private static final String AUDIO_PATH = "audio";
    private static final String VIDEO_PATH = "video";
    private static final String LOGGER_PATH = "logger";
    private static final String CACHE_PATH = "cache";
    private final static String RICH_TEXT = "richText";
    private final static String TENCENT_VIDEO = "TXUGC";

    /**
     * 视频：3pg、mp4、avi  大小限制：10m，最多2个
     * 文档：doc、docx、ppt、pptx、xls、xlsx、txt、pdf、wps、dps、et
     * 图片：jpg、jpeg、png、gif、bmp  大小限制2m,最多9张
     */
    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_GIF = MediaType.parse("image/gif");
    private static final MediaType MEDIA_TYPE_BMP = MediaType.parse("image/gif");
    private static final MediaType MEDIA_TYPE_MP3 = MediaType.parse("audio/mp3");
    private static final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");
    private static final MediaType MEDIA_TYPE_3GP = MediaType.parse("video/3pg");
    private static final MediaType MEDIA_TYPE_AVI = MediaType.parse("video/avi");
    private static final MediaType MEDIA_TYPE_FLV = MediaType.parse("video/flv");
    private static final MediaType MEDIA_TYPE_OBJECT = MediaType.parse("application/octet-stream");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private final static byte[] SYNC_CODE = new byte[0];

    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    /**
     * 这是一种坑爹的写法
     * 现在没时间优化
     * 以后改进
     *
     * @param bitmap
     * @param fileName
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveBitmap(Bitmap bitmap, @NonNull String fileName) {
        String subfix = null;
        com.yumore.common.common.utility.LoggerUtils.e(TAG, "fileName = " + fileName);
        String regex = ".+(.[Gg][Ii][Ff])|(.[Jj][Pp][Gg])|(.[Bb][Mm][Pp])|(.[Jj][Pp][Ee][Gg])$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);
        if (fileName.contains(".")) {
            // 不要用indexOf
            subfix = fileName.substring(fileName.indexOf("."));
        }
        try {
            File file = new File(getImagePath(), fileName);
            if (file.exists()) {
                file.delete();
            }
            // TODO 删除之后重新创建 否则会报 FileNotFoundException
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // TODO 保证保存的图片没有黑色背景
            String pngSubfix = ".png";
            if (pngSubfix.equals(subfix)) {
                com.yumore.common.common.utility.LoggerUtils.e(TAG, "png file");
                bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            } else {
                bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用的时候
     * Gson gson = new Gson();
     * String jsonString = gson.toJson(regions);
     *
     * @param context  context
     * @param fileName fileName
     * @param json     json
     * @throws IOException IOException
     */
    public static void writeToJson(Context context, String fileName, String json) throws IOException {
        FileOutputStream outStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outStream.write(json.getBytes());
        outStream.close();
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return EmptyUtils.isObjectEmpty(filePath) ? null : new File(filePath);
    }

    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) {
            return false;
        }
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 使用的时候
     * Gson gson = new Gson();
     * List<Person> statusLs = gson.fromJson(result, new TypeToken<List<Person>>(){}.getCourseType());
     *
     * @param context  context
     * @param fileName fileName
     * @return string
     * @throws IOException IOException
     */
    public static String readFromJson(Context context, String fileName) throws IOException {
        FileInputStream inStream = context.openFileInput(fileName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int length;
        byte[] buffer = new byte[1024];
        while ((length = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, length);
        }
        byte[] bytes = outStream.toByteArray();
        return new String(bytes);
    }

    private static boolean isExists(String path) {
        return new File(path).exists();
    }

    public static Intent getHtmlFileIntent(String path) {
        File file = new File(path);
        Uri uri = Uri.parse(file.toString()).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString()).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    public static Intent getImageFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getPdfFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public static Intent getTextFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    public static Intent getAudioFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    public static Intent getVideoFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    public static Intent getChmFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    public static Intent getWordFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    public static Intent getExcelFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getPPTFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getApkFileIntent(Context context, String path) {
        File file = new File(path);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    public static String convertFileSzie(long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }

    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".ppt")
                || path.endsWith(".PPT")
                || path.endsWith(".pptx")
                || path.endsWith(".PPTX")
                || path.endsWith(".doc")
                || path.endsWith(".DOC")
                || path.endsWith(".docx")
                || path.endsWith(".DOCX")
                || path.endsWith(".xls")
                || path.endsWith(".XLS")
                || path.endsWith(".xlsx")
                || path.endsWith(".XLSX")
                || path.endsWith(".txt")
                || path.endsWith(".TXT")
                || path.endsWith(".pdf")
                || path.endsWith(".PDF")
        ) {
            return TYPE_DOC;
        } else if (path.endsWith(".apk")) {
            return TYPE_APK;
        } else if (path.endsWith(".zip")
                || path.endsWith(".rar")
                || path.endsWith(".tar")
                || path.endsWith(".gz")) {
            return TYPE_ZIP;
        } else {
            return -1;
        }
    }

    public static int getFileIconByPath(String path) {
        path = path.toLowerCase();
        int iconId = R.mipmap.icon_file_blue;
        if (path.endsWith(".txt") || path.endsWith(".TXT")) {
            iconId = R.mipmap.icon_text_grey;
        } else if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".DOC") || path.endsWith(".DOCX")) {
            iconId = R.mipmap.icon_word_blue;
        } else if (path.endsWith(".xls") || path.endsWith(".xlsx") || path.endsWith("XLS") || path.endsWith(".XLSX")) {
            iconId = R.mipmap.icon_excel_green;
        } else if (path.endsWith(".ppt") || path.endsWith(".pptx") || path.endsWith(".PPT") || path.endsWith(".PPTX")) {
            iconId = R.mipmap.icon_ppt_orange;
        } else if (path.endsWith(".xml") || path.endsWith(".XML")) {
            iconId = R.mipmap.icon_file_blue;
        } else if (path.endsWith(".htm") || path.endsWith(".html") || path.endsWith(".htmlx")) {
            iconId = R.mipmap.icon_file_blue;
        }
        return iconId;
    }

    private static boolean isEnglish(char c) {
        return false;
    }

    private static boolean isHanZi(char c) {
        return false;
    }

    public static String getRootPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), ROOT_PATH);
            makeDirs(file);
            return file.getAbsolutePath();
        } else {
            LoggerUtils.e(TAG, "SDCard not mounted.");
            return null;
        }
    }

    public static String getImagePath() {
        File file = new File(getRootPath(), IMAGE_PATH);
        makeDirs(file);
        return file.getAbsolutePath();
    }

    public static String getAudioPath() {
        File file = new File(getRootPath(), AUDIO_PATH);
        makeDirs(file);
        return file.getAbsolutePath();
    }

    public static String getVideoPath() {
        File file = new File(getRootPath(), VIDEO_PATH);
        makeDirs(file);
        return file.getAbsolutePath();
    }

    public static String getLoggerPath() {
        File file = new File(getRootPath(), LOGGER_PATH);
        makeDirs(file);
        return file.getAbsolutePath();
    }

    public static String getCachePath() {
        File file = new File(getRootPath(), CACHE_PATH);
        makeDirs(file);
        return file.getAbsolutePath();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void makeDirs(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static Uri getFileUri(Context context, String path) {
        File file = new File(path);
        Uri uri;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.fileProviderName, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static long getFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = statFs.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = statFs.getAvailableBlocks();
        //返回SD卡空闲大小
        //单位Byte
        //return freeBlocks * blockSize;
        //单位KB
        //return (freeBlocks * blockSize)/1024;
        //单位MB
        return (freeBlocks * blockSize) / 1024 / 1024;
    }

    public static void copyFile(File source, File target) {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
            fileInputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getCacheFile(String url) {
        return new File(getCachePath() + File.separator + getFileName(url));
    }

    public static String getFileName(String url) {
        return Md5Utils.hashKey(url) + "." + getFileType2(url);
    }

    private static String getFileType2(String path) {
        String string = "";
        if (TextUtils.isEmpty(path)) {
            return string;
        }
        int i = path.lastIndexOf('.');
        if (i <= -1) {
            return string;
        }
        string = path.substring(i + 1);
        return string;
    }

    /**
     * 扫描某个文件
     *
     * @param filePath
     */
    public static void scannrFile(Context context, String filePath) {
        Uri data = Uri.parse("file://" + filePath);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(getRootPath() + fileName);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(getRootPath() + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteDir() {
        File dir = new File(getRootPath());
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDir();
            }
        }
        dir.delete();
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public static boolean deleteFile(String path) {
        synchronized (SYNC_CODE) {
            if (TextUtils.isEmpty(path)) {
                return true;
            }

            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }
            File[] filesList = file.listFiles();

            if (filesList != null) {
                for (File f : filesList) {
                    if (f.isFile()) {
                        f.delete();
                    } else if (f.isDirectory()) {
                        deleteFile(f.getAbsolutePath());
                    }
                }
            }

            return file.delete();
        }
    }

    public static MediaType getMediaTypeByFile(File file) {
        if (file.getName().endsWith(".png") || file.getName().endsWith(".PNG")) {
            return MEDIA_TYPE_PNG;
        } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".jpeg") || file.getName().endsWith(".JPG") || file.getName().endsWith(".JPEG")) {
            return MEDIA_TYPE_JPG;
        } else if (file.getName().endsWith(".bmp") || file.getName().endsWith(".BMP")) {
            return MEDIA_TYPE_BMP;
        } else if (file.getName().endsWith(".gif") || file.getName().endsWith(".GIF")) {
            return MEDIA_TYPE_GIF;
        } else if (file.getName().endsWith(".mp4") || file.getName().endsWith(".mpeg4") || file.getName().endsWith(".MP4") || file.getName().endsWith(".MPEG4")) {
            return MEDIA_TYPE_MP4;
        } else if (file.getName().endsWith(".3gp") || file.getName().endsWith(".3GP")) {
            return MEDIA_TYPE_3GP;
        } else if (file.getName().endsWith(".avi") || file.getName().endsWith(".AVI")) {
            return MEDIA_TYPE_AVI;
        } else if (file.getName().endsWith(".flv") || file.getName().endsWith(".FLV")) {
            return MEDIA_TYPE_FLV;
        } else {
            return MEDIA_TYPE_OBJECT;
        }
    }

    public static String getRichTextImageDirectory() {
        File file = new File(getRootPath(), RICH_TEXT);
        makeDirs(file);
        return file.getAbsolutePath();
    }

    public static File createFile(@NonNull String fileName) {
        return new File(fileName);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String savaRichTextImage(String fileName, Bitmap bitmap) {
        fileName = fileName.replaceAll("[^\\w]", "");
        if (bitmap == null) {
            return null;
        }
        String result = null;
        String path = getRichTextImageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(path + File.separator + fileName + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            result = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void deleteRichTextImage() {
        String path = getRichTextImageDirectory();
        File f = new File(path);
        deleteFile(f);
    }

    public void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    public String getFilePathFromUri(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
