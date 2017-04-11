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

<c:set var="language" scope="session" value="${renderContext.mainResource.locale}"/>

<c:set var="german" scope="session" value="de"/>
<c:set var="french" scope="session" value="fr"/>
<c:set var="italian" scope="session" value="it"/>


<c:set var="baseUrl" scope="session" value="${url.base}"/>
<c:set var="renderContextUrl" scope="session" value="${renderContext.mainResource.node.url}"/>

<c:set var="urlBaseLength" scope="session" value="${fn:length(baseUrl)}"/>
<c:set var="renderContextUrlLength" scope="session" value="${fn:length(renderContextUrl)}"/>

<c:set var="prefix" value="${fn:substring(baseUrl, 0, urlBaseLength -2)}" />
<c:set var="sufix" scope="session" value="${fn:substring(renderContextUrl, urlBaseLength, renderContextUrlLength)}"/>
<style>
    @import "css/bgerv10.css";
</style>
<div class="mod modNavService">
<h2 class="invisible">Changer la langue</h2>
<ul class="block block_last">
    <li class="item item_first"><a href="${prefix}<c:choose><c:when test="${language == german}">${french}</c:when><c:otherwise>${german}</c:otherwise></c:choose>${sufix}" target="_top"><span <c:choose><c:when test="${language == german}">lang="fr" title="french">F</c:when><c:otherwise>lang="de" title="deutsch">D</c:otherwise></c:choose></span></a></li>
    <li class="item"><a href="${prefix}<c:choose><c:when test="${language == italian}">${french}</c:when><c:otherwise>${italian}</c:otherwise></c:choose>${sufix}" target="_top"><span <c:choose><c:when test="${language == italian}">lang="fr" title="french">F</c:when><c:otherwise>lang="it" title="italiano">I</c:otherwise></c:choose></span></a></li>
</ul>
</div>

