DELIMITER //

CREATE PROCEDURE getEmployeeName(IN emp_id VARCHAR(50))  -- Accepts a string (bad practice)
BEGIN
    SET @query = CONCAT('SELECT name FROM employees WHERE id = ', emp_id);  -- ❌ Dynamic SQL with user input
    PREPARE stmt FROM @query;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END //

DELIMITER ;
