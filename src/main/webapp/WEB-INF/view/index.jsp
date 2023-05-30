<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML>
<html>
<head>
  <title>Main</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<div>
  <sec:authorize access="!isAuthenticated()">
    <h4><a href="/login">Log in</a></h4>
    <h4><a href="/registration">Registration</a></h4>
  </sec:authorize>
  <sec:authorize access="isAuthenticated()">
    <h4><a href="/logout">Log out</a></h4>
    <h4><a href="/user/profile?pageNumber=0&pageSize=5">Your profile</a></h4>
  </sec:authorize>
</div>
</body>
</html>