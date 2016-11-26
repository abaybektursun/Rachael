package server;

import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.face.*;
//import org.opencv.contrib.FaceRecognizer;
//import org.opencv.contrib.FaceRecognizer.*;
import org.opencv.core.Algorithm.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;
import org.opencv.core.Mat;

public class ClientProtocol {
    
    boolean sessionStarted;
    String modelFilePath = "server/models/CustomModel.xml";
    FaceRecognizer model;
    
    public ClientProtocol(){
        this.sessionStarted = false;
        
        FaceRecognizer model = Face.createLBPHFaceRecognizer();
        model.load(modelFilePath);
        
    }
    //-------------------------------------------------------------//
    public void TestProcess(int test) {                            //
        System.out.println("Processed: " + String.valueOf(test) ); //
    }                                                              //
    public void TestProcess2(String test) {                        //
        System.out.println("Processed: " + test );                 //
    }                                                              //
    //-------------------------------------------------------------//
    
    // Login Process (Predict face) -----------------------------------------
    public void startRecognition(BufferedImage startImage)
    {
        
    }
    public void processRecognition(BufferedImage processImage)
    {
        
    }
    public void resultRecognition(BufferedImage lastImage)
    {
        
    }
    // -----------------------------------------------------------------------
    
    // Training Process (Memorization of face)--------------------------------
    
    // -----------------------------------------------------------------------
    
    // This will return Mat converted from input buffered image
    // It will also convert the image to Grayscale just to be sure
    public Mat imageToMat(BufferedImage img)
    {
        Mat matImage     = new Mat();
        Mat matImageGray = new Mat();
        
        // Read the incoming image into a byte array
        byte[] byteBuffer = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        matImage.put(0, 0, byteBuffer);
        
        try{
            Imgproc.cvtColor(matImage, matImageGray, Imgproc.COLOR_RGB2GRAY);
        }catch(Exception grayExc){grayExc.printStackTrace();}
        
        return matImageGray;
    }
}
