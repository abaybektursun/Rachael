package client;

import javafx.event.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.effect.*;

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

import javafx.concurrent.*;

public class Client extends Application {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
   
    
    /*public void launch(){
        FDthread = null;
        capture  = null;
        bytemem  = new MatOfByte();
        frame    = new Mat();
        
        System.out.println("initialize FD!");
        
        // Using "Haar-like" features model for face detection
        // Pre-trained model comes with openCV
        String haar_model_file = "models/haarcascade_frontalface_alt.xml";
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
    }*/

    @Override
    public void start(Stage stage){
        //faceDetectionService fDS = new faceDetectionService();
        ////fDS.reset();
        //Task t = fDS.createTask();
        //fDS.start();
        //t.call();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceDetection FDthread = new faceDetection();
        Thread fdt = new Thread(FDthread);
        fdt.setDaemon(true);
        FDthread.runnable = true;
        fdt.start();
        
        Group root = new Group();
        javafx.scene.image.Image image = new javafx.scene.image.Image("client/img/giphy.gif");
        ImageView view = new ImageView(image);
        root.getChildren().add(view);
        Scene scene = new Scene(root, 600, 600, Color.TRANSPARENT);
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
        view.setOnMouseDragged(
            new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        );
        
        view.setEffect( new Reflection() );
        stage.setTitle("Title");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
    
    class faceDetection implements Runnable {
    protected volatile boolean runnable = false;
    
        @Override
        public void run() {
            synchronized (this) {
                    // User default webcam
                    VideoCapture capture = new VideoCapture(0);
                    
                    //ArrayList<Mat> cropped_faces;
                    MatOfByte bytemem  = new MatOfByte();
                    Mat frame = new Mat(); 
                        
                    // Using "Haar-like" features model for face detection
                    // Pre-trained model comes with openCV
                    String haar_model_file = "models/haarcascade_frontalface_alt.xml";
                    CascadeClassifier haar_model = new CascadeClassifier(Client.class.getResource(haar_model_file).getPath().substring(1));
                    MatOfRect detected_faces = new MatOfRect();
                
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

    
    private static class faceDetectionService extends Service {
        

        //System.out.println("initialized FD!");
        
        protected Task createTask() {
        
            return new Task() {
                @Override
                protected Void call() throws Exception {        
                    // User default webcam
                    VideoCapture capture = new VideoCapture(0);
                    
                    //ArrayList<Mat> cropped_faces;
                    MatOfByte bytemem  = new MatOfByte();
                    Mat frame = new Mat(); 
                        
                    // Using "Haar-like" features model for face detection
                    // Pre-trained model comes with openCV
                    String haar_model_file = "models/haarcascade_frontalface_alt.xml";
                    CascadeClassifier haar_model = new CascadeClassifier(Client.class.getResource(haar_model_file).getPath().substring(1));
                    MatOfRect detected_faces = new MatOfRect();
                    while (true) {
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
            };
        }
    }
}
