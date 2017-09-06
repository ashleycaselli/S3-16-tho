package view.component;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import utils.Resources;
import view.GoogleMapsFXMLController;

import java.io.IOException;

/**
 * JFXPanel containing a Google Map.
 */
public class GoogleMapsPanel extends JFXPanel {

    private Scene scene;
    private final GoogleMapsFXMLController controller;

    public GoogleMapsPanel(final GoogleMapsFXMLController controller) {
        super();
        this.controller = controller;
    }

    public void initFX() {
        this.scene = createScene();
        this.setScene(this.scene);
    }

    private Scene createScene() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.GOOGLE_MAPS_VIEW));
            loader.setController(this.controller);
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Scene(root);
    }

}
