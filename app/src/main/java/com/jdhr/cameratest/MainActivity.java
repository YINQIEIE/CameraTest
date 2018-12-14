package com.jdhr.cameratest;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        int cameraId = getIntent().getIntExtra("Camera", 0);
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("cameraId = " + cameraId);
        Camera camera = Camera.open();
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            tv.append("id = " + i + "\n");
        }
        List<Camera.Size> supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            tv.append(supportedPreviewSizes.get(i).width + "x" + supportedPreviewSizes.get(i).height);
            tv.append("\n");
        }

    }

}
