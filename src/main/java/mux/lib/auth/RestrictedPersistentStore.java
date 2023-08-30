package mux.lib.auth;

import lombok.AllArgsConstructor;
import mux.lib.auth.ext.PS;
import mux.lib.auth.ext.P;
import mux.lib.auth.ext.S;

import java.util.List;

@AllArgsConstructor
public class RestrictedPersistentStore {

    private final MuxAuthorizationLayer authorizationLayer;
    private final PS resourceStore;
    private final AuthorizationStore authorizationStore;

    public List<S> getAllS(User user) {
        return authorizationLayer.filterResources(resourceStore.getAllS(), user, authorizationStore.getResourceAuthContexts(), S::getSId);
    }

    public List<P> getAllP(User user) {
        return authorizationLayer.filterResources(resourceStore.getAllP(), user, authorizationStore.getResourceAuthContexts(), P::getPId);
    }

    public void save(S s, ResourceAuthContext ctx, User user) {
        ctx.setId(s.getSId());
        if (authorizationLayer.userAuthorizedWrite(ctx, user)) {
            resourceStore.save(s);
            authorizationStore.save(ctx);
        }
    }

    public void save(P p, ResourceAuthContext ctx, User user) {
        ctx.setId(p.getPId());
        if (authorizationLayer.userAuthorizedWrite(ctx, user)) {
            resourceStore.save(p);
            authorizationStore.save(ctx);
        }
    }
}
