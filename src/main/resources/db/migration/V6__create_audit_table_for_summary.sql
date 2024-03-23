CREATE TABLE ratings_summary_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    action VARCHAR(10) NOT NULL,
    summary_id BIGINT,
    five_star_count INTEGER,
    four_star_count INTEGER,
    three_star_count INTEGER,
    two_star_count INTEGER,
    one_star_count INTEGER,
    total_weight FLOAT NOT NULL,
    total_weighted_sum FLOAT NOT NULL,
    total_count BIGINT NOT NULL,
    score FLOAT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    product_id VARCHAR(64) NOT NULL,
    tenant_id BIGINT NOT NULL,
    version BIGINT,
    user_name VARCHAR(255) NOT NULL
);

CREATE OR REPLACE FUNCTION ratings_summary_audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO ratings_summary_audit (action, summary_id, five_star_count, four_star_count, three_star_count, two_star_count, one_star_count, total_weight, total_weighted_sum, total_count, score, created_at, updated_at, product_id, tenant_id, version, user_name)
        VALUES ('INSERT', NEW.id, NEW.five_star_count, NEW.four_star_count, NEW.three_star_count, NEW.two_star_count, NEW.one_star_count, NEW.total_weight, NEW.total_weighted_sum, NEW.total_count, NEW.score, NEW.created_at, NEW.updated_at, NEW.product_id, NEW.tenant_id, NEW.version, current_user);
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO ratings_summary_audit (action, summary_id, five_star_count, four_star_count, three_star_count, two_star_count, one_star_count, total_weight, total_weighted_sum, total_count, score, created_at, updated_at, product_id, tenant_id, version, user_name)
        VALUES ('UPDATE', NEW.id, NEW.five_star_count, NEW.four_star_count, NEW.three_star_count, NEW.two_star_count, NEW.one_star_count, NEW.total_weight, NEW.total_weighted_sum, NEW.total_count, NEW.score, NEW.created_at, NEW.updated_at, NEW.product_id, NEW.tenant_id, NEW.version, current_user);
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO ratings_summary_audit (action, summary_id, five_star_count, four_star_count, three_star_count, two_star_count, one_star_count, total_weight, total_weighted_sum, total_count, score, created_at, updated_at, product_id, tenant_id, version, user_name)
        VALUES ('DELETE', OLD.id, OLD.five_star_count, OLD.four_star_count, OLD.three_star_count, OLD.two_star_count, OLD.one_star_count, OLD.total_weight, OLD.total_weighted_sum, OLD.total_count, OLD.score, OLD.created_at, OLD.updated_at, OLD.product_id, OLD.tenant_id, OLD.version, current_user);
    END IF;

    RETURN NULL; -- Since this is an AFTER trigger, we don't need to return anything
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ratings_summary_audit_insert_trigger
AFTER INSERT ON ratings_summary
FOR EACH ROW EXECUTE FUNCTION ratings_summary_audit_trigger_function();

CREATE TRIGGER ratings_summary_audit_update_trigger
AFTER UPDATE ON ratings_summary
FOR EACH ROW EXECUTE FUNCTION ratings_summary_audit_trigger_function();

CREATE TRIGGER ratings_summary_audit_delete_trigger
AFTER DELETE ON ratings_summary
FOR EACH ROW EXECUTE FUNCTION ratings_summary_audit_trigger_function();
