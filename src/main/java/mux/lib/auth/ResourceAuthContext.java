package mux.lib.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAuthContext {
    private String id;
    private String type;
    private String domain;
    private int level;
}
