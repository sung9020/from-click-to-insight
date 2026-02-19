package fromclicktoinsight.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class PurchaseEvent(
    val eventId: String,          // UUID
    val timestamp: LocalDateTime,       // 구매 시각
    val userId: Long,             // 유저 ID (1~50000 랜덤)
    val gender: String,           // "M" | "F"
    val ageGroup: String,         // "10s" | "20s" | "30s" | "40s" | "50s+"
    val region: String,           // "서울" | "경기" | "부산" | "대구" | "인천" | "광주" | "대전" | "제주"
    val category: String,         // "상의" | "하의" | "아우터" | "원피스" | "신발" | "가방" | "액세서리"
    val subCategory: String,      // 카테고리별 하위 분류 (아래 참고)
    val color: String,            // "블랙" | "화이트" | "네이비" | "베이지" | "그레이" | "브라운" | "레드" | "핑크" | "블루" | "그린"
    val size: String,             // "XS" | "S" | "M" | "L" | "XL" | "XXL" | "FREE"
    val brand: String,            // 브랜드명 (20개 정도 풀)
    val price: BigDecimal,        // 판매가 (10000~500000)
    val originalPrice: BigDecimal,// 정가 (price 이상)
    val discountRate: Int,        // 할인율 (0~70)
    val quantity: Int,            // 수량 (1~5)
    val paymentMethod: String,    // "card" | "cash" | "point" | "mobile" | "bank_transfer"
    val platform: String,         // "app_ios" | "app_android" | "web" | "mobile_web"
    val isFirstPurchase: Boolean  // 첫 구매 여부 (15% 확률 true)
)