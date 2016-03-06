//package com.bidhee.nagariknews.Utils;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.ContentUris;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.media.ThumbnailUtils;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.provider.DocumentsContract;
//import android.provider.MediaStore;
//import android.support.v4.app.Fragment;
//import android.util.Base64;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.google.gson.JsonPrimitive;
//import com.squareup.picasso.Picasso;
//import com.view9.cmap.BuildConfig;
//import com.view9.cmap.R;
//import com.view9.testmethods.TestMethodInformationList;
//
//import org.apache.commons.validator.routines.UrlValidator;
//import org.apache.http.entity.mime.content.ByteArrayBody;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * Created by view9 on 3/3/15.
// */
//public class BasicUtilFunctions {
//
//
//    public static final int GALLERY_PICTURE = 0;
//    public static final int CAMERA_REQUEST = 1;
//
//    public static boolean isEmail(final String hex) {
//        Pattern pattern;
//        Matcher matcher;
//
//        final String EMAIL_PATTERN =
//                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//
//        pattern = Pattern.compile(EMAIL_PATTERN);
//        matcher = pattern.matcher(hex);
//        return matcher.matches();
//    }
//
//    public static boolean isTextWithNoSpace(String inputeText) {
//        if (inputeText.equals("")) {
//            return true;
//        } else {
//            Pattern ps = Pattern.compile("^[a-zA-Z]+$");
//            Matcher ms = ps.matcher(inputeText);
//            return ms.matches();
//        }
//    }
//
//    public static boolean isTextWithSpace(String inputeText) {
//        Pattern ps = Pattern.compile("^[a-zA-Z ]+$");
//        Matcher ms = ps.matcher(inputeText);
//        return ms.matches();
//    }
//
//    public static void setBackgroundCustom(Drawable d, View v) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//            v.setBackgroundDrawable(d);
//        } else {
//            v.setBackground(d);
//        }
//    }
//
//    public static ProgressDialog getProgerssDialog(Context context) {
//        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setTitle(context.getString(R.string.text_loading));
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage(context.getString(R.string.text_please_wait));
//        return progressDialog;
//    }
//
//    public static void choosePhotoDialog(final Fragment activity) {
//        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(activity.getActivity());
////        mAlertDialog.setTitle(activity.getString(R.string.text_pictures_options));
////        mAlertDialog.setMessage(activity.getString(R.string.text_pictures_options_message));
//        mAlertDialog.setPositiveButton("Gallery",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent;
//                        intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//                        intent.setType("image/*");
//                        activity.startActivityForResult(intent, GALLERY_PICTURE);
//                    }
//                }
//        );
//
//        mAlertDialog.setNegativeButton("Camera",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent;
//                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        activity.startActivityForResult(intent, CAMERA_REQUEST);
//                    }
//                }
//        );
//
//        mAlertDialog.show();
//    }
//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public static String getRealPathFromURI(Context context, Uri uri) {
//
//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//
//                // TODO handle non-primary volumes
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//
//    /**
//     * Get the value of the data column for this Uri. This is useful for
//     * MediaStore Uris, and other file-based ContentProviders.
//     *
//     * @param context       The context.
//     * @param uri           The Uri to query.
//     * @param selection     (Optional) Filter used in the query.
//     * @param selectionArgs (Optional) Selection arguments used in the query.
//     * @return The value of the _data column, which is typically a file path.
//     */
//    public static String getDataColumn(Context context, Uri uri, String selection,
//                                       String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int column_index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(column_index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is ExternalStorageProvider.
//     */
//    public static boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is DownloadsProvider.
//     */
//    public static boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    /**
//     * @param uri The Uri to check.
//     * @return Whether the Uri authority is MediaProvider.
//     */
//    public static boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }
//
//    public static String saveBitmapToDirectory(Context context, Bitmap bitmap) {
//        FileOutputStream fileOutputStream = null;
//        File image = null;
//        try {
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            String imageFileName = "CMAP" + timeStamp + "_" + ".jpg";
//            String storageDir = Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name);
//            File dir = new File(storageDir);
//            if (!dir.exists())
//                dir.mkdirs();
//            image = new File(storageDir, imageFileName);
//
//            fileOutputStream = new FileOutputStream(image);
//
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image)));
//        } catch (FileNotFoundException e) {
//
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        }
//        return image.getPath();
//    }
//
//    public static Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
//        try {
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            FileInputStream stream1 = new FileInputStream(new File(path));
//            BitmapFactory.decodeStream(stream1, null, options);
//            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//            options.inJustDecodeBounds = false;
//            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//            stream1.close();
//            return bitmap;
//        } catch (IOException e) {
//            return null;
//        }
//    }
//
//    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//            final int heightRatio = Math.round((float) height / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//        }
//
//        return inSampleSize;
//    }
//
//
//    public static int getPxFromDp(Context context, int sizeInDp) {
//        float scale = context.getResources().getDisplayMetrics().density;
//        int pxFromDp = (int) (sizeInDp * scale + 0.5f);
//        return pxFromDp;
//    }
//
//    //taken from http://stackoverflow.com/a/4009133/3378714
//    public static boolean isNetworkOnline(Context context) {
//        ConnectivityManager cm =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//
//    }
//
//    public static void hideKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//    }
//
//    public static void showKeyboard(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//    }
//
//    public static String getFileName(String filePath) {
//        if (filePath == null)
//            return "";
//        ArrayList<String> _list = new ArrayList<String>();
//        for (String s : filePath.split("/")) {
//            _list.add(s);
//        }
//        String _file = _list.get(_list.size() - 1);
//        return _file;
//    }
//
//    public static String getBase64StringOfFile(String realPath) throws FileNotFoundException {
//
//        InputStream inputStream = new FileInputStream(realPath);//You can get an inputStream using any IO API
//        byte[] bytes;
//        byte[] buffer = new byte[500];
//        int bytesRead;
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        try {
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                output.write(buffer, 0, bytesRead);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        bytes = output.toByteArray();
//        String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT);
//        encodedString = encodedString.replace("+", "-");
//        encodedString = encodedString.replace("/", "_");
//        encodedString = encodedString.replace("=", ".");
//        encodedString = encodedString.replace("\n", "");
//        return encodedString;
//    }
//
//    @Deprecated
//    public static String getFileSize(String actualPath) {
//        File file = new File(actualPath);
//        double bytes = file.length();
//        double kiloBytes = bytes / 1024;
//        String size = String.format("%.2f", kiloBytes);
//
//        return size;
//
//    }
//
//    public static void appLog(String message) {
//        if (BuildConfig.DEBUG) {
//            Log.d("cmap", message);
//        }
//    }
//
//    public static String getExtenstionFromFileName(String fileName) {
//        String extension = "";
//
//        int i = fileName.lastIndexOf('.');
//        if (i > 0) {
//            extension = fileName.substring(i + 1);
//        }
//        return extension;
//    }
//
//    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
//
//    /**
//     * Generate a value suitable for use in {@link #setId(int)}.
//     * This value will not collide with ID values generated at build time by aapt for R.id.
//     *
//     * @return a generated ID value
//     */
//    public static int generateViewId() {
//        for (; ; ) {
//            final int result = sNextGeneratedId.get();
//            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
//            int newValue = result + 1;
//            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
//            if (sNextGeneratedId.compareAndSet(result, newValue)) {
//                return result;
//            }
//        }
//    }
//
//
//    public static boolean isNumeric(String str) {
//        try {
//            double d = Double.parseDouble(str);
//        } catch (NumberFormatException nfe) {
//            return false;
//        }
//        return true;
//    }
//
//    public static int nthOccurrence(String str, char c, int n) {
//        int pos = str.indexOf(c, 0);
//        while (n-- > 0 && pos != -1)
//            pos = str.indexOf(c, pos + 1);
//        return pos;
//    }
//
//    public static int getDeviceWidth(Context context) {
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        return width;
//    }
//
//    public static int getDeviceHeight(Context context) {
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        int height = displayMetrics.heightPixels;
//        return height;
//    }
//
//    public static View getViewByPosition(int pos, ListView listView) {
//        final int firstListItemPosition = listView.getFirstVisiblePosition();
//        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
//
//        if (pos < firstListItemPosition || pos > lastListItemPosition) {
//            return listView.getAdapter().getView(pos, null, listView);
//        } else {
//            final int childIndex = pos - firstListItemPosition;
//            return listView.getChildAt(childIndex);
//        }
//    }
//
//    public static byte[] decodeFromBase64String(String base64PDF) {
//        String encodedString = base64PDF;
//        encodedString = encodedString.replace("-", "+");
//        encodedString = encodedString.replace("_", "/");
//        encodedString = encodedString.replace(".", "=");
//        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
//        return decodedBytes;
//    }
//
//    public static String getDateStringFromYMD(int year, int month, int day) {
//        String smonth, sday;
//
//        smonth = String.format("%02d", (month + 1));
//        sday = String.format("%02d", day);
//        return year + "-" + smonth + "-" + sday;
//    }
//
//    public static boolean isValidJSONObject(String value) {
//
//        try {
//            new JSONObject(value);
//        } catch (JSONException e) {
//            return false;
//        }
//        return true;
//    }
//
//    public static Date getDateFromString(String dateString) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            return simpleDateFormat.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static void setVideoThumbnail(String filePath, ImageView imageView) {
//        UrlValidator urlValidator = new UrlValidator();
//        if (filePath != null && new File(filePath).exists()) {
//            Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND);
//            imageView.setImageBitmap(bmThumbnail);
//        } else if (filePath != null && urlValidator.isValid(filePath)) {
//            Picasso.with(imageView.getContext()).load(filePath).resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size).
//                    centerInside().into(imageView);
//        } else {
//            imageView.setImageResource(R.drawable.ic_videos);
//        }
//    }
//
//    public static String getCurrentDate() {
//        String currentDate = "";
//        Date currentdate = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        currentDate = dateFormat.format(currentdate);
//        return currentDate;
//    }
//
//    public static double doubleValue(String doubleString) {
//        double value = 0;
//
//        try {
//            value = Double.valueOf(doubleString);
//        } catch (NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//
//        return value;
//    }
//
//    public static Integer getIntegerFromString(String stringNumb) {
//        Integer value = null;
//        try {
//            value = Integer.valueOf(stringNumb);
//        } catch (NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//
//        return value;
//    }
//
//    public static String getFileSizeString(String path) {
//        File file = new File(path);
//        double bytes;
//        double fileSizeInt = 0;
//        String fileSizeUnit = "";
//        if (file.exists()) {
//            bytes = file.length();
//
//            if (bytes > 1024) {
//                fileSizeInt = bytes / 1024;
//                fileSizeUnit = "KB";
//            }
//
//            if (fileSizeInt > 1024) {
//                fileSizeInt = fileSizeInt / 1024;
//                fileSizeUnit = "MB";
//            }
//
//            if (fileSizeInt > 1024) {
//                fileSizeInt = fileSizeInt / 1024;
//                fileSizeUnit = "GB";
//            }
//        }
//
//        return String.format("%.2f  %s", fileSizeInt, fileSizeUnit);
//    }
//
//    public static ByteArrayBody getByteArrayBodyFromBitmap(Bitmap bitmap) {
//        ByteArrayOutputStream bao = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
//        byte[] ba = bao.toByteArray();
//        String photoName = "thumb_" + Calendar.getInstance().getTimeInMillis();
//        return new ByteArrayBody(ba, photoName + ".jpg");
//    }
//
//    public static String getDateStringFromTimeStamp(long completedDate) {
//        Date date = new Date(completedDate * 1000);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE dd MMM yyyy");
//        return simpleDateFormat.format(date);
//    }
//
//    public static JsonObject convertJSONToJson(JSONObject jsonObject) {
//        JsonParser jsonParser = new JsonParser();
//        String jsonString = jsonObject.toString();
//        return (JsonObject) jsonParser.parse(jsonString);
//    }
//
//    public static Boolean isImage(String extension) {
//        if (extension.contains("PNG")) {
//            return true;
//        } else if (extension.contains("png")) {
//            return true;
//        } else if (extension.contains("JPG")) {
//            return true;
//        } else if (extension.contains("jpg")) {
//            return true;
//        } else if (extension.contains("JPEG")) {
//            return true;
//        } else if (extension.contains("jpeg")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static Boolean isDocumentFile(String extension) {
//        if (extension.contains("pdf")) {
//            return true;
//        } else if (extension.contains("doc")) {
//            return true;
//        } else if (extension.contains("docx")) {
//            return true;
//        } else if (extension.contains("doc")) {
//            return true;
//        } else if (extension.contains("xlsx")) {
//            return true;
//        } else if (extension.contains("xls")) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public static long longValueOf(String value) {
//        long longValue = 0;
//
//        try {
//            longValue = Long.valueOf(value);
//        } catch (NumberFormatException ex) {
//            ex.printStackTrace();
//        }
//
//        return longValue;
//    }
//
//    //    This function prints testmethod json
//    public static void printTestMethodInfoJson() {
//        try {
//            List<JSONObject> testMethodInfoLIst = TestMethodInformationList.getTestMethodInformationList();
//            List<JSONObject> activityMethodInfoLIst = TestMethodInformationList.getActivityMethodInformationList();
//            JsonParser jsonParser = new JsonParser();
//            JsonArray testMethodJsonArray = new JsonArray();
//            JsonArray tableNameArray = new JsonArray();
//            for (JSONObject testMehod : testMethodInfoLIst) {
//                String string = testMehod.toString();
//                JsonObject jsonObject = (JsonObject) jsonParser.parse(string);
//                testMethodJsonArray.add(jsonObject);
//                JSONArray jsonArrayFields = testMehod.getJSONArray(TestMethodInformationList.KEY_TEST_METHOD_FIELD);
//                for (int i = 0; i < jsonArrayFields.length(); i++) {
//                    JSONObject field = jsonArrayFields.getJSONObject(i);
//                    boolean isFieldTypeNumeric = BasicUtilFunctions.isNumeric(field.getString(TestMethodInformationList.KEY_TEST_METHOD_FIELD_VIEW_TYPE));
//                    if (!isFieldTypeNumeric) {
//                        tableNameArray.add(new JsonPrimitive(field.getString(TestMethodInformationList.KEY_TEST_METHOD_FIELD_VIEW_TYPE)));
//                    }
//                }
//
//            }
//            Log.i("testMethodInfoList", testMethodJsonArray.toString());
//            Log.i("tables", tableNameArray.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
