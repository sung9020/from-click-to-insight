package fromclicktoinsight.producer.service

import fromclicktoinsight.domain.PurchaseEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, PurchaseEvent>,
    @Value("\${app.kafka.topic}") private val topic: String,
){
    fun publish(event: PurchaseEvent) {
        kafkaTemplate.send(topic, event.eventId, event)
    }

}