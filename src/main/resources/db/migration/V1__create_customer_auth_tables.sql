CREATE TABLE customers (
                           id VARCHAR(36) PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(255) NOT NULL,
                           phone VARCHAR(20) NOT NULL,
                           address VARCHAR(500) NOT NULL,
                           kyc_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

                           created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
                           updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',

                           CONSTRAINT uk_customers_email
                               UNIQUE (email),

                           CONSTRAINT uk_customers_phone
                               UNIQUE (phone)
);


CREATE TABLE credentials (
                             id VARCHAR(36) PRIMARY KEY,
                             customer_id VARCHAR(36) NOT NULL,
                             username VARCHAR(100) NOT NULL,
                             password_hash VARCHAR(255) NOT NULL,
                             role VARCHAR(30) NOT NULL,

                             created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
                             updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',

                             CONSTRAINT uk_credentials_customer_id
                                 UNIQUE (customer_id),

                             CONSTRAINT uk_credentials_username
                                 UNIQUE (username),

                             CONSTRAINT fk_credentials_customer
                                 FOREIGN KEY (customer_id)
                                     REFERENCES customers(id)
                                     ON DELETE CASCADE
);