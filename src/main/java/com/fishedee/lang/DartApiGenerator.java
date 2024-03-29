package com.fishedee.lang;

import com.fishedee.BusinessException;
import com.fishedee.CrashException;
import com.fishedee.EnumDTO;
import com.fishedee.SwaggerJson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DartApiGenerator {
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

        private String upperName;

        private String type;

        private String formatter = "";

        private String parser = "";

        private String copyer = "";
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

        private String responseParser = "";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnumConstant{
        private String value;

        private String label;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnumType{
        private String name;

        private List<EnumConstant> constantList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DTO{
        private String importPath = "";

        private List<EnumType> enumList = new ArrayList<>();

        private List<Type> typeList = new ArrayList<Type>();

        private List<Api> apiList = new ArrayList<Api>();
    }

    private String stripGeneric(String name){
        name = name.replaceAll("«|»","");
        return name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class FieldInfo{
        private String type;

        private String formatter;

        private String parser;

        private String copyer;
    }

    private static FieldInfo voidField = new FieldInfo()
            .setType("void")
            .setFormatter("")
            .setParser("")
            .setCopyer("");
    private static FieldInfo boolField = new FieldInfo()
                    .setType("bool")
                    .setFormatter("BoolHelper.toDynamic")
                    .setParser("BoolHelper.fromDynamic")
                    .setCopyer("BoolHelper.deepCopy");

    private static FieldInfo intField = new FieldInfo()
            .setType("int")
            .setFormatter("IntHelper.toDynamic")
            .setParser("IntHelper.fromDynamic")
            .setCopyer("IntHelper.deepCopy");

    private static FieldInfo doubleField = new FieldInfo()
            .setType("double")
            .setFormatter("DoubleHelper.toDynamic")
            .setParser("DoubleHelper.fromDynamic")
            .setCopyer("DoubleHelper.deepCopy");

    private static FieldInfo stringField = new FieldInfo()
            .setType("String")
            .setFormatter("StringHelper.toDynamic")
            .setParser("StringHelper.fromDynamic")
            .setCopyer("StringHelper.deepCopy");

    private static FieldInfo objectField = new FieldInfo()
            .setType("Object")
            .setFormatter("ObjectHelper.toDynamic")
            .setParser("ObjectHelper.fromDynamic")
            .setCopyer("ObjectHelper.deepCopy");

    private String getIndent(int depth){
        StringBuilder result = new StringBuilder();
        for( int i = 0 ; i < depth+1;i++){
            result.append("  ");
        }
        return result.toString();
    }
    private FieldInfo getSchemaDescription(SwaggerJson.Schema schema,int depth){
        if( schema.getType() == SwaggerJson.PropertyTypeEnum.BOOLEAN){
            return boolField;
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.INTEGER ||
                schema.getType() == SwaggerJson.PropertyTypeEnum.NUMBER){
            if( schema.getFormat() == SwaggerJson.PropertyFormatEnum.BIG_DECIMAL){
                return stringField;
            }else if(schema.getFormat() == SwaggerJson.PropertyFormatEnum.DOUBLE){
                return doubleField;
            }else{
                return intField;
            }
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.STRING){
            if( schema.getFormat() == SwaggerJson.PropertyFormatEnum.BYTE){
                return intField;
            }else if( schema.getFormat() == SwaggerJson.PropertyFormatEnum.DATE_TIME){
                return stringField;
            }else if( schema.getEnumList() != null ){
                String enumKey =  this.joinStr(schema.getEnumList());
                String enumTypeName = this.enumMapType.get(enumKey);
                if( enumTypeName == null ){
                    throw new BusinessException("找不到枚举体["+enumKey+"]");
                }
                return new FieldInfo()
                        .setType(enumTypeName)
                        .setFormatter(enumTypeName+".toDynamic")
                        .setParser(enumTypeName+".fromDynamic")
                        .setCopyer(enumTypeName+".deepCopy");
            }else {
                return stringField;
            }
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.ARRAY){
            //list类型
            FieldInfo subFieldInfo = this.getSchemaDescription(schema.getItems(),depth+1);
            String indent = getIndent(depth);
            String type = "List<"+subFieldInfo.type+">";
            String formatter = "ListHelper.wrapToDynamic<"+subFieldInfo.type+">((single){\n"+
                    indent+"  final handler = "+subFieldInfo.formatter+";\n"+
                    indent+"  return handler(single)!;\n"+
                    indent+"})";
            String parser = "ListHelper.wrapFromDynamic<"+subFieldInfo.type+">((single){\n"+
                    indent+"  final handler = "+subFieldInfo.parser+";\n"+
                    indent+"  return handler(single)!;\n"+
                    indent+"})";
            String copyer = "ListHelper.wrapDeepCopy<"+subFieldInfo.type+">((single){\n"+
                    indent+"  final handler = "+subFieldInfo.copyer+";\n"+
                    indent+"  return handler(single)!;\n"+
                    indent+"})";
            return new FieldInfo()
                    .setType(type)
                    .setFormatter(formatter)
                    .setParser(parser)
                    .setCopyer(copyer);
        }else if( schema.getRef() != null ) {
            String ref = schema.getRef();
            String prefix = "#/components/schemas/";
            if (ref.startsWith(prefix)) {
                String classTypeName = "Type" + this.stripGeneric(ref.substring(prefix.length()));
                return new FieldInfo()
                        .setType(classTypeName)
                        .setFormatter(classTypeName+".toDynamic")
                        .setParser(classTypeName+".fromDynamic")
                        .setCopyer(classTypeName+".deepCopy");
            } else {
                throw new BusinessException("未知的ref类型" + ref);
            }
        }else if( schema.getType() == SwaggerJson.PropertyTypeEnum.OBJECT){
            //map类型
            SwaggerJson.Schema childSchema = schema.getAdditionalProperties();
            if( childSchema != null ){
                FieldInfo subFieldInfo = this.getSchemaDescription(childSchema,depth+1);
                String indent = getIndent(depth);
                String type = "Map<String,"+subFieldInfo.type+">";
                String formatter = "MapHelper.wrapToDynamic<"+subFieldInfo.type+">((single){\n"+
                        indent + "  final handler = "+subFieldInfo.formatter+";\n"+
                        indent + "  return handler(single)!;\n"+
                        indent + "})";
                String parser = "MapHelper.wrapFromDynamic<"+subFieldInfo.type+">((single){\n"+
                        indent + "  final handler = "+subFieldInfo.parser+";\n"+
                        indent + "  return handler(single)!;\n"+
                        indent + "})";
                String copyer = "MapHelper.wrapDeepCopy<"+subFieldInfo.type+">((single){\n"+
                        indent + "  final handler = "+subFieldInfo.copyer+";\n"+
                        indent + "  return handler(single)!;\n"+
                        indent + "})";
                return new FieldInfo()
                        .setType(type)
                        .setFormatter(formatter)
                        .setParser(parser)
                        .setCopyer(copyer);
            }else{
                return objectField;
            }
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
                FieldInfo fieldInfo = this.getSchemaDescription(schema,2);
                Field field = new Field();
                field.setName(single2.getKey());
                field.setUpperName(this.firstUpper(single2.getKey()));
                field.setType(fieldInfo.getType());
                field.setFormatter(fieldInfo.getFormatter());
                field.setParser(fieldInfo.getParser());
                field.setCopyer(fieldInfo.getCopyer());
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

    private FieldInfo getResponseType(Map<String, SwaggerJson.Response> responseMap ){
        SwaggerJson.Response okResponse = responseMap.get("200");
        if( okResponse == null ){
            throw new BusinessException("找不到200返回下的Response");
        }
        SwaggerJson.Schema schema[] = new SwaggerJson.Schema[]{null};
        okResponse.getContent().values().stream().forEach((single)->{
            schema[0] = single.getSchema();
        });
        if( schema[0] == null ){
            return voidField;
        }else{
            return this.getSchemaDescription(schema[0],0);
        }

    }
    private List<Api> convertApi(SwaggerJson input){
        List<Api> allApiList = new ArrayList<>();
        input.getPaths().entrySet().stream().forEach(single->{
            String path = single.getKey();
            Map<String,SwaggerJson.Path> pathMap = single.getValue();

            List<Api> apiList = pathMap.entrySet().stream().map(single2->{
                Api result = new Api();
                FieldInfo responseType = this.getResponseType(single2.getValue().getResponses());
                result.setName(this.getApiName(path));
                result.setMethod(single2.getKey().toUpperCase());
                result.setUrl(path);
                result.setResponseType(responseType.getType());
                result.setResponseParser(responseType.getParser());
                return result;
            }).collect(Collectors.toList());

            allApiList.addAll(apiList);
        });

        return allApiList;
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

    public String generate(String importPath,List<EnumDTO.EnumInfo> enumInfoList,SwaggerJson input){
        this.enumMapType = this.convertToEnumMap(enumInfoList);
        List<Type> typeList = this.convertType(input);
        List<Api> apiList = this.convertApi(input);

        DTO result = new DTO();
        result.setImportPath(importPath);
        result.setTypeList(typeList);
        result.setApiList(apiList);
        result.setEnumList(enumInfoList.stream().map(single->{
            EnumType enumType = new EnumType();
            enumType.setName(this.getEnumTypeName(single));
            enumType.setConstantList(single.getConstantList().stream().map(single2->{
                return new EnumConstant(single2.getValue(),single2.getLabel());
            }).collect(Collectors.toList()));
            return enumType;
        }).collect(Collectors.toList()));

        return this.executeTemplate("dartApi.ftl",result);
    }
}
