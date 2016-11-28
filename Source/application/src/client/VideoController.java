package client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.*;


public class VideoController implements Initializable {
    @FXML
    private ImageView currentFrame;
    @FXML
    private JFXDrawersStack drawersStack;
    @FXML
    private Pane mainPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton topButton;
    @FXML
    private JFXButton bottomButton;

    ServerProtocol server;
    Session session;

    ExecutorService executionThreadPool;
    volatile boolean cameraActive = false;

    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    private final int shadowSize = 50;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        executionThreadPool = Executors.newCachedThreadPool();
        initDrawers();
    }

    private void initDrawers(){
        //content.setMaxSize(200, 200);
        JFXDrawer leftDrawer = new JFXDrawer();
        StackPane leftDrawerPane = new StackPane();
        leftDrawerPane.getStyleClass().add("red-400");
        leftDrawerPane.getChildren().add(new JFXButton("Left Content"));
        leftDrawer.setSidePane(leftDrawerPane);
        leftDrawer.setDefaultDrawerSize(150);
        leftDrawer.setOverLayVisible(false);
        leftDrawer.setResizableOnDrag(true);

        JFXDrawer bottomDrawer = new JFXDrawer();
        StackPane bottomDrawerPane = new StackPane();
        bottomDrawerPane.getStyleClass().add("deep-purple-400");
        bottomDrawerPane.getChildren().add(new JFXButton("Bottom Content"));
        bottomDrawer.setDefaultDrawerSize(150);
        bottomDrawer.setDirection(JFXDrawer.DrawerDirection.BOTTOM);
        bottomDrawer.setSidePane(bottomDrawerPane);
        bottomDrawer.setOverLayVisible(false);
        bottomDrawer.setResizableOnDrag(true);

        JFXDrawer rightDrawer = new JFXDrawer();
        StackPane rightDrawerPane = new StackPane();
        rightDrawerPane.getStyleClass().add("blue-400");
        rightDrawerPane.getChildren().add(new JFXButton("Right Content"));
        rightDrawer.setDirection(JFXDrawer.DrawerDirection.RIGHT);
        rightDrawer.setDefaultDrawerSize(150);
        rightDrawer.setSidePane(rightDrawerPane);
        rightDrawer.setOverLayVisible(false);
        rightDrawer.setResizableOnDrag(true);

        JFXDrawer topDrawer = new JFXDrawer();
        StackPane topDrawerPane = new StackPane();
        topDrawerPane.getStyleClass().add("green-400");
        topDrawerPane.getChildren().add(new JFXButton("Top Content"));
        topDrawer.setDirection(JFXDrawer.DrawerDirection.TOP);
        topDrawer.setDefaultDrawerSize(150);
        topDrawer.setSidePane(topDrawerPane);
        topDrawer.setOverLayVisible(false);
        topDrawer.setResizableOnDrag(true);

        drawersStack.setContent(borderPane);
        leftDrawer.setId("LEFT");
        rightDrawer.setId("RIGHT");
        bottomDrawer.setId("BOT");
        topDrawer.setId("TOP");

        bottomButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
            drawersStack.toggle(bottomDrawer);
        });
        topButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{
            drawersStack.toggle(topDrawer);
        });
    }


    private void stopCameraFeed()
    {
        if (timer!=null && !timer.isShutdown())
        {
            try
            {
                // stop the timer
                timer.shutdown();
                timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) { e.printStackTrace(); }
        }
        // Release the camera
        if (capture.isOpened())
            capture.release();
    }

    public void initServices() {
        //executionThreadPool.submit(cameraTask);
    }


    class cameraTask extends Task {
        @Override
        protected Void call() {
            if (!cameraActive)
            {
                // start the default video cam
                capture.open(0);
                // Chech if the video stream available
                if (capture.isOpened())
                {
                    // Get the resolution of the camera
                    double camWidth  = capture.get(Highgui.CV_CAP_PROP_FRAME_WIDTH);
                    double camHeight = capture.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT);
                    drawersStack.setPrefSize(camWidth,camHeight);
                    currentFrame.setFitHeight(camHeight);
                    currentFrame.setFitWidth(camWidth);
                    mainPane.setPrefSize(camWidth,camHeight);
                    borderPane.setPrefSize(camWidth,camHeight);
                    bottomButton.setPrefSize(camWidth,camHeight/3);
                    topButton.setPrefSize(camWidth,camHeight/3);
                    cameraActive = true;

                    // capture video according to set 'timer' parameter
                    Runnable frameGrabber = new Runnable() {

                        @Override
                        public void run()
                        {
                            // Capture a frame from camera
                            Mat frame = new Mat();
                            capture.read(frame);
                            // convert and show the frame
                            BufferedImage stdBuffImage = RachaelUtil.Mat2BufferedImage(frame);
                            Image jFX_image = SwingFXUtils.toFXImage(stdBuffImage, null);

                            Platform.runLater(new Runnable() {
                                @Override public void run() { currentFrame.setImage(jFX_image); }
                            });
                        }
                    };

                    timer = Executors.newSingleThreadScheduledExecutor();
                    timer.scheduleAtFixedRate(frameGrabber, 0, 50, TimeUnit.MILLISECONDS);
                }
                else
                { System.err.println("Failed to open camera!"); }
            }
            else
            {
                // the camera is not active
                cameraActive = false;
                // stop the timer
                stopCameraFeed();
            }
            return null;
        }
    };


    public void setServerProtocol(ServerProtocol server)
    {
        this.server = server;
    }
    public void setSession(Session session)
    {
        this.session = session;
    }

    // Create a shadow effect as a halo around the pane and not within the pane's content area.
    private void createShadowPane(Pane shadowPane) {
        // I sohuld do this in CSS stylesheet.
        shadowPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-effect: dropshadow(gaussian, red, " + shadowSize + ", 0, 0, 0);" +
                        "-fx-background-insets: " + shadowSize + ";"
        );

        Rectangle innerRect = new Rectangle();
        Rectangle outerRect = new Rectangle();
        shadowPane.layoutBoundsProperty().addListener(
                (observable, oldBounds, newBounds) -> {
                    innerRect.relocate(
                            newBounds.getMinX() + shadowSize,
                            newBounds.getMinY() + shadowSize
                    );
                    innerRect.setWidth(newBounds.getWidth() - shadowSize * 2);
                    innerRect.setHeight(newBounds.getHeight() - shadowSize * 2);

                    outerRect.setWidth(newBounds.getWidth());
                    outerRect.setHeight(newBounds.getHeight());

                    Shape clip = Shape.subtract(outerRect, innerRect);
                    shadowPane.setClip(clip);
                }
        );
    }

    public void startCallReceiver(Socket socket)
    {
        callReceiver callReceiverTask = new callReceiver(socket);
        //TODO Debug
        System.out.println("callReceiverTask instance!");
        //TODO Debug
        executionThreadPool.submit(callReceiverTask);

        //TODO Debug
        System.out.println("startCallReceiver is Done!");
        //TODO Debug
    }

    //----------------------------------------------------------------------------------------------------------------------
    public class callReceiver extends Task {
        Session session;
        ScheduledExecutorService executor;
        Stage videoStage;
        ScheduledFuture<?> future;
        Socket socket;
        public volatile boolean continListening = false;
        public volatile boolean responding = false;

        public callReceiver(Socket socket)
        {
            this.socket = socket;
        }

        @Override
        public Void call() {
            continListening = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //TODO Debug
            System.out.println("try start");
            //TODO Debug
            try (

                    // IO streams
                    ObjectOutputStream out_stream = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in_stream = new ObjectInputStream(socket.getInputStream());
            ) {


                Task endListen = new Task<Void>() {
                    @Override
                    protected Void call() {
                        boolean first_frame = true;
                        while (continListening) {
                            try {
                                //TODO Debug
                                System.out.println("loop");
                                //TODO Debug
                                ArrayList<Object> in_data;
                                // This will result EOFException if there is no more data in the queue
                                in_data = (ArrayList<Object>) in_stream.readObject();
                                int scenario = (Integer) in_data.get(0);

                                if (scenario == Session.CODE_CALL_REQUEST) {
                                    byte[] imageInByte = (byte[]) in_data.get(1);
                                    InputStream in = new ByteArrayInputStream(imageInByte);
                                    BufferedImage bImageFromConvert = ImageIO.read(in);


                                    Image jFX_image = SwingFXUtils.toFXImage(bImageFromConvert, null);


                                    if (first_frame) {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                int camWidth  = bImageFromConvert.getWidth();
                                                int camHeight = bImageFromConvert.getHeight();

                                                drawersStack.setPrefSize(camWidth, camHeight);
                                                currentFrame.setFitHeight(camHeight);
                                                currentFrame.setFitWidth(camWidth);
                                                mainPane.setPrefSize(camWidth, camHeight);
                                                borderPane.setPrefSize(camWidth, camHeight);
                                                bottomButton.setPrefSize(camWidth, camHeight / 3);
                                                topButton.setPrefSize(camWidth, camHeight / 3);
                                            }
                                        });
                                        first_frame = false;
                                    }


                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            currentFrame.setImage(jFX_image);
                                        }
                                    });
                                } else if (scenario == Session.CODE_ROLL_BACK_CALL_REQUEST) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            videoStage.hide();
                                        }
                                    });
                                    continListening = false;
                                    responding=false;
                                    break;
                                } else {
                                    System.out.println("Unknown Request code");
                                }

                            }catch (Exception e){e.printStackTrace();}
                        }
                        return null;
                    }
                };
                executionThreadPool.submit(endListen);

                Task miniRespond = new Task<Void>() {
                    @Override
                    protected Void call() {
                        while(responding) {
                            ArrayList<Object> out_data = new ArrayList<Object>();
                            out_data.add(session.NO_RESPONSE);
                            try {
                                out_stream.writeObject(out_data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                };
                executionThreadPool.submit(miniRespond);

                while (continListening && responding)
                {
                    //wait
                }

            } catch (SocketTimeoutException toe) {
                toe.printStackTrace();
            }
            // Empty Stream, or it's ended
            // Assuming this is fine case
            catch (EOFException eofe) {
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
            /*catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }*/


            return null;
        }

    }
//----------------------------------------------------------------------------------------------------------------------


}