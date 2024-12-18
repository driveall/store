<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW main page</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center480">
    <h1>Your login is ${account.login}</h1>
    <h2>Your email is ${account.email}</h2>
    <h2>Your phone is ${account.phone}</h2>
    <form action="/update" method="get">
        <input type="submit" value="Update" class="btn200" >
    </form>
    <form action="/delete" method="post">
        <input type="hidden" name="login" value="${account.login}">
        <input type="submit" value="Delete" class="btn200" >
    </form>
    <form action="/unlogin" method="post">
        <input type="submit" value="Unlogin" class="btn200" >
    </form>
</div>
</body>
</html>