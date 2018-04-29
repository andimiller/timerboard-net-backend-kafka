import java.util.Properties

import cats.effect.{Effect, IO}
import fs2._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import scala.concurrent.Future
import cats._, cats.implicits._, cats.syntax._

object Producer {

  def kafka[E[_] : Effect](topic: String) = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("acks", "all")
    props.put("retries", new Integer(0))
    props.put("batch.size", new Integer(16384))
    props.put("linger.ms", new Integer(1))
    props.put("buffer.memory", "33554432")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val prod = new KafkaProducer[String, String](props)
    Sink[E, String] { x: String =>
      Effect[E].delay(prod.send(new ProducerRecord[String, String](topic, x)).get()).map(_ => ())
    }
  }

}
