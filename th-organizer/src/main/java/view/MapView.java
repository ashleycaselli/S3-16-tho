package view;

import javafx.application.Platform;
import utils.Resources;
import utils.Strings;
import view.component.GoogleMapsPanel;
import view.component.LogoPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MapView extends JFrame {

    public static final Dimension FRAME_DIMENSION = new Dimension(1024, 768);
    public static final int DEFAULT_INSET_VALUE = 10;

    private LogoPanel logoPanel;
    private final List<JButton> buttonList = new ArrayList();
    private GoogleMapsPanel googleMapsPanel;
    private final String treasureHuntID;

    public MapView(final String treasureHuntID) {
        this.treasureHuntID = treasureHuntID;
        init();
    }

    private void init() {
        this.getContentPane().setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        this.setTitle(this.treasureHuntID);
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

        this.buttonList.add(new JButton(Strings.ADD_POI_BUTTON));
        this.buttonList.add(new JButton(Strings.SET_QUIZ_BUTTON));
        this.buttonList.add(new JButton(Strings.SET_CLUE_BUTTON));
        this.buttonList.add(new JButton(Strings.CREATE_HUNT_CODE_BUTTON));
        this.buttonList.add(new JButton(Strings.START_HUNT_BUTTON));
        this.buttonList.add(new JButton(Strings.SHOW_PLAYERS_BUTTON));
        c.anchor = GridBagConstraints.LINE_START;
        this.buttonList.forEach(button -> {
            button.setFont(Resources.DEFAULT_FONT);
            c.gridy++;
            this.add(button, c);
        });

        this.googleMapsPanel = new GoogleMapsPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridheight = 7;
        this.add(this.googleMapsPanel, c);

    }


}
