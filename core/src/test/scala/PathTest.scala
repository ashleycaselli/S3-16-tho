import org.scalatest.FunSuite

class PathTest extends FunSuite{

  val poi1: Poi = new PoiImpl(new PositionImpl(-13.163056,-72.545556))    //machupichu
  val poi2: Poi = new PoiImpl(new PositionImpl(51.3838362,-0.5818918))    //millennium falcon
  val poi3: Poi = new PoiImpl(new PositionImpl(36.5430468,-151.8278928))  //nothing

  val path: Path = new PathImpl(List(poi1,poi2,poi3))

  test("getPath of a team") {
    assert(path.getPois==List(poi1,poi2,poi3))
    assertThrows[IndexOutOfBoundsException] {
      path.getPois(3)
    }
  }

}