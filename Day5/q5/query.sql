SELECT b.job_id,d.dept_name
FROM background_jobs b
INNER JOIN departments d
ON b.dept_id=d.dept_id
WHERE b.status='PENDING'
AND d.dept_name='Engineering'
ORDER BY b.created_at
LIMIT 1
FOR UPDATE SKIP LOCKED;