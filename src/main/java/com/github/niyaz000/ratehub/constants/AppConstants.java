package com.github.niyaz000.ratehub.constants;

public final class AppConstants {

  public static final String TENANT_NAME = "X-Tenant-Name";

  public static final String USER_ID = "X-User-Id";

  public static final String ID = "id";

  public static final String SYSTEM_ALONE = "hasAnyRole('SYSTEM')";

  public static final String SYSTEM_AND_ADMIN = "hasAnyRole('SYSTEM', 'ADMIN')";

  public static final String ALL_ROLES = "hasAnyRole('SYSTEM', 'ADMIN', 'USER')";

}
