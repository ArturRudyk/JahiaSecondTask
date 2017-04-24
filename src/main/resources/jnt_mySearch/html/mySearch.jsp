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
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>



<template:addResources type="css" resources="simplesearchform.css" />

<template:addCacheDependency uuid="${currentNode.properties.result.string}"/>
<c:if test="${not empty currentNode.properties.result.node}">
    <c:url value='${url.base}${currentNode.properties.result.node.path}.html' var="searchUrl"/>
    <s:form method="post"  name="search" action="${searchUrl}">
        <jcr:nodeProperty name="jcr:title" node="${currentNode}" var="title"/>
        <c:if test="${not empty title.string}">
            <label for="searchTerm">${fn:escapeXml(title.string)}:&nbsp;</label>
        </c:if>

        <fmt:message key='search.startSearching' var="startSearching"/>
        <s:term match="all_words" id="searchTerm" value="${startSearching}" searchIn="siteContent,tags,files" onfocus="if(this.value=='${startSearching}')this.value='';" onblur="if(this.value=='')this.value='${startSearching}';" class="text-input"/>
        <s:site value="${renderContext.site.name}" includeReferencesFrom="systemsite" display="false"/>
        <s:language value="${renderContext.mainResource.locale}" display="false" />
        <input class="searchsubmit" type="submit"  title="<fmt:message key='search.submit'/>" value="Search"/>

    </s:form><br class="clear"/>
</c:if>

<script>
    if(document.forms.search && document.forms.search.searchTerm)
    {
        if(window.location.hash =="#form") {
            document.forms.search.searchTerm.value = ('${param.query}');
            document.forms.search.action=window.location.pathname+"?st="+${param.st};
            document.forms.search.submit();
        }
    }
</script>
