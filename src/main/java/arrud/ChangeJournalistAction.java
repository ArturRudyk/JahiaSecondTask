package arrud;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRUserNode;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.jahia.services.usermanager.JahiaUser;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 30.03.17.
 */
public class ChangeJournalistAction extends Action {

    String[] massiveOfProperties = {"gender", "academicTitle", "firstName", "lastName", "address",
            "zipCode", "city", "phone", "mobile", "email", "newspapers"};

    String[] massiveOfLanguages = {"French", "German", "Itallian"};

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource,
                                  JCRSessionWrapper jcrSessionWrapper, Map<String, List<String>> map, URLResolver urlResolver) throws Exception {
        String userName = getParameter(map, "userName");
        if (!userName.isEmpty()) {
            modifyJournalist(userName, map);
        }
        renderContext.getResponse().sendRedirect(getParameter(map, "url"));
        return new ActionResult(HttpServletResponse.SC_OK);
    }

    private void modifyJournalist(String userName, Map<String, List<String>> map) throws RepositoryException {
        JCRSessionWrapper session = JCRSessionFactory.getInstance().getCurrentSystemSession(
                "default",null, null);
        String query = "SELECT * FROM [jnt:journalist] WHERE [j:nodename]='" + userName + "'";
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        Query q  = queryManager.createQuery(query, Query.JCR_SQL2);
        QueryResultWrapper queryResult = (QueryResultWrapper) q.execute();
        JCRNodeIteratorWrapper jcrNodeIteratorWrapper = queryResult.getNodes();
        if (jcrNodeIteratorWrapper.hasNext()) {
            final JCRNodeWrapper jcrNodeWrapper = (JCRNodeWrapper) jcrNodeIteratorWrapper.nextNode();
            for (int i = 0; i < massiveOfProperties.length; i++) {
                if (jcrNodeWrapper.hasProperty(massiveOfProperties[i]) &&
                        !getParameter(map, massiveOfProperties[i]).isEmpty()) {
                    jcrNodeWrapper.setProperty(massiveOfProperties[i], getParameter(map, massiveOfProperties[i]));
                }
            }
            setLanguage(jcrNodeWrapper, map);
            jcrNodeWrapper.grantRoles("u:" + jcrNodeWrapper.getName(), Collections.singleton("owner"));
            jcrNodeWrapper.getSession().save();
            JCRPublicationService.getInstance().publishByMainId(jcrNodeWrapper.getIdentifier(),
                    "default", "live", null, true, null);
        }
    }

    private void setLanguage(JCRNodeWrapper jcrNodeWrapper, Map<String, List<String>> map) throws RepositoryException {
        String[] markedLanguages = new String[3];
        for (int i = 0; i < massiveOfLanguages.length; i++) {
            if (map.get("languageOfWork").contains(massiveOfLanguages[i])) {
                markedLanguages[i] = massiveOfLanguages[i];
            }
        }
        jcrNodeWrapper.setProperty("languageOfWork", markedLanguages);
    }
}