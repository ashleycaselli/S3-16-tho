package view

import controller.THOrganizer
import core.Observer

trait OrganizerView extends Observer[String]

object OrganizerView {

    val cli = "CLI"
    val gui = "GUI"

    def apply(s: String)(implicit controller: THOrganizer): OrganizerView = s match {
        case `cli` => new OrganizerCLI(controller)
        case `gui` => new MainView(controller)
        case _ => new OrganizerCLI(controller)
    }

}
