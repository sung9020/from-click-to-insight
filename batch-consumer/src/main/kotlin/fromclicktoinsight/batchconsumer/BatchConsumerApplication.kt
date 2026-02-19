package fromclicktoinsight.batchconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchConsumerApplication

fun main(args: Array<String>) {
    runApplication<BatchConsumerApplication>(*args)
}
