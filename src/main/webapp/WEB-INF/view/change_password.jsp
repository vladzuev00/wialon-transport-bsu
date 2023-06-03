<%@ taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>

    <head>
        <title>Change password</title>
    </head>

    <body>

        <div>
            <div>
                <h2>Change password</h2>
            </div>
        </div>

        <div>
                <spring-form:form acceptCharset="UTF-8" modelAttribute="change_password_form" method="POST" action="/user/changePassword">
                            <table>
                                <tbody>
                                    <tr>
                                        <td><spring-form:label path="oldPassword">Your password:</spring-form:label></td>
                                        <td>
                                            <spring-form:input path="oldPassword" type="password" />
                                            <spring-form:errors path="oldPassword" />
                                        </td>
                                        ${oldPasswordConfirmingError}
                                    </tr>
                                    <tr>
                                        <td><spring-form:label path="newPassword">New password:</spring-form:label></td>
                                        <td>
                                            <spring-form:input path="newPassword" type="password" />
                                            <spring-form:errors path="newPassword" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><spring-form:label path="confirmedNewPassword">Confirm new password:</spring-form:label></td>
                                        <td>
                                            <spring-form:input path="confirmedNewPassword" type="password" />
                                            <spring-form:errors path="confirmedNewPassword" />
                                        </td>
                                        ${newPasswordConfirmingError}
                                    </tr>
                                    <tr>
                                        <td><label></label></td>
                                        <td><input type="submit" value="Change"/></td>
                                    </tr>
                                </tbody>
                            </table>
               </spring-form:form>
        </div>

        <br />

        <p>
              <a href="${pageContext.request.contextPath}/user/profile">
                   Cancel
              </a>
        </p>

    </body>

</html>