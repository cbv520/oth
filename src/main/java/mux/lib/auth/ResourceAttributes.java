package mux.lib.auth;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat(shape=JsonFormat.Shape.OBJECT)
public class ResourceAttributes extends HashMap<String, String> {

    private String id;
    private ResourceType type;

    @JsonAnySetter
    @Override
    public String put(String key, String value) {
        return super.put(key, value);
    }

    @JsonAnyGetter
    @Override
    public String get(Object key) {
        return super.get(key);
    }

    @JsonIgnore
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    enum ResourceType {
        A,
        B,
        C
    }
}
