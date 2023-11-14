package com.github.irbis.akkalearnapp

import akka.actor.typed.SupervisorStrategy
import akka.actor.typed.{ActorSystem, Behavior, PostStop, PreRestart, Signal}
import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}

object SupervisingActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new SupervisingActor(context))
}

class SupervisingActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  private val child = context.spawn(
    Behaviors.supervise(SupervisedActor()).onFailure(SupervisorStrategy.restart),
    name = "supervised-actor"
  )

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "failChild" =>
        child ! "fail"
        this
    }
}

object SupervisedActor {
  def apply(): Behavior[String] =
    Behaviors.setup(context => new SupervisedActor(context))
}

class SupervisedActor(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  println("supervised actor started")

  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "fail" =>
        println("supervised actor fails now")
        throw new Exception("I failed!")
    }

  override def onSignal: PartialFunction[Signal, Behavior[String]] = {
    case PreRestart =>
      println("supervised actor will be restarted")
      this
    case PostStop =>
      println("supervised actor stopped")
      this
  }
}

object MainActorHandlingExceptions {
  def apply(): Behavior[String] =
    Behaviors.setup(new MainActorHandlingExceptions(_))
}

class MainActorHandlingExceptions(context: ActorContext[String]) extends AbstractBehavior[String](context) {
  override def onMessage(msg: String): Behavior[String] =
    msg match {
      case "start" =>
        val supervisingActor = context.spawn(SupervisingActor(), "supervising-actor")
        supervisingActor ! "failChild"
        this
    }
}

object ActorHandlingExceptionsExperiments extends App {
  val actorHandlingExceptions = ActorSystem(MainActorHandlingExceptions(), "actorHanlingExceptions")
  actorHandlingExceptions ! "start"
}
