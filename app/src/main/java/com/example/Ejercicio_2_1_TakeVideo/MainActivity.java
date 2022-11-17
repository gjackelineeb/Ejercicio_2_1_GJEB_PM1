package com.example.Ejercicio_2_1_TakeVideo;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Ejercicio_2_1_Tomar_Video.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoViews);

    }

    public void takeVideo(View v) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.start();

            try {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream inputStream = videoAsset.createInputStream();
                FileOutputStream archivovid = openFileOutput(crearMP4(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];

                int len;
                while ((len = inputStream.read(buf)) > 0){
                    archivovid.write(buf, 0, len);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                Toast.makeText(this, "Error en la grabacion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String crearMP4() {
        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String vid = date + ".mp4";
        return vid;
    }
}