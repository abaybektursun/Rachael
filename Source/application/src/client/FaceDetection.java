package client;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class FaceDetection implements Runnable {
    protected volatile boolean runnable = false;

    @Override
    public void run() {
        synchronized (this) {
            // User default webcam
            VideoCapture capture = new VideoCapture(0);
            // Get the resolution of the camera
            double camWidth  = capture.get(Highgui.CV_CAP_PROP_FRAME_WIDTH);
            double camHeight = capture.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT);
            // Since face detection is expensive we need to shrink the resolution (that division by 2)
            capture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH,(camWidth/2));
            capture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT,(camHeight/2));

            //ArrayList<Mat> cropped_faces;
            MatOfByte bytemem  = new MatOfByte();
            Mat frame = new Mat();

            // Using "Haar-like" features model for face detection
            // Pre-trained model comes with openCV
            String haar_model_file = "models/haarcascade_frontalface_alt.xml";
            //CascadeClassifier haar_model = new CascadeClassifier(Client.class.getResource(haar_model_file).getPath().substring(1));
            //Path when using IJ IDEA
            CascadeClassifier haar_model = new CascadeClassifier(haar_model_file);
            MatOfRect detected_faces = new MatOfRect();

            while (runnable) {
                if (capture.grab()) {
                    try {
                        capture.retrieve(frame);
                        haar_model.detectMultiScale(frame, detected_faces);

                        ArrayList<Mat> cropped_faces = new ArrayList<Mat>();

                        // Iterate through all the faces
                        for (Rect detected_box : detected_faces.toArray()) {

                            Rect crop_rect = new Rect(detected_box.x, detected_box.y, detected_box.width, detected_box.height);
                            new Mat(frame,crop_rect);

                            cropped_faces.add(new Mat(frame,crop_rect));
                        }

                        if ( !cropped_faces.isEmpty() )
                        {
                            System.out.println("FACE_DETECTED");
                            //view.setImage(new javafx.scene.image.Image("client/img/giphy2.gif"));
                        }
                        else
                        {
                            System.out.println("NOT_DETECTED");
                        }

                    } catch (Exception ex) {
                        System.out.println("Error");
                    }
                }
            }
        }

    }
}