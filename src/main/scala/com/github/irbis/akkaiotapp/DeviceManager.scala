package com.github.irbis.akkaiotapp

import akka.actor.typed.scaladsl.{AbstractBehavior, ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior, PostStop, Signal}

object DeviceManager {

  sealed trait Command

  final case class RequestTrackDevice(
                                       groupId: String,
                                       deviceId: String,
                                       replyTo: ActorRef[DeviceRegistered])
      extends DeviceManager.Command
      with DeviceGroup.Command

  final case class DeviceRegistered(device: ActorRef[Device.Command])

  final case class RequestDeviceList(requestId: Long,
                                     groupId: String,
                                     replyTo: ActorRef[ReplyDeviceList])
      extends DeviceManager.Command
      with DeviceGroup.Command

  final case class ReplyDeviceList(requestId: Long,
                                   ids: Set[String])

  private final case class DeviceGroupTerminated(groupId: String)
      extends DeviceManager.Command

  final case class RequestAllTemperatures(requestId: Long,
                                          groupId: String,
                                          replyTo: ActorRef[RespondAllTemperatures])
      extends DeviceGroupQuery.Command
      with DeviceGroup.Command
      with DeviceManager.Command

  final case class RespondAllTemperatures(requestId: Long,
                                          temperatures: Map[String, TemperatureReading])

  sealed trait TemperatureReading
  final case class Temperature(value: Double) extends TemperatureReading
  case object TemperatureNotAvailable extends TemperatureReading
  case object DeviceNotAvailable extends TemperatureReading
  case object DeviceTimedOut extends TemperatureReading

  def apply(): Behavior[Command] =
    Behaviors.setup(context => new DeviceManager(context))
}

class DeviceManager(context: ActorContext[DeviceManager.Command])
    extends AbstractBehavior[DeviceManager.Command](context) {
  import DeviceManager._

  private var groupIdToActor = Map.empty[String, ActorRef[DeviceGroup.Command]]

  context.log.info("DeviceManager started")

  override def onMessage(msg: Command): Behavior[Command] =
    msg match {
      case trackMsg @ RequestTrackDevice(groupId, _, _) =>
        groupIdToActor.get(groupId) match {
          case Some(deviceGroup) =>
            deviceGroup ! trackMsg
          case None =>
            context.log.info("Creating device group actor for {}", groupId)
            val groupActor = context.spawn(DeviceGroup(groupId), "group-" + groupId)
            context.watchWith(groupActor, DeviceGroupTerminated(groupId))
            groupActor ! trackMsg
            groupIdToActor += groupId -> groupActor
        }
        this

      case req @ RequestDeviceList(requestId, groupId, replyTo) =>
        groupIdToActor.get(groupId) match {
          case Some(deviceGroup) =>
            deviceGroup ! req
          case None =>
            replyTo ! ReplyDeviceList(requestId, Set.empty)
        }
        this

      case DeviceGroupTerminated(groupId) =>
        context.log.info("Device group actor for {} has been terminated", groupId)
        groupIdToActor -= groupId
        this
    }

  override def onSignal: PartialFunction[Signal, Behavior[Command]] = {
    case PostStop =>
      context.log.info("DeviceManager stopped")
      this
  }
}
