<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Student Profiles - Student Profile Portal</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
  <header class="topbar">
    <div class="topbar-inner">
      <span class="brand">Student Profile Portal</span>
      <div class="topbar-user">
        <span>Signed in as <strong><c:out value="${sessionScope.fullName}"/></strong></span>
        <form method="post" action="<c:url value='/logout'/>" class="inline-form">
          <input type="hidden" name="csrfToken" value="<c:out value='${sessionScope.csrfToken}'/>">
          <button type="submit" class="btn btn-outline">Log out</button>
        </form>
      </div>
    </div>
  </header>

  <main class="container">
    <h1>Student Profiles</h1>

    <form method="get" action="<c:url value='/students'/>" class="search-form">
      <input type="text" name="q" maxlength="100" placeholder="Search by name, email or department"
             value="<c:out value='${search}'/>" aria-label="Search students">
      <button type="submit" class="btn btn-primary">Search</button>
      <c:if test="${not empty search}">
        <a class="btn btn-outline" href="<c:url value='/students'/>">Clear</a>
      </c:if>
    </form>

    <c:choose>
      <%-- Conditional: no students found --%>
      <c:when test="${empty students}">
        <div class="empty-state">
          <c:choose>
            <c:when test="${not empty search}">
              No students match "<c:out value="${search}"/>".
            </c:when>
            <c:otherwise>There are no student records yet.</c:otherwise>
          </c:choose>
        </div>
      </c:when>

      <c:otherwise>
        <p class="result-count">Showing <strong>${students.size()}</strong>
          student<c:if test="${students.size() != 1}">s</c:if></p>

        <section class="grid">
          <%-- Loop: one card per student --%>
          <c:forEach var="s" items="${students}">
            <c:url var="imageUrl" value="/file">
              <c:param name="id" value="${s.id}"/>
              <c:param name="type" value="image"/>
            </c:url>
            <c:url var="docViewUrl" value="/file">
              <c:param name="id" value="${s.id}"/>
              <c:param name="type" value="document"/>
            </c:url>
            <c:url var="docDownloadUrl" value="/file">
              <c:param name="id" value="${s.id}"/>
              <c:param name="type" value="document"/>
              <c:param name="download" value="1"/>
            </c:url>

            <article class="card">
              <div class="photo">
                <c:choose>
                  <c:when test="${s.hasImage}">
                    <%-- The src points at FileServlet, which streams the image from the upload folder --%>
                    <img src="<c:out value='${imageUrl}'/>" alt="Photo of <c:out value='${s.name}'/>"
                         width="220" height="220" loading="lazy">
                  </c:when>
                  <c:otherwise>
                    <div class="avatar-fallback" aria-hidden="true"><c:out value="${s.initials}"/></div>
                  </c:otherwise>
                </c:choose>
              </div>

              <div class="card-body">
                <h2><c:out value="${s.name}"/></h2>
                <p class="dept"><c:out value="${s.department}"/></p>
                <p class="email"><a href="mailto:<c:out value='${s.email}'/>"><c:out value="${s.email}"/></a></p>

                <div class="doc-box">
                  <c:choose>
                    <c:when test="${s.hasDocument}">
                      <span class="doc-name" title="<c:out value='${s.documentFileName}'/>">
                        <c:out value="${s.documentFileName}"/>
                      </span>
                      <span class="doc-actions">
                        <a class="btn btn-small" href="<c:out value='${docViewUrl}'/>" target="_blank"
                           rel="noopener">View</a>
                        <a class="btn btn-small btn-outline" href="<c:out value='${docDownloadUrl}'/>">Download</a>
                      </span>
                    </c:when>
                    <c:otherwise>
                      <span class="muted">No document uploaded</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </article>
          </c:forEach>
        </section>
      </c:otherwise>
    </c:choose>
  </main>
</body>
</html>
