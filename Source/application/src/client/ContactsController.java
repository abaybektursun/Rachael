package client;

import com.jfoenix.controls.JFXNodesList;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.animation.KeyValue;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ContactsController implements Initializable {
    @FXML
    volatile JFXListView<Label> listView;
    @FXML
    JFXNodesList nodesListOptions;
    @FXML
    AnchorPane mainPane;

    ServerProtocol server;
    Session session;

    Stage videoStage;
    VideoController videoController;


    ExecutorService executionThreadPool;
    Task<Void> renderContactsTask;
    volatile boolean renderRunnable = false;


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        executionThreadPool = Executors.newCachedThreadPool();

        mainPane.setStyle("-fx-background-color:WHITE");

        initNodesListButtons();

        listView.getStyleClass().add("mylistview");
        listView.depthProperty().set(1);
    }

    private void initNodesListButtons()
    {
        // Node list Buttons
        JFXButton sbutton1 = new JFXButton();
        sbutton1.setTooltip(new Tooltip("Options"));
        Label slabel = new Label();
        try {
            slabel.setGraphic(new ImageView(new Image(new FileInputStream("img/Attachment_icon.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        slabel.setStyle("-fx-text-fill:WHITE");
        sbutton1.setGraphic(slabel);
        sbutton1.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton1.getStyleClass().addAll("animated-option-button","animated-option-sub-button");

        JFXButton sbutton2 = new JFXButton();
        Label slabel2 = new Label();
        try {
            slabel2.setGraphic(new ImageView(new Image(new FileInputStream("img/settings_icon.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sbutton2.setGraphic(slabel2);
        sbutton2.setTooltip(new Tooltip("Will change margin sizes between contact cards"));
        sbutton2.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton2.getStyleClass().addAll("animated-option-button","animated-option-sub-button");
        sbutton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                expand();
            }
        });


        JFXButton sbutton3 = new JFXButton();
        try {
            sbutton3.setGraphic(new ImageView(new Image(new FileInputStream("img/phone_icon.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sbutton3.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton3.getStyleClass().addAll("animated-option-button");
        sbutton3.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int selectedIndex = listView.getFocusModel().getFocusedIndex();
                // Check that both receiver and the client are available
                if (session.contacts.get(selectedIndex).status == session.AVAILABLE && session.getStatus() == session.AVAILABLE )
                {
                    System.out.println("Selected index: " + selectedIndex);
                    callRequest requestCall = new callRequest(session.contacts.get(selectedIndex).IP,session);
                    //callRequestTask request = new callRequestTask(session.contacts.get(selectedIndex).IP);
                    executionThreadPool.submit(requestCall);
                }
            }
        });

        JFXButton sbutton4 = new JFXButton();
        try {
            sbutton4.setGraphic(new ImageView(new Image(new FileInputStream("img/out_icon.png"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sbutton4.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton4.getStyleClass().addAll("animated-option-button","animated-option-sub-button2");
        sbutton4.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                renderRunnable = false;
                executionThreadPool.shutdown();
            }
        });


        //JFXNodesList nodesListOptions = new JFXNodesList();
        nodesListOptions.setSpacing(5);
        // init nodes
        nodesListOptions.addAnimatedNode(sbutton1, (expanded)->{ return new ArrayList<KeyValue>(){{ add(new KeyValue(slabel.rotateProperty(), expanded? 360:0 , Interpolator.EASE_BOTH));}};});
        nodesListOptions.addAnimatedNode(sbutton2);
        nodesListOptions.addAnimatedNode(sbutton3);
        nodesListOptions.addAnimatedNode(sbutton4);
        nodesListOptions.setRotate(90);
    }

    // Will create margins between contact cards
    @FXML
    private void expand() {
        if (!listView.isExpanded()) {
            listView.depthProperty().set(1);
            listView.setExpanded(true);
            listView.setExpanded(false);
            listView.setExpanded(true);
        }
        else{
            listView.depthProperty().set(1);
            listView.setExpanded(false);
        }
    }

    public void initServices() {
        renderContactsTask = new Task<Void>() {
            @Override
            protected Void call() {
                // Save status of the every contact every iteration
                int[] userStatusArray = new int[session.contacts.size()];

                if (listView.getItems().size() == 0)
                {
                    for (int i = 0; i < session.contacts.size(); i++) {
                        userStatusArray[i] = session.contacts.get(i).status;
                        Label lbl = new Label(session.contacts.get(i).firstName + " " + session.contacts.get(i).lastName);
                        try {
                            lbl.setGraphic(new ImageView(new Image(new FileInputStream("img/" + session.contacts.get(i).status + ".png"))));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        listView.getItems().add(lbl);
                    }
                }

                while (renderRunnable) {
                    // Renders contacts
                    try {
                        for (int i = 0; i < session.contacts.size(); i++) {
                            if(userStatusArray[i] != session.contacts.get(i).status) {
                                Label lbl = new Label(session.contacts.get(i).firstName + " " + session.contacts.get(i).lastName);
                                lbl.setGraphic(new ImageView(new Image(new FileInputStream("img/" + session.contacts.get(i).status + ".png"))));
                                listView.getItems().set(i,lbl);
                            }
                        }
                    } catch (NullPointerException exc) { /*Contacts were not yer initialized*/ } catch (IOException ioexc) {
                        ioexc.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        renderRunnable = true;
        executionThreadPool.submit(renderContactsTask);
        initVideoChat();
        //callListener call_listener = new callListener(session, videoStage, videoController);
        //executionThreadPool.submit(call_listener);
    }


    class callRequestTask extends Task<Void>
    {
        private volatile String callAddress;
        public callRequestTask(String callAddress)
        {
            this.callAddress = callAddress;
        }
        @Override
        protected Void call() {
            if(session.getStatus() == session.AVAILABLE) {
                session.busy();
                try {
                    InetAddress address = InetAddress.getByName(callAddress);
                    DatagramSocket socket = new DatagramSocket();
                    socket.setSoTimeout(3000);
                    byte[] buf = new byte[128];
                    // Request
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, session.getDefaultPort());
                    socket.send(packet);
                    // Response
                    packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    ByteArrayInputStream ByteInStream = new ByteArrayInputStream(packet.getData());
                    DataInputStream DataInStream      = new DataInputStream(ByteInStream);

                    int response = DataInStream.readInt();

                    if (response == 0)
                    {
                        session.available();
                    }
                    else
                    {
                        videoStage.show();
                    }

                    socket.close();
                }catch (Exception iae) {iae.printStackTrace(); session.available();}
            }
            else{
                System.out.println("Busy");
            }
            return null;
        }
    }


    private void initVideoChat(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader fxmlLoaderVideo = new FXMLLoader(getClass().getResource("VideoView.fxml"));
                    videoController = (VideoController)fxmlLoaderVideo.getController();
                    Parent videoRoot = (Parent) fxmlLoaderVideo.load();
                    VideoController videoControl = fxmlLoaderVideo.<VideoController>getController();
                    videoControl.setServerProtocol(server);
                    videoControl.setSession(session);
                    videoControl.initServices();
                    videoStage = new Stage();
                    //videoStage.initModality(Modality.WINDOW_MODAL);
                    videoStage.setTitle("Video Chat");
                    final Scene videoScene = new Scene(videoRoot);
                    // Load the style sheet
                    videoScene.getStylesheets().add(getClass().getResource("jfoenix-components.css").toExternalForm());
                    videoScene.getStylesheets().add(getClass().getResource("jfoenix-design.css").toExternalForm());
                    videoScene.setFill(Color.TRANSPARENT);
                    videoStage.setScene(videoScene);
                    //videoStage.setResizable(false);
                    videoStage.initStyle(StageStyle.TRANSPARENT);
                    //videoStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setServerProtocol(ServerProtocol server)
    {
        this.server = server;
    }
    public void setSession(Session session)
    {
        this.session = session;
    }

}
