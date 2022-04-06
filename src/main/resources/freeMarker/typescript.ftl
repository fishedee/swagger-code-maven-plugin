import myRequest from '@/util/request';

type BigDecimal = string;

type DateTime = string;

<#list enumTypeList as singleEnumType>
<#list singleEnumType.constants>
    type ${singleEnumType.name} = <#items as constant> '${constant}' <#sep>|</#sep></#items>
    <#else>
    type ${singleEnumType.name} = ''
</#list>
</#list>

<#list typeList as singleType>
type ${singleType.name} = {
    <#list singleType.fieldList as field>
        ${field.name}:${field.type};
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