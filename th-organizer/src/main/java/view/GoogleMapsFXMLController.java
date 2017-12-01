package view;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import controller.THOrganizer$;
import domain.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;
import utils.Strings;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Google Maps controller. It allows to render the Google Maps View specified in .fxml file.
 */
public class GoogleMapsFXMLController implements Initializable, MapComponentInitializedListener {

    private final static int DEFAULT_ZOOM = 15;
    private final static double DEFAULT_LAT = 47.6097;
    private final static double DEFAULT_LONG = -122.3331;

    @FXML
    private GoogleMapView mapView;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button confirmButton;

    private GoogleMap map;

    private GeocodingService geocodingService;

    private final StringProperty address = new SimpleStringProperty();

    private final TreasureHunt currentTreasureHunt;

    public GoogleMapsFXMLController(final TreasureHunt th) {
        this.currentTreasureHunt = th;
    }

    @Override
    public void mapInitialized() {
        this.geocodingService = new GeocodingService();
        MapOptions mapOptions = new MapOptions();

        mapOptions.overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoom(DEFAULT_ZOOM)
                .center(new LatLong(DEFAULT_LAT, DEFAULT_LONG));

        if (this.currentTreasureHunt.location() != null) {
            this.geocodingService.geocode(this.currentTreasureHunt.location(), (GeocodingResult[] results, GeocoderStatus status) -> {
                LatLong latLong;
                if (status != GeocoderStatus.ZERO_RESULTS) {
                    latLong = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
                    this.map.setCenter(latLong);
                }
            });
        }

        this.map = this.mapView.createMap(mapOptions);
        this.map.addMouseEventHandler(UIEventType.click, mouseEvent -> {

            //showing POI's marker
            LatLong poiLocation = mouseEvent.getLatLong();
            Marker poiMarker = showMarker(poiLocation);

            int count = 0;
            String name;
            String clue;
            String quiz;
            String answer;
            TextInputDialog dialog = new TextInputDialog("Insert POI name");
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setHeaderText("POI Name");
            dialog.setTitle("POI");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                count++;
                name = result.get();
                dialog = new TextInputDialog("Insert Clue");
                dialog.setHeaderText("Clue Text");
                dialog.setTitle("Clue");
                result = dialog.showAndWait();
                if (result.isPresent()) {
                    count++;
                    clue = result.get();
                    dialog = new TextInputDialog("Insert Question");
                    dialog.setHeaderText("Question Text");
                    dialog.setTitle("Question");
                    result = dialog.showAndWait();
                    if (result.isPresent()) {
                        count++;
                        quiz = result.get();
                        dialog = new TextInputDialog("Insert Answer");
                        dialog.setHeaderText("Answer Text");
                        dialog.setTitle("Answer");
                        result = dialog.showAndWait();
                        if (result.isPresent()) {
                            count++;
                            answer = result.get();
                            setMarkerTitle(poiMarker, name);
                            POI poi = new POIImpl(0, new PositionImpl(poiLocation.getLatitude(), poiLocation.getLongitude()), name, GoogleMapsFXMLController.this.currentTreasureHunt.ID(), new QuizImpl(0, quiz, answer), new ClueImpl(0, clue));
                            THOrganizer$.MODULE$.instance().addPoi(poi, poiMarker);
                        }
                    }
                }
            }
            if (count < 4) {
                deleteMarker(poiMarker);
            }
        });

        //loading old POI
        List<POI> poisList = convertList(THOrganizer$.MODULE$.instance().getPois());
        for (POI poi :
                poisList) {
            THOrganizer$.MODULE$.instance().addPoiMarker(showMarker(new LatLong(poi.position().latitude(), poi.position().longitude())), poi);

        }
    }

    private ArrayList<POI> convertList(scala.collection.Seq<POI> seq) {
        return new ArrayList<>(scala.collection.JavaConversions.seqAsJavaList(seq));
    }


    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.mapView.addMapInializedListener(this);
        this.address.bind(this.addressTextField.textProperty());
        this.addressTextField.setOnAction(event -> this.geocodingService.geocode(this.address.get(), (GeocodingResult[] results, GeocoderStatus status) -> {
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
            this.currentTreasureHunt.location_$eq(this.addressTextField.getText());
        }));

        this.confirmButton.setText(Strings.CONFIRM_TH_BUTTON);
        this.confirmButton.setOnAction(event -> {
            this.confirmButton.setDisable(true);
            this.addressTextField.setDisable(true);
            THOrganizer$.MODULE$.instance().createTreasureHunt(this.currentTreasureHunt);
        });
        if (this.currentTreasureHunt.location() != null) {
            this.confirmButton.setDisable(true);
            this.addressTextField.setDisable(true);
        }

    }


    //MARKERS OPERATION
    private Marker showMarker(LatLong poiLocation) {
        Marker poiMarker = new Marker(new MarkerOptions().position(poiLocation));
        GoogleMapsFXMLController.this.map.addMarker(poiMarker);
        return poiMarker;
    }

    public void deleteMarker(Marker poiMarker) {
        this.map.removeMarker(poiMarker);
    }

    private void setMarkerTitle(Marker poiMarker, String poiName) {
        poiMarker.setTitle(poiName);
    }


}