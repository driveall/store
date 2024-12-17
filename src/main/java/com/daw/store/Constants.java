package com.daw.store;

public class Constants {
    public static final String TABLE_NAME = "account";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_LOGIN = "login";
    public static final String ACCOUNT_PREFIX = "ACCOUNT:";

    public static final String INDEX_PAGE_PATH = "/index.html";
    public static final String SUCCESS_PAGE_PATH = "/success";

    public static final String API_GET_URL = "http://localhost:8080/api/get?login=%s";
    public static final String API_CREATE_URL = "http://localhost:8080/api/create";
    public static final String API_DELETE_URL = "http://localhost:8080/api/delete?login=%s";

    public static final Long CAPACITY_UNITS = 10L;

    public static final String SUCCESS_PAGE_CONTENT = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <link rel="stylesheet" href="styles.css">
                    </head>
                    <body>
                    <div class="center470">
                    <h1>Login Successful for %s</h1>
                    <form action="/delete" method="post">
                        <input type="hidden" name="%s" value="%s">
                        <input type="submit" value="Delete" class="btn200" >
                    </form>
                    <form action="/unlogin" method="post">
                        <input type="submit" value="Unlogin" class="btn200" >
                    </form>
                    </div>
                    </body>
                    </html>
                    """;
}
