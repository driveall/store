<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
    <div class="center160">
        <form action="/login">
            <input type="submit" value="Login" class="btn200">
        </form>

        <form action="/register">
            <input type="submit" value="Register" class="btn200">
        </form>
    </div>
</body>
</html>