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
    public static class ResponseContent{
        private Schema schema;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private String description;

        private Map<String,ResponseContent> content = new LinkedHashMap<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Path{
        private List<String> tags = new ArrayList<String>();

        private String summary;

        private Map<String,Response> responses = new LinkedHashMap<>();
    }

    public enum PropertyTypeEnum{
        ARRAY("array"),
        INTEGER("integer"),
        NUMBER("number"),
        STRING("string"),
        OBJECT("object");

        private String name;

        private PropertyTypeEnum(String name){
            this.name = name;
        }

        public String toString(){
            return this.name;
        }
    }

    public enum PropertyFormatEnum{
        INT32("int32"),
        INT64("int64"),
        BYTE("byte"),
        BIG_DECIMAL("bigdecimal"),
        DATE_TIME("date-time");

        private String name;

        private PropertyFormatEnum(String name){
            this.name = name;
        }

        public String toString(){
            return this.name;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Schema{
        private PropertyTypeEnum type;

        //可能为空
        private PropertyFormatEnum format;

        @JsonProperty("$ref")
        private String ref;

        //可能为空
        @JsonProperty("enum")
        private List<String> enumList;

        //可能为空
        private Schema items;

        //可能为空
        private Schema additionalProperties;
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

        private Map<String,Schema> properties = new LinkedHashMap<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Components{
        private Map<String,Definition> schemas = new LinkedHashMap<>();
    }

    private String openapi;

    private Map<String,Map<String,Path>> paths = new LinkedHashMap<>();

    private Components components;

}
