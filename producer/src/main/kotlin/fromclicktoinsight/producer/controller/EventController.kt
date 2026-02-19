package fromclicktoinsight.producer.controller

import fromclicktoinsight.producer.service.EventPublisher
import fromclicktoinsight.producer.service.PurchaseEventGenerator
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("v1/api/events")
class EventController(
    private val generator: PurchaseEventGenerator,
    private val publisher: EventPublisher,
) {
    @PostMapping("/generate")
    fun generate(): Map<String, Any> {
        val event = generator.randomEvent()
        publisher.publish(event)
        generator.totalCount.incrementAndGet()
        return mapOf("status" to "ok", "eventId" to event.eventId)
    }

    @PostMapping("/bulk")
    fun bulk(@RequestParam count: Int = 1000): Map<String, Any> {
        repeat(count) {
            val event = generator.randomEvent()
            publisher.publish(event)
            generator.totalCount.incrementAndGet()
        }
        return mapOf("status" to "ok", "count" to count)
    }

    @PostMapping("/auto/start")
    fun start(): Map<String, String> {
        generator.start()
        return mapOf("status" to "started")
    }

    @PostMapping("/auto/stop")
    fun stop(): Map<String, String> {
        generator.stop()
        return mapOf("status" to "stopped")
    }

    @GetMapping("/stats")
    fun stats(): Map<String, Any> {
        return mapOf(
            "totalCount" to generator.totalCount.get(),
            "running" to generator.running.get(),
        )
    }
}