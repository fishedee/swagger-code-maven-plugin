package com.fishedee;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fishedee.target.CodeGenGenerator;
import com.fishedee.target.CodeGenGeneratorParams;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

@Mojo(name="codegen")
public class CodeGenMojo
    extends AbstractMojo
{

    @Parameter(
            property = "project",
            required = true,
            readonly = true
    )
    private MavenProject mavenProject;

    @Parameter(
            property = "outputTargets"
    )
    private List<CodeGenTarget> outputTargets;

    @Parameter(
            property = "swaggerUrl",
            defaultValue = "http://localhost:8080/v2/api-docs"
    )
    private String swaggerUrl;

    @Parameter(
            property = "enumUrl"
    )
    private String enumUrl;

    public void execute()
        throws MojoExecutionException
    {
        Log log = getLog();
        SwaggerJsonGetter swaggerJsonGetter = GlobalBean.getSwaggerJsonGetter();
        EnumGetter enumGetter = GlobalBean.getEnumGetter();
        CodeGenGeneratorFactory codeGenGeneratorFactory = GlobalBean.getCodeGenGeneratorFactory();

        //获取swagger和enum
        final List<EnumDTO.EnumInfo> enumInfoList = new ArrayList<>();
        SwaggerJson swaggerApi = swaggerJsonGetter.get(swaggerUrl);
        if( this.enumUrl != null && this.enumUrl.trim().length() != 0 ){
            EnumDTO enumDTO = enumGetter.get(this.enumUrl);
            if( enumDTO.getCode() != 0 ){
                throw new BusinessException("获取枚举失败:"+enumDTO.getMsg());
            }
            enumInfoList.addAll(enumDTO.getData());
        }

        //生成数据
        outputTargets.forEach(outputTarget->{
           CodeGenGenerator codeGenGenerator = codeGenGeneratorFactory.getGenerator(outputTarget.getType());
           this.generateTarget(codeGenGenerator,outputTarget,swaggerApi,enumInfoList);
        });
    }

    private void generateTarget(CodeGenGenerator generator, CodeGenTarget codeGenTarget, SwaggerJson swaggerApi,List<EnumDTO.EnumInfo> enumInfoList){
        CodeWriter codeWriter = GlobalBean.getCodeWriter();
        CodeGenGeneratorParams params = new CodeGenGeneratorParams()
                .setApiImportPath(codeGenTarget.getApiImportPath())
                .setSwaggerApi(swaggerApi)
                .setEnumImportPath(codeGenTarget.getEnumImportPath())
                .setEnumList(enumInfoList);
        String apiOutputFile = codeGenTarget.getApiOutputFile();
        if( apiOutputFile != null && apiOutputFile.trim().length() != 0 ){
            String code = generator.generateApi(params);
            codeWriter.write(codeGenTarget.getApiOutputFile(),code);
        }

        String enumOutputFile = codeGenTarget.getEnumOutputFile();
        if( enumOutputFile != null && enumOutputFile.trim().length() != 0 ){
            String code2 = generator.generateEnum(params);
            codeWriter.write(enumOutputFile,code2);
        }
    }
}
