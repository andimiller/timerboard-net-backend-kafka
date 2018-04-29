import cats.effect._
import fs2._
import org.http4s.Uri
import org.http4s.client.ConnectionManager
import org.http4s.client.blaze.{BlazeClient, BlazeClientConfig, Http1Client}
import org.http4s.server.blaze.BlazeBuilder

object InputMain extends StreamApp[IO] {
  val tq = Uri.unsafeFromString("https://esi.tech.ccp.is/latest/sovereignty/campaigns/?datasource=tranquility")

  override def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, StreamApp.ExitCode] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    for {
      s <- Scheduler[IO](4)
      client <- Http1Client.stream[IO](BlazeClientConfig.defaultConfig)
      data = new Input(tq).go[IO](s, client)
      sink = fs2.io.stdout[IO]
      transformer = fs2.text.utf8Encode[IO]
      main <- data.through(transformer).to(sink).filter(_ => false).flatMap(_ => Stream.emit(StreamApp.ExitCode.Success)) ++ Stream.emit(StreamApp.ExitCode.Success)
    } yield main
  }
}
