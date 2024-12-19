package com.daw.store;

public class Constants {
    public static final String TABLE_NAME = "account";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ACCOUNT_PREFIX = "ACCOUNT:";
    public static final String ITEM_PREFIX = "ITEM:";

    public static final String INDEX_PAGE_PATH = "/index";
    public static final String SHOP_PAGE_PATH = "/shop";
    public static final String WEAR_PAGE_PATH = "/wear";
    public static final String SUCCESS_PAGE_PATH = "/success";

    public static final String API_GET_URL = "http://localhost:8080/api/get?login=%s";
    public static final String API_GET_ITEM_URL = "http://localhost:8080/api/get-item?itemId=%s";
    public static final String API_CREATE_URL = "http://localhost:8080/api/create";
    public static final String API_UPDATE_URL = "http://localhost:8080/api/update";
    public static final String API_DELETE_URL = "http://localhost:8080/api/delete?login=%s";

    public static final String DEFAULT_REGION = "UA";

    public static final Long CAPACITY_UNITS = 10L;

}
