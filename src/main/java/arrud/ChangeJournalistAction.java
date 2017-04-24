package arrud;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.*;
import org.jahia.services.mail.MailService;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 30.03.17.
 */
public class ChangeJournalistAction extends Action {

    @Autowired
    private MailService mailService;

    String[] massiveOfProperties = {"gender", "academicTitle", "firstName", "lastName", "address",
            "zipCode", "city", "phone", "mobile", "email", "newspapers"};

    String[] massiveOfMandatoryProperties = {"gender", "firstName", "lastName", "address",
            "zipCode", "city", "email"};

    String[] massiveOfLanguages = {"French", "German", "Itallian"};

    String errorMessage = "";

    @Override
    public ActionResult doExecute(HttpServletRequest httpServletRequest, RenderContext renderContext, Resource resource,
                                  JCRSessionWrapper jcrSessionWrapper, Map<String, List<String>> map, URLResolver urlResolver) throws Exception {
        String userName = getParameter(map, "userName");
            modifyJournalist(userName, map);
        renderContext.getResponse().sendRedirect(getParameter(map, "url") + errorMessage);
        errorMessage = "";
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
        if (checkForMandatory(map)) {
            boolean email = checkProperty("email", getParameter(map, "email"), "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            boolean zipCode = checkProperty("ZipCode", getParameter(map, "zipCode"), "^[0-9]+$");
            return (email & zipCode);
        } else {
            return false;
        }
    }

    private boolean checkProperty(String propertyName, String propertyValue, String expression) {
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(propertyValue);
        if (matcher.matches()) {
            return true;
        } else {
            setErrorMessage("errorMessage", "invalidProperties");
            setErrorMessage("invalidProperty", propertyName);
            return false;
        }
    }

    private boolean checkForMandatory(Map<String, List<String>> map) {
        boolean result = true;
        if (map.containsKey("languageOfWork")) {
            for (int i = 0; i < massiveOfMandatoryProperties.length; i++) {
                if (getParameter(map, massiveOfMandatoryProperties[i]) == null) {
                    result = false;
                    setErrorMessage("errorMessage", "invalidProperties");
                    setErrorMessage("invalidProperty", massiveOfMandatoryProperties[i]);
                    break;
                }
            }
        } else {
            result = false;
            setErrorMessage("errorMessage", "invalidProperties");
            setErrorMessage("invalidProperty", "language");
        }
        return result;
    }

    private void modifyJournalist(String userName, Map<String, List<String>> map) throws RepositoryException, ScriptException {
        final JCRNodeWrapper jcrNodeWrapper = getJournalist(userName);
        if (isValidProperties(map)) {
            boolean isModificationSuccessful = true;
            if (isNewPasswordExists(map)) {
                 isModificationSuccessful = modifyPassword(map);
            }
            if (isModificationSuccessful) {
                modifyProperies(jcrNodeWrapper, map);
                setLanguage(jcrNodeWrapper, map);
                jcrNodeWrapper.getSession().save();
                publish(jcrNodeWrapper);
                sendMessage(jcrNodeWrapper,"/mails/templates/modifyingUserConfirmation.vm");

            }
        }
    }

    private boolean modifyPassword( Map<String, List<String>> map) throws RepositoryException {
        boolean result = false;
        if (isPasswordConfirm(map)) {
            if (isPasswordDoNotMatch(map)) {
                setPassword(getParameter(map, "userName"), getParameter(map, "newPassword"));
                result = true;
            } else {
                setErrorMessage("errorMessage", "currentAndNewPasswordMatches");
            }
        } else {
            setErrorMessage("errorMessage", "passwordNotConfirm");
        }
        return result;
    }

    private void setPassword(String userName, String newPassword) throws RepositoryException {
        final JCRNodeWrapper jcrNodeWrapper = getJournalist(userName);
        jcrNodeWrapper.setProperty("password", newPassword);
        jcrNodeWrapper.getSession().save();
        publish(jcrNodeWrapper);
    }

    private void modifyProperies(JCRNodeWrapper jcrNodeWrapper, Map<String, List<String>> map) throws RepositoryException {
        for (int i = 0; i < massiveOfProperties.length; i++) {
            if ((getParameter(map, massiveOfProperties[i]) != null) &&
                    !getParameter(map, massiveOfProperties[i]).isEmpty()) {
                jcrNodeWrapper.setProperty(massiveOfProperties[i], getParameter(map, massiveOfProperties[i]));
            } else {
                jcrNodeWrapper.setProperty(massiveOfProperties[i], "");
            }
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

    private void sendMessage(JCRNodeWrapper jcrNodeWrapper, String mailConfirmationTemplate)
            throws ScriptException, RepositoryException {
        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("ModifiedUser", jcrNodeWrapper);
        String email = jcrNodeWrapper.getPropertyAsString("email");
        mailService.sendMessageWithTemplate(mailConfirmationTemplate, bindings, email,
                mailService.defaultSender(), "", "", Locale.ENGLISH, "ListOfJudges");
    }

    private void setErrorMessage(String key, String value) {
        if (errorMessage.equals("")) {
            errorMessage = "?" + key + "=" + value;
        } else {
            errorMessage += "&" + key + "=" + value;
        }
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

}