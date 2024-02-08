package com.fishedee.lang;

public interface CodeGenGenerator {
    String generateApi(CodeGenGeneratorParams params);

    String generateEnum(CodeGenGeneratorParams params);
}
