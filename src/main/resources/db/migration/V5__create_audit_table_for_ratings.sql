CREATE TABLE ratings_audit (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(10) NOT NULL,
    rating_id BIGINT,
    product_id VARCHAR(64) NOT NULL,
    user_id VARCHAR(64) NOT NULL,
    tenant_id INT NOT NULL,
    meta JSONB,
    score FLOAT,
    verified BOOLEAN,
    weight FLOAT,
    variation VARCHAR(16) NOT NULL,
    region VARCHAR(5) NOT NULL,
    version BIGINT,
    flushed BOOLEAN,
    txn_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_name VARCHAR(255) NOT NULL
);
CREATE OR REPLACE FUNCTION ratings_audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO ratings_audit (action, rating_id, product_id, user_id, tenant_id, meta, score, verified, weight, variation, region, version, flushed, txn_id, user_name)
        VALUES ('INSERT', NEW.id, NEW.product_id, NEW.user_id, NEW.tenant_id, NEW.meta, NEW.score, NEW.verified, NEW.weight, NEW.variation, NEW.region, NEW.version, NEW.flushed, NEW.txn_id, current_user);
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO ratings_audit (action, rating_id, product_id, user_id, tenant_id, meta, score, verified, weight, variation, region, version, flushed, txn_id, user_name)
        VALUES ('UPDATE', NEW.id, NEW.product_id, NEW.user_id, NEW.tenant_id, NEW.meta, NEW.score, NEW.verified, NEW.weight, NEW.variation, NEW.region, NEW.version, NEW.flushed, NEW.txn_id, current_user);
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO ratings_audit (action, rating_id, product_id, user_id, tenant_id, meta, score, verified, weight, variation, region, version, flushed, txn_id, user_name)
        VALUES ('DELETE', OLD.id, OLD.product_id, OLD.user_id, OLD.tenant_id, OLD.meta, OLD.score, OLD.verified, OLD.weight, OLD.variation, OLD.region, OLD.version, OLD.flushed, OLD.txn_id, current_user);
    END IF;

    RETURN NULL; -- Since this is an AFTER trigger, we don't need to return anything
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER ratings_audit_insert_trigger
AFTER INSERT ON ratings
FOR EACH ROW EXECUTE FUNCTION ratings_audit_trigger_function();

CREATE TRIGGER ratings_audit_update_trigger
AFTER UPDATE ON ratings
FOR EACH ROW EXECUTE FUNCTION ratings_audit_trigger_function();

CREATE TRIGGER ratings_audit_delete_trigger
AFTER DELETE ON ratings
FOR EACH ROW EXECUTE FUNCTION ratings_audit_trigger_function();
