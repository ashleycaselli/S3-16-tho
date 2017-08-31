package view;

import controller.THOrganizer;
import domain.ClueImpl;
import domain.PositionImpl;
import domain.QuizImpl;
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
    private final THOrganizer controller;
    private Boolean waitingForCoords = false;

    public MapView(final String treasureHuntID, THOrganizer controller) {
        this.treasureHuntID = treasureHuntID;
        this.controller = controller;
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

        Platform.runLater(() -> this.googleMapsPanel.initFX(this));
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
        addPoiButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> {
            JFrame dialog = new JFrame();
            JOptionPane.showMessageDialog(dialog, "Click on the map to select a point");
            this.waitingForCoords = true;
        }));
        this.buttonList.add(addPoiButton);
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

    void newCoordsSelected(Double latitude, Double longitude) {
        if (this.waitingForCoords) {
            this.waitingForCoords = false;
            (new Thread(() -> {
                JFrame dialog = new JFrame();
                String poiName = JOptionPane.showInputDialog(dialog, "Enter point's name:", JOptionPane.PLAIN_MESSAGE);
                String clue = JOptionPane.showInputDialog(dialog, "Enter a clue that helps to reach this point:", JOptionPane.PLAIN_MESSAGE);
                String quizQuestion = JOptionPane.showInputDialog(dialog, "Enter a quiz to be solved when this point is reached:", JOptionPane.PLAIN_MESSAGE);
                String quizAnswer = JOptionPane.showInputDialog(dialog, "Enter the quiz's answer:", JOptionPane.PLAIN_MESSAGE);
                this.controller.addPoi(new PositionImpl(latitude, longitude), poiName, this.treasureHuntID, new QuizImpl(quizQuestion, quizAnswer), new ClueImpl(clue));
            })).start();
        }
    }
}
