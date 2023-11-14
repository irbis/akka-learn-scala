package com.github.irbis.akkalearnapp

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object GreeterActor {
  final case class GreetMessage(whom: String, replyTo: ActorRef[GreetedMessage])
  final case class GreetedMessage(whom: String, from: ActorRef[GreetMessage])

  def apply(): Behavior[GreetMessage] = Behaviors.receive { (context, message) =>
    context.log.info("Hello {}!", message.whom)
    // message.replyTo (bang) GreetedMessage(message.whom, context.self)
    message.replyTo ! GreetedMessage(message.whom, context.self)
    Behaviors.same
  }
}

object GreeterBotActor {
  def apply(max: Int): Behavior[GreeterActor.GreetedMessage] = bot(0, max)

  private def bot(greetingCounter: Int, max: Int): Behavior[GreeterActor.GreetedMessage] =
    Behaviors.receive { (context, message) =>
      val n = greetingCounter + 1
      context.log.info("Greeting {} for {}", n, message.whom)
      if (n == max) {
        Behaviors.stopped
      } else {
        message.from ! GreeterActor.GreetMessage(message.whom, context.self)
        bot(n, max)
      }
    }
}

object GreeterMainActor {
  final case class SayHello(name: String)

  def apply() : Behavior[SayHello] =
    Behaviors.setup { context =>
      // In Akka you can't create an instance of an Actor using the new
      // keyword. Instead, you create Actor instances using a factory
      // spawn (породити) method. Spawn does not return an actor instance,
      // but a reference, akka.actor.typed.ActorRef, that points to the
      // actor instance. This level of indirection adds a lot of power and
      // flexibility in a distributed system.
      val greeterActor = context.spawn(GreeterActor(), "greeter")

      Behaviors.receiveMessage { message =>
        val replyTo = context.spawn(GreeterBotActor(max = 3), message.name)
        greeterActor ! GreeterActor.GreetMessage(message.name, replyTo)
        Behaviors.same
      }
    }
}

object AkkaLearnApp extends App {
  val greeterMainActor: ActorSystem[GreeterMainActor.SayHello] = ActorSystem(GreeterMainActor(), "AkkaLearnApp")

  // To put a message into an Actor's mailbox, use the ! (bang) method
  // of ActorRef.
  // ( bang a message - надіслати повідомлення )
  greeterMainActor ! GreeterMainActor.SayHello("Charles")
}
