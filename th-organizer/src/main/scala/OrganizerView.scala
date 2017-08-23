trait OrganizerView extends Observer[String]

object OrganizerView {

    def apply(s: String, controller: THOrganizer): OrganizerView = s match {
        case "cli" => new OrganizerCLI(controller)
        case _ => new OrganizerCLI(controller)
    }

}


