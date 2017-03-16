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

<style>
  td, th {
      padding: 8px;
  }
  th {
    font-weight: bold;
  }
</style>  

<h1 style="font-size:24px">${currentNode.properties['titleOfLisy'].string}</h1>
<br />
<br />

<jcr:jqom var="judges">
  <query:selector nodeTypeName="jnt:judgeModel"/>
  <query:descendantNode path="/sites/bger/contents/judges"/>
  <c:if test="${param.order=='lastName' || param.order=='inputFunction' || param.order=='canton'}">
    <query:sortBy propertyName="${param.order}" order="${param.type}"/> 
  </c:if>
</jcr:jqom>
<table align="left" frame="hsides" rules="rows" cellpadding="5" cellspacing="5" width="80%">
  <tr >
      <th>Nom</th>
      <th>Prénom</th>
      <th>Entrée en fonction</th>
      <th>Démission</th>
      <th>Cour</th>
      <th>Canton</th>
      <th>Parti</th>
      <th>Naissance/décès</th>
  </tr>
  <br/>
  
  <tr>
    <th valign="top"><a href="${renderContent.mailResource.node.url}?order=lastName&type=asc"><img src="${url.currentModule}/images/asc.gif" width="15" height="15" border="0" /></a>
      <a href="${renderContent.mailResource.node.url}?order=lastName&type=desc"><img src="${url.currentModule}/images/desc.gif" width="15" height="15" border="0" /></a></th>
	<th><img src=""/></th>
    <th valign="top"><a href="${renderContent.mailResource.node.url}?order=inputFunction&type=asc"><img src="${url.currentModule}/images/asc.gif" width="15" height="15" border="0" /></a>
      <a href="${renderContent.mailResource.node.url}?order=inputFunction&type=desc"><img src="${url.currentModule}/images/desc.gif" width="15" height="15" border="0"  /></a></th>
	<th><img src=""/></th>
    <th><img src=""/></th>
	<th valign="top"><a href="${renderContent.mailResource.node.url}?order=canton&type=asc"><img src="${url.currentModule}/images/asc.gif" width="15" height="15" border="0" /></a>
      <a href="${renderContent.mailResource.node.url}?order=canton&type=desc"><img src="${url.currentModule}/images/desc.gif" width="15" height="15" border="0" /></a></th>	
    <th><img src=""/></th>	
  </tr>
  
  <c:forEach items="${judges.nodes}" var="judge">
    <tr>
      <td><a href="${url.base}${renderContext.site.path}/home/judgeview.html?judgeIdentifier=${judge.properties['j:nodename'].string}" 
             onclick="window.open(this.href, '', 'scrollbars=1, height='+Math.min(500, screen.availHeight)+',width='+Math.min(500, screen.availWidth)); return false;">${judge.properties['lastName'].string}</a></td>
      <td>${judge.properties['firstName'].string}</td>
      <td> <fmt:formatDate value="${judge.properties.inputFunction.date.time}" pattern="yyyy" /></td>
      <td> <fmt:formatDate value="${judge.properties.resignation.date.time}" pattern="yyyy" /></td>
      <td>${judge.properties['court'].string}</td>
      <td>${judge.properties['canton'].string}</td>
      <td>${judge.properties['left'].string}</td>
      <td><fmt:formatDate value="${judge.properties.birth.date.time}" pattern="yyyy" />-
        <fmt:formatDate value="${judge.properties.death.date.time}" pattern="yyyy" /></td>
    </tr>
  </c:forEach>
 
</table>
