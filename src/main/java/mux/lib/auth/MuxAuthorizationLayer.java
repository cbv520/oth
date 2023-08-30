package mux.lib.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface MuxAuthorizationLayer {

    default <T> List<T> filterResources(List<T> resources, User user, Map<String, ResourceAuthContext> resourceContext, Function<T, String> idGetter) {
        List<T> filtered = new ArrayList<>();
        for (T resource : resources) {
            var id = idGetter.apply(resource);
            var ctx = resourceContext.get(id);
            if (ctx != null && this.userAuthorizedRead(ctx, user)) {
                filtered.add(resource);
            }
        }
        return filtered;
    }

    default List<ResourceAuthContext> filterResourceContexts(List<ResourceAuthContext> ctxs, User user) {
        List<ResourceAuthContext> filtered = new ArrayList<>();
        for (ResourceAuthContext ctx : ctxs) {
            if (this.userAuthorizedRead(ctx, user)) {
                filtered.add(ctx);
            }
        }
        return filtered;
    }

    default List<User> filterUsers(List<User> users, ResourceAuthContext ctx) {
        List<User> filtered = new ArrayList<>();
        for (User user : users) {
            if (this.userAuthorizedRead(ctx, user)) {
                filtered.add(user);
            }
        }
        return filtered;
    }

    boolean userAuthorizedRead(ResourceAuthContext resourceAuthContext, User user);

    boolean userAuthorizedWrite(ResourceAuthContext resourceAuthContext, User user);

    boolean userAuthorizedExecute(ResourceAuthContext resourceAuthContext, User user);
}
