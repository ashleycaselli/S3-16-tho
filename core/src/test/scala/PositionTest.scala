import org.scalatest.FunSuite

class PositionTest extends FunSuite {

    val latitude = 45.467255
    val longitude = 9.1896145
    val position: PositionImpl = new PositionImpl(latitude, longitude)

    test("method getLatitude must return a Double") {
        assert(position.getLatitude.isInstanceOf[Double])
    }

    test("method getLongitude must return a Double") {
        assert(position.getLongitude.isInstanceOf[Double])
    }

    test("position latitude must be between -85 and +85") {
        assert(position.getLatitude >= -85 && position.getLatitude <= 85)
    }

    test("position longitude must be between -180 and +180") {
        assert(position.getLongitude >= -180 && position.getLongitude <= 180)
    }

}
