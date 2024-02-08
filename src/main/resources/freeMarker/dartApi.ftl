import '${importPath}';

abstract interface class IData{
    dynamic operator[](String name);
    void operator[]=(String name, dynamic value);
}

mixin IDataMixin on
<#list typeList as singleType>
class ${singleType.name}{
    ${singleType.name}(){
    }

    <#list singleType.fieldList as field>
        ${field.type}${field.name}:;
    </#list>
}
</#list>

<#list apiList as singleApi>
    const ${singleApi.name} = async (data:any)=>{
    return await myRequest({
    method:"${singleApi.method}",
    url:"${singleApi.url}",
    data:data,
    }) as ${singleApi.responseType};
    }
</#list>

export {
<#list exportList as singleExport>
    ${singleExport},
</#list>
}