import cats.effect._
import fs2._
import org.http4s.Uri
import org.http4s.client.Client
import org.slf4j.LoggerFactory

import cats._, cats.implicits._, cats.syntax._, cats.syntax.either._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class Input(uri: Uri)(implicit val ec: ExecutionContext) {
  val log = LoggerFactory.getLogger(getClass)

  def go[E[_]: Effect](s: Scheduler, c: Client[E]): Stream[E, String] = {
    s.awakeEvery(10.seconds).flatMap { d: FiniteDuration =>
      Stream.attemptEval(c.expect[String](uri))
    }.flatMap{x =>
      x.leftMap(e => log.warn("Unable to get data", e)).toOption.map(Stream.emit).getOrElse(Stream.empty)
    }.zipWithPrevious.flatMap { _ match {
      case (None, c) =>
        log.info("Letting first item through")
        Stream.emit(c)
      case (Some(p), c) if p != c =>
        log.info("Result changed, letting it through")
        Stream.emit(c)
      case _ =>
        log.info("Dropping dupe")
        Stream.empty
    }}
  }

}
