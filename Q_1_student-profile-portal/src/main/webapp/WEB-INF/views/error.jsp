<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Error - Student Profile Portal</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body class="login-page">
  <main class="login-card">
    <c:set var="code" value="${pageContext.errorData.statusCode}"/>
    <h1>
      <c:choose>
        <c:when test="${code == 404}">Not found</c:when>
        <c:when test="${code == 401}">Please sign in</c:when>
        <c:when test="${code == 403}">Access denied</c:when>
        <c:when test="${code == 400}">Bad request</c:when>
        <c:when test="${code == 405}">Not allowed</c:when>
        <c:otherwise>Something went wrong</c:otherwise>
      </c:choose>
    </h1>
    <p class="subtitle">
      <c:choose>
        <c:when test="${code == 404}">The page or file you asked for is not available.</c:when>
        <c:when test="${code == 401}">You need to be logged in to see this.</c:when>
        <c:when test="${code == 403}">You are not allowed to do that.</c:when>
        <c:when test="${code == 400 or code == 405}">The request could not be processed.</c:when>
        <c:otherwise>An unexpected error occurred. Please try again; if it continues, check the Tomcat logs
          (database not running or wrong credentials in db.properties are common causes).</c:otherwise>
      </c:choose>
    </p>
    <a class="btn btn-primary btn-block" href="<c:url value='/students'/>">Back to the portal</a>
  </main>
</body>
</html>
