package mux.lib.auth;

import lombok.AllArgsConstructor;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class RestrictedTriplestore {

    private final MuxAuthorizationLayer authorizationLayer;
    private final Dataset dataset;

    private Model getAllData(User user) {
        Model data = ModelFactory.createDefaultModel();
        authorizationLayer.filterResourceContexts(getGraphContexts(), user).stream()
                .map(ResourceAuthContext::getId)
                .map(dataset::getNamedModel)
                .forEach(data::add);
        return data;
    }

    private Model getModel(String graphUri, User user) {
        var model = dataset.getNamedModel(graphUri);
        var ctx = getGraphContext(graphUri);
        if (model != null && ctx != null && authorizationLayer.userAuthorizedRead(ctx, user)) {
            return model;
        }
        return null;
    }

    private QueryExecution query(String query, User user) {
        return QueryExecutionFactory.create(query, getAllData(user));
    }



    private List<ResourceAuthContext> getGraphContexts() {
        return new ArrayList<>();
    }

    private ResourceAuthContext getGraphContext(String graphUri) {
        if (graphUri.equals(""))
            return null;
        return new ResourceAuthContext();
    }
}
