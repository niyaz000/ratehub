CREATE TABLE ratings_summary (
    id BIGSERIAL PRIMARY KEY,
    five_star_count INTEGER DEFAULT 0,
    four_star_count INTEGER DEFAULT 0,
    three_star_count INTEGER DEFAULT 0,
    two_star_count INTEGER DEFAULT 0,
    one_star_count INTEGER DEFAULT 0,
    total_weight FLOAT NOT NULL DEFAULT 0,
    total_weighted_sum FLOAT NOT NULL DEFAULT 0,
    total_count BIGINT NOT NULL DEFAULT 0,
    score FLOAT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    product_id VARCHAR(64) NOT NULL,
    tenant_id BIGINT NOT NULL,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id),
    CONSTRAINT uc_tenant_product UNIQUE (tenant_id, product_id)
);
