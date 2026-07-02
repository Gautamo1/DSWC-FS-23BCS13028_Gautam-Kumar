CREATE INDEX idx_gps_rider_time
ON gps_pings(rider_id, recorded_at DESC);