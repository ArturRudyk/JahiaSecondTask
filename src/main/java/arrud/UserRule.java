package arrud;

import org.jahia.registries.ServicesRegistry;
import org.jahia.services.content.*;
import org.jahia.services.content.decorator.JCRUserNode;
import org.jahia.services.content.rules.AddedNodeFact;
import org.jahia.services.content.rules.User;
import org.jahia.services.usermanager.JahiaUser;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CreateUserRule {
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
                return true;
            }
        });
    }

    public void setJahiaUserManagerService(JahiaUserManagerService jahiaUserManagerService) {
        this.jahiaUserManagerService = jahiaUserManagerService;
    }
}
