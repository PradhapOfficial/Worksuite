-- CREATE DATABASE IF NOT EXISTS worksuite;
-- USE worksuite;

CREATE TABLE IF NOT EXISTS Accounts(
    ACCOUNT_ID BIGINT PRIMARY KEY,
    EMAIL_ID VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR(100) NOT NULL,
    INDEX Accounts_Email_Id_IDX (EMAIL_ID)
);

CREATE TABLE IF NOT EXISTS User(
    USER_ID BIGINT PRIMARY KEY,
    FIRST_NAME VARCHAR(50) NOT NULL,
    LAST_NAME VARCHAR(50),
    EMAIL_ID VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (EMAIL_ID) REFERENCES Accounts (EMAIL_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Organization(
    ORG_ID BIGINT PRIMARY KEY,
    ORG_NAME VARCHAR(50) NOT NULL,
    CREATED_BY BIGINT NOT NULL,
    MODIFIED_BY BIGINT NOT NULL,
    CREATED_TIME BIGINT NOT NULL,
    MODIFIED_TIME BIGINT NOT NULL,
    STATUS BOOLEAN NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS OrganizationUserMapping(
    ORG_ID BIGINT NOT NULL,
    USER_ID BIGINT NOT NULL,
    ROLE_ID BIGINT NOT NULL,
    FOREIGN KEY (ORG_ID) REFERENCES Organization (ORG_ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES User (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (ROLE_ID) REFERENCES Role (ROLE_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Role(
    ROLE_ID BIGINT PRIMARY KEY,
    ROLE_NAME VARCHAR(50) NOT NULL UNIQUE,
    ROLE_VALUE ENUM('1', '2', '3') NOT NULL UNIQUE DEFAULT 3
);


CREATE TABLE IF NOT EXISTS Department(
    DEPARTMENT_ID BIGINT PRIMARY KEY,
    DEPARTMENT_NAME VARCHAR(50) NOT NULL,
    CREATED_BY BIGINT NOT NULL,
    MODIFIED_BY BIGINT NOT NULL,
    CREATED_TIME BIGINT NOT NULL,
    MODIFIED_TIME BIGINT NOT NULL,
    STATUS BOOLEAN NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS DepartmentUserMapping(
    DEPARTMENT_ID BIGINT NOT NULL UNIQUE,
    USER_ID BIGINT NOT NULL UNIQUE,
    UNIQUE (DEPARTMENT_ID, USER_ID),
    FOREIGN KEY (DEPARTMENT_ID) REFERENCES Department (DEPARTMENT_ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES User (USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Integration(
    INTEGRATION_ID BIGINT PRIMARY KEY,
    APP_ID VARCHAR(50) NOT NULL,
    STATUS BOOLEAN NOT NULL,
    ORG_ID BIGINT NOT NULL,
    LEVEL ENUM('1', '2', '3') NOT NULL,
    CREATED_BY BIGINT NOT NULL,
    MODIFIED_BY BIGINT NOT NULL,
    CREATED_TIME BIGINT NOT NULL,
    MODIFIED_TIME BIGINT NOT NULL,
    FOREIGN KEY (ORG_ID) REFERENCES Organization (ORG_ID)
);

CREATE TABLE IF NOT EXISTS Scope(
    SCOPE_ID BIGINT PRIMARY KEY,
    APP_ID VARCHAR(50) NOT NULL,
    ORG_ID BIGINT NOT NULL,
    LEVEL ENUM('1', '2', '3') NOT NULL,
    CREATED_BY BIGINT NOT NULL,
    MODIFIED_BY BIGINT NOT NULL,
    CREATED_TIME BIGINT NOT NULL,
    MODIFIED_TIME BIGINT NOT NULL,
    FOREIGN KEY (ORG_ID) REFERENCES Organization (ORG_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Auth(
    AUTH_ID BIGINT PRIMARY KEY,
    SCOPE VARCHAR(50),
    TOKEN VARCHAR(250),
    INTEGRATION_ID BIGINT NOT NULL,
    FOREIGN KEY (INTEGRATION_ID) REFERENCES Integration (INTEGRATION_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS IntegrationUserMapping(
    INTEGRATION_USER_MAPPING_ID BIGINT PRIMARY KEY,
    USER_ID BIGINT NOT NULL,
    INTEGRATION_ID BIGINT NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES User (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (INTEGRATION_ID) REFERENCES Integration (INTEGRATION_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS IntegrationDepartmentMapping(
    INTEGRATION_DEPARTMENT_MAPPING_ID BIGINT PRIMARY KEY,
    DEPARTMENT_ID BIGINT NOT NULL,
    INTEGRATION_ID BIGINT NOT NULL,
    FOREIGN KEY (DEPARTMENT_ID) REFERENCES Department (DEPARTMENT_ID) ON DELETE CASCADE,
    FOREIGN KEY (INTEGRATION_ID) REFERENCES Integration (INTEGRATION_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS IntegrationProperty(
    INTEGRATION_PROPERTY_ID BIGINT PRIMARY KEY,
    PROPERTY_NAME VARCHAR(50) NOT NULL,
    PROPERTY_VALUE VARCHAR(100),
    INTEGRATION_ID BIGINT NOT NULL,
    UNIQUE(PROPERTY_NAME, INTEGRATION_ID),
    FOREIGN KEY (INTEGRATION_ID) REFERENCES Integration (INTEGRATION_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS IntegrationOrgMapping(
    INTEGRATION_ORG_MAPPING_ID BIGINT PRIMARY KEY,
    ORG_ID BIGINT NOT NULL,
    INTEGRATION_ID BIGINT NOT NULL,
    FOREIGN KEY (ORG_ID) REFERENCES Organization (ORG_ID) ON DELETE CASCADE,
    FOREIGN KEY (INTEGRATION_ID) REFERENCES Integration (INTEGRATION_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TokenMapping(
    TOKEN_MAPPING_ID BIGINT PRIMARY KEY,
    USER_ID BIGINT NOT NULL,
    TOKEN VARCHAR(300) NOT NULL,
    UNIQUE(USER_ID, TOKEN),
    FOREIGN KEY (USER_ID) REFERENCES User (USER_ID) ON DELETE CASCADE
);