import org.scalatest.FunSuite

class PositionableTest extends FunSuite {

    val latitude: Double = 40.44633
    val longitude: Double = 21.46533

    val player = new PlayerImpl
    val poi = new POIImpl(new PositionImpl(latitude, longitude))

    test("getPosition of a player when position is not set") {
        assert(player.getPosition.isEmpty)
    }

    test("getPosition of a player when position is already set") {
        player.setPosition(new PositionImpl(latitude, longitude))
        assert(player.getPosition.isDefined)
        assert(player.getPosition.get.getLatitude == latitude && player.getPosition.get.getLongitude == longitude)
    }

    test("getPosition of a POI") {
        assert(poi.getPosition.isDefined)
        assert(poi.getPosition.get.getLatitude == latitude && poi.getPosition.get.getLongitude == longitude)
    }

}