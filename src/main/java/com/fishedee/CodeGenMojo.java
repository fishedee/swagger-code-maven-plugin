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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name="typescript")
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
            property = "outputFile",
            defaultValue = "swagger-code"
    )
    private String outputFile;

    @Parameter(
            property = "swaggerUrl",
            defaultValue = "http://localhost:8080/v2/api-docs"
    )
    private String swaggerUrl;

    public void execute()
        throws MojoExecutionException
    {
        Log log = getLog();

        SwaggerJsonGetter swaggerJsonGetter = GlobalBean.getSwaggerJsonGetter();
        TypescriptGenerator typescriptGenerator = GlobalBean.getTypescriptGenerator();
        CodeWriter codeWriter = GlobalBean.getCodeWriter();

        SwaggerJson result = swaggerJsonGetter.get(swaggerUrl);
        String code = typescriptGenerator.generate(result);
        codeWriter.write(this.outputFile,code);
    }
}
