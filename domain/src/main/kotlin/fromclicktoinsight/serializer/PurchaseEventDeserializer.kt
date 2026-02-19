package fromclicktoinsight.serializer

import fromclicktoinsight.config.JsonUtils
import fromclicktoinsight.domain.PurchaseEvent
import org.apache.kafka.common.serialization.Deserializer

class PurchaseEventDeserializer: Deserializer<PurchaseEvent> {
    override fun deserialize(topic: String?, data: ByteArray?): PurchaseEvent? {
        return data?.let { JsonUtils.objectMapper.readValue(it, PurchaseEvent::class.java) }
    }
}