import {cacheRequest} from '@/util/request';

<#list typeList as singleType>
    type ${singleType.name} = {
        <#list singleType.fieldList as field>
            ${field.name}:${field.type};
        </#list>
    }
</#list>

<#list apiList as singleApi>
    const ${singleApi.name} = async (data:any)=>{
        return await cacheRequest.request({
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