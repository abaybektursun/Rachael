package client;

import com.jfoenix.controls.JFXNodesList;
import javafx.animation.Interpolator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.BackgroundFill;
import javafx.animation.KeyValue;

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ContactsController implements Initializable {

    @FXML
    JFXListView<Label> listView;

    @FXML
    JFXNodesList nodesListOptions;

    @FXML
    AnchorPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        mainPane.setStyle("-fx-background-color:WHITE");
        for (int i = 0; i < 4; i++){
            try{
                Label lbl = new Label("Item " + i);
                listView.getItems().add(lbl);
            }catch (Exception exc){ exc.printStackTrace();}
        }

        listView.getStyleClass().add("mylistview");
        listView.depthProperty().set(1);
        //listView.setExpanded(true);

        // Node list Buttons
        JFXButton sbutton1 = new JFXButton();
        sbutton1.setTooltip(new Tooltip("Options"));
        Label slabel = new Label("Click");
        slabel.setFont(new Font("Arial", 12));
        slabel.setStyle("-fx-text-fill:WHITE");
        sbutton1.setGraphic(slabel);
        sbutton1.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton1.getStyleClass().addAll("animated-option-button","animated-option-sub-button");

        JFXButton sbutton2 = new JFXButton();
        Label slabel2 = new Label("Toggle");
        slabel2.setFont(new Font("Arial", 9));
        sbutton2.setGraphic(slabel2);
        sbutton2.setTooltip(new Tooltip("Will create margins between contact cards"));
        sbutton2.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton2.getStyleClass().addAll("animated-option-button");

        sbutton2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                expand();
            }
        });

        JFXButton sbutton3 = new JFXButton("Off");
        sbutton3.setTooltip(new Tooltip("Log Off"));
        sbutton3.setButtonType(JFXButton.ButtonType.RAISED);
        sbutton3.getStyleClass().addAll("animated-option-button","animated-option-sub-button2");

        //JFXNodesList nodesListOptions = new JFXNodesList();
        nodesListOptions.setSpacing(6);
        // init nodes
        nodesListOptions.addAnimatedNode(sbutton1, (expanded)->{ return new ArrayList<KeyValue>(){{ add(new KeyValue(slabel.rotateProperty(), expanded? 360:0 , Interpolator.EASE_BOTH));}};});
        nodesListOptions.addAnimatedNode(sbutton2);
        nodesListOptions.addAnimatedNode(sbutton3);
        nodesListOptions.setRotate(90);


    }

    @FXML
    private void expand() {
        if (!listView.isExpanded()) {
            listView.depthProperty().set(1);
            listView.setExpanded(true);
        }
        else{
            listView.depthProperty().set(1);
            listView.setExpanded(false);
        }
    }


}
