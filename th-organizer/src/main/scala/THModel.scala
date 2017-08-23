import domain.TreasureHunt

trait THModel extends Observable[String] {

    def broker: Broker

    def broker_=(broker: Broker): Unit

    def addTreasureHunt(th: TreasureHunt): Unit
}

class THModelImpl(override var broker: Broker) extends THModel {

    require(broker != null)

    private var ths: Seq[TreasureHunt] = Seq empty

    override def addTreasureHunt(th: TreasureHunt): Unit = {
        require(ths != null && !ths.contains(th))
        broker send (th.ID)
        ths = ths :+ th
    }

}
