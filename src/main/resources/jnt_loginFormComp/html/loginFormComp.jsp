<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<%--@elvariable id="currentAliasUser" type="org.jahia.services.usermanager.JahiaUser"--%>

<template:addResources type="css" resources="loginForm.css"/>

    <ui:loginArea class="loginForm" >

        <div class="mod modContent">
            <div class="inner">
                <div class="richtext">
                    <h1>Login</h1>

                </div>
            </div>
        </div>
        <div class="mod modForm skinFormBlock">

            <ui:isLoginError var="loginResult">
                <span class="error"><fmt:message key="${loginResult == 'account_locked' ? 'message.accountLocked' : 'message.invalidUsernamePassword'}"/></span>
            </ui:isLoginError>
            <c:if test="${not empty param['loginError']}">
                <span class="error"><fmt:message key="${param['loginError'] == 'account_locked' ? 'message.accountLocked' : 'message.invalidUsernamePassword'}"/></span>
            </c:if>

            <p class="row">
                <label class="left" for="username"><fmt:message key="label.username"/></label>
                <input class="text" type="text" value="" tabindex="1" maxlength="250" name="username" id="username"/>
            </p>

            <p class="row">
                <label class="left" for="password"><fmt:message key="label.password"/></label>
                <input class="text" type="password" tabindex="2" maxlength="250" name="password" id="password" autocomplete="off"/>
            </p>

            <div class="divButton">
                <input type="submit" name="loginButton" class="button" value="Sign In"/>
            </div>
        </div>
    </ui:loginArea>
