package com.fishedee;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TypescriptGenerator {
    private Configuration configuration;

    public void setConfiguration(Configuration configuration){
        this.configuration = configuration;
    }

    private String executeTemplate(String templateName,Object data){
        try{
            StringWriter stream = new StringWriter();
            Template tpl = this.configuration.getTemplate(templateName);
            tpl.process(data,stream);
            return stream.toString();
        }catch(IOException e ){
            throw new CrashException("无法读取模板",e);
        }catch(TemplateException e){
            throw new CrashException("模板执行错误",e);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field{
        private String name;

        private String type;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Type{
        private String name;

        private List<Field> fieldList = new ArrayList<Field>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Api{
        private String name;

        private String method;

        private String url;

        private String responseType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnumType{
        private String name;

        private List<String> constants;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DTO{
        private List<EnumType> enumTypeList = new ArrayList<>();

        private List<Type> typeList = new ArrayList<Type>();

        private List<Api> apiList = new ArrayList<Api>();

        private List<String> exportList = new ArrayList<String>();

    }

    private String stripGeneric(String name){
        name = name.replaceAll("«|»","");
        return name;
    }
    private String getSchemaDescription(SwaggerJson.Schema schema){
        if( schema.getType() == SwaggerJson.PropertyTypeEnum.INTEGER ||
                schema.getType() == SwaggerJson.PropertyTypeEnum.NUMBER){
            if( schema.getFormat() == SwaggerJson.PropertyFormatEnum.BIG_DECIMAL){
                return "BigDecimal";
            }else{
                return "number";
            }
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.STRING){
            if( schema.getFormat() == SwaggerJson.PropertyFormatEnum.BYTE){
                return "number";
            }else if( schema.getFormat() == SwaggerJson.PropertyFormatEnum.DATE_TIME){
                return "DateTime";
            }else if( schema.getEnumList() != null ){
                String enumKey =  this.joinStr(schema.getEnumList());
                String enumTypeName = this.enumMapType.get(enumKey);
                if( enumTypeName == null ){
                    throw new BusinessException("找不到枚举体["+enumKey+"]");
                }
                return enumTypeName;
            }else {
                return "string";
            }
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.ARRAY){
            //list类型
            return this.getSchemaDescription(schema.getItems())+"[]";
        }else if( schema.getRef() != null ) {
            String ref = schema.getRef();
            String prefix = "#/components/schemas/";
            if (ref.startsWith(prefix)) {
                return "Type" + this.stripGeneric(ref.substring(prefix.length()));
            } else {
                throw new BusinessException("未知的ref类型" + ref);
            }
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.OBJECT){
            //map类型
            String valueType = this.getSchemaDescription(schema.getAdditionalProperties());
            return "{\n" +
                    "[key in string]:"+valueType+"\n"+
                    "}";
        }else{
            throw new BusinessException("未定义的属性"+schema.getType());
        }
    }
    private List<Type> convertType(SwaggerJson input){
       return input.getComponents().getSchemas().entrySet().stream().map(single->{
            String definitionName = single.getKey();
            SwaggerJson.Definition definition = single.getValue();
            List<Field> fieldList = definition.getProperties().entrySet().stream().map(single2->{
                SwaggerJson.Schema schema = single2.getValue();
                Field field = new Field();
                field.setName(single2.getKey());
                field.setType(this.getSchemaDescription(schema));
                return field;
            }).collect(Collectors.toList());

            Type singleType = new Type();
            singleType.setName("Type"+this.stripGeneric(definitionName));
            singleType.setFieldList( fieldList);
            return singleType;
        }).collect(Collectors.toList());
    }

    private String firstUpper(String key){
       return key.substring(0,1).toUpperCase() + key.substring(1);
    }

    private String getApiName(String path){
        String[] pathSegments = path.split("/");
        StringBuilder result = new StringBuilder();
        for( String pathSeg : pathSegments){
            if( pathSeg.length() == 0 ){
                continue;
            }
            result.append(this.firstUpper(pathSeg));
        }
        return result.toString();
    }

    private String getResponseType(Map<String, SwaggerJson.Response> responseMap ){
        SwaggerJson.Response okResponse = responseMap.get("200");
        if( okResponse == null ){
            throw new BusinessException("找不到200返回下的Response");
        }
        SwaggerJson.Schema schema[] = new SwaggerJson.Schema[]{null};
        okResponse.getContent().values().stream().forEach((single)->{
             schema[0] = single.getSchema();
        });
        if( schema[0] == null ){
            return "void";
        }else{
            return this.getSchemaDescription(schema[0]);
        }

    }
    private List<Api> convertApi(SwaggerJson input){
        List<Api> allApiList = new ArrayList<>();
        input.getPaths().entrySet().stream().forEach(single->{
            String path = single.getKey();
            Map<String,SwaggerJson.Path> pathMap = single.getValue();

            List<Api> apiList = pathMap.entrySet().stream().map(single2->{
                Api result = new Api();
                result.setName(this.getApiName(path));
                result.setMethod(single2.getKey().toUpperCase());
                result.setUrl(path);
                result.setResponseType(this.getResponseType(single2.getValue().getResponses()));
                return result;
            }).collect(Collectors.toList());

            allApiList.addAll(apiList);
        });

        return allApiList;
    }

    private List<String> getExport(List<Type> typeList,List<Api> apiList,List<EnumDTO.EnumInfo> enumInfoList){
        List<String> result = new ArrayList<>();
        typeList.forEach(single->{
            result.add(single.getName());
        });
        apiList.forEach(single->{
            result.add(single.getName());
        });
        enumInfoList.forEach(single->{
            result.add(this.getEnumTypeName(single));
        });
        return result;
    }

    private String getEnumTypeName(EnumDTO.EnumInfo enumInfo){
        String fullName = enumInfo.getEncloseName()+enumInfo.getName();
        return "TypeEnum"+this.firstUpper(fullName);
    }

    private String joinStr(List<String> listStr){
        List<String> newList = listStr.stream().sorted().collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for( int i = 0 ;i != newList.size();i++){
            if( i != 0 ){
                builder.append("#");
            }
            builder.append(newList.get(i));
        }
        return builder.toString();
    }

    private List<String> getEnumConstants(EnumDTO.EnumInfo enumInfo){
        List<String> result = new ArrayList<>();
        enumInfo.constantList.forEach((single)->{
            result.add(single.getValue());
        });
        return result;
    }

    public Map<String,String> convertToEnumMap(List<EnumDTO.EnumInfo> enumInfoList ){
        return enumInfoList.stream().collect(Collectors.toMap(ref->{
            List<String> enumConstants = this.getEnumConstants(ref);
            return this.joinStr(enumConstants);
        },ref->{
            return this.getEnumTypeName(ref);
        }));
    }

    private Map<String,String> enumMapType = new HashMap<>();

    public String generate(List<EnumDTO.EnumInfo> enumInfoList,SwaggerJson input){
        this.enumMapType = this.convertToEnumMap(enumInfoList);
        List<Type> typeList = this.convertType(input);
        List<Api> apiList = this.convertApi(input);
        List<String> exportList = this.getExport(typeList,apiList,enumInfoList);

        DTO result = new DTO();
        result.setTypeList(typeList);
        result.setApiList(apiList);
        result.setEnumTypeList(enumInfoList.stream().map(single->{
            EnumType enumType = new EnumType();
            enumType.setName(this.getEnumTypeName(single));
            enumType.setConstants(this.getEnumConstants(single));
            return enumType;
        }).collect(Collectors.toList()));
        result.setExportList(exportList);

        return this.executeTemplate("typescript.ftl",result);
    }
}
