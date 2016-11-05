package client;

import javafx.event.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.effect.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;

import java.util.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
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

public class ClientControl implements Initializable {
        
    @FXML //fx:id="mainView"
    ImageView mainView;
    
    // Using "Haar-like" features model for face detection
    // Pre-trained model comes with openCV
    CascadeClassifier haar_model;
    MatOfRect detected_faces;
    
    faceDetection FDthread;
    VideoCapture capture;
    ArrayList<Mat> cropped_faces;
    MatOfByte bytemem;
    Mat frame;

    @Override
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        view.setEffect(new Reflection());
        view.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                //System.exit(0);
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if(mouseEvent.getClickCount() == 2)
                    {
                        System.out.println("Double clicked");
    
                        view.setImage(new javafx.scene.image.Image("client/img/giphy2.gif"));
                    }
                }
            }
        });    
        view.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        
        System.out.println("initialize control!");
        
        FDthread = null;
        capture  = null;
        bytemem  = new MatOfByte();
        frame    = new Mat();
        
        // Using "Haar-like" features model for face detection
        // Pre-trained model comes with openCV
        String haar_model_file = "haarcascade_frontalface_alt.xml";
        haar_model             = new CascadeClassifier(Client.class.getResource(haar_model_file).getPath().substring(1));
        detected_faces         = new MatOfRect();
        
        // User default webcam
        capture = new VideoCapture(0);
        
        //Start the Thread for face detection
        FDthread   = new faceDetection();
        Thread fdt = new Thread(FDthread);
        fdt.setDaemon(true);
        FDthread.runnable = true;
        fdt.start();    
        
        //DEBUG
        System.out.println("started");
    }
    
    class faceDetection implements Runnable {
    protected volatile boolean runnable = false;
    
        @Override
        public void run() {
            synchronized (this) {
                
                while (runnable) {
                    System.out.println("| CHECK |");
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
                            
                            if ( cropped_faces.isEmpty() )
                            {
                                System.out.println("FACE_DETECTED");
                                view.setImage(new javafx.scene.image.Image("client/img/giphy2.gif"));
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

}