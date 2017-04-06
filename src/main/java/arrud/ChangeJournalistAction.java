package arrud;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.*;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 30.03.17.
 */
public class ChangeJournalistAction extends Action {

    String[] massiveOfProperties = {"gender", "academicTitle", "firstName", "lastName", "address",
            "zipCode", "city", "phone", "mobile", "email", "newspapers"};

    String[] massiveOfMandatoryProperties = {"gender", "firstName", "lastName", "address",
            "zipCode", "city", "email"};

    String[] massiveOfLanguages = {"French", "German", "Itallian"};

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource,
                                  JCRSessionWrapper jcrSessionWrapper, Map<String, List<String>> map, URLResolver urlResolver) throws Exception {
        String userName = getParameter(map, "userName");
        String errorMessage = "";
        if (!userName.isEmpty()) {
            if (isNewPasswordExists(map)) {
                if (isPasswordConfirm(map)) {
                    if (isPasswordDoNotMatch(map)) {
                        if (isValidProperties(map)) {
                            setPassword(userName, getParameter(map, "newPassword"));
                            modifyJournalist(userName, map);
                        } else {
                            errorMessage += "?errorMessage=invalidProperties";
                        }
                    } else {
                        errorMessage += "?errorMessage=CurrentAndNewPasswordMatches";
                    }
                } else {
                    errorMessage += "?errorMessage=PasswordNotConfirm";
                }
            } else {
                if (isValidProperties(map)) {
                    modifyJournalist(userName, map);
                } else {
                    errorMessage += "?errorMessage=invalidProperties";
                }
            }
        }
        renderContext.getResponse().sendRedirect(getParameter(map, "url")+ errorMessage);
        return new ActionResult(HttpServletResponse.SC_OK);
    }

    private boolean isNewPasswordExists(Map<String, List<String>> map) {
        boolean result = (getParameter(map, "newPassword") == null) ? false : true;
        return result;
    }

    private boolean isPasswordConfirm(Map<String, List<String>> map) throws RepositoryException {
        String newPassword = getParameter(map, "newPassword");
        String confirmNewPassword = getParameter(map, "confirmNewPassword");
        boolean result = (newPassword.equals(confirmNewPassword)) ? true : false;
        return  result;
    }
    
    private boolean isPasswordDoNotMatch(Map<String, List<String>> map) {
        String currentPassword = getParameter(map, "currentPassword");
        String newPassword = getParameter(map, "newPassword");
        boolean result = (currentPassword.equals(newPassword)) ? false : true;
        return result;
    }

    private boolean isValidProperties(Map<String, List<String>> map) {
        boolean result = false;
        if (checkForMandatory(map)) {
            boolean email = checkProperty(getParameter(map, "email"), "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            boolean zipCode = checkProperty(getParameter(map, "zipCode"), "^[0-9]+$");
            result = (email & zipCode);
        }
        return result;
    }

    private boolean checkProperty(String property, String expression) {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(property);
        return matcher.matches();
    }

    private boolean checkForMandatory(Map<String, List<String>> map) {
        boolean result = true;
        for (int i = 0; i < massiveOfMandatoryProperties.length; i++) {
            if (getParameter(map, massiveOfProperties[i]) == null) {
                result = false;
            }
        }
        if (!map.containsKey("languageOfWork")) {
            result = false;
        }
        return result;
    }

    private void setPassword(String userName, String newPassword) throws RepositoryException {
        final JCRNodeWrapper jcrNodeWrapper = getJournalist(userName);
        jcrNodeWrapper.setProperty("password", newPassword);
        jcrNodeWrapper.getSession().save();
        publish(jcrNodeWrapper);
    }

    private void modifyJournalist(String userName, Map<String, List<String>> map) throws RepositoryException {
        final JCRNodeWrapper jcrNodeWrapper = getJournalist(userName);
        for (int i = 0; i < massiveOfProperties.length; i++) {
            if (jcrNodeWrapper.hasProperty(massiveOfProperties[i]) &&
                    (getParameter(map, massiveOfProperties[i]) != null) &&
                    !getParameter(map, massiveOfProperties[i]).isEmpty()) {
                jcrNodeWrapper.setProperty(massiveOfProperties[i], getParameter(map, massiveOfProperties[i]));
            }
        }
        setLanguage(jcrNodeWrapper, map);
        jcrNodeWrapper.grantRoles("u:" + jcrNodeWrapper.getName(), Collections.singleton("owner"));
        jcrNodeWrapper.getSession().save();
        publish(jcrNodeWrapper);
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

    private JCRNodeWrapper getJournalist(String userName) throws RepositoryException {
        JCRSessionWrapper session = JCRSessionFactory.getInstance().getCurrentSystemSession(
                "default",null, null);
        String query = "SELECT * FROM [jnt:journalist] WHERE [j:nodename]='" + userName + "'";
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        Query q  = queryManager.createQuery(query, Query.JCR_SQL2);
        QueryResultWrapper queryResult = (QueryResultWrapper) q.execute();
        JCRNodeIteratorWrapper jcrNodeIteratorWrapper = queryResult.getNodes();
        if (jcrNodeIteratorWrapper.hasNext()) {
            final JCRNodeWrapper jcrNodeWrapper = (JCRNodeWrapper) jcrNodeIteratorWrapper.nextNode();
            return jcrNodeWrapper;
        } else {
            return null;
        }
    }

    private void publish(JCRNodeWrapper jcrNodeWrapper) throws RepositoryException {
        JCRPublicationService.getInstance().publishByMainId(jcrNodeWrapper.getIdentifier(),
                "default", "live", null, true, null);
    }
}