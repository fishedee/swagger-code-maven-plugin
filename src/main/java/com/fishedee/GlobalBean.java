package com.fishedee;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class GlobalBean {
    private static ObjectMapper objectMapper;

    private static OkHttpClient okHttpClient;

    private static SwaggerJsonGetter swaggerJsonGetter;

    private static Configuration configuration;

    public synchronized static SwaggerJsonGetter getSwaggerJsonGetter(){
        if( swaggerJsonGetter != null ){
            return swaggerJsonGetter;
        }
        swaggerJsonGetter = new SwaggerJsonGetter();
        swaggerJsonGetter.setObjectMapper(getObjectMapper());
        swaggerJsonGetter.setOkHttpClient(getOkHttpClient());
        return swaggerJsonGetter;
    }

    public synchronized static OkHttpClient getOkHttpClient(){
        if( okHttpClient != null ){
            return okHttpClient;
        }
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    public synchronized static ObjectMapper getObjectMapper(){
        if( objectMapper != null ){
            return objectMapper;
        }
        objectMapper = new ObjectMapper();
        //未知属性反序列化，不会报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING ,true);
        return objectMapper;
    }

    public synchronized static Configuration getFreeMarkerConfiguration(){
        if( configuration != null ){
            return configuration;
        }
        configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(new GlobalBean().getClass(), "/freeMarker");
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return configuration;
    }

    public synchronized  static TypescriptGenerator createTypescriptGenerator(){
        TypescriptGenerator typescriptGenerator = new TypescriptGenerator();
        typescriptGenerator.setConfiguration(getFreeMarkerConfiguration());
        return typescriptGenerator;
    }

    public synchronized  static TypescriptSelectGenerator createTypescriptSelectGenerator(){
        TypescriptSelectGenerator typescriptGenerator = new TypescriptSelectGenerator();
        typescriptGenerator.setConfiguration(getFreeMarkerConfiguration());
        return typescriptGenerator;
    }


    private static EnumGetter enumGetter;

    public synchronized  static EnumGetter getEnumGetter(){
        if(enumGetter != null){
            return enumGetter;
        }
        enumGetter = new EnumGetter();
        enumGetter.setObjectMapper(getObjectMapper());
        enumGetter.setOkHttpClient(getOkHttpClient());
        return enumGetter;
    }

    private static CodeWriter codeWriter;

    public  synchronized  static CodeWriter getCodeWriter(){
        if( codeWriter != null ){
            return codeWriter;
        }
        codeWriter = new CodeWriter();
        return codeWriter;
    }
}
