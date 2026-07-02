SELECT h.asset_class, SUM(h.current_value) AS total_value
FROM investors i
INNER JOIN holdings h
ON i.investor_id = h.investor_id
WHERE i.investor_id = ?
GROUP BY h.asset_class;