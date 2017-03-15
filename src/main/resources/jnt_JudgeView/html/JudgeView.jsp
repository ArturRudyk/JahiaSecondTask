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

<jcr:jqom var="judges">
  <query:selector nodeTypeName="jnt:judgeModel"  />
  <query:equalTo propertyName="j:nodename" value="${param.judgeIdentifier}" />
  <query:descendantNode path="/sites/bger/contents/judges"/>
</jcr:jqom>

<c:forEach items="${judges.nodes}" var="judge">
   <h1 align="center">${judge.properties['lastName'].string} ${judge.properties['firstName'].string} ${judge.properties['birthDeath'].string}</h1>
   <c:set var="photo" value="${judge.properties['photo']}"/>
   <c:url var="imgUrl" value="${photo.node.url}"></c:url>
   <p align="center"><img src="${imgUrl}"/></p>
   <p>${judge.properties['biography'].string}</p>
</c:forEach>
  