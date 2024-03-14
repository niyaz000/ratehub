package com.github.niyaz000.ratehub.constants;

public final class ApiErrorConstants {

  public static final String ERROR_CODE = "validation_failed";

  public static final String ERROR_MESSAGE = "Request processing failed due to invalid data.";

  public static final String DUPLICATE_ENTITY = "duplicate_entity";

  public static final String CONCURRENT_MODIFICATION = "concurrent_modification";

  public static final String CONCURRENT_MODIFICATION_MESSAGE = "Resource is being modified by another transaction, please retry again after sometime.";

  public static final String NOT_FOUND_ERROR_CODE = "not_found";

  public static final String DUPLICATE_ENTITY_MESSAGE = "Entity with the same unique identifier already exists in system.";

  public static final String INVALID_FIELD = "invalid_field";

  public static final String INVALID_VALUE = "invalid_value";

  public static final String NOT_FOUND_ERROR_MESSAGE = "Could not find the requested resource on the server";

  public static  final String INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong, Please retry in sometime";

}
