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

<h1>${currentNode.properties['titleOfLisy'].string}</h1>
<br />
<br />

<jcr:jqom var="judges">
  <query:selector nodeTypeName="jnt:judgeModel"/>
    <query:descendantNode path="/sites/bger/contents/judges"/>
</jcr:jqom>

<table>
  <tr>
      <td>Nom</td>
      <td>Prénom</td>
      <td>Entrée en fonction</td>
      <td>Démission</td>
      <td>Cour</td>
      <td>Canton</td>
      <td>Parti</td>
      <td>Naissance/décès</td>
  </tr>
  <br/>
  
  <c:forEach items="${judges.nodes}" var="judge">
    <tr>
      <td><a href="${url.base}${renderContext.site.path}/home/judgeview.html?judgeIdentifier=${judge.properties['j:nodename'].string}">${judge.properties['lastName'].string}</a></td>
      <td>${judge.properties['firstName'].string}</td>
      <td>${judge.properties['inputFunction'].string}</td>
      <td>${judge.properties['resignation'].string}</td>
      <td>${judge.properties['court'].string}</td>
      <td>${judge.properties['canton'].string}</td>
      <td>${judge.properties['left'].string}</td>
      <td>${judge.properties['birthDeath'].string}</td>
    </tr>
  </c:forEach>
 
</table>
