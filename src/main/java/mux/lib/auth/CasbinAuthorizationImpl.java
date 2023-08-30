package mux.lib.auth;

import lombok.AllArgsConstructor;
import org.casbin.jcasbin.main.Enforcer;

@AllArgsConstructor
public class CasbinAuthorizationImpl implements MuxAuthorizationLayer {

    private final Enforcer enforcer;

    @Override
    public boolean userAuthorizedRead(ResourceAuthContext resourceAuthContext, User user) {
        return enforcer.enforce(user, resourceAuthContext, "r");
    }

    @Override
    public boolean userAuthorizedWrite(ResourceAuthContext resourceAuthContext, User user) {
        return enforcer.enforce(user, resourceAuthContext, "w");
    }

    @Override
    public boolean userAuthorizedExecute(ResourceAuthContext resourceAuthContext, User user) {
        return enforcer.enforce(user, resourceAuthContext, "x");
    }
}
