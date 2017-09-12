package com.example.saeedbaig.gallery;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Uri uri;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_IMAGE = 100;
    ImageView imgView1, imgView2, imgView3, imgView4, imgView5;
    GridView myGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myGridView = (GridView) findViewById(R.id.mygridview);
        imgView1 = (ImageView) findViewById(R.id.imgView);
        imgView2 = (ImageView) findViewById(R.id.imgView2);
        imgView3 = (ImageView) findViewById(R.id.imgView3);
        imgView4 = (ImageView) findViewById(R.id.imgView4);
        imgView5 = (ImageView) findViewById(R.id.imgView5);
        imgView1.setOnClickListener(this);
        imgView2.setOnClickListener(this);
        imgView3.setOnClickListener(this);
        imgView4.setOnClickListener(this);
        imgView5.setOnClickListener(this);


    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, CAMERA_REQUEST);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            selectedImageView.setImageBitmap(photo);
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                        bitmapOptions);


                String path = android.os.Environment
                        .getExternalStorageDirectory()
                        + File.separator
                        + "Phoenix" + File.separator + "default";
                f.delete();
                OutputStream outFile = null;
                File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            Log.w("path of image from gallery......******************.........", picturePath + "");
            selectedImageView.setImageBitmap(thumbnail);


        }

    }

    ImageView selectedImageView;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgView:
                selectedImageView = imgView1;
                break;
            case R.id.imgView2:
                selectedImageView = imgView2;
                break;
            case R.id.imgView3:
                selectedImageView = imgView3;
                break;
            case R.id.imgView4:
                selectedImageView = imgView4;
                break;
            case R.id.imgView5:
                selectedImageView = imgView5;
                break;

        }
        selectImage();

    }
}




