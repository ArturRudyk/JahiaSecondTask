package arrud;

import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.services.content.JCRNodeIteratorWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.query.QueryResultWrapper;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;

import javax.jcr.Node;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ReverseAction extends Action {

    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, final Resource resource, JCRSessionWrapper session, final Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        String query = "SELECT * FROM [jnt:judgeModel]";
        Query q = queryManager.createQuery(query, Query.JCR_SQL2);
        QueryResultWrapper queryResult = (QueryResultWrapper) q.execute();
        JCRNodeIteratorWrapper jcrNodeIteratorWrapper = queryResult.getNodes();
        while (jcrNodeIteratorWrapper.hasNext()) {
            Node node = (Node)jcrNodeIteratorWrapper.next();
            String lastName = node.getProperty("lastName").getString();
            String firstName = node.getProperty("firstName").getString();
            node.setProperty("lastName", firstName);
            node.setProperty("firstName", lastName);
            node.getSession().save();
        }
        return new ActionResult(HttpServletResponse.SC_OK, resource.getNode().getResolveSite().getPath() + "/home");
    }
}