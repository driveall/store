<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW register</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center580">
    <h1>Registration:</h1>
    <form method="post" action="/register">
        <h2 class="red">Create login: <input type="text" name="login" class="field200"> </h2>
        <h2 class="red">Create pasword: <input type="password" name="pass" class="field200"> </h2>
        <h2 class="red">Password again: <input type="password" name="pass2" class="field200"> </h2>
        <h2>Enter email: <input type="text" name="email" class="field200"> </h2>
        <h2>Enter phone(+..): <input type="text" name="phone" class="field200"> </h2>
        <input type="submit" value="ok" class="btn200">
    </form>

    <form action="/index">
        <input type="submit" value="Back" class="btn200">
    </form>

</div>
</body>
</html>