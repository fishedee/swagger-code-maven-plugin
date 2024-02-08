package com.fishedee.lang;

import com.fishedee.GlobalBean;

public class TypescriptGenerator implements CodeGenGenerator {
    private TypescriptApiGenerator typescriptApiGenerator;

    private TypescriptEnumGenerator typescriptEnumGenerator;

    public TypescriptGenerator(){
        this.typescriptApiGenerator = new TypescriptApiGenerator();
        typescriptApiGenerator.setConfiguration(GlobalBean.getFreeMarkerConfiguration());

        this.typescriptEnumGenerator = new TypescriptEnumGenerator();
        typescriptEnumGenerator.setConfiguration(GlobalBean.getFreeMarkerConfiguration());
    }

    @Override
    public String generateApi(CodeGenGeneratorParams params){
        return this.typescriptApiGenerator.generate(
                params.getApiImportPath(),
                params.getEnumList(),
                params.getSwaggerApi());
    }

    @Override
    public String generateEnum(CodeGenGeneratorParams params){
        return this.typescriptEnumGenerator.generate(params.getEnumList());
    }
}
