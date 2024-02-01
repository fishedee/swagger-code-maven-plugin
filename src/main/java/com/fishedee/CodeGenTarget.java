package com.fishedee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.maven.plugins.annotations.Parameter;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeGenTarget {
    CodeGenType type;

    private String apiOutputFile;

    private String apiImportPath;

    private String enumOutputFile;

    private String enumImportPath;
}
