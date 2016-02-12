package me.ciaranoconnor.api

import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl.{Flow, Sink, Source}
import me.ciaranoconnor.streams.flows.NumbersFlow
import scala.concurrent.forkjoin.ThreadLocalRandom

trait WebSockets extends Directives with NumbersFlow{

  def websocket = {
    path("randomNums") {
      //send the the websocket random numbers
      val src =
      Source.fromIterator(() => Iterator.continually(ThreadLocalRandom.current.nextInt()))
      .filter(i => i > 0 && i % 2 == 0).map(i => TextMessage(i.toString))

      val myFlow = Flow.fromSinkAndSource(Sink.ignore, numbersSource map {d => TextMessage.Strict(d.data)})

      //send the user the numbers 1 to 5
      //val src = Source(1 to 5).map(i => TextMessage(i.toString))
        //log.info("client connected")
        handleWebSocketMessages(myFlow)

    }
  }

}
