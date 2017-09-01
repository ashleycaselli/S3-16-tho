package domain

import org.scalatest.FunSuite

class PathTest extends FunSuite {

    val treasureHuntID: String = "thID"
    val poi1 = POIImpl(PositionImpl(-13.163056, -72.545556), "Machupichu", treasureHuntID)
    val poi2 = POIImpl(PositionImpl(51.3838362, -0.5818918), "Millennium Falcon", treasureHuntID)
    val poi3 = POIImpl(PositionImpl(36.5430468, -151.8278928), "Nothing", treasureHuntID)

    test("POIs must be not empty") {
        assertThrows[IllegalArgumentException] {
            new PathImpl(Seq.empty[POI])
        }
    }

    test("POIs must be not null") {
        assertThrows[NullPointerException] {
            new PathImpl(null)
        }
    }

    test("Checking the POIs sequence") {
        val path = new PathImpl(Seq(poi1, poi2, poi3))
        assert(path.POIs.length === 3)
        assertThrows[IndexOutOfBoundsException] {
            path POIs 3
        }
    }

    test("Checking the domain.POI addition to an existing Path") {
        val path = new PathImpl(Seq(poi1, poi2, poi3))
        val newPOI = POIImpl(PositionImpl(-10.163056, 72.5426), "Pool", treasureHuntID)
        assert(path.POIs.length === 3)
        path addPOI newPOI
        assert(path.POIs.length === 4)
        assertThrows[IndexOutOfBoundsException] {
            path POIs 4
        }
        assert(path.POIs(3) === newPOI)
    }

}