DELETE FROM staging.staging WHERE location = '' OR location IS NULL; 
DELETE FROM staging.staging
WHERE COALESCE(DATE, '') = '' OR DATE = 0;
UPDATE staging.staging
SET status = 'Bầu trời quang đãng'
WHERE status = '' OR status IS NULL;
UPDATE staging.staging
SET low = (
    SELECT MIN(low)
    FROM staging.staging
    WHERE low <> 0
) 
WHERE low IS NULL OR low = 0;

UPDATE staging.staging
SET high = (
    SELECT MAX(high)
    FROM staging.staging
    WHERE high <> 0
)
WHERE high IS NULL OR high = 0;
UPDATE staging.staging
SET humidity = 70
WHERE COALESCE(humidity, '') = '' OR humidity = 0;
UPDATE staging.staging
SET precipitation = 
    CASE 
        WHEN status = 'Mưa vừa' THEN 8
        WHEN status = 'Mưa to' THEN 25
        WHEN status = 'Mưa rất to' THEN 50
        WHEN status IN ('Mưa cụm', 'Mưa nhẹ', 'Mây rải rác') THEN 3
        ELSE precipitation  -- Giữ nguyên giá trị nếu không khớp với các điều kiện trên
    END
WHERE precipitation IS NULL;
UPDATE staging.staging
SET average_temp = (CAST(low AS FLOAT) + CAST(high AS FLOAT)) / 2
WHERE (average_temp = 0 OR average_temp = 0) AND low <> 0 AND high <> 0;
UPDATE staging.staging
SET staging.day = high
WHERE (staging.day = null OR staging.day = 0) AND high <> 0;
UPDATE staging.staging
SET night= low
WHERE (night= null OR night = 0) AND low<> 0;
UPDATE staging.staging
SET morning = high
WHERE (morning= null OR morning = 0) AND high <> 0;
UPDATE staging.staging
SET evening= low
WHERE (evening= null OR evening = 0 ) AND low<> 0;
UPDATE staging.staging
SET pressure= 760
WHERE COALESCE(pressure, '') = '' OR pressure= 0;
UPDATE staging.staging
SET wind= 5.5
WHERE COALESCE(wind, '') = '' OR wind= 0;
UPDATE staging.staging
SET sunrise = COALESCE(sunrise, '05:30:00')
WHERE sunrise IS NULL;
UPDATE staging.staging
SET sunset= COALESCE(sunset, '05:30:00')
WHERE sunset IS NULL;