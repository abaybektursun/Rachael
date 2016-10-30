package swe_Project;
import javafx.event.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.input.*;
import javafx.scene.effect.*;

public class cssgui extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    
    @Override
    public void start(Stage stage){
        Group root = new Group();
        Image image = new Image("giphy.gif");
        ImageView view = new ImageView(image);
        root.getStylesheets().add(".idlespinner");
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
                        view.setImage(new Image("giphy2.gif"));
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
        view.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        
        view.setEffect(new Reflection());
        stage.setTitle("Title");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}