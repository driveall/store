<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW wear</title>
    <meta charset="UTF-8">
    <style><%@include file="/WEB-INF/style/styles.css"%></style>
</head>
<body>
<div class="center700">
    <h1>${account.login} your items:</h1>
    <table>
        <tr>
            <c:forEach items="${account.storage}" var="i">
            <td class="border2px">
                <h2>${i.name}</h2>
                <h2>${i.type} ${i.points}</h2>
                <form action="/wear" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="Wear" class="btn50" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <h1>Your wear:</h1>
    <h2>Money ${account.money}</h2>
    <form action="/unwear" method="post">
        <span><h3>Head: ${account.head.name}</h3></span>
        <input type="hidden" name="itemId" value="${account.head.id}" >
        <input type="submit" value="Unwear" class="btn50" >
    </form>
    <form action="/unwear" method="post">
        <span><h3>Body: ${account.body.name}</h3></span>
        <input type="hidden" name="itemId" value="${account.body.id}" >
        <input type="submit" value="Unwear" class="btn50" >
    </form>
    <form action="/unwear" method="post">
        <span><h3>Legs: ${account.legs.name}</h3></span>
        <input type="hidden" name="itemId" value="${account.legs.id}" >
        <input type="submit" value="Unwear" class="btn50" >
    </form>
    <form action="/unwear" method="post">
        <span><h3>Weapon: ${account.weapon.name}</h3></span>
        <input type="hidden" name="itemId" value="${account.weapon.id}" >
        <input type="submit" value="Unwear" class="btn50" >
    </form>
    <form action="/success" method="get">
        <input type="submit" value="Back" class="btn200" >
    </form>
</div>
</body>
</html>