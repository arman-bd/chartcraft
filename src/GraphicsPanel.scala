package dk.au.chartcraft

import javax.swing._
import java.awt.{Color, Font, Graphics, Rectangle}

sealed trait Drawable {
  def draw(g: Graphics, boundingBox: Option[Rectangle]): Unit
  def fillColor: Option[Color]
}

case class DrawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: Color = Color.BLACK, override val fillColor: Option[Color] = None) extends Drawable {
  override def draw(g: Graphics, boundingBox: Option[Rectangle]): Unit = {
    g.setColor(color)
    boundingBox.foreach(g.setClip)
    g.drawLine(x1, y1, x2, y2)
  }
}

case class DrawRectangle(x1: Int, y1: Int, x2: Int, y2: Int, color: Color = Color.BLACK, override val fillColor: Option[Color] = None) extends Drawable {
  override def draw(g: Graphics, boundingBox: Option[Rectangle]): Unit = {
    boundingBox.foreach(g.setClip)
    fillColor match {
      case Some(fillCol) =>
        g.setColor(fillCol)
        g.fillRect(x1, y1, x2 - x1, y2 - y1)  // Fill Color
      case None =>
        g.setColor(color)
        g.drawRect(x1, y1, x2 - x1, y2 - y1)  // Outline
    }
  }
}

case class DrawCircle(x: Int, y: Int, radius: Int, color: Color = Color.BLACK, override val fillColor: Option[Color] = None) extends Drawable {
  override def draw(g: Graphics, boundingBox: Option[Rectangle]): Unit = {
    boundingBox.foreach(g.setClip)
    fillColor match {
      case Some(fillCol) =>
        g.setColor(fillCol)
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius)  // Fill Color
      case None =>
        g.setColor(color)
        g.drawOval(x - radius, y - radius, 2 * radius, 2 * radius)  // Outline
    }
  }
}

case class DrawTextAt(x: Int, y: Int, text: String, color: Color = Color.BLACK, override val fillColor: Option[Color] = None) extends Drawable {
  override def draw(g: Graphics, boundingBox: Option[Rectangle]): Unit = {
    g.setFont(new Font("SansSerif", Font.PLAIN, 16))
    g.setColor(color)
    boundingBox.foreach(g.setClip)
    g.drawString(text, x, y)
  }
}

case class DrawBoundingBox(x1: Int, y1: Int, x2: Int, y2: Int) {
  def toRectangle: Rectangle = new Rectangle(x1, y1, x2 - x1, y2 - y1)
}

class GraphicsPanel extends JPanel {
  private var drawables: List[Drawable] = List()
  private var boundingBox: Option[Rectangle] = None

  def addDrawable(d: Drawable): Unit = {
    drawables ::= d
    repaint()
  }

  def clearDrawables(): Unit = {
    drawables = List()
  }

  def setBoundingBox(x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
    boundingBox = Some(DrawBoundingBox(x1, y1, x2, y2).toRectangle)
  }

  override def paintComponent(g: Graphics): Unit = {
    super.paintComponent(g)
    boundingBox.foreach(g.setClip)
    drawables.foreach(_.draw(g, boundingBox))
  }
}

object CommandParser {
  private val colorMap: Map[String, Color] = Map(
    "black" -> Color.BLACK,
    "red" -> Color.RED,
    "green" -> Color.GREEN,
    "blue" -> Color.BLUE,
    "yellow" -> Color.YELLOW
  )

  private val boundingBoxRegex = """\(BOUNDING-BOX\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)\)""".r
  private val lineRegex = """\(LINE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)\)""".r
  private val rectangleRegex = """\(RECTANGLE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*\(\s*(\d+)\s+(\d+)\s*\)\)""".r
  private val circleRegex = """\(CIRCLE\s*\(\s*(\d+)\s+(\d+)\s*\)\s*(\d+)\)""".r
  private val textAtRegex = """\(TEXT-AT\s*\(\s*(\d+)\s+(\d+)\s*\)\s*"([^"]*)"\)""".r
  private val drawCommandRegex = """\(DRAW\s+(\w+)\s+(.+)\)""".r
  private val fillCommandRegex = """\(FILL\s+(\w+)\s+(.+)\)""".r

  private def normalizeInput(input: String): String = input.trim.replaceAll("\\s{2,}", " ")

  private def parseColor(color: String): Color = colorMap.getOrElse(color.toLowerCase, Color.BLACK)

  def parseCommand(commands: String, canvas: GraphicsPanel): Unit = {
    commands.split("\n").foreach { cmd =>
      val normalizedCommand = normalizeInput(cmd)
      normalizedCommand match {
        case boundingBoxRegex(x1, y1, x2, y2) =>
          canvas.setBoundingBox(x1.toInt, y1.toInt, x2.toInt, y2.toInt)
        case drawCommandRegex(color, sub_command) =>
          val drawColor = parseColor(color)
          parseDrawCommands(sub_command, canvas, drawColor)
        case fillCommandRegex(color, sub_command) =>
          val fillColor = parseColor(color)
          parseFillCommand(sub_command, canvas, fillColor)
        case _ =>
          // Parse Single Commands [ Line, Rect, Circle, Text ]
          // Set Default Color: Black
          parseSingleCommand(normalizedCommand, canvas, Color.BLACK)
      }
    }
  }

  private def parseDrawCommands(commands: String, canvas: GraphicsPanel, color: Color): Unit = {
    commands.split(";").foreach { command =>
      parseSingleCommand(command.trim, canvas, color)
    }
  }

  private def parseFillCommand(command: String, canvas: GraphicsPanel, color: Color): Unit = {
    val normalizedCommand = normalizeInput(command)
    normalizedCommand match {
      case rectangleRegex(x1, y1, x2, y2) =>
        canvas.addDrawable(DrawRectangle(x1.toInt, y1.toInt, x2.toInt, y2.toInt, color, Some(color)))
      case circleRegex(x, y, radius) =>
        canvas.addDrawable(DrawCircle(x.toInt, y.toInt, radius.toInt, color, Some(color)))
      case _ =>
        println(s"Invalid Command @ FILL -> : $command")
    }
  }


  private def parseSingleCommand(command: String, canvas: GraphicsPanel, color: Color): Unit = {
    command match {
      case lineRegex(x1, y1, x2, y2) =>
        canvas.addDrawable(DrawLine(x1.toInt, y1.toInt, x2.toInt, y2.toInt, color))
      case rectangleRegex(x1, y1, x2, y2) =>
        canvas.addDrawable(DrawRectangle(x1.toInt, y1.toInt, x2.toInt, y2.toInt, color))
      case circleRegex(x, y, radius) =>
        canvas.addDrawable(DrawCircle(x.toInt, y.toInt, radius.toInt, color))
      case textAtRegex(x, y, text) =>
        canvas.addDrawable(DrawTextAt(x.toInt, y.toInt, text, color))
      case _ =>
        println(s"Invalid Command: $command")
    }
  }
}
