package fromclicktoinsight.streamconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StreamConsumerApplication

fun main(args: Array<String>) {
    runApplication<StreamConsumerApplication>(*args)
}
