package arrud;

import org.jahia.registries.ServicesRegistry;
import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRUserNode;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.content.rules.DeletedNodeFact;
import org.jahia.services.content.rules.PublishedNodeFact;
import org.jahia.services.content.rules.User;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class UserRule {
    @Autowired
    private JahiaUserManagerService jahiaUserManagerService;

    public void createUser(AddedNodeFact addedNodeFact) throws RepositoryException {
        final JCRNodeWrapper jcrNodeWrapper = addedNodeFact.getNode();
        final Properties properties = new Properties();
        properties.setProperty("title", jcrNodeWrapper.getProperty("title").getString());
        properties.setProperty("academicTitle", jcrNodeWrapper.getProperty("academicTitle").getString());
        properties.setProperty("firstName", jcrNodeWrapper.getProperty("firstName").getString());
        properties.setProperty("lastName", jcrNodeWrapper.getProperty("lastName").getString());
        properties.setProperty("adress", jcrNodeWrapper.getProperty("adress").getString());
        properties.setProperty("NPA", jcrNodeWrapper.getProperty("NPA").getString());
        properties.setProperty("place", jcrNodeWrapper.getProperty("place").getString());
        properties.setProperty("phoneNumber", jcrNodeWrapper.getProperty("phoneNumber").getString());
        properties.setProperty("cellphoneNumber", jcrNodeWrapper.getProperty("cellphoneNumber").getString());
        properties.setProperty("email", jcrNodeWrapper.getProperty("email").getString());
        properties.setProperty("newspapers", jcrNodeWrapper.getProperty("newspapers").getString());
        properties.setProperty("workLanguage", jcrNodeWrapper.getProperty("workLanguage").getString());
        properties.setProperty("typeOfAccreditation", jcrNodeWrapper.getProperty("typeOfAccreditation").getString());
        properties.setProperty("accreditedFor", jcrNodeWrapper.getProperty("accreditedFor").getString());
        JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
            @Override
            public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                JahiaUser journalistUser = jahiaUserManagerService.createUser(jcrNodeWrapper.getName(),
                        jcrNodeWrapper.getProperty("password").getString(), properties, session).getJahiaUser();
                session.save();
                JCRPublicationService.getInstance().publishByMainId(jcrNodeWrapper.getIdentifier(),
                        "default", "live", null, true, null);                return true;
            }
        });
    }

    public void modifyUser(PublishedNodeFact publishedNodeFact) throws RepositoryException {
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
        } else {
            JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
                @Override
                public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                    JCRUserNode userNode = jahiaUserManagerService.lookupUser(jcrNodeWrapper.getName(), session);
                    userNode.setProperty("title", jcrNodeWrapper.getProperty("title").getString());
                    userNode.setProperty("academicTitle", jcrNodeWrapper.getProperty("academicTitle").getString());
                    userNode.setProperty("firstName", jcrNodeWrapper.getProperty("firstName").getString());
                    userNode.setProperty("lastName", jcrNodeWrapper.getProperty("lastName").getString());
                    userNode.setProperty("adress", jcrNodeWrapper.getProperty("adress").getString());
                    userNode.setProperty("NPA", jcrNodeWrapper.getProperty("NPA").getString());
                    userNode.setProperty("place", jcrNodeWrapper.getProperty("place").getString());
                    userNode.setProperty("phoneNumber", jcrNodeWrapper.getProperty("phoneNumber").getString());
                    userNode.setProperty("cellphoneNumber", jcrNodeWrapper.getProperty("cellphoneNumber").getString());
                    userNode.setProperty("email", jcrNodeWrapper.getProperty("email").getString());
                    userNode.setProperty("newspapers", jcrNodeWrapper.getProperty("newspapers").getString());
                    userNode.setProperty("workLanguage", jcrNodeWrapper.getProperty("workLanguage").getString());
                    userNode.setProperty("typeOfAccreditation", jcrNodeWrapper.getProperty("typeOfAccreditation").getString());
                    userNode.setProperty("accreditedFor", jcrNodeWrapper.getProperty("accreditedFor").getString());
                    userNode.getSession().save();
                    JCRPublicationService.getInstance().publishByMainId(userNode.getIdentifier(),
                            "default", "live", null, true, null);
                    return true;
                }
            });
        }
    }

    public void setJahiaUserManagerService(JahiaUserManagerService jahiaUserManagerService) {
        this.jahiaUserManagerService = jahiaUserManagerService;
    }
}