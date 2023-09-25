package mux.lib.auth.handshake;

import lombok.Data;

@Data
public class Proposal {
    public String id;
    public String state;
}
