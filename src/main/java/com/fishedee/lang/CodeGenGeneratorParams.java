package com.fishedee.lang;

import com.fishedee.EnumDTO;
import com.fishedee.SwaggerJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CodeGenGeneratorParams {

    private String apiImportPath;

    private SwaggerJson swaggerApi;

    private String enumImportPath;

    private List<EnumDTO.EnumInfo> enumList;
}
