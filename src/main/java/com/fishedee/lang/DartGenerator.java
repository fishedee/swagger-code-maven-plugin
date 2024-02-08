package com.fishedee.lang;

import com.fishedee.GlobalBean;

public class DartGenerator implements CodeGenGenerator{
    private DartApiGenerator dartApiGenerator;

    public DartGenerator(){
        this.dartApiGenerator = new DartApiGenerator();
        dartApiGenerator.setConfiguration(GlobalBean.getFreeMarkerConfiguration());
    }

    @Override
    public String generateApi(CodeGenGeneratorParams params){
        return this.dartApiGenerator.generate(
                params.getApiImportPath(),
                params.getEnumList(),
                params.getSwaggerApi());
    }

    @Override
    public String generateEnum(CodeGenGeneratorParams params){
        return "";
    }
}
