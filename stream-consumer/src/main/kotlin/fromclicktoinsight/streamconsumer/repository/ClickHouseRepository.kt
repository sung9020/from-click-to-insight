package fromclicktoinsight.streamconsumer.repository

import fromclicktoinsight.domain.PurchaseEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.concurrent.ConcurrentLinkedQueue

@Repository
class ClickHouseRepository(
    private val jdbcTemplate: JdbcTemplate,
    @Value("\${app.clickhouse.batch-size}") private val batchSize: Int,
) {
    private val buffer = ConcurrentLinkedQueue<PurchaseEvent>()
    private var onFlushSuccess: (() -> Unit)? = null

    fun setOnFlushSuccess(callback: () -> Unit) {
        onFlushSuccess = callback
    }

    fun addToBuffer(event: PurchaseEvent) {
        this.buffer.add(event)
        if (buffer.size >= batchSize) {
            flush()
        }
    }

    @Scheduled(fixedRateString = "\${app.clickhouse.flush-interval-ms}")
    fun scheduledFlush() {
        if (buffer.isNotEmpty()) {
            flush()
        }
    }

    @Synchronized
    private fun flush() {
        val batch = mutableListOf<PurchaseEvent>()
        while (true) {
            val event = buffer.poll() ?: break
            batch.add(event)
        }

        if (batch.isEmpty()) return

        val sql =
            """
            INSERT INTO purchase (
                event_id, timestamp, user_id, gender, age_group, region,
                category, sub_category, color, size, brand,
                price, original_price, discount_rate, quantity,
                payment_method, platform, is_first_purchase
            )  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent()

        jdbcTemplate.batchUpdate(sql, batch, batch.size) { ps, event ->
            ps.setString(1, event.eventId)
            ps.setTimestamp(2, Timestamp.valueOf(event.timestamp))
            ps.setLong(3, event.userId)
            ps.setString(4, event.gender)
            ps.setString(5, event.ageGroup)
            ps.setString(6, event.region)
            ps.setString(7, event.category)
            ps.setString(8, event.subCategory)
            ps.setString(9, event.color)
            ps.setString(10, event.size)
            ps.setString(11, event.brand)
            ps.setBigDecimal(12, event.price)
            ps.setBigDecimal(13, event.originalPrice)
            ps.setInt(14, event.discountRate)
            ps.setInt(15, event.quantity)
            ps.setString(16, event.paymentMethod)
            ps.setString(17, event.platform)
            ps.setInt(18, if (event.isFirstPurchase) 1 else 0)
        }

        onFlushSuccess?.invoke()
        println("[stream-consumer] ClickHouse에 ${batch.size}건 INSERT 완료 + offset 커밋")
    }
}
