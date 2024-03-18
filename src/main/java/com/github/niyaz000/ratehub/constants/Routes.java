package com.github.niyaz000.ratehub.constants;

public final class Routes {

  public static final String BASE_PATH = "/api/v1/";

  public static final String TENANTS_ENDPOINT = BASE_PATH + "tenants";

  public static final String TENANTS_RESTORE_ENDPOINT = BASE_PATH + "tenants/{name}/restore";

  public static final String SINGLE_TENANT_ENDPOINT = BASE_PATH + "tenants/{name}";

  public static final String RATINGS_ENDPOINT = BASE_PATH + "ratings";

  public static final String SINGLE_RATINGS_ENDPOINT = RATINGS_ENDPOINT + "/{id}";

  public static final String ACTUATOR_ENDPOINT = "actuator";

}
