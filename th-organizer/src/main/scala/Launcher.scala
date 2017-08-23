object Launcher {

    def main(args: Array[String]): Unit = {
        val broker = new ModelBroker
        val model: THModel = new THModelImpl(broker)
        val controller: THOrganizer = new THOrganizerImpl(model)
        val view: OrganizerView = OrganizerView("cli", controller)
        model addObserver view

        controller createTreasureHunt "ciao"
    }

}
