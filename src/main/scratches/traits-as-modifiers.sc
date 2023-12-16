abstract class IntQueue {
  def get(): Int
  def put(x: Int): Unit
}

import scala.collection.mutable.ArrayBuffer

class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]
  override def get(): Int = buf.remove(0)
  override def put(x: Int): Unit = { buf += x }
}

val queue = new BasicIntQueue
queue.put(10)
queue.put(20)

queue.get()
queue.get()

trait Doubling extends IntQueue {
  // scalable modification - abstract override before method definition
  // super is expecting in another class - dynamic linking
  abstract override def put(x: Int) = { super.put(2 * x) }
}

class DoubleQueue extends BasicIntQueue with Doubling
val doubleQueue = new DoubleQueue

doubleQueue.put(10)
doubleQueue.get()

// no need to define empty class like DoubleQueue
val defDoubleQueue = new BasicIntQueue with Doubling
defDoubleQueue.put(10)
defDoubleQueue.get()

trait Incrementing extends IntQueue {
  abstract override def put(x: Int) = { super.put(x + 1) }
}
trait Filtering extends IntQueue {
  abstract override def put(x: Int) = {
    if (x >= 0) super.put(x)
  }
}

val filterIncQueue = (new BasicIntQueue with Incrementing with Filtering)
filterIncQueue.put(-1); filterIncQueue.put(0); filterIncQueue.put(1)
filterIncQueue.get()
filterIncQueue.get()

val incFilterQueue = (new BasicIntQueue with Filtering with Incrementing)
incFilterQueue.put(-1); incFilterQueue.put(0); incFilterQueue.put(1)
incFilterQueue.get()
incFilterQueue.get()
incFilterQueue.get()
