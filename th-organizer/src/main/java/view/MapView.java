package view;

import controller.THOrganizer;
import controller.THOrganizer$;
import domain.POI;
import domain.TreasureHunt;
import domain.TreasureHunt$;
import domain.messages.Message;
import domain.messages.Message$;
import domain.messages.msgType;
import javafx.application.Platform;
import play.api.libs.json.Json;
import utils.Resources;
import utils.Strings;
import view.component.GoogleMapsPanel;
import view.component.LogoPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapView extends JFrame implements OrganizerView {

    private static final Dimension FRAME_DIMENSION = new Dimension(1024, 768);
    private static final int DEFAULT_INSET_VALUE = 10;

    private LogoPanel logoPanel;
    private final List<JButton> buttonList = new ArrayList();
    private GoogleMapsPanel googleMapsPanel;
    private final GoogleMapsFXMLController googleMapsController;
    private TreasureHunt currentTreasureHunt;
    private final THOrganizer controller;

    public MapView(final TreasureHunt currentTreasureHunt, final THOrganizer controller) {
        THOrganizer$.MODULE$.instance().model().addObserver(this);
        this.currentTreasureHunt = currentTreasureHunt;
        this.controller = controller;
        this.googleMapsController = new GoogleMapsFXMLController(currentTreasureHunt);
        init();
    }

    private void init() {
        this.getContentPane().setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        this.setTitle(this.currentTreasureHunt.name());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(FRAME_DIMENSION);
        this.setResizable(false);
        this.initComponent();
        this.setLocation(100, 0);
        this.setVisible(true);

        Platform.runLater(() -> this.googleMapsPanel.initFX());
    }

    private void initComponent() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE);
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.CENTER;

        this.logoPanel = new LogoPanel(LogoPanel.Position.LEFT);
        this.add(this.logoPanel, c);

        JButton addPoiButton = new JButton(Strings.ADD_POI_BUTTON);
        addPoiButton.setPreferredSize(new Dimension(300, 45));
        addPoiButton.addActionListener((actionEvent) -> JOptionPane.showMessageDialog(null, "Click on the map to select a point"));
        this.buttonList.add(addPoiButton);
        JButton showPoisButton = new JButton(Strings.SHOW_POIS_BUTTON);
        showPoisButton.setPreferredSize(new Dimension(300, 45));
        showPoisButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            List<POI> pois = scala.collection.JavaConversions.seqAsJavaList(this.controller.getPois());
            String message = "";
            for (POI poi : pois) {
                message += "- " + poi.name() + "\n";
            }
            if (message.isEmpty()) {
                message = "No POIs. Add one first.";
            }
            JFrame dialog = new JFrame();
            JOptionPane.showMessageDialog(dialog, message);
        }));
        this.buttonList.add(showPoisButton);
        JButton createCodeButton = new JButton(Strings.CREATE_HUNT_CODE_BUTTON);
        createCodeButton.setPreferredSize(new Dimension(300, 45));
        createCodeButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            Integer code = this.controller.getCode();
            JFrame dialog = new JFrame();
            JOptionPane.showMessageDialog(dialog, "Your code is: " + code + "\nGive it to the players");
        }));
        this.buttonList.add(createCodeButton);
        JButton startHuntButton = new JButton(Strings.START_HUNT_BUTTON);
        startHuntButton.setPreferredSize(new Dimension(300, 45));
        startHuntButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            this.controller.startHunt();
            JFrame dialog = new JFrame();
            JOptionPane.showMessageDialog(dialog, "Treasure Hunt started.\nDon't forget to create a code.");
        }));
        this.buttonList.add(startHuntButton);
        JButton showPlayersButton = new JButton(Strings.SHOW_PLAYERS_BUTTON);
        showPlayersButton.setPreferredSize(new Dimension(300, 45));
        showPlayersButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {

        }));
        this.buttonList.add(showPlayersButton);
        c.anchor = GridBagConstraints.LINE_START;
        this.buttonList.forEach(button -> {
            button.setFont(Resources.DEFAULT_FONT);
            c.gridy++;
            this.add(button, c);
            button.setEnabled(false);
        });

        this.googleMapsPanel = new GoogleMapsPanel(this.googleMapsController);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 7;
        this.add(this.googleMapsPanel, c);
    }

    @Override
    public void receiveUpdate(final String message) {
        Message msg = Json.parse(message).as(Message$.MODULE$.messageReads());
        String payload = msg.payload();
        if (msg.messageType().equals(msgType.TreasureHunt())) {
            this.buttonList.forEach(button -> button.setEnabled(true));
            this.currentTreasureHunt = Json.parse(payload).as(TreasureHunt$.MODULE$.thReads());
        }
        // TODO method implementation
    }
}
