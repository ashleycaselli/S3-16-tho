package controller

import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.{JOptionPane, JTextField}

import domain.TreasureHuntImpl
import utils.Strings
import view.MapView

class NewTHButtonController extends ActionListener {

    override def actionPerformed(e: ActionEvent): Unit = {
        val huntNameField = new JTextField
        val huntDateField = new JTextField
        val huntTimeField = new JTextField

        val message = Array("Name:", huntNameField, "Date:", huntDateField, "Time", huntTimeField)

        val option = JOptionPane.showConfirmDialog(null, message, Strings.NEW_TH_TITLE_JOPTIONPANE, JOptionPane.OK_CANCEL_OPTION)
        if (option == JOptionPane.OK_OPTION) {
            val huntName = huntNameField.getText
            val huntDate = huntDateField.getText
            val huntTime = huntTimeField.getText
            if (huntName.isEmpty || huntDate.isEmpty || huntTime.isEmpty) {
                JOptionPane.showMessageDialog(null, Strings.NEW_TH_ERROR_JOPTIONPANE)
            } else {
                new MapView(TreasureHuntImpl(name = huntName, date = huntDate, time = huntTime), THOrganizer.instance)
            }
        }
    }

}
