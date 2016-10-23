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
import android.widget.Toast;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.voghdev.pdfviewpager.library.PDFViewPagerZoom;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PdfScale;

/**
 * Created by ronem on 10/23/16.
 */

public class PDFViewer extends BaseThemeActivity {
    @Bind(R.id.pdfViewPager)
    PDFViewPagerZoom pdfViewPager;

    private String remotePdfPath;
    private String localPdfPath;
    private String pdfName;
    private ProgressDialog dialog;
    private String EPAPER_DIRECTORY = File.separator + StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_EPAPER;
    ;
    private BasePDFPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_viewer_layout);
        ButterKnife.bind(this);

        remotePdfPath = getIntent().getStringExtra("pdf");
        pdfName = remotePdfPath.substring(remotePdfPath.lastIndexOf("/"));

        localPdfPath = Environment.getExternalStorageDirectory() + File.separator + EPAPER_DIRECTORY + File.separator + pdfName;

        File localFile = new File(localPdfPath);

        if (localFile.exists()) {
            loadPDF(localPdfPath);
        } else {
            new Downloader().execute(remotePdfPath);
        }

    }

    public class Downloader extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PDFViewer.this);
            dialog.setTitle("Download in progress...");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            File directory = new File(Environment.getExternalStorageDirectory(), EPAPER_DIRECTORY);
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
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "Download complete...";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            loadPDF(Environment.getExternalStorageDirectory() + File.separator + EPAPER_DIRECTORY + File.separator + pdfName);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setProgress(values[0]);
        }

    }

    private void loadPDF(String filePath) {
        //            adapter = new PDFPagerAdapter(PDFViewer.this, Environment.getExternalStorageDirectory() + File.separator + EPAPER_DIRECTORY + File.separator + pdfName);
//            pdfViewPager.setAdapter(adapter);

        pdfViewPager.setAdapter(new PDFPagerAdapter.Builder(PDFViewer.this)
                .setPdfPath(filePath)
                .setScale(getPdfScale())
                .setOnPageClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pdfViewPager.setVisibility(View.GONE);
                        Toast.makeText(PDFViewer.this, "CLICKED", Toast.LENGTH_LONG).show();
                    }
                })
                .create());
    }

    private PdfScale getPdfScale() {
        PdfScale scale = new PdfScale();
        scale.setScale(5.0f);
        scale.setCenterX(getScreenWidth(this));
        scale.setCenterY(0f);
        return scale;
    }

    public int getScreenWidth(Context ctx) {
        int w = 0;
        if (ctx instanceof Activity) {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            w = displaymetrics.widthPixels;
        }
        return w;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.close();
            adapter = null;
        }
    }
}
