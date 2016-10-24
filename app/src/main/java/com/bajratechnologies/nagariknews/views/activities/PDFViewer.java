package com.bajratechnologies.nagariknews.views.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.views.customviews.MySnackbar;
import com.joanzapata.pdfview.PDFView;
import com.squareup.okhttp.internal.Util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 10/23/16.
 */

public class PDFViewer extends BaseThemeActivity {
    private String TAG = getClass().getSimpleName();
    @Bind(R.id.pdfview)
    PDFView pdfView;
    @Bind(R.id.error_layout)
    LinearLayout errorLayout;
    @Bind(R.id.error_text_view)
    TextView errorTv;
    @Bind(R.id.error_image_view)
    ImageView errorImageView;

    private Downloader downloader;
    private String remotePdfPath;
    private String localPdfPath;
    private String pdfName;
    private ProgressDialog dialog;
    private String EPAPER_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_EPAPER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer_layout);
        ButterKnife.bind(this);

        //configure progress dialog
        dialog = new ProgressDialog(PDFViewer.this);
        dialog.setTitle("Download in progress...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setProgress(0);

        //get pdf path from the intent and substring the path to get the pdf file name
        remotePdfPath = getIntent().getStringExtra("pdf");
        pdfName = remotePdfPath.substring(remotePdfPath.lastIndexOf("/") + 1);

        localPdfPath = EPAPER_DIRECTORY + File.separator + pdfName;

        File localFile = new File(localPdfPath);

        if (localFile.exists()) {
            loadPDF(localPdfPath);
        } else {

            downloader = new Downloader();
            if (BasicUtilMethods.isNetworkOnline(this)) {
                downloader.execute(remotePdfPath);
            } else {
                setErrorMessage(BaseThemeActivity.NO_NETWORK);
            }
        }

    }

    public class Downloader extends AsyncTask<String, Integer, File> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.show();

            pdfView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
        }

        @Override
        protected File doInBackground(String... params) {

            File directory = new File(EPAPER_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdir();
            }

            String remote_pdf_path = params[0];
            File fileName = new File(directory, pdfName);

            try {
                URL url = new URL(remote_pdf_path);
                URLConnection connection = url.openConnection();
                connection.connect();

                int TOTAL_LENGTH = connection.getContentLength();
                int count;
                int downloaded = 0;

                byte[] buffer = new byte[1024];

                InputStream inputStream = new BufferedInputStream(url.openStream());
                OutputStream outputStream = new FileOutputStream(fileName);

                while ((count = inputStream.read(buffer)) != -1) {
                    downloaded += count;
                    int downloadedPercent = (int) (downloaded * 100) / TOTAL_LENGTH;
                    Log.i("Downloaded::", downloadedPercent + "");
                    publishProgress(downloadedPercent);
                    outputStream.write(buffer, 0, count);

                    if (isCancelled()) {
                        fileName.delete();
                        break;
                    }
                }


                outputStream.flush();
                outputStream.close();
                inputStream.close();

                //delete the partial downloaded file
                //occurred may be due to network problem
            } catch (MalformedURLException e) {
                e.printStackTrace();

                fileName.delete();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
                fileName.delete();

            } catch (IOException e) {

                e.printStackTrace();
                fileName.delete();

            }
            return new File(directory, pdfName);
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            dialog.dismiss();
            if (file.exists()) {
                loadPDF(file.getAbsolutePath());
            } else {
                setErrorMessage("Some thing went wrong");
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
        }

    }

    private void setErrorMessage(String s) {
        errorLayout.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.INVISIBLE);
        errorTv.setText(s);
    }

    private void loadPDF(String filePath) {
        Log.i(TAG, "LOCAL::" + filePath);
        pdfView.fromFile(new File(filePath))
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                .load();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy() Called");
        if (downloader != null)
            downloader.cancel(true);
    }

    @OnClick(R.id.error_image_view)
    public void onErrorImageViewClicked() {
        if (BasicUtilMethods.isNetworkOnline(this)) {
            downloader = new Downloader();
            downloader.execute(remotePdfPath);
        } else {
            setErrorMessage(BaseThemeActivity.NO_NETWORK);
        }
    }
}
