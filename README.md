# swaggerCode-maven-plugin

![Release](https://jitpack.io/v/fishedee/swagger-code-maven-plugin.svg)
(https://jitpack.io/#fishedee/swagger-code-maven-plugin)

使用Swagger提供的OpenAPI接口生成客户端代码，Maven插件

## 特性

* 暂时只支持TypeScript
* 同时支持生成类型描述和接口描述
* 支持枚举变量的自动合并

## 例子

```typescript
import myRequest from '@/util/request';

type BigDecimal = string;

type DateTime = string;

type TypeAccountDocument = {
        createTime:DateTime;
        hasRollback:string;
        id:number;
        items:TypeItem[];
        modifyTime:DateTime;
        operatorId:number;
        operatorName:string;
        refId:string;
        refType:string;
        refTypeName:string;
        remark:string;
        rollbackDocumentId:number;
}

type TypeAccountSubject = {
    assistType:string;
    balance:BigDecimal;
    children:TypeAccountSubject[];
    childrenNextId:number;
    createTime:DateTime;
    id:number;
    isReconciliation:string;
    modifyTime:DateTime;
    name:string;
    nextChildrenId:number;
    parentId:number;
    quickType:string;
    remark:string;
    type:string;
}

const ApiAccountAccountDocumentGet = async (data:any)=>{
    return await myRequest({
        method:"GET",
        url:"/api/account/accountDocument/get",
        data:data,
    }) as TypeAccountDocument;
}
const ApiAccountAccountSubjectGet = async (data:any)=>{
    return await myRequest({
        method:"GET",
        url:"/api/account/accountSubject/get",
        data:data,
    }) as TypeAccountSubject;
}
export {
    TypeAccountDocument,
    TypeAccountSubject,
    ApiAccountAccountDocumentGet,
    ApiAccountAccountSubjectGet
}
```

生成的代码大概如上

## 安装

先查看[这里](https://blog.fishedee.com/2021/05/29/SpringBoot%E7%9A%84%E7%BB%8F%E9%AA%8C%E6%B1%87%E6%80%BB/#22-swagger)为SpringBoot添加Swagger的OpenAPI生成

```bash
git clone git@github.com:fishedee/swagger-code-maven-plugin.git
cd swagger-code-maven-plugin
mvn install
```

下载并安装此插件

```xml
<pluginRepositories>
    <pluginRepository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </pluginRepository>
</pluginRepositories>

<plugin>
    <groupId>com.github.fishedee</groupId>
    <artifactId>swagger-code-maven-plugin</artifactId>
    <version>1.8</version>
    <configuration>
        <outputFile>../static/src/api/index.tsx</outputFile>
        <outputFileImportPath>@/util/request</outputFileImportPath>
        <swaggerUrl>http://localhost:7878/v3/api-docs</swaggerUrl>
        <enumOutputFile>../static/src/api/enum.tsx</enumOutputFile>
        <enumUrl>http://localhost:7878/enum/getAll</enumUrl>
    </configuration>
</plugin>
```

在自己的SpringBoot项目中的pom.xml加入以上的插件

```bash
mvn swagger-code:typescript
```

在命令行执行以上代码即可

## 注意点


```
public class ColumnConstraint extends BaseEntityType {

    //不要定义为Item的类型，容易与其他类名冲突
    public static class ColumnConstraintItem extends BaseEntityType{
        @Id
        private String id;

    }

    @Id
    private String id;

    private List<ColumnConstraintItem> columnConstraintItems = new ArrayList<>();
}
```

swagger在生成实体名称的时候，相当愚蠢，直接用类名作为Component的名字，不考虑嵌套类和包名的情况。如果多个类都使用Item的类名，那么他们的字段就会冲突，生成的数据就不准确