package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Google Maps controller. It allows to render the Google Maps View specified in .fxml file.
 */
public class GoogleMapsFXMLController implements Initializable, MapComponentInitializedListener {

    @FXML
    private GoogleMapView mapView;

    @FXML
    private TextField addressTextField;

    private GoogleMap map;

    private GeocodingService geocodingService;

    private final StringProperty address = new SimpleStringProperty();

    private MapView view;

    @Override
    public void mapInitialized() {
        this.geocodingService = new GeocodingService();
        MapOptions mapOptions = new MapOptions();
        mapOptions.overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoom(12)
                .center(new LatLong(47.6097, -122.3331));
        this.map = this.mapView.createMap(mapOptions);
        this.map.addMouseEventHandler(UIEventType.click, mouseEvent -> {
            this.view.newCoordsSelected(mouseEvent.getLatLong().getLatitude(), mouseEvent.getLatLong().getLongitude());
        });
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.mapView.addMapInializedListener(this);
        this.address.bind(this.addressTextField.textProperty());
    }

    /**
     * Event Handler used to set the map's center to the current city searched on the textfield.
     */
    public void addressTextFieldAction() {
        this.geocodingService.geocode(this.address.get(), (GeocodingResult[] results, GeocoderStatus status) -> {
            LatLong latLong;
            if (status == GeocoderStatus.ZERO_RESULTS) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
                alert.show();
                return;
            } else if (results.length > 1) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Multiple results found, showing the first one.");
                alert.show();
                latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
            } else {
                latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
            }
            this.map.setCenter(latLong);
        });
    }

    public void setView(MapView view) {
        this.view = view;
    }
}
