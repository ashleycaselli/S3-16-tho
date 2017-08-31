package view.component;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import utils.Resources;
import view.GoogleMapsFXMLController;
import view.MapView;

import java.io.IOException;

/**
 * JFXPanel containing a Google Map.
 */
public class GoogleMapsPanel extends JFXPanel {

    private Scene scene;

    MapView view;

    public GoogleMapsPanel() {
        super();
    }

    public void initFX(MapView view) {
        this.view = view;
        this.scene = createScene();
        this.setScene(this.scene);
    }

    private Scene createScene() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.GOOGLE_MAPS_VIEW));
            root = loader.load();
            GoogleMapsFXMLController controller = loader.getController();
            controller.setView(this.view);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        return scene;
    }

}
