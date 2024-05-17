// #Sireum #Logika
import org.sireum._

@datatype trait Drawable {
  def draw(): ISZ[(Z, Z)]
}

@datatype class DrawLine(x1: Z, y1: Z, x2: Z, y2: Z) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    val coordinates: ISZ[(Z, Z)] = ISZ((x1, y1), (x1 + 1, y1 + 1), (x2, y2))  // Limiting to 3 points for simplicity
    println(s"DrawLine from ($x1, $y1) to ($x2, $y2) -> $coordinates")
    return coordinates
  }
}

@datatype class DrawRectangle(x1: Z, y1: Z, x2: Z, y2: Z) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    val coordinates: ISZ[(Z, Z)] = ISZ((x1, y1), (x2, y1), (x1, y2))  // Limiting to 3 points for simplicity
    println(s"DrawRectangle from ($x1, $y1) to ($x2, $y2) -> $coordinates")
    return coordinates
  }
}

@datatype class DrawCircle(x: Z, y: Z, radius: Z) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    val coordinates: ISZ[(Z, Z)] = ISZ((x, y), (x + radius, y), (x, y + radius))  // Limiting to 3 points for simplicity
    println(s"DrawCircle center ($x, $y) with radius $radius -> $coordinates")
    return coordinates
  }
}

@datatype class DrawTextAt(x: Z, y: Z, text: String) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    val coordinates: ISZ[(Z, Z)] = ISZ((x, y))
    println(s"DrawTextAt ($x, $y) with text '$text' -> $coordinates")
    return coordinates
  }
}

@record class GraphicsPanel {
  var drawables: ISZ[Drawable] = ISZ[Drawable]()

  def addDrawable(d: Drawable): Unit = {
    drawables = drawables :+ d
  }

  def paintComponent(): ISZ[(Z, Z)] = {
    var coordinates: ISZ[(Z, Z)] = ISZ[(Z, Z)]()
    for (d <- drawables) {
      val drawCoordinates: ISZ[(Z, Z)] = d.draw()
      coordinates = coordinates ++ drawCoordinates
    }
    println(s"GraphicsPanel paintComponent -> $coordinates")
    return coordinates
  }
}

@record class GraphicsPanelTest {

  def testDrawLine(): Unit = {
    val line: DrawLine = DrawLine(12, 23, 14, 25)
    val coordinates: ISZ[(Z, Z)] = line.draw()
    assert(coordinates(0) == ((12, 23)), "DrawLine start point failed")
    assert(coordinates(2) == ((14, 25)), "DrawLine end point failed")
  }

  def testDrawRectangle(): Unit = {
    val rect: DrawRectangle = DrawRectangle(10, 10, 12, 12)
    val coordinates: ISZ[(Z, Z)] = rect.draw()
    assert(coordinates(0) == ((10, 10)), "DrawRectangle top-left failed")
    assert(coordinates(2) == ((10, 12)), "DrawRectangle bottom-left failed")
  }

  def testDrawCircle(): Unit = {
    val circle: DrawCircle = DrawCircle(50, 50, 1)
    val coordinates: ISZ[(Z, Z)] = circle.draw()
    assert(coordinates(0) == ((50, 50)), "DrawCircle center point failed")
    assert(coordinates(1) == ((51, 50)), "DrawCircle right point failed")
  }

  def testDrawTextAt(): Unit = {
    val text: DrawTextAt = DrawTextAt(20, 30, "Hello")
    val coordinates: ISZ[(Z, Z)] = text.draw()
    assert(coordinates(0) == ((20, 30)), "DrawTextAt position failed")
  }

  def testGraphicsPanel(): Unit = {
    val panel: GraphicsPanel = GraphicsPanel()
    panel.addDrawable(DrawLine(10, 20, 15, 25))
    panel.addDrawable(DrawRectangle(15, 15, 18, 18))
    panel.addDrawable(DrawCircle(45, 45, 2))
    panel.addDrawable(DrawTextAt(25, 35, "Test"))
    val coordinates: ISZ[(Z, Z)] = panel.paintComponent()
    assert(coordinates(0) == ((10, 20)), "GraphicsPanel line start point failed")
    assert(coordinates(2) == ((15, 25)), "GraphicsPanel line end point failed")
    assert(coordinates(3) == ((15, 15)), "GraphicsPanel rectangle top-left failed")
    assert(coordinates(5) == ((15, 18)), "GraphicsPanel rectangle bottom-left failed")
    assert(coordinates(6) == ((45, 45)), "GraphicsPanel circle center point failed")
    assert(coordinates(7) == ((47, 45)), "GraphicsPanel circle right point failed")
    assert(coordinates(9) == ((25, 35)), "GraphicsPanel text position failed")
  }
}

val panelTest: GraphicsPanelTest = GraphicsPanelTest()
println(":: Testing Graphics Methods ::")
panelTest.testDrawLine()
panelTest.testDrawRectangle()
panelTest.testDrawCircle()
panelTest.testDrawTextAt()
println(":: Testing Graphics Panel ::")
panelTest.testGraphicsPanel()
