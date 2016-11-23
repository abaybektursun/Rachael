package client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;


public class ContactsView {

    //JFXListView<Label> contacts;
    //AnchorPane listPane;
    //VBox contactsBox;
    //StackPane main;
    //Stage stage;

    BorderPane border;

    ContactsController control;


    int counter = 0 ;
    public ContactsView(){
        /*
        contacts = new JFXListView<Label>();
        for(int i = 0 ; i < 4 ; i++) contacts.getItems().add(new Label("Item " + i));
        contacts.getStyleClass().add("contactsListView");

        contacts.depthProperty().set(++counter%2);
        contacts.depthProperty().set(1);
        contacts.setExpanded(true);

        listPane = new AnchorPane();

        listPane.getChildren().add(contacts);

        //AnchorPane.setLeftAnchor(contacts, 20.0);

        contactsBox = new VBox();
        //contactsBox.getChildren().add(pane);
        contactsBox.getChildren().add(listPane);
        contactsBox.setSpacing(40);

        border = new BorderPane();
        border.setCenter(contactsBox);
        stage = new Stage();
        final Scene scene = new Scene(border, 600, 600, Color.WHITE);
        stage.setTitle("Contacts");
        scene.getStylesheets().add("resources/css/jfoenix-components.css");
        stage.setScene(scene);
        //stage.setResizable(false);



        //control = new ContactsController(contacts, listPane, main, pane, contactsBox, stage);

        stage.show();
        */
        JFXListView<Label> list = new JFXListView<Label>();
        for(int i = 0 ; i < 4 ; i++) list.getItems().add(new Label("Item " + i));
        list.getStyleClass().add("mylistview");

        FlowPane pane = new FlowPane();
        pane.setStyle("-fx-background-color:WHITE");

        JFXButton button3D = new JFXButton("3D");
        button3D.setOnMouseClicked((e)-> list.depthProperty().set(++counter%2));
        list.depthProperty().set(1);

        JFXButton buttonExpand = new JFXButton("EXPAND");
        buttonExpand.setOnMouseClicked((e)-> {list.depthProperty().set(1);list.setExpanded(true);});

        JFXButton buttonCollapse = new JFXButton("COLLAPSE");
        buttonCollapse.setOnMouseClicked((e)-> {list.depthProperty().set(1);list.setExpanded(false);});

        //pane.getChildren().add(button3D);
        pane.getChildren().add(buttonExpand);
        pane.getChildren().add(buttonCollapse);

        AnchorPane listsPane = new AnchorPane();
        //listsPane.getChildren().add(list);
        //AnchorPane.setLeftAnchor(list, 20.0);

        VBox box = new VBox();
        box.getChildren().add(pane);
        //box.getChildren().add(listsPane);
        box.setSpacing(40);

        //StackPane main = new StackPane();
        //main.getChildren().add(box);
        //main.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        //StackPane.setMargin(pane, new Insets(20,0,0,20));

        border = new BorderPane();
        border.setCenter(list);

        Stage stage = new Stage();
        final Scene scene = new Scene(border, 600, 600, Color.WHITE);
        stage.setTitle("JFX ListView Demo ");
        scene.getStylesheets().add("resources/css/jfoenix-components.css");
        stage.setScene(scene);
        //stage.setResizable(false);
        stage.show();
    }
}
