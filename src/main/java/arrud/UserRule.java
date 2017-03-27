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
import javax.jcr.Value;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class UserRule {
    @Autowired
    private JahiaUserManagerService jahiaUserManagerService;
    String[] massiveOfProperties = {"title", "academicTitle", "firstName", "firstName", "lastName", "adress",
            "NPA", "place", "phoneNumber", "cellphoneNumber", "email", "newspapers", "workLanguage",
            "typeOfAccreditation", "accreditedFor"};

    public void createUser(AddedNodeFact addedNodeFact) throws RepositoryException {
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
        }
    }

    public void setJahiaUserManagerService(JahiaUserManagerService jahiaUserManagerService) {
        this.jahiaUserManagerService = jahiaUserManagerService;
    }
}