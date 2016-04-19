package com.bidhee.nagariknews.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ronem on 2/4/16.
 */
public class BasicUtilMethods {

    public static String getImageNameFromImagepath(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf("/"));
    }

    public static void saveFileToGalery(Context context, String folderName, String fileName, ImageView imageView) {
        FileOutputStream fops;
        BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();

        //folder
        File folder = new File(Environment.getExternalStorageDirectory(), File.separator + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //file
        File file = new File(folder, fileName + ".PNG");


        if (!file.exists()) {
            try {
                fops = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fops);
                fops.flush();
                fops.close();
                Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "file already exists", Toast.LENGTH_SHORT).show();
        }

    }

    public static void collapseAppbar(ControllableAppBarLayout appBarLayout, Menu menu) {
        appBarLayout.setEnabled(false);
        appBarLayout.setActivated(false);
        appBarLayout.collapseToolbar(true);
        if (menu != null)
            menu.getItem(0).setVisible(false);
    }

    public static void expandAppbar(ControllableAppBarLayout appBarLayout, Menu menu) {
        appBarLayout.setEnabled(true);
        appBarLayout.expandToolbar(true);
        if (menu != null)
            menu.getItem(0).setVisible(true);
    }


    public static void shareLink(Context context, String link) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);
        context.startActivity(Intent
                .createChooser(sharingIntent, "Share using"));
    }

    public static void loadImage(final Context context, final String url, final ImageView galleryThumbnail) {
        Picasso.with(context)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(galleryThumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(url)
                                .error(R.drawable.nagariknews)
                                .into(galleryThumbnail, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });

    }
}
