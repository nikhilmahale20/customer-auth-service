ALTER TABLE customers
    ADD COLUMN status VARCHAR(30)
        NOT NULL
        DEFAULT 'INACTIVE';

ALTER TABLE customers
    ADD CONSTRAINT chk_customers_status
        CHECK (
            status IN (
                       'ACTIVE',
                       'INACTIVE'
                )
            );