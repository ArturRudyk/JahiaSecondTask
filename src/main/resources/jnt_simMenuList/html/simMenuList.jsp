<%@ page language="java" contentType="text/html;charset=UTF-8" %>
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


<%--<div class="mod modNavMain">--%>
    <%--<div class="inner">--%>
<%--<c:forEach items="${currentNode.nodes}" var="subchild">--%>
<%--<template:module node="${subchild}" editable="false"/><%= System.getProperty("line.separator") %> --%>
<%--</c:forEach>--%>
  <%--</div>--%>
<%--</div>--%>

<c:if test="${renderContext.editMode and empty currentNode.properties.items}">
    Main menu to contribute
</c:if>

<div class="mod modNavMain">
    <div class="inner">
        <ul>
            <c:forEach items="${currentNode.properties.items}" var="item" varStatus="status">
                <c:set var="class_first" value="" />
                <c:if test="${status.first}">
                    <c:set var="class_first">item_first</c:set>
                </c:if>
                <li class="item ${class_first}">
                    <a href="${item.node.url}">
                            ${item.node.properties["jcr:title"].string}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>