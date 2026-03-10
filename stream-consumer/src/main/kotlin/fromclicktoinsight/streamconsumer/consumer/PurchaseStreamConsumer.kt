package fromclicktoinsight.streamconsumer.consumer

import fromclicktoinsight.domain.PurchaseEvent
import fromclicktoinsight.streamconsumer.repository.ClickHouseRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class PurchaseStreamConsumer(
    private val repository: ClickHouseRepository,
) {
    @KafkaListener(
        topics = ["\${app.kafka.topic}"],
        groupId = "\${spring.kafka.consumer.group-id}",
    )
    fun consume(
        record: ConsumerRecord<String, PurchaseEvent>,
        ack: Acknowledgment,
    ) {
        repository.setOnFlushSuccess { ack.acknowledge() }
        repository.addToBuffer(record.value())
    }
}
