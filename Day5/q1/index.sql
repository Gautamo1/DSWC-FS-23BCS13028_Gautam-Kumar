CREATE INDEX idx_shipments_status_date
ON shipments(status, dispatch_date DESC);