package fromclicktoinsight.producer.service

import fromclicktoinsight.domain.PurchaseEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@Service
class PurchaseEventGenerator(
    private val publisher: EventPublisher,
) {
    val running = AtomicBoolean(false)
    val totalCount = AtomicLong(0)

    @Scheduled(fixedRateString = "\${app.generator.rate-ms}")
    fun generate(){
        if(!running.get()) return
        val event = randomEvent()
        publisher.publish(event)
        totalCount.incrementAndGet()
    }

    fun start() { running.set(true) }
    fun stop() { running.set(false) }

    fun randomEvent(): PurchaseEvent {
        val random = ThreadLocalRandom.current()

        val gender = weightedChoice(listOf("Male" to 45, "Female" to 55))
        val ageGroup = weightedChoice(listOf("10s" to 10, "20s" to 35, "30s" to 30, "40s" to 20, "50s" to 5))
        val category = listOf("상의", "하의", "아우터", "원피스", "신발", "가방", "액세서리").random()
        val subCategory = subCategoryMap[category]!!.random()
        val color = listOf("블랙", "화이트", "네이비", "베이지", "그레이",
            "브라운", "레드", "핑크", "블루", "그린").random()
        val size = listOf("XS", "S", "M", "L", "XL", "XXL", "FREE").random()
        val region = listOf("서울", "경기", "부산", "대구", "인천", "광주", "대전", "제주").random()
        val paymentMethod = listOf("card", "cash", "point", "mobile", "bank_transfer").random()
        val platform = listOf("app_ios", "app_android", "web", "mobile_web").random()
        val brand = brands.random()

        val discountRate = random.nextInt(0,71)

        val originalPrice = BigDecimal(random.nextInt(10000, 500001))
            .setScale(0, RoundingMode.FLOOR)

        val price = originalPrice
            .multiply(BigDecimal(100 - discountRate))
            .divide(BigDecimal(100), 0, RoundingMode.FLOOR)

        return PurchaseEvent(
            eventId = UUID.randomUUID().toString(),
            timestamp = LocalDateTime.now(),
            userId = random.nextLong(1, 50001),
            gender = gender,
            ageGroup = ageGroup,
            region = region,
            category = category,
            subCategory = subCategory,
            color = color,
            size = size,
            brand = brand,
            price = price,
            originalPrice = originalPrice,
            discountRate = discountRate,
            quantity = random.nextInt(1, 6),
            paymentMethod = paymentMethod,
            platform = platform,
            isFirstPurchase = random.nextInt(100) < 15,
        )
    }

    private fun weightedChoice(items: List<Pair<String, Int>>): String {
        val total = items.sumOf { it.second }
        var rand = ThreadLocalRandom.current().nextInt(total)
        for ((value, weight) in items) {
            rand -= weight
            if (rand < 0) return value
        }
        return items.last().first
    }


    val subCategoryMap = mapOf(
        "상의" to listOf("티셔츠", "셔츠", "블라우스", "니트", "맨투맨", "후드"),
        "하의" to listOf("청바지", "슬랙스", "면바지", "반바지", "레깅스", "치마"),
        "아우터" to listOf("자켓", "코트", "패딩", "가디건", "바람막이", "점퍼"),
        "원피스" to listOf("미니원피스", "롱원피스", "셔츠원피스", "니트원피스"),
        "신발" to listOf("스니커즈", "구두", "샌들", "부츠", "슬리퍼", "로퍼"),
        "가방" to listOf("백팩", "토트백", "크로스백", "클러치", "숄더백"),
        "액세서리" to listOf("모자", "머플러", "벨트", "양말", "선글라스", "시계", "쥬얼리")
    )


    val brands = listOf(
        "알꼴라 스탠다드", "별의컵이다", "디즈이즈아이스", "사자비", "조지아스크래퍼",
        "폴루 루돌프렌", "나이스", "삼선디다스", "뉴브랜드", "콤포트",
    )
}