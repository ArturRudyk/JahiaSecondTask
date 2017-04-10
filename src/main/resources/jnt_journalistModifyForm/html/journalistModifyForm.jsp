<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="org.jahia.services.content.JCRSessionFactory" %>
<%@ page import="org.jahia.services.usermanager.JahiaUser" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%@ taglib prefix="my" uri="http://www.jahia.org/tags/setValues" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<style>
    @import "css/bgerv10.css";
</style>

<%final JahiaUser user = JCRSessionFactory.getInstance().getCurrentUser();%>

<c:set var="currentPasswordProperty" scope="session" value="<%=user.getProperty("password")%>"/>
<c:set var="newspapersProperty" scope="session" value="<%=user.getProperty("newspapers")%>"/>
<c:set var="genderProperty" scope="session" value="<%=user.getProperty("gender")%>"/>
<c:set var="academicProperty" scope="session" value="<%=user.getProperty("academicTitle")%>"/>
<c:set var="firstNameProperty" scope="session" value="<%=user.getProperty("firstName")%>"/>
<c:set var="lastNameProperty" scope="session" value="<%=user.getProperty("lastName")%>"/>
<c:set var="addressProperty" scope="session" value="<%=user.getProperty("address")%>"/>
<c:set var="zipCodeProperty" scope="session" value="<%=user.getProperty("zipCode")%>"/>
<c:set var="cityProperty" scope="session" value="<%=user.getProperty("city")%>"/>
<c:set var="phoneProperty" scope="session" value="<%=user.getProperty("phone")%>"/>
<c:set var="mobileProperty" scope="session" value="<%=user.getProperty("mobile")%>"/>
<c:set var="emailProperty" scope="session" value="<%=user.getProperty("email")%>"/>

<c:set var="passwordNotConfirm" scope="session" value="passwordNotConfirm"/>
<c:set var="currentAndNewPasswordMatches" scope="session" value="currentAndNewPasswordMatches"/>
<c:set var="invalidProperties" scope="session" value="invalidProperties"/>

<c:choose>
    <c:when test="${param.errorMessage == currentAndNewPasswordMatches}">
        <p>Current and new password matches</p>
    </c:when>
    <c:when test="${param.errorMessage == passwordNotConfirm}">
        <p>New password not confirm</p>
    </c:when>
    <c:when test="${param.errorMessage == invalidProperties}">
        <c:set var="invalidProperty" scope="session" value="${param.invalidProperty}"/>
        <p>Invalid property <c:out value="${invalidProperty}"/></p>
    </c:when>
    <c:otherwise>
        <div class="mod modForm">
    <form action="<c:url value='${url.base}${currentNode.path}'/>.changeJournalist.do" method="post">
    <ol>
        <li class="row"><div class="label"><label for="userName"></label></div><div class="fields"><input type="hidden" name="userName" id="userName" value="<%=user.getUsername()%>"/></div></li>
        <li class="row"><div class="label"><label for="currentPassword"></label></div><div class="fields"><input type="hidden" name="currentPassword" id="currentPassword" value="${currentPasswordProperty}"/></div></li>
        <li class="row"><div class="label"><label for="currentPasswordView">Current password</label></div><div class="fields"><input type="password" name="currentPasswordView" id="currentPasswordView" value="<c:if test="${currentPasswordProperty != null}">${currentPasswordProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="newPassword">New password</label></div><div class="fields"><input type="password" name="newPassword" id="newPassword" value=""/></div></li>
        <li class="row"><div class="label"><label for="confirmNewPassword"></label></div><div class="fields"><input type="password" name="confirmNewPassword" id="confirmNewPassword" value=""/></div></li>
        <li class="row"><div class="label"><label for="newspapers">Newspapers concerned</label></div><div class="fields"><input type="text" name="newspapers" id="newspapers" value="<c:if test="${newspapersProperty != null}">${newspapersProperty}</c:if>"/></div></li>
        <li class="row"><div class="label">Language of work</div><div class="fields">
            <c:set var="languagesStr" scope="session" value="<%=user.getProperty("languageOfWork")%>"/>
            <c:set var="French" scope="session" value="French"/>
            <c:set var="German" scope="session" value="German"/>
            <c:set var="Itallian" scope="session" value="Itallian"/>
            <c:set var="isFrench" scope="session" value="false"/>
            <c:set var="isGerman" scope="session" value="false"/>
            <c:set var="isItallian" scope="session" value="false"/>
            <c:if test="${languagesStr != null}">
                <c:forEach var="language" items="<%=user.getProperty("languageOfWork").split(" ")%>">
                    <c:if test="${language==French}">
                        <c:set var="isFrench" scope="session" value="true"/>
                    </c:if>
                    <c:if test="${language==German}">
                        <c:set var="isGerman" scope="session" value="true"/>
                    </c:if>
                    <c:if test="${language==Itallian}">
                        <c:set var="isItallian" scope="session" value="true"/>
                    </c:if>
                </c:forEach>
            </c:if>
            <label class="fieldrow"><input type="checkbox" class="checkbox" name="languageOfWork" value="French" <c:if test="${isFrench}">checked="true"</c:if>/>French</label>
            <label class="fieldrow"><input type="checkbox" class="checkbox" name="languageOfWork" value="German" <c:if test="${isGerman}">checked="true"</c:if>/>German</label>
            <label class="fieldrow"><input type="checkbox" class="checkbox" name="languageOfWork" value="Itallian" <c:if test="${isItallian}">checked="true"</c:if>/>Itallian</label></div></li>
        <li class="row"><div class="label"><label for="gender">Gender</label></div><div class="fields"><input type="hidden" name="gender" id="gender" value="<%=user.getProperty("gender")%>"/></div> <div><c:if test="${genderProperty != null}">${genderProperty}</c:if></div></li>
        <li class="row"><div class="label"><label for="academicTitle">Academic Title</label></div><div class="fields"><input type="hidden" name="academicTitle" id="academicTitle" value="<c:if test="${academicProperty != null}">${academicProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="firstName">Name</label></div><div class="fields"><input type="hidden" name="firstName" id="firstName" value="<%=user.getProperty("firstName")%>"/></div> <div><c:if test="${firstNameProperty != null}">${firstNameProperty}</c:if></div></li>
        <li class="row"><div class="label"><label for="lastName">Surname</label></div><div class="fields"><input type="hidden" name="lastName" id="lastName" value="<%=user.getProperty("lastName")%>"/></div><div><c:if test="${lastNameProperty != null}">${lastNameProperty}</c:if></div></li>
        <li class="row"><div class="label"><label for="address">Address</label></div><div class="fields"><input type="text" name="address" id="address" value="<c:if test="${addressProperty != null}">${addressProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="zipCode">Zip Code</label></div><div class="fields"><input type="text" name="zipCode" id="zipCode" value="<c:if test="${zipCodeProperty != null}">${zipCodeProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="city">City</label></div><div class="fields"><input type="text" name="city" id="city" value="<c:if test="${cityProperty != null}">${cityProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="phone">Phone</label></div><div class="fields"><input type="text" name="phone" id="phone" value="<c:if test="${phoneProperty != null}">${phoneProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="mobile">Mobile</label></div><div class="fields"><input type="text" name="mobile" id="mobile" value="<c:if test="${mobileProperty != null}">${mobileProperty}</c:if>"/></div></li>
        <li class="row"><div class="label"><label for="email">Email</label></div><div class="fields"><input type="text" name="email" id="email" value="<c:if test="${emailProperty != null}">${emailProperty}</c:if>"/></div></li>
        <input type="hidden" name="url" id="url" value="${renderContext.mainResource.node.url}"/>
    </ol>
    <button type="submit">Submit </button>
    </form>
        </div>
    </c:otherwise>
</c:choose>

<script type="text/javascript">
    window.onload = function () {
        if(window.location.hash) {
            window.location = window.location.pathname;
        }
        if(window.location.search) {
           window.location.hash = "#loader";
        }
    }
</script>