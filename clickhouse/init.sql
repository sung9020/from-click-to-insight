-- ============================================================
-- 메인 테이블: purchase
-- ============================================================
CREATE TABLE IF NOT EXISTS purchase (
    event_id       String,
    timestamp      DateTime64(3),
    user_id        UInt64,
    gender         String,
    age_group      String,
    region         String,
    category       String,
    sub_category   String,
    color          String,
    size           String,
    brand          String,
    price          Decimal(10, 2),
    original_price Decimal(10, 2),
    discount_rate  UInt8,
    quantity       UInt16,
    payment_method String,
    platform       String,
    is_first_purchase UInt8,
    event_date     Date DEFAULT toDate(timestamp)
    ) ENGINE = MergeTree()
    PARTITION BY event_date
    ORDER BY (gender, age_group, category, timestamp)
    TTL event_date + INTERVAL 90 DAY;


-- ============================================================
-- Materialized View 1: 성별/연령대/카테고리별 실시간 집계
-- ============================================================
CREATE MATERIALIZED VIEW IF NOT EXISTS purchase_demographics_mv
       ENGINE = SummingMergeTree()
ORDER BY (event_date, gender, age_group, category)
AS SELECT
              toDate(timestamp) as event_date,
              gender,
              age_group,
              category,
              count()              as purchase_count,
              sum(price * quantity) as total_revenue,
              sum(quantity)        as total_quantity
   FROM purchase
   GROUP BY event_date, gender, age_group, category;


-- ============================================================
-- Materialized View 2: 색상 선호도 집계
-- ============================================================
CREATE MATERIALIZED VIEW IF NOT EXISTS color_preference_mv
ENGINE = SummingMergeTree()
ORDER BY (event_date, gender, age_group, color)
AS SELECT
              toDate(timestamp) as event_date,
              gender,
              age_group,
              color,
              count() as purchase_count
   FROM purchase
   GROUP BY event_date, gender, age_group, color;


-- ============================================================
-- Materialized View 3: 브랜드별 집계
-- ============================================================
CREATE MATERIALIZED VIEW IF NOT EXISTS brand_stats_mv
ENGINE = SummingMergeTree()
ORDER BY (event_date, brand, category)
AS SELECT
              toDate(timestamp) as event_date,
              brand,
              category,
              count()              as purchase_count,
              sum(price * quantity) as total_revenue
   FROM purchase
   GROUP BY event_date, brand, category;