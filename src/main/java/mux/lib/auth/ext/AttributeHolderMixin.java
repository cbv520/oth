package mux.lib.auth.ext;

import mux.lib.auth.ResourceAttributes;

import java.util.Arrays;

public interface AttributeHolderMixin {

    default String getId() {
        try {
            return (String) Arrays.stream(getClass().getDeclaredFields())
                    .filter(f -> {
                        var b = f.isAnnotationPresent(Id.class);
                        f.setAccessible(true);
                        return b;
                    })
                    .findFirst().get()
                    .get(this);
        } catch (Exception e) {
            return null;
        }
    }

    default void setAttributes(ResourceAttributes attrs) {
        getStore().save(getId(), getClass(), attrs);
    }

    default ResourceAttributes getAttributes() {
        return getStore().get(getId(), getClass());
    }

    default ResourceAttributeStore getStore() {
        return ResourceAttributeStore.getSingleton();
    }
}
