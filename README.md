SELECT * 
FROM (
    SELECT your_table.*, ROWNUM AS rn 
    FROM (
        SELECT * 
        FROM your_table
        ORDER BY your_column DESC
    ) 
    WHERE ROWNUM <= 2
)
WHERE rn = 2;
