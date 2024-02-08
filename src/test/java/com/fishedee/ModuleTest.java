package com.fishedee;

import com.fishedee.lang.CodeGenGeneratorParams;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class ModuleTest {

    private SwaggerJson getNetworkOpenAPI(){
        String swaggerUrl = "http://localhost:8110/v3/api-docs";
        SwaggerJsonGetter swaggerJsonGetter = GlobalBean.getSwaggerJsonGetter();
        SwaggerJson result = swaggerJsonGetter.get(swaggerUrl);
        return result;
    }

    private SwaggerJson getLocalOpenAPI(){
        byte[] info = this.loadResources("/swagger.json");
        SwaggerJsonGetter swaggerJsonGetter = GlobalBean.getSwaggerJsonGetter();
        SwaggerJson result = swaggerJsonGetter.parseData(info);
        return result;
    }

    private EnumDTO getNetworkEnumAPI(){
        String enumUrl = "http://localhost:8110/enum/getAll";
        EnumGetter enumGetter = GlobalBean.getEnumGetter();
        EnumDTO result = enumGetter.get(enumUrl);
        return result;
    }

    private EnumDTO getLocalEnumAPI(){
        byte[] info = this.loadResources("/enum.json");
        EnumGetter enumGetter = GlobalBean.getEnumGetter();
        EnumDTO result = enumGetter.parseData(info);
        return result;
    }

    public void testGetter(){
        SwaggerJson json = getNetworkOpenAPI();
        System.out.println(json.toString());
    }

    public byte[] loadResources(String path)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream input = null;
        try{
            input = GlobalBean.class.getResourceAsStream(path);
            byte[] buffer = new byte[1024];
            int length = 0;
            while( (length = input.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void testTypescriptGenerator(){
        SwaggerJson swaggerJson = this.getLocalOpenAPI();
        EnumDTO enumDTO = this.getLocalEnumAPI();
        CodeGenGeneratorFactory factory = GlobalBean.getCodeGenGeneratorFactory();
        String code = factory.getGenerator(CodeGenType.TYPESCRIPT).generateApi(new CodeGenGeneratorParams()
                .setApiImportPath("cc")
                .setSwaggerApi(swaggerJson)
                .setEnumList(enumDTO.getData()));
        System.out.println(code);
    }

    @Test
    public void testDartGenerator()throws Exception{
        SwaggerJson swaggerJson = this.getLocalOpenAPI();
        EnumDTO enumDTO = this.getLocalEnumAPI();
        CodeGenGeneratorFactory factory = GlobalBean.getCodeGenGeneratorFactory();
        String code = factory.getGenerator(CodeGenType.DART).generateApi(new CodeGenGeneratorParams()
                .setApiImportPath("package:trade_app/util/request.dart")
                .setSwaggerApi(swaggerJson)
                .setEnumList(enumDTO.getData()));
        FileUtils.write(new File("api.dart"),code,"UTF-8");
    }
}
