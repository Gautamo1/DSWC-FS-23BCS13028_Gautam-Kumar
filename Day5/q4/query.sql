SELECT rider_name,bike_model,latitude,longitude,recorded_at
FROM (
    SELECT r.rider_name,
           r.bike_model,
           g.latitude,
           g.longitude,
           g.recorded_at,
           ROW_NUMBER() OVER(
               PARTITION BY g.rider_id
               ORDER BY g.recorded_at DESC
           ) AS rn
    FROM riders r
    INNER JOIN gps_pings g
    ON r.rider_id = g.rider_id
) t
WHERE rn = 1;