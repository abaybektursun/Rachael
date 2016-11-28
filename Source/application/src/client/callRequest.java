package client;

import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class callRequest extends Task {
    String IP;
    Session session;
    public volatile boolean sendddd = false;
    public volatile boolean miniReceive = false;
    public volatile boolean callAcceptedOut = false;

    ExecutorService executionThreadPool;

    public final static int ALLOCATE_BUFFER = 5022386;
    public final static int RESPONSE_BUFFER_SIZE = 128;

    Stage videoStage;
    VideoController videoControl;

    public callRequest(String IP, Session session, Stage videoStage, VideoController videoControl){
        this.IP = IP;
        this.session = session;
        executionThreadPool = Executors.newCachedThreadPool();
        this.videoStage = videoStage;
        this.videoControl = videoControl;
    }

    @Override
    public Void call()
    {
        // the OpenCV object that realizes the video capture
        VideoCapture capture = new VideoCapture();
        // start the default video cam
        capture.open(0);

        if (capture.isOpened()) {
            sendddd = true;
            miniReceive = true;
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


                    Task request = new Task<Void>() {
                        @Override
                        protected Void call() throws IOException {
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
                                try {
                                    ImageIO.write(singleFrame, "jpg", baos);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    baos.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                byte[] imageInByte = baos.toByteArray();

                                out_data.add(imageInByte);
                                out_stream.writeObject(out_data);
                                baos.close();
                            }
                            return null;
                        }
                    };
                    executionThreadPool.submit(request);

                    Task miniListen = new Task<Void>() {
                        @Override
                        protected Void call() {
                            while (miniReceive) {
                                try {
                                    ArrayList<Object> in_data;
                                    // This will result EOFException if there is no more data in the queue
                                    in_data = (ArrayList<Object>) in_stream.readObject();
                                    int scenario = (Integer) in_data.get(0);
                                    if (scenario == session.DECLINED) {
                                        capture.release();
                                        sendddd = false;
                                        miniReceive = false;
                                        break;
                                    }
                                    else if(scenario == session.ACCEPTED)
                                    {
                                        miniReceive = false;
                                        callAcceptedOut = true;

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                videoStage.show();
                                            }
                                        });
                                        videoControl.startCallReceiver(socket);



                                        break;
                                    }
                                }
                                catch (SocketTimeoutException toe) {
                                    toe.printStackTrace();
                                }
                                catch (SocketException se){
                                    se.printStackTrace();
                                    sendddd = false;
                                    miniReceive = false;
                                    capture.release();

                                }
                                // Empty Stream, or it's ended
                                // Assuming this is fine case
                                catch (EOFException eofe) {
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                    sendddd = false;
                                    miniReceive = false;
                                    capture.release();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sendddd = false;
                                    miniReceive = false;
                                    capture.release();
                                }

                            }
                            return null;
                        }
                    };

                    executionThreadPool.submit(miniListen);

                    while (sendddd && miniReceive)
                    {
                        //wait
                    }
                    //while()

                }
                catch (SocketException se){
                    se.printStackTrace();
                    sendddd = false;
                    miniReceive = false;
                    capture.release();

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