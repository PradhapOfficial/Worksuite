USE worksuite;

DELIMITER $$
CREATE TRIGGER IF NOT EXISTS Generate_Account_PK BEFORE INSERT ON Accounts
FOR EACH ROW
BEGIN
    DECLARE generatedValue BIGINT;  
    CALL Generate_PK_Procedure('Accounts', generatedValue);
    SET NEW.ACCOUNT_ID = generatedValue;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER IF NOT EXISTS Generate_User_PK BEFORE INSERT ON User
FOR EACH ROW
BEGIN
    DECLARE generatedValue BIGINT;  
    CALL Generate_PK_Procedure('User', generatedValue);
    SET NEW.USER_ID = generatedValue;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER IF NOT EXISTS Generate_Org_PK BEFORE INSERT ON Organization
FOR EACH ROW
BEGIN
    DECLARE generatedValue BIGINT;  
    CALL Generate_PK_Procedure('Organization', generatedValue);
    SET NEW.ORG_ID = generatedValue;
END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER IF NOT EXISTS Generate_Role_PK BEFORE INSERT ON Role
FOR EACH ROW
BEGIN
    DECLARE generatedValue BIGINT;  
    CALL Generate_PK_Procedure('Role', generatedValue);
    SET NEW.ROLE_ID = generatedValue;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE IF NOT EXISTS Generate_PK_Procedure(IN tableName VARCHAR(255), OUT generatedValue BIGINT)
BEGIN
    CASE tableName
        WHEN 'Accounts' THEN
            SET generatedValue = FLOOR(600000 + RAND() * (999999 - 600000));
        
        WHEN 'User' THEN
            SET generatedValue = FLOOR(60000000 + RAND() * (99999999 - 60000000));
        
        WHEN 'Organization' THEN
            SET generatedValue = FLOOR(6000000 + RAND() * (9999999 - 6000000));
        
        WHEN 'Role' THEN
            SET generatedValue = FLOOR(600000000 + RAND() * (999999999 - 600000000));
        
        WHEN 'Scope' THEN
            SET generatedValue = FLOOR(6000000000 + RAND() * (9999999999 - 6000000000));
        
        WHEN 'Integration' THEN
            SET generatedValue = FLOOR(60000000000 + RAND() * (99999999999 - 60000000000));
        
        WHEN 'Department' THEN
            SET generatedValue = FLOOR(600000000000 + RAND() * (999999999999 - 600000000000));
        
        WHEN 'DepartmentUserMapping' THEN
            SET generatedValue = FLOOR(60000000000000 + RAND() * (99999999999999 - 60000000000000));
       
        WHEN 'IntegrationDepartmentMapping' THEN
            SET generatedValue = FLOOR(600000000000000 + RAND() * (999999999999999 - 60000000000000));
       
        WHEN 'IntegrationUserMapping' THEN
            SET generatedValue = FLOOR(6000000000000000 + RAND() * (9999999999999999 - 60000000000000));
       
        WHEN 'OrganizationUserMapping' THEN
            SET generatedValue = FLOOR(60000000000000000 + RAND() * (99999999999999999 - 600000000000000));
       
        WHEN 'Auth' THEN
            SET generatedValue = FLOOR(600000000000000000 + RAND() * (999999999999999999 - 6000000000000000));
    END CASE;
END$$
DELIMITER ;