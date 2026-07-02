CREATE INDEX idx_pending_jobs
ON background_jobs(created_at)
WHERE status='PENDING';