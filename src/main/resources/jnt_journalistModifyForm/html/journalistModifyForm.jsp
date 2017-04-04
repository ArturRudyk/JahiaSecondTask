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
<div class="mod modForm">
<form action="<c:url value='${url.base}${currentNode.path}'/>.changeJournalist.do" method="post">
    <ol>
        <li class="row"><div class="label"><label for="userName">User</label></div><div class="fields"><input type="hidden" name="userName" id="userName" value="<%=user.getUsername()%>"/></div> <div><%=user.getUsername()%></div></li>
        <li class="row"><div class="label"><label for="newspapers">Newspapers concerned</label></div><div class="fields"><input type="text" name="newspapers" id="newspapers" value="<%=user.getProperty("newspapers")%>"/></div></li>
        <li class="row"><div class="label">Language of work</div><div class="fields">
            <c:set var="languagesStr" scope="session" value="<%=user.getProperty("languageOfWork")%>"/>
            <c:set var="French" scope="session" value="French"/>
            <c:set var="German" scope="session" value="German"/>
            <c:set var="Itallian" scope="session" value="Itallian"/>
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
        <li class="row"><div class="label"><label for="gender">Gender</label></div><div class="fields"><input type="hidden" name="gender" id="gender" value="<%=user.getProperty("gender")%>"/></div> <div><%=user.getProperty("gender")%></div></li>
        <li class="row"><div class="label"><label for="academicTitle">Academic Title</label></div><div class="fields"><input type="hidden" name="academicTitle" id="academicTitle" value="<%=user.getProperty("academicTitle")%>"/></div> <div><%=user.getProperty("academicTitle")%></div></li>
        <li class="row"><div class="label"><label for="firstName">Name</label></div><div class="fields"><input type="hidden" name="firstName" id="firstName" value="<%=user.getProperty("firstName")%>"/></div> <div><%=user.getProperty("firstName")%></div></li>
        <li class="row"><div class="label"><label for="lastName">Surname</label></div><div class="fields"><input type="hidden" name="lastName" id="lastName" value="<%=user.getProperty("lastName")%>"/></div><div><%=user.getProperty("lastName")%></div></li>
        <li class="row"><div class="label"><label for="address">Address</label></div><div class="fields"><input type="text" name="address" id="address" value="<%=user.getProperty("address")%>"/></div></li>
        <li class="row"><div class="label"><label for="zipCode">Zip Code</label></div><div class="fields"><input type="text" name="zipCode" id="zipCode" value="<%=user.getProperty("zipCode")%>"/></div></li>
        <li class="row"><div class="label"><label for="city">City</label></div><div class="fields"><input type="text" name="city" id="city" value="<%=user.getProperty("city")%>"/></div></li>
        <li class="row"><div class="label"><label for="phone">Phone</label></div><div class="fields"><input type="text" name="phone" id="phone" value="<%=user.getProperty("phone")%>"/></div></li>
        <li class="row"><div class="label"><label for="mobile">Mobile</label></div><div class="fields"><input type="text" name="mobile" id="mobile" value="<%=user.getProperty("mobile")%>"/></div></li>
        <li class="row"><div class="label"><label for="email">Email</label></div><div class="fields"><input type="text" name="email" id="email" value="<%=user.getProperty("email")%>"/></div></li>
        <input type="hidden" name="url" id="url" value="${renderContext.mainResource.node.url}"/>
    </ol>
    <button type="submit">Submit </button>
</form>
</div>