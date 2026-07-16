CREATE TABLE otp_verifications (
                                   id VARCHAR(36) PRIMARY KEY,
                                   customer_id VARCHAR(36) NOT NULL,
                                   otp_code VARCHAR(10) NOT NULL,
                                   expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                   verified BOOLEAN NOT NULL DEFAULT FALSE,

                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   created_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',
                                   updated_by VARCHAR(100) NOT NULL DEFAULT 'SYSTEM',

                                   CONSTRAINT fk_otp_verifications_customer
                                       FOREIGN KEY (customer_id)
                                           REFERENCES customers(id)
                                           ON DELETE CASCADE
);

CREATE INDEX idx_otp_verifications_customer_id
    ON otp_verifications(customer_id);