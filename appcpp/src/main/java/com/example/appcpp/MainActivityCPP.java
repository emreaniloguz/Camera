package com.example.appcpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

public class MainActivityCPP extends CameraActivity {

    private static String LOGTAG =  "OpenCV_Log";
    private CameraBridgeViewBase mOpenCVCameraView;
    static {
     System.loadLibrary("appcpp");
    }

    public native void FindFeatures(long addrGray, long addrRGB);
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status) {
                case LoaderCallbackInterface.SUCCESS:{
                    Log.v(LOGTAG, "Camera Initialized");
                    mOpenCVCameraView.enableView();
                }break;

                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
            super.onManagerConnected(status);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cpp);


        mOpenCVCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_surface_view);
        mOpenCVCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCVCameraView.setCvCameraViewListener(cvCameraViewListener);

    }
    @Override
    protected List<?extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCVCameraView);
    }




    private CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener = new CameraBridgeViewBase.CvCameraViewListener2() {
        @Override
        public void onCameraViewStarted(int width, int height) {

        }

        @Override
        public void onCameraViewStopped() {

        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            Mat input_rgba = inputFrame.rgba();
            Mat input_gray = inputFrame.gray();


            // Process using CPP

            FindFeatures(input_gray.getNativeObjAddr(), input_rgba.getNativeObjAddr());
            return input_rgba;

        }
    };


    @Override
    public  void onPause() {
        super.onPause();

        if (mOpenCVCameraView != null){
            mOpenCVCameraView.disableView();
        }
    }

    @Override
    public  void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()){
            Log.d(LOGTAG, "OpenCV not found, Initializing");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION,this, mLoaderCallback);
        } else{
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }
    @Override
    public  void onDestroy() {
        super.onDestroy();

        if (mOpenCVCameraView != null){
            mOpenCVCameraView.disableView();
        }
    }
}