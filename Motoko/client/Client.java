package client;

import javafx.event.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.effect.*;
import javafx.fxml.FXMLLoader;

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

public class Client extends Application{
    
    private double xOffset = 0;
    private double yOffset = 0;    
    
    @Override
    public void start(Stage stage){  
        
        try
        {
            System.out.println("START!");
            
            //Group root  = (Group)FXMLLoader.load(Client.class.getResource("client/interface.fxml"));
            Group root  = FXMLLoader.load(getClass().getResource("interface.fxml"));
            
            try
            {
                Scene scene = new Scene(root, 600, 600, Color.TRANSPARENT);
                stage.setTitle("Title");
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
                
            }
            catch(Exception e2)
            {
                System.out.println("SETUP ERROR!");
            }
            
            System.out.println("LOADED!");
        }
       catch(Exception e)
       {
            e.printStackTrace();
       }

        //javafx.scene.image.Image image = new javafx.scene.image.Image("client/img/giphy.gif");
        //view = new ImageView(image);
        //root.getChildren().add(view);        
        
    }

}
