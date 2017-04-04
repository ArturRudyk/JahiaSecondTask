package arrud;

import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRUserNode;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.content.rules.PublishedNodeFact;
import org.jahia.services.mail.MailService;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.jcr.RepositoryException;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class UserRule {

    @Autowired
    private JahiaUserManagerService jahiaUserManagerService;

    @Autowired
    private MailService mailService;

    String[] massiveOfProperties = {"gender", "academicTitle", "firstName", "firstName", "lastName", "adress",
            "zipCode", "city", "phone", "mobile", "email", "newspapers", "languageOfWork",
            "typeOfAccreditation", "accreditedFor"};

    public void createUser(AddedNodeFact addedNodeFact) throws RepositoryException, ScriptException {
        final JCRNodeWrapper jcrNodeWrapper = addedNodeFact.getNode();
        final Properties properties = new Properties();
        for (int i = 0; i < massiveOfProperties.length; i++) {
            if (jcrNodeWrapper.hasProperty(massiveOfProperties[i])) {
                properties.setProperty(massiveOfProperties[i], jcrNodeWrapper.getPropertyAsString(massiveOfProperties[i]));
            }
        }
        JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
            @Override
            public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                JahiaUser journalistUser = jahiaUserManagerService.createUser(jcrNodeWrapper.getName(),
                        jcrNodeWrapper.getProperty("password").getString(), properties, session).getJahiaUser();
                session.save();
                JCRPublicationService.getInstance().publishByMainId(jcrNodeWrapper.getIdentifier(),
                        "default", "live", null, true, null);
                return true;
            }
        });
        String mailConfirmationTemplate = "/mails/templates/newUserConfirmation.vm";
        sendMessage(jcrNodeWrapper, mailConfirmationTemplate);
    }

    public void modifyUser(PublishedNodeFact publishedNodeFact) throws RepositoryException, ScriptException {
        final JCRNodeWrapper jcrNodeWrapper = publishedNodeFact.getNode();
        if (jcrNodeWrapper.isMarkedForDeletion()) {
            final String userPath = jahiaUserManagerService.getUserPath(publishedNodeFact.getName());
            JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
                @Override
                public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                    jahiaUserManagerService.deleteUser(userPath, session);
                    session.save();
                    return true;
                }
            });
            String mailConfirmationTemplate = "/mails/templates/removingUserConfirmation.vm";
            sendMessage(jcrNodeWrapper, mailConfirmationTemplate);
        } else {
            JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
                @Override
                public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                    JCRUserNode userNode = jahiaUserManagerService.lookupUser(jcrNodeWrapper.getName(), session);
                    for (int i = 0; i < massiveOfProperties.length; i++) {
                        if (jcrNodeWrapper.hasProperty(massiveOfProperties[i])) {
                            userNode.setProperty(massiveOfProperties[i], jcrNodeWrapper.getPropertyAsString(massiveOfProperties[i]));
                        }
                    }
                    userNode.getSession().save();
                    JCRPublicationService.getInstance().publishByMainId(userNode.getIdentifier(),
                            "default", "live", null, true, null);
                    return true;
                }
            });
            String mailConfirmationTemplate = "/mails/templates/modifyingUserConfirmation.vm";
            sendMessage(jcrNodeWrapper, mailConfirmationTemplate);
        }
    }

    private void sendMessage(JCRNodeWrapper jcrNodeWrapper, String mailConfirmationTemplate)
            throws ScriptException, RepositoryException {
        Map<String, Object> bindings = new HashMap<String, Object>();
        bindings.put("newUser", jcrNodeWrapper);
        String email = jcrNodeWrapper.getPropertyAsString("email");
        mailService.sendMessageWithTemplate(mailConfirmationTemplate, bindings, email,
                mailService.defaultSender(), "", "", Locale.ENGLISH, "ListOfJudges");
    }

    public void setJahiaUserManagerService(JahiaUserManagerService jahiaUserManagerService) {
        this.jahiaUserManagerService = jahiaUserManagerService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

}