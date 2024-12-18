<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center470">
    <h1>Login Successful for ${login}</h1>
    <form action="/delete" method="post">
        <input type="hidden" name="login" value="${login}">
        <input type="submit" value="Delete" class="btn200" >
    </form>
    <form action="/unlogin" method="post">
        <input type="submit" value="Unlogin" class="btn200" >
    </form>
</div>
</body>
</html>