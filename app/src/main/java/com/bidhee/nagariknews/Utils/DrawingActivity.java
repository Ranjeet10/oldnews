package com.bidhee.nagariknews.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.view9.costumviews.CustomConfirm;
import com.view9.costumviews.CustomDialog;
import com.view9.costumviews.CustomToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by view9 on 5/15/15.
 */
public class DrawingActivity extends ActionBarActivity implements OnClickListener {
    //    private DrawingView drawView;
    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, chooseBtn;
    private float smallBrush, mediumBrush, largeBrush;
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private SeekBar brushSizeBar;
    private TextView brushSizeTextView;
    private Button btnOkBrushSize;
    int DRAW_BRUSH_SIZE = 1;
    int ERASE_BRUSH_SIZE = 30;
    Boolean isBrush = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing_activity);
        drawView = (DrawingView) findViewById(R.id.drawing);
        drawView.setDrawingCacheEnabled(true);
        drawView.setBrushSize(DRAW_BRUSH_SIZE);

        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);

        currPaint = (ImageButton) paintLayout.getChildAt(0);

        //
        drawBtn = (ImageButton) findViewById(R.id.draw_btn);

        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        chooseBtn = (ImageButton) findViewById(R.id.choose_btn);
        chooseBtn.setOnClickListener(this);


        if (AppController.cacheBitmapForEditDrawing != null) {
            bitmap = AppController.cacheBitmapForEditDrawing;
            drawView.setBackground(new BitmapDrawable(getResources(), bitmap));
        }
    }

    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(DRAW_BRUSH_SIZE);
        //use chosen
        if (view != currPaint) {
//update color
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            if (currPaint.getTag().equals("#000000")) {
                ViewGroup.LayoutParams params = currPaint.getLayoutParams();
                params.width = 27;
                params.height = 27;
                currPaint.setLayoutParams(params);
                currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_black));
            }

            if (color.equals("#000000")) {
                ViewGroup.LayoutParams params = imgView.getLayoutParams();
                params.width = 20;
                params.height = 20;
                imgView.setLayoutParams(params);

                imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_black));
                currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            }

            currPaint = (ImageButton) view;

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.draw_btn) {

            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            btnOkBrushSize = (Button) brushDialog.findViewById(R.id.btnOk);
            brushSizeTextView = (TextView) brushDialog.findViewById(R.id.brushSizeTextView);
            brushSizeBar = (SeekBar) brushDialog.findViewById(R.id.brushSizeSeekbar);
            brushSizeBar.setProgress(DRAW_BRUSH_SIZE);
            brushSizeTextView.setText(DRAW_BRUSH_SIZE + "");
            brushSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    DRAW_BRUSH_SIZE = progress;
                    drawView.setBrushSize(DRAW_BRUSH_SIZE);
                    drawView.setErase(false);
                    brushSizeTextView.setText(DRAW_BRUSH_SIZE + "");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            btnOkBrushSize.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    brushDialog.dismiss();
                    drawView.setErase(false);
                    drawView.setBrushSize(DRAW_BRUSH_SIZE);

                    isBrush = true;
                    drawBtn.setImageResource(R.drawable.ic_brush);
                    eraseBtn.setImageResource(R.drawable.ic_erase);
                }
            });


            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {

            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            btnOkBrushSize = (Button) brushDialog.findViewById(R.id.btnOk);
            brushSizeTextView = (TextView) brushDialog.findViewById(R.id.brushSizeTextView);
            brushSizeBar = (SeekBar) brushDialog.findViewById(R.id.brushSizeSeekbar);
            brushSizeBar.setProgress(ERASE_BRUSH_SIZE);
            brushSizeTextView.setText(ERASE_BRUSH_SIZE + "");
            brushSizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    ERASE_BRUSH_SIZE = progress;
                    drawView.setBrushSize(ERASE_BRUSH_SIZE);
                    brushSizeTextView.setText(ERASE_BRUSH_SIZE + "");
                    drawView.setErase(true);
                    drawView.setBrushSize(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            btnOkBrushSize.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    brushDialog.dismiss();
                    drawView.setErase(true);
                    drawView.setBrushSize(ERASE_BRUSH_SIZE);
                    isBrush = false;
                    drawBtn.setImageResource(R.drawable.ic_brush_white);
                    eraseBtn.setImageResource(R.drawable.ic_erase_blue);

                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {


            final Dialog d = CustomConfirm.showConfirmDialog(DrawingActivity.this, "Performing this action will erase previous drawing\nyou want to continue ?");
            Button btnYes = (Button) d.findViewById(R.id.btnYes);
            Button btnNo = (Button) d.findViewById(R.id.btnNo);
            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBackgroundColor(Color.WHITE);
                    drawView.startNew();

                    d.dismiss();
                }
            });
            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }

            });
            d.show();


        } else if (view.getId() == R.id.save_btn) {

            final Dialog progressDialog = CustomDialog.progressDialog(DrawingActivity.this, "Saving Please wait...");
            final Dialog d = CustomConfirm.showConfirmDialog(DrawingActivity.this, "Drawing will be saved To CMAPDrawings Folder.\nWould you like to save it  ?");
            Button btnYes = (Button) d.findViewById(R.id.btnYes);
            Button btnNo = (Button) d.findViewById(R.id.btnNo);
            btnYes.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressDialog.show();
                    String nameOfFolder = "/CMAPDrawings";
                    String nameOfFilePrefix = "nuspecDrawing";
                    String createdDate = getCurrentDateAndTime();
                    Bitmap finalBmp = Bitmap.createBitmap(drawView.getDrawingCache());
                    String folderPath = Environment.getExternalStorageDirectory() + nameOfFolder.toString();
                    File dir = new File(folderPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File file = new File(folderPath, nameOfFilePrefix + createdDate + ".PNG");

                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        finalBmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        d.dismiss();

                        drawView.setBackground(new BitmapDrawable(getBaseContext().getResources(), finalBmp));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(ConstantsData.KEY_RETURNED_EDITED_DRAWING, file.getAbsolutePath());

                    setResult(RESULT_OK, returnIntent);
                    d.dismiss();
                    progressDialog.dismiss();
                    CustomToast.showSucessDialog(DrawingActivity.this, "Drawing saved to CMAPDrawings Folder", true);
                }
            });
            btnNo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }

            });
            d.show();

        } else if (view.getId() == R.id.choose_btn) {
            drawBtn.setImageResource(R.drawable.ic_brush);
            eraseBtn.setImageResource(R.drawable.ic_erase);

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();
                drawView.setBackground(new BitmapDrawable(getResources(), bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
