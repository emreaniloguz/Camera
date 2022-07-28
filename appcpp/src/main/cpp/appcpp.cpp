#include <jni.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>
#include <vector>
#include <string>



using namespace cv;


extern "C" {

    JNIEXPORT void JNICALL Java_com_example_appcpp_MainActivityCPP_FindFeatures(JNIEnv* jniEnv, jobject, jlong addrGray, jlong addrRGB)
    {
        Mat* mGray = (Mat*)addrGray;
        Mat* mRGBA = (Mat*)addrRGB;

        std::vector<Point2f> corners;

        goodFeaturesToTrack(*mGray,corners,20,0.01,10,Mat(),3);

        for (int i=0; i<corners.size(); i++){

            putText(*mRGBA, //target image
                        "Example", //text
                        Point(200, 300), //top-left position
                        FONT_HERSHEY_DUPLEX,
                        1.0,
                        CV_RGB(118, 185, 0), //font color
                        2);
            circle(*mRGBA,corners[i],10,Scalar(0,255,0),2);
        }
    }
}