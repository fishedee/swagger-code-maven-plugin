package com.fishedee;

import jdk.nashorn.internal.objects.Global;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ModuleTest {

    /*
    private SwaggerJson getOpenAPI(){
        String swaggerUrl = "http://localhost:7878/v3/api-docs";
        SwaggerJsonGetter swaggerJsonGetter = GlobalBean.getSwaggerJsonGetter();
        SwaggerJson result = swaggerJsonGetter.get(swaggerUrl);
        return result;
    }

    public void testGetter(){
        SwaggerJson json = getOpenAPI();
        System.out.println(json.toString());
    }

    public void loadProperties1()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream input = null;
        try
        {
            input = GlobalBean.class.getResourceAsStream("/freeMarker/typescript.ftl");
            byte[] buffer = new byte[1024];
            int length = 0;
            while( (length = input.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }
            System.out.println(outputStream.toString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerator(){
        TypescriptGenerator generator = GlobalBean.getTypescriptGenerator();
        SwaggerJson swaggerJson = this.getOpenAPI();
        String result = generator.generate(swaggerJson);
        System.out.println(result.toString());
    }
     */
}
