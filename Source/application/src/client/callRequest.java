package client;

import javafx.concurrent.Task;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

class callRequest extends Task {
    String IP;
    Session session;
    public volatile boolean sendddd = false;

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
        //Socket socket;

        // the OpenCV object that realizes the video capture
        VideoCapture capture = new VideoCapture();
        // start the default video cam
        capture.open(0);

        if (capture.isOpened()) {
            sendddd = true;
            boolean connected = false;
            try (
                    Socket socket = new Socket(IP, session.getDefaultPort());
                    //socket.setSoTimeout(3000);
            ){
                try(
                        // IO streams
                        ObjectOutputStream out_stream = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in_stream = new ObjectInputStream(socket.getInputStream());
                ){
                    while (sendddd) {
                        // Capture a frame from camera
                        Mat frame = new Mat();
                        capture.read(frame);
                        // convert and show the frame
                        BufferedImage singleFrame = RachaelUtil.Mat2BufferedImage(frame);

                        int response;


                        ArrayList<Object> out_data = new ArrayList<Object>();
                        out_data.add(session.CODE_CALL_REQUEST);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(singleFrame, "jpg", baos);
                        baos.flush();
                        byte[] imageInByte = baos.toByteArray();

                        out_data.add(imageInByte);
                        out_stream.writeObject(out_data);
                        baos.close();

                        // TODO Remove debug
                        System.out.println("Sent!");
                        // TODO Remove debug
                        try{
                            ArrayList<Object> in_data;
                            // This will result EOFException if there is no more data in the queue
                            in_data = (ArrayList<Object>) in_stream.readObject();
                            int scenario = (Integer) in_data.get(0);
                            if(scenario == session.DECLINED)
                            {
                                break;
                            }
                        }
                        catch (SocketTimeoutException toe) {
                            toe.printStackTrace();
                        }
                        // Empty Stream, or it's ended
                        // Assuming this is fine case
                        catch (EOFException eofe) {
                        }
                        catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
                catch (Exception cnfe) {
                    cnfe.printStackTrace();
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}