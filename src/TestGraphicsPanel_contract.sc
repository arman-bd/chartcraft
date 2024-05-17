// #Sireum #Logika
import org.sireum._

@datatype trait Drawable {
  def draw(): ISZ[(Z, Z)]
}

@datatype class DrawLine(x1: Z, y1: Z, x2: Z, y2: Z) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    Contract(
      Requires(x1 >= 0 & y1 >= 0 & x2 >= 0 & y2 >= 0),
      Ensures(Res.size == 3, Res(0) == (x1, y1), Res(2) == (x2, y2))
    )
    val coordinates: ISZ[(Z, Z)] = ISZ((x1, y1), (x1 + 1, y1 + 1), (x2, y2))
    return coordinates
  }
}

@datatype class DrawRectangle(x1: Z, y1: Z, x2: Z, y2: Z) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    Contract(
      Requires(x1 >= 0 & y1 >= 0 & x2 >= 0 & y2 >= 0),
      Ensures(Res.size == 3, Res(0) == (x1, y1), Res(2) == (x1, y2))
    )
    val coordinates: ISZ[(Z, Z)] = ISZ((x1, y1), (x2, y1), (x1, y2))
    return coordinates
  }
}

@datatype class DrawCircle(x: Z, y: Z, radius: Z) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    Contract(
      Requires(x >= 0 & y >= 0 & radius > 0),
      Ensures(Res.size == 3, Res(0) == (x, y), Res(1) == (x + radius, y))
    )
    val coordinates: ISZ[(Z, Z)] = ISZ((x, y), (x + radius, y), (x, y + radius))  // Limiting to 3 points for simplicity
    return coordinates
  }
}

@datatype class DrawTextAt(x: Z, y: Z, text: String) extends Drawable {
  override def draw(): ISZ[(Z, Z)] = {
    Contract(
      Requires(x >= 0 & y >= 0), // Ensure coordinates are non-negative
      Ensures(Res.size == 1, Res(0) == (x, y))
    )
    val coordinates: ISZ[(Z, Z)] = ISZ((x, y))
    return coordinates
  }
}

DrawLine(12, 23, 14, 25)
DrawRectangle(10, 10, 12, 12)
DrawCircle(50, 50, 1)
DrawTextAt(20, 30, "Hello")
