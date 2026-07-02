SELECT s.student_id, s.full_name
FROM students s
LEFT JOIN course_registrations c
ON s.student_id = c.student_id
WHERE c.student_id IS NULL;