import controller.THOrganizer
import model.{ModelBroker, THModel, THModelImpl}
import view.OrganizerView

object Launcher extends App {

    val broker = new ModelBroker
    val model: THModel = new THModelImpl(broker)
    implicit val controller: THOrganizer = THOrganizer.apply(model)
    val view: OrganizerView = OrganizerView(OrganizerView.gui)
    model addObserver view

}
