
<#list enumList as singlEnum>
const ${singlEnum.name} = [
<#list singlEnum.constantList as constant>
    {
        label:"${constant.label}",
        value:"${constant.value}",
    }<#sep>,</#sep>
</#list>
];
</#list>

const EnumBoolean = [{
    label:"是",
    value:1,
},{
    label:"否",
    value:0,
}]


export {
<#list enumList as singlEnum>
    ${singlEnum.name},
</#list>
EnumBoolean,
}