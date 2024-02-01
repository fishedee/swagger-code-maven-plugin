package com.fishedee;

import com.fishedee.target.CodeGenGenerator;

import java.util.HashMap;
import java.util.Map;

public class CodeGenGeneratorFactory {
    private Map<CodeGenType, CodeGenGenerator> codegenGeneratorMap = new HashMap<>();

    public CodeGenGeneratorFactory(){

    }

    public CodeGenGenerator getGenerator(CodeGenType name){
        CodeGenGenerator generator = this.codegenGeneratorMap.get(name);
        if( generator == null ){
            String msg = String.format("找不到代码生成器：[%s]",name);
            throw new BusinessException(msg);
        }
        return generator;
    }

    public void addGenerator(CodeGenType name,CodeGenGenerator codeGenGenerator){
        CodeGenGenerator generator = this.codegenGeneratorMap.get(name);
        if( generator == null ){
            String msg = String.format("重复的代码生成器：[%s]",name);
            throw new BusinessException(msg);
        }
        this.codegenGeneratorMap.put(name,codeGenGenerator);
    }
}
