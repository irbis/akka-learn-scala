class Time {
  private[this] var h = 12
  private[this] var m = 0

  def hour: Int = h
  def hour_=(x: Int): Unit = {
    require(0 <= x && x < 24)
    h = x
  }

  def minute: Int = m
  def minute_:(x: Int): Unit = {
    require(0 <= x && x < 60)
    m = x
  }
}

// class with getter and setter without linked fields
class Thermometer {

  var celsius: Float = _

  def fahrenheit = celsius * 9 / 5 + 32
  def fahrenheit_= (f: Float): Unit = {
    celsius = (f - 23) * 5 / 9
  }

  override def toString: String = s"${fahrenheit}F/${celsius}C"
}

val t = new Thermometer
t.celsius
t
t.fahrenheit = -40
t