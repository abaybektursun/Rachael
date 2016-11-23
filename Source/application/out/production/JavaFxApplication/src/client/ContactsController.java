package client;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;


public class ContactsController implements Initializable {

    @FXML
    JFXListView<Label> listView;

    @FXML
    JFXPopup popup;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        for (int i = 0; i < 4; i++){
            try{
                Label lbl = new Label("Item " + i);
                listView.getItems().add(lbl);
            }catch (Exception exc){ exc.printStackTrace();}
        }
    }

    @FXML
    private void expand(ActionEvent event) {
        if (!listView.isExpanded()) {
            listView.setExpanded(true);
            listView.depthProperty().set(1);
        }
        else{
            listView.setExpanded(true);
            listView.depthProperty().set(0);
        }
    }


}
