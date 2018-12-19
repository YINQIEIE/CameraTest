package com.jdhr.cameratest;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();

    private SurfaceView surfaceView;
    private Camera camera;
    private int cameraId;

    Button btnChange;
    SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraId = getIntent().getIntExtra("Camera", 0);
        final TextView tv = findViewById(R.id.sample_text);
        tv.setText("cameraId = " + cameraId + "\n");
        camera = Camera.open();
        showCameraInfo(tv);
        updateCameraOrientation();
//        camera.setParameters(parameters);
        initSurfaceView();
        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraId = cameraId == 0 ? 1 : 0;
                openCamera();
                showCameraInfo(tv);
            }
        });

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        tv.append(screenWidth + "x" + screenHeight);

    }

    private void showCameraInfo(TextView tv) {
        tv.setText("cameraId = " + cameraId);
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        tv.append("orientation = " + cameraInfo.orientation + "\n");
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (int i = 0; i < supportedPreviewSizes.size(); i++) {
            tv.append(supportedPreviewSizes.get(i).width + "x" + supportedPreviewSizes.get(i).height);
            tv.append("\n");
        }
    }

    private void openCamera() {
        try {
            camera.stopPreview();
            camera.release();
            camera = Camera.open(cameraId);
            updateCameraOrientation();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
//            camera.startFaceDetection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSurfaceView() {
        surfaceView = findViewById(R.id.surfaceView);
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i(TAG, "surfaceCreated: ");
                surfaceHolder = holder;
                openCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i(TAG, "surfaceChanged: ");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG, "surfaceDestroyed: ");
            }
        });
    }

    private void updateCameraOrientation() {
        int mOrientation = 0;
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            //rotation参数为 0、90、180、270。水平方向为0。
            int rotation = 90 + mOrientation == 360 ? 0 : 90 + mOrientation;
            //前置摄像头需要对垂直方向做变换，否则照片是颠倒的
            if (cameraId != 0) {
                if (rotation == 90) rotation = 270;
                else if (rotation == 270) rotation = 90;
            }
            parameters.setRotation(rotation);//生成的图片转90°
            //预览图片旋转90°
            camera.setDisplayOrientation(0);//预览转90°
//            parameters.setPreviewSize(720, 720);
            camera.setParameters(parameters);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != camera) {
            camera.stopPreview();
            camera.release();
        }
    }
}
