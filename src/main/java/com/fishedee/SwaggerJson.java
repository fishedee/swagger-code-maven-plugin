package com.fishedee;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerJson {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Schema{
        @JsonProperty("$ref")
        private String ref;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String description;

        //这个字段可能为空
        private Schema schema;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Path{
        private List<String> tags = new ArrayList<String>();

        private String summary;

        private Map<String,Response> responses = new HashMap<String, Response>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Items{
        @JsonProperty("$ref")
        private String ref;
    }

    public enum PropertyTypeEnum{
        array,
        integer,
        number,
        string,
    }

    public enum PropertyFormatEnum{
        int32,
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Property{
        private PropertyTypeEnum type;

        //可能为空
        private PropertyFormatEnum format;

        //可能为空
        private Items items;
    }

    public enum DefinitionTypeEnum{
        object,
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Definition{
        private String title;

        private DefinitionTypeEnum type;

        private Map<String,Property> properties = new LinkedHashMap<String, Property>();
    }

    private String swagger;

    private Map<String,Map<String,Path>> paths = new LinkedHashMap<String, Map<String, Path>>();

    private Map<String,Definition> definitions = new LinkedHashMap<String, Definition>();

}
