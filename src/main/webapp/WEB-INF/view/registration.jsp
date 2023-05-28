<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Registration</title>
</head>

<body>
<div>
  <form:form method="POST" modelAttribute="userForm">
    <h2>Registration</h2>
    <div>
      <form:input type="text" path="email" placeholder="email" autofocus="true"></form:input>
      <form:errors path="email"></form:errors>
        ${emailAlreadyExistsError}
    </div>
    <div>
      <form:input type="password" path="password" placeholder="Password"></form:input>
    </div>
    <div>
      <form:input type="password" path="confirmedPassword" placeholder="Confirm your password"></form:input>
      <form:errors path="password"></form:errors>
        ${confirmingPasswordError}
    </div>
    <button type="submit">Check in</button>
  </form:form>
  <a href="/">Main page</a>
</div>
</body>
</html>