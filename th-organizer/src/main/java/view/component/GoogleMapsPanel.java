package view.component;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import utils.Resources;

import java.io.IOException;

/**
 * JFXPanel containing a Google Map.
 */
public class GoogleMapsPanel extends JFXPanel {

    private Scene scene;

    public GoogleMapsPanel() {
        super();
    }

    public void initFX() {
        this.scene = createScene();
        this.setScene(this.scene);
    }

    private Scene createScene() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(Resources.GOOGLE_MAPS_VIEW));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        return scene;
    }

}
