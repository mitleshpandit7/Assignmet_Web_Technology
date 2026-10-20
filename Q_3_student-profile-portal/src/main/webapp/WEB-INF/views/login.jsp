<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login - Student Profile Portal</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body class="login-page">
  <main class="login-card">
    <h1>Student Profile Portal</h1>
    <p class="subtitle">Please sign in to continue</p>

    <c:if test="${not empty error}">
      <div class="alert alert-error" role="alert"><c:out value="${error}"/></div>
    </c:if>
    <c:if test="${not empty message}">
      <div class="alert alert-info" role="status"><c:out value="${message}"/></div>
    </c:if>

    <form method="post" action="<c:url value='/login'/>" autocomplete="on">
      <label for="username">Username</label>
      <input type="text" id="username" name="username" maxlength="100" required autofocus
             value="<c:out value='${enteredUsername}'/>">

      <label for="password">Password</label>
      <input type="password" id="password" name="password" maxlength="100" required>

      <button type="submit" class="btn btn-primary btn-block">Sign in</button>
    </form>
  </main>
</body>
</html>
