CREATE TABLE IF NOT EXISTS Accounts(
    ACCOUNT_ID BIGINT PRIMARY KEY,
    USER_NAME VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR(100) NOT NULL,
    INDEX Accounts_User_Name_IDX (USER_NAME)
);


DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS generate_custom_id(IN table_name VARCHAR(255), OUT custom_id BIGINT)
BEGIN
    -- Check the table name and generate custom ID accordingly
    CASE table_name
        WHEN 'Accounts' THEN
            -- Your custom logic for my_table
            SET custom_id = FLOOR(RAND()*(99999999-11111111));
        WHEN 'another_table' THEN
            -- Your custom logic for another_table
            SET custom_id = 67890; -- Replace with your custom logic
        ELSE
            -- Default logic or error handling
            SET custom_id = 0; -- Default value or handle error
    END CASE;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER IF NOT EXISTS before_insert_my_table
BEFORE INSERT ON Accounts
FOR EACH ROW
BEGIN
    DECLARE generated_id BIGINT;
    
    -- Call the stored procedure to generate the custom ID
    CALL generate_custom_id('Accounts', generated_id);
    
    -- Set the generated ID as the value for the primary key column
    SET NEW.ACCOUNT_ID = generated_id;
END$$
DELIMITER ;
