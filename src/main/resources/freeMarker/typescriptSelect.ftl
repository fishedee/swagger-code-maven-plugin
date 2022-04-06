import createEnumSelect from '@/util/enumSelect';

<#list enumList as singlEnum>
const ${singlEnum.name} = createEnumSelect([
<#list singlEnum.constantList as constant>
    {
        label:"${constant.label}",
        value:"${constant.value}",
    }<#sep>,</#sep>
</#list>
]);
</#list>

export {
<#list enumList as singlEnum>
    ${singlEnum.name},
</#list>
}