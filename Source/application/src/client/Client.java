package client;


import javafx.application.Platform;
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
import javafx.concurrent.Task;

import javafx.fxml.FXMLLoader;

import javafx.stage.WindowEvent;
import org.opencv.core.Core;

public class Client extends Application {
    
    private double xOffset = 0;
    private double yOffset = 0;

    Thread fdt;

    // TODO Use either semaphore or mutex?
    ImageView view;

    FaceDetection FDthread;

    Stage contactsStage;

    ChatService chatService;

    Session thisSession;

    ServerProtocol server;

    @Override
    public void start(Stage stage){

        // This line needed to load the OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //FDthread = new FaceDetection();
        //fdt = new Thread(FDthread);
        //fdt.setDaemon(true);
        //FDthread.runnable = true;
        //fdt.start();

        chatService = new ChatService();
        server = new ServerProtocol();
        thisSession = server.tempSession();

        //TEST -------------------------------------------------------------------------------
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ContactsView.fxml"));
            ContactsController controller = (ContactsController)fxmlLoader.getController();
            Parent contacsRoot = (Parent) fxmlLoader.load();
            ContactsController contactsControl = fxmlLoader.<ContactsController>getController();
            contactsControl.setChatService(chatService);
            contactsControl.setServerProtocol(server);
            contactsControl.setSession(thisSession);
            contactsControl.initServices();
            contactsStage = new Stage();
            contactsStage.initModality(Modality.APPLICATION_MODAL);
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
        //------------------------------------------------------------------------------------

        
        Group root = new Group();
        javafx.scene.image.Image image = new javafx.scene.image.Image("img/giphy2.gif");
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
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        );
        
        view.setEffect( new Reflection() );
        //stage.setTitle("Title");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        stage.setOnCloseRequest(e -> Platform.exit());
    }

    @Override
    public void stop(){
        FDthread.runnable = false;
        try { fdt.join(); }
        catch (Exception fdtEx){ fdtEx.printStackTrace(); }
        System.out.println("Stage is closing");
    }
}