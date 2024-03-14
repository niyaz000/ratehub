package com.github.niyaz000.ratehub.constants;

public final class Routes {

  public static final String BASE_PATH = "/api/v1/";

  public static final String TENANTS_ENDPOINT = BASE_PATH + "tenants";

  public static final String TENANTS_RESTORE_ENDPOINT = BASE_PATH + "tenants/{name}/restore";

  public static final String SINGLE_TENANT_ENDPOINT = BASE_PATH + "tenants/{name}";

}