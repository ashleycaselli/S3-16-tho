package view;

import controller.NewTHButtonController;
import controller.THOrganizer;
import domain.ListTHs$;
import domain.TreasureHunt;
import domain.messages.Message;
import domain.messages.Message$;
import domain.messages.msgType;
import play.api.libs.json.Json;
import utils.Resources;
import utils.Strings;
import view.component.LogoPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This is the Main Window of the Organizer Desktop app.
 */
public class MainView extends JFrame implements OrganizerView {

    private static final Dimension FRAME_DIMENSION = new Dimension(600, 700);
    private static final int DEFAULT_INSET_VALUE = 10;
    private static final int LIST_ROW_VISIBLE = 10;

    private JButton newTHButton;
    private JLabel existingTHLabel;
    private JScrollPane existingTHScrollPane;
    private JList existingTHList;
    private LogoPanel logoPanel;
    private JButton loadButton, deleteButton;
    private final THOrganizer controller;
    private GridBagConstraints c;
    private TreasureHunt selectedTH;

    public MainView(final THOrganizer controller) {
        init();
        this.controller = controller;
    }

    private void init() {
        this.getContentPane().setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        this.setTitle(Strings.ORGANIZER_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_DIMENSION);
        this.setResizable(false);
        this.initComponent();
        this.setVisible(true);
    }

    private void initComponent() {
        this.c = new GridBagConstraints();
        this.c.insets = new Insets(DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE);
        this.c.weightx = 1;
        this.c.gridx = 0;
        this.c.gridy = 0;
        this.c.fill = GridBagConstraints.HORIZONTAL;
        this.logoPanel = new LogoPanel(LogoPanel.Position.CENTER);
        this.add(this.logoPanel, this.c);

        this.newTHButton = new JButton(Strings.NEW_TREASURE_HUNT_BUTTON.toUpperCase());
        this.newTHButton.setFont(Resources.DEFAULT_FONT);
        this.newTHButton.setFocusPainted(false);
        this.c.gridy++;
        this.c.fill = GridBagConstraints.CENTER;
        this.add(this.newTHButton, this.c);
        this.newTHButton.addActionListener(new NewTHButtonController());

        this.existingTHLabel = new JLabel(Strings.EXISTING_TH_LABEL);
        this.existingTHLabel.setFont(Resources.DEFAULT_FONT);
        this.c.anchor = GridBagConstraints.LINE_START;
        this.c.gridy++;
        this.add(this.existingTHLabel, this.c);
    }

    @Override
    public void receiveUpdate(final String update) {
        Message msg = toMessage(update);
        String payload = msg.payload();
        if (msg.messageType().equals(msgType.ListTHs())) {
            ArrayList<TreasureHunt> list = convertList(Json.parse(payload).as(ListTHs$.MODULE$.listTHsReads()).list());
            this.existingTHList = new JList(list.toArray());
            this.existingTHList.addListSelectionListener(arg -> {
                if (!arg.getValueIsAdjusting()) {
                    this.selectedTH = (TreasureHunt) this.existingTHList.getSelectedValue();
                }
            });
            this.existingTHScrollPane = new JScrollPane(this.existingTHList);
            this.existingTHList.setFixedCellWidth(50);
            this.existingTHList.setCellRenderer(new THListCellRenderer());
            this.existingTHList.setVisibleRowCount(LIST_ROW_VISIBLE);
            this.existingTHList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.existingTHScrollPane = new JScrollPane(this.existingTHList);
            this.c.fill = GridBagConstraints.HORIZONTAL;
            this.c.gridy++;
            this.add(this.existingTHScrollPane, this.c);

            JPanel buttonPanel = new JPanel(new GridBagLayout());
            buttonPanel.setBackground(Color.white);
            GridBagConstraints c1 = new GridBagConstraints();
            c1.insets = new Insets(DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE, DEFAULT_INSET_VALUE);
            c1.weightx = 1;
            c1.gridx = 0;
            c1.gridy = 0;

            this.loadButton = new JButton(Strings.LOAD_BUTTON);
            this.loadButton.setBackground(Color.blue);
            this.loadButton.setForeground(Color.white);
            this.loadButton.setOpaque(true);
            this.loadButton.setBorderPainted(false);
            this.loadButton.setFocusPainted(false);
            buttonPanel.add(this.loadButton, c1);

            this.c.gridy++;
            this.add(buttonPanel, this.c);
            this.loadButton.setFont(Resources.DEFAULT_FONT);

            this.loadButton.addActionListener((actionEvent) -> SwingUtilities.invokeLater(() -> new MapView(this.selectedTH, this.controller)));
        }
    }

    private ArrayList<TreasureHunt> convertList(scala.collection.Seq<TreasureHunt> seq) {
        return new ArrayList<>(scala.collection.JavaConversions.seqAsJavaList(seq));
    }

    private Message toMessage(String string) {
        return Json.parse(string).as(Message$.MODULE$.messageReads());
    }
}



