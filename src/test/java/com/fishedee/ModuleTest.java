package com.fishedee;

import com.fishedee.target.CodeGenGeneratorParams;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ModuleTest {

    private SwaggerJson getOpenAPI(){
        String swaggerUrl = "http://localhost:8110/v3/api-docs";
        SwaggerJsonGetter swaggerJsonGetter = GlobalBean.getSwaggerJsonGetter();
        SwaggerJson result = swaggerJsonGetter.get(swaggerUrl);
        return result;
    }

    private EnumDTO getEnumAPI(){
        String enumUrl = "http://localhost:8110/enum/getAll";
        EnumGetter enumGetter = GlobalBean.getEnumGetter();
        EnumDTO result = enumGetter.get(enumUrl);
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
            input = GlobalBean.class.getResourceAsStream("/freeMarker/typescriptApi.ftl");
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
        SwaggerJson swaggerJson = this.getOpenAPI();
        EnumDTO enumDTO = this.getEnumAPI();
        CodeGenGeneratorFactory factory = GlobalBean.getCodeGenGeneratorFactory();
        String code = factory.getGenerator(CodeGenType.TYPESCRIPT).generateApi(new CodeGenGeneratorParams()
                .setApiImportPath("cc")
                .setSwaggerApi(swaggerJson)
                .setEnumList(enumDTO.getData()));
        System.out.println(code);
    }
}
