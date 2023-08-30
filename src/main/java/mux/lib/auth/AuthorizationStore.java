package mux.lib.auth;

import java.util.Map;

public interface AuthorizationStore {

    Map<String, ResourceAuthContext> getResourceAuthContexts();

    void save(ResourceAuthContext ctx);

}
