CREATE TABLE ratings (
    id BIGSERIAL PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL,
    user_id VARCHAR(64) NOT NULL,
    tenant_id INT NOT NULL,
    meta JSONB DEFAULT '{}'::JSONB,
    score FLOAT DEFAULT 0,
    verified BOOLEAN DEFAULT FALSE,
    weight FLOAT DEFAULT 0,
    variation VARCHAR(16) NOT NULL,
    region VARCHAR(5) NOT NULL,
    version BIGINT DEFAULT 0,
    flushed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT uc_tenant_id_user_id_product_id UNIQUE (tenant_id, user_id, product_id)
);
