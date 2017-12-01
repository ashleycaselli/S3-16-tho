package domain

import org.scalatest.{BeforeAndAfter, FunSuite}

class PositionTest extends FunSuite with BeforeAndAfter {

    private val latitude = 45.467255
    private val longitude = 9.1896145
    var position: Position = _

    before {
        position = PositionImpl(latitude, longitude)
    }

    test("Latitude property must return a Double") {
        assert(position.latitude.isInstanceOf[Double])
    }

    test("Longitude property must return a Double") {
        assert(position.longitude.isInstanceOf[Double])
    }

    test("Latitude must be between -85 and +85") {
        assert(position.latitude >= -85 && position.latitude <= 85)
    }

    test("Longitude must be between -180 and +180") {
        assert(position.longitude >= -180 && position.longitude <= 180)
    }

    test("Latitude value checking") {
        assert(position.latitude === latitude)
    }

    test("Longitude value checking") {
        assert(position.longitude === longitude)
    }

}
