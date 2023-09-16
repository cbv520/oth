package mux.lib.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Configuration
public class RestConfig {

    @Bean
    public AbstractJackson2HttpMessageConverter httpMapper() {
        var httpMapper = new MappingJackson2HttpMessageConverter();
        var yamlType = new MediaType("application", "x-yaml");
        httpMapper.setSupportedMediaTypes(List.of(
                MediaType.APPLICATION_JSON,
                yamlType
        ));
        httpMapper.registerObjectMappersForType(Object.class, registrar ->
                registrar.put(yamlType, new ObjectMapper(new YAMLFactory()))
        );
        return httpMapper;
    }

    @Bean
    public OpenApiCustomiser openApiConfig() {
        return openAPI -> {
            addExamples(openAPI);
            orderControllers(openAPI);
        };
    }

    private void orderControllers(OpenAPI openAPI) {

    }

    private void addExamples(OpenAPI openAPI) {
        try {
            var mapper = new ObjectMapper(new YAMLFactory());
            var examples = mapper.readValue(getClass().getResourceAsStream("/examples.yml"), new TypeReference<Map<String, Object>>() {});
            examples.forEach((path, example) ->
                openAPI.getPaths()
                        .get(path)
                        .getPost()
                        .getRequestBody()
                        .getContent().forEach((e,mediaType) -> mediaType.example(example))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
