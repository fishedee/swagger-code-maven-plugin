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
import java.util.List;
import java.util.stream.Collectors;

public class TypescriptSelectGenerator {

    private Configuration configuration;

    public void setConfiguration(Configuration configuration){
        this.configuration = configuration;
    }

    private String firstUpper(String key){
        return key.substring(0,1).toUpperCase() + key.substring(1);
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

    private String getEnumSelectName(EnumDTO.EnumInfo enumInfo){
        String fullName = enumInfo.getEncloseName()+enumInfo.getName();
        return "WidgetSelectEnum"+this.firstUpper(fullName);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SingleEnum{
        private String name;

        public List<EnumDTO.Constant> constantList = new ArrayList<>();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DTO{
        public List<SingleEnum> enumList = new ArrayList<>();
    }

    public String generate(List<EnumDTO.EnumInfo> enumInfoList){

        DTO result = new DTO();
        result.setEnumList(enumInfoList.stream().map(single->{
            SingleEnum singleEnum = new SingleEnum();
            singleEnum.setName(this.getEnumSelectName(single));
            singleEnum.setConstantList(single.getConstantList());
            return singleEnum;
        }).collect(Collectors.toList()));

        return this.executeTemplate("typescriptSelect.ftl",result);
    }
}
