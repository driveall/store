<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>DAW shop</title>
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
                <h2>${i.type} ${i.price}</h2>
                <form action="/sell" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="Sell" class="btn50" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <h1>Shop:</h1>
    <table>
        <tr>
            <c:forEach items="${stuff}" var="i">
            <td class="border2px">
                <h2>${i.name}</h2>
                <h2>${i.type} ${i.price}</h2>
                <form action="/buy" method="post">
                    <input type="hidden" name="itemId" value="${i.id}" >
                    <input type="submit" value="Buy" class="btn50" >
                </form>
            </td>
            </c:forEach>
        </tr>
    </table>

    <form action="/success" method="get">
        <input type="submit" value="Back" class="btn200" >
    </form>
</div>
</body>
</html>