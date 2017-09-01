import controller.{THOrganizer, THOrganizerImpl}
import model.{ModelBroker, THModel, THModelImpl}
import view.OrganizerView

object Launcher {

    def main(args: Array[String]): Unit = {
        val broker = new ModelBroker
        val model: THModel = new THModelImpl(broker)
        implicit val controller: THOrganizer = new THOrganizerImpl(model)
        val view: OrganizerView = OrganizerView(OrganizerView.gui)
        model addObserver view
    }

}
