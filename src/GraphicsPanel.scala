package dk.au.chartcraft

import javax.swing._
import java.awt.{Color, Graphics, Rectangle}

sealed trait Drawable {
  def draw(g: Graphics): Unit
}

case class DrawLine(canvas: JPanel, x1: Int, y1: Int, x2: Int, y2: Int) extends Drawable {
  override def draw(g: Graphics): Unit = {
    g.setColor(Color.BLACK)
    g.drawLine(x1, y1, x2, y2)
  }
}

case class DrawRectangle(canvas: JPanel, x1: Int, y1: Int, x2: Int, y2: Int) extends Drawable {
  override def draw(g: Graphics): Unit = {
    g.setColor(Color.RED)
    g.drawRect(x1, y1, x2 - x1, y2 - y1)
  }
}

case class DrawCircle(canvas: JPanel, x: Int, y: Int, radius: Int) extends Drawable {
  override def draw(g: Graphics): Unit = {
    g.setColor(Color.BLUE)
    g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius)
  }
}

case class DrawTextAt(canvas: JPanel, x: Int, y: Int, text: String) extends Drawable {
  override def draw(g: Graphics): Unit = {
    g.setColor(Color.BLACK)
    g.drawString(text, x, y)
  }
}

case class DrawBoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) {
  def toRectangle: Rectangle = new Rectangle(x1, y1, x2 - x1, y2 - y1)
}

class GraphicsPanel extends JPanel {
  var drawables: List[Drawable] = List()

  def addDrawable(d: Drawable): Unit = {
    drawables ::= d
    repaint()
  }

  def clearDrawables(): Unit = {
    drawables = List()
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    drawables.foreach(_.draw(g))
  }
}

object CommandParser {
  val colorMap: Map[String, Color] = Map(
    "black" -> Color.BLACK,
    "red" -> Color.RED,
    "green" -> Color.GREEN,
    "blue" -> Color.BLUE,
    "yellow" -> Color.YELLOW
  )

  val boundingBoxRegex = """BOUNDING-BOX\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)""".r
  val lineRegex = """LINE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)""".r
  val rectangleRegex = """RECTANGLE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)""".r
  val circleRegex = """CIRCLE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*(\d+)""".r
  val textAtRegex = """TEXT-AT\s*\(\s*(\d+)\s+(\d+)\s*\)\s*"([^"]*)"""".r
  val drawCommandRegex = """DRAW\s+(\w+)\s+(.+)""".r
  val fillCommandRegex = """FILL\s+(\w+)\s+(.+)""".r

  def normalizeInput(input: String): String = input.trim.replaceAll("\\s{2,}", " ")

  def parseColor(color: String): Color = colorMap.getOrElse(color.toLowerCase, Color.BLACK)

  def parseCommand(command: String, canvas: GraphicsPanel): Option[Drawable] = {
    val normalizedCommand = normalizeInput(command)
    normalizedCommand match {
      case lineRegex(x1, y1, x2, y2) => Some(DrawLine(canvas, x1.toInt, y1.toInt, x2.toInt, y2.toInt))
      case rectangleRegex(x1, y1, x2, y2) => Some(DrawRectangle(canvas, x1.toInt, y1.toInt, x2.toInt, y2.toInt))
      case circleRegex(x, y, radius) => Some(DrawCircle(canvas, x.toInt, y.toInt, radius.toInt))
      case textAtRegex(x, y, text) => Some(DrawTextAt(canvas, x.toInt, y.toInt, text))
      case _ => None
    }
  }
}
