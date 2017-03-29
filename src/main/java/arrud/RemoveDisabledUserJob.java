package arrud;

import org.jahia.services.SpringContextSingleton;
import org.jahia.services.content.*;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.scheduler.BackgroundJob;
import org.jahia.services.usermanager.JahiaUserManagerService;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.mail.Session;

/**
 * Created by root on 27.03.17.
 */
public class RemoveDisabledUserJob extends BackgroundJob {

    JahiaUserManagerService jahiaUserManagerService;

    public RemoveDisabledUserJob() {
        jahiaUserManagerService = (JahiaUserManagerService) SpringContextSingleton.getInstance().getBean(
                "JahiaUserManagerService");
    }

    @Override
    public void executeJahiaJob(JobExecutionContext jobExecutionContext) throws Exception {
        JCRSessionWrapper sessionLive = JCRSessionFactory.getInstance().getCurrentSystemSession("live",
                null, null);
        String query = "SELECT * FROM [jnt:journalist]";
        JCRNodeIteratorWrapper jcrNodeIteratorWrapper = getNodes(sessionLive, query);
        while (jcrNodeIteratorWrapper.hasNext()) {
            JCRNodeWrapper jcrNodeWrapper = (JCRNodeWrapper) jcrNodeIteratorWrapper.nextNode();
            Boolean isEnabled = jcrNodeWrapper.getProperty("isEnabled").getBoolean();
            if (!isEnabled) {
                removeUser(jcrNodeWrapper);
                String name = jcrNodeWrapper.getName();
                removeJournalistInDefault(jcrNodeWrapper.getName());
                jcrNodeWrapper.remove();
                sessionLive.save();
            }
        }
    }
    private JCRNodeIteratorWrapper getNodes (JCRSessionWrapper session, String query) throws RepositoryException {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        Query q  = queryManager.createQuery(query, Query.JCR_SQL2);
        QueryResultWrapper queryResult = (QueryResultWrapper) q.execute();
        return queryResult.getNodes();
    }

    private void removeUser(JCRNodeWrapper jcrNodeWrapper) throws RepositoryException {
        final String userPath = jahiaUserManagerService.getUserPath(jcrNodeWrapper.getName());
        JCRTemplate.getInstance().doExecuteWithSystemSession(new JCRCallback<Boolean>() {
            @Override
            public Boolean doInJCR(final JCRSessionWrapper session) throws RepositoryException {
                jahiaUserManagerService.deleteUser(userPath, session);
                session.save();
                return true;
            }
        });
    }

    private void removeJournalistInDefault(String name) throws RepositoryException {
        JCRSessionWrapper sessionDefault = JCRSessionFactory.getInstance().getCurrentSystemSession(
                "default",null, null);
        String query = "SELECT * FROM [jnt:journalist] WHERE [j:nodename]='" + name + "'";
        JCRNodeIteratorWrapper jcrNodeIteratorWrapper = getNodes(sessionDefault, query);
        if (jcrNodeIteratorWrapper.hasNext()) {
            JCRNodeWrapper jcrNodeWrapper = (JCRNodeWrapper) jcrNodeIteratorWrapper.nextNode();
            jcrNodeWrapper.remove();
            sessionDefault.save();
        }
    }

}
