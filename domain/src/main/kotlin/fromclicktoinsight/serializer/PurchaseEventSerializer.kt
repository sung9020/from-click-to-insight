package fromclicktoinsight.serializer

import fromclicktoinsight.config.JsonUtils
import fromclicktoinsight.domain.PurchaseEvent
import org.apache.kafka.common.serialization.Serializer

class PurchaseEventSerializer : Serializer<PurchaseEvent> {
    override fun serialize(topic: String?, data: PurchaseEvent?): ByteArray? {
        return data?.let { JsonUtils.objectMapper.writeValueAsBytes(it) }
    }
}