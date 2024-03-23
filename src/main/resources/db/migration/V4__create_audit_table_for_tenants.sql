CREATE TABLE tenants_audit (
    audit_id BIGSERIAL PRIMARY KEY,
    action VARCHAR(10) NOT NULL,
    tenant_id BIGINT,
    deleted BOOLEAN,
    name VARCHAR(255),
    config JSONB,
    version BIGINT,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_name VARCHAR(255) NOT NULL
);

CREATE OR REPLACE FUNCTION tenants_audit_trigger_function()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO tenants_audit (action, tenant_id, name, config, version, user_name)
        VALUES ('INSERT', NEW.id, NEW.name, NEW.config, NEW.version, current_user);
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO tenants_audit (action, tenant_id, name, config, version, user_name)
        VALUES ('UPDATE', NEW.id, NEW.name, NEW.config, NEW.version, current_user);
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO tenants_audit (action, tenant_id, deleted, name, config, version, user_name)
        VALUES ('DELETE', OLD.id, OLD.deleted, OLD.name, OLD.config, OLD.version, current_user);
    END IF;

    RETURN NULL; -- Since this is an AFTER trigger, we don't need to return anything
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tenants_audit_insert_trigger
AFTER INSERT ON tenants
FOR EACH ROW EXECUTE FUNCTION tenants_audit_trigger_function();

CREATE TRIGGER tenants_audit_update_trigger
AFTER UPDATE ON tenants
FOR EACH ROW EXECUTE FUNCTION tenants_audit_trigger_function();

CREATE TRIGGER tenants_audit_delete_trigger
AFTER DELETE ON tenants
FOR EACH ROW EXECUTE FUNCTION tenants_audit_trigger_function();
