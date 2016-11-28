package client;


import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.application.Application;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.effect.Reflection;

import javafx.fxml.FXMLLoader;

import javafx.stage.WindowEvent;
import org.opencv.core.Core;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client extends Application {
    
    private double xOffset = 0;
    private double yOffset = 0;

    Thread fdt;

    ImageView view;

    FaceDetection FDthread;

    Stage contactsStage;

    Session thisSession;

    ServerProtocol server;


    @Override
    public void start(Stage primaryStage){

        // This line needed to load the OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //FDthread = new FaceDetection();
        //fdt = new Thread(FDthread);
        //fdt.setDaemon(true);
        //FDthread.runnable = true;
        //fdt.start();

        //server = new ServerProtocol("192.168.1.103",3141);
        server = new ServerProtocol();
        thisSession = server.tempSession();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                    try {
                        FXMLLoader fxmlLoaderContacts = new FXMLLoader(getClass().getResource("ContactsView.fxml"));
                        ContactsController controller = (ContactsController)fxmlLoaderContacts.getController();
                        Parent contacsRoot = (Parent) fxmlLoaderContacts.load();
                        ContactsController contactsControl = fxmlLoaderContacts.<ContactsController>getController();
                        contactsControl.setServerProtocol(server);
                        contactsControl.setSession(thisSession);
                        contactsControl.initServices();
                        contactsStage = new Stage();
                        //contactsStage.initModality(Modality.WINDOW_MODAL);
                        //contactsStage.initStyle(StageStyle.UNIFIED);
                        contactsStage.setTitle("Contacts");
                        final Scene contactsScene = new Scene(contacsRoot);
                        // Load the style sheet
                        contactsScene.getStylesheets().add(getClass().getResource("jfoenix-components.css").toExternalForm());
                        contactsStage.setScene(contactsScene);
                        contactsStage.getIcons().add(new Image("img/contacts-icon.png"));
                        contactsStage.setResizable(false);
                        // Consume standard windows event
                        contactsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) { event.consume(); contactsStage.setIconified(true);}
                        });
                        contactsStage.show();
                    }
                    catch(Exception lol){ lol.printStackTrace(); }
            }
        });


        //TEST -------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------
        
        Group root = new Group();
        javafx.scene.image.Image image = new javafx.scene.image.Image("img/giphy.gif");
        //ImageView view = new ImageView(image);
        view = new ImageView(image);
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
                    primaryStage.setX(event.getScreenX() - xOffset);
                    primaryStage.setY(event.getScreenY() - yOffset);
                }
            }
        );
        
        view.setEffect( new Reflection() );
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }

    @Override
    public void stop(){
        FDthread.runnable = false;
        try { fdt.join(); }
        catch (Exception fdtEx){ fdtEx.printStackTrace(); }
        System.out.println("Stage is closing");
    }
}