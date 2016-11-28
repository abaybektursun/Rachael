package client;

import javafx.concurrent.Task;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

class callRequest extends Task {
    String IP;
    Session session;

    public final static int ALLOCATE_BUFFER = 5022386;
    public final static int RESPONSE_BUFFER_SIZE = 128;

    public callRequest(String IP, Session session){
        this.IP = IP;
        this.session = session;
    }

    @Override
    public Void call()
    {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        Socket socket;

        // the OpenCV object that realizes the video capture
        VideoCapture capture = new VideoCapture();
        // start the default video cam
        capture.open(0);

        if (capture.isOpened())
        {
            // Capture a frame from camera
            Mat frame = new Mat();
            capture.read(frame);
            // convert and show the frame
            BufferedImage singleFrame = RachaelUtil.Mat2BufferedImage(frame);

            try {
                int response;
                socket = new Socket(IP, session.getDefaultPort());
                socket.setSoTimeout(3000);
                System.out.println("Connecting...");

                // IO streams
                ObjectOutputStream out_stream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream  in_stream  = new ObjectInputStream (socket.getInputStream ());

                ArrayList<Object> out_data = new ArrayList<Object>();
                out_data.add(RachaelUtil.CODE_CALL_REQUEST);
                out_data.add(singleFrame);
                out_stream.writeObject(out_data);

                out_stream.close();
                in_stream.close();
                socket.close();
            }
            catch(Exception e) { e.printStackTrace(); }
            finally {}
        }

        return null;
    }

}