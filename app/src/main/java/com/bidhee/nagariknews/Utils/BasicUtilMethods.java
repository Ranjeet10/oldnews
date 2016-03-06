package com.bidhee.nagariknews.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

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
}
