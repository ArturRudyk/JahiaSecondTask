package arrud;

import org.jahia.services.content.*;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.taglibs.jcr.AbstractJCRTag;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

/**
 * Created by root on 10.04.17.
 */
public class SetPropertyToJournalistTag extends AbstractJCRTag {

    private String propertyName;

    private String propertyValue;

    private String journalist;

    public int doStartTag() {
        try {
            JCRNodeWrapper jcrNodeWrapper = getJournalist(journalist);
            setProperty(jcrNodeWrapper, propertyName, propertyValue);
            jcrNodeWrapper.getSession().save();
            JCRPublicationService.getInstance().publishByMainId(jcrNodeWrapper.getIdentifier(),
                    "default", "live", null, true, null);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
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

    private void setProperty(JCRNodeWrapper jcrNodeWrapper, String propertyName, String propertyValue) throws RepositoryException {
        if (propertyValue != null) {
            if (jcrNodeWrapper.getProperty(propertyName).isMultiple()) {
                String[] multipleProperty = propertyValue.split(" ");
                jcrNodeWrapper.setProperty(propertyName, multipleProperty);
            } else {
                jcrNodeWrapper.setProperty(propertyName, propertyValue);
            }
        } else {
            jcrNodeWrapper.setProperty(propertyName, "");
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getJournalist() {
        return journalist;
    }

    public void setJournalist(String journalist) {
        this.journalist = journalist;
    }
}
