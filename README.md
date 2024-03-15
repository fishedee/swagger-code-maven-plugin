# swaggerCode-maven-plugin

![Release](https://jitpack.io/v/fishedee/swagger-code-maven-plugin.svg)
(https://jitpack.io/#fishedee/swagger-code-maven-plugin)

使用Swagger提供的OpenAPI接口生成客户端代码，Maven插件

## 特性

* 支持TypeScript和dart的接口生成
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

生成的typescript代码大概如上

```dart
import 'package:trade_app/util/request.dart';

abstract interface class IData {
  Object? operator [](String name);
  void operator []=(String name, Object? value);
}

abstract interface class IDataDynamic {
  Object encodeDynamic();
}

class BoolHelper {
  static bool? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is bool) {
      return data;
    } else if (data is String) {
      if (data.trim().toLowerCase() == 'true') {
        return true;
      } else if (data.trim().toLowerCase() == 'false') {
        return false;
      }
    }
    throw FormatException('can not parse to bool: [$data]');
  }

  static Object? toDynamic(bool? data) {
    return data;
  }
}

class IntHelper {
  static int? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is int) {
      return data;
    } else if (data is double) {
      return data.toInt();
    } else if (data is String) {
      return int.parse(data);
    }
    throw FormatException("can not parse to int: [$data]");
  }

  static Object? toDynamic(int? data) {
    return data;
  }
}

class DoubleHelper {
  static double? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is int) {
      return data.toDouble();
    } else if (data is double) {
      return data;
    } else if (data is String) {
      return double.parse(data);
    }
    throw FormatException('can not parse to double: [$data]');
  }

  static Object? toDynamic(double? data) {
    return data;
  }
}

class StringHelper {
  static String? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is String ||
        data is int ||
        data is double ||
        data is bool) {
      return data.toString();
    }
    throw FormatException('can not parse to String: [$data]');
  }

  static Object? toDynamic(String? data) {
    return data;
  }
}

class ObjectHelper {
  static Object? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is String ||
        data is int ||
        data is double ||
        data is bool) {
      return data;
    }
    throw FormatException('can not parse to Object: [$data]');
  }

  static Object? toDynamic(Object? data) {
    return data;
  }
}

class ListHelper {
  static List<T>? Function(Object? data) wrapFromDynamic<T>(
      T Function(Object? item) fromDynamicItem) {
    return (Object? data) {
      if (data == null) {
        return null;
      } else if (data is List) {
        return data.map((single) => fromDynamicItem(single)).toList();
      }
      throw FormatException('can not parse to list: [$data]');
    };
  }

  static Object? Function(List<T>? data) wrapToDynamic<T>(
      Object Function(T data) toDynamicItem) {
    return (List<T>? data) {
      if (data == null) {
        return null;
      }
      return data.map((single) => toDynamicItem(single)).toList();
    };
  }

  static bool equals<T>(List<T>? a, List<T>? b) {
    if (a == null) return b == null;
    if (b == null || a.length != b.length) return false;

    /// Check whether two references are to the same object.
    if (identical(a, b)) return true;
    for (var i = 0; i != a.length; i++) {
      if (a[i] != b[i]) {
        return false;
      }
    }
    return true;
  }
}

class MapHelper {
  static Map<String, T>? Function(Object? data) wrapFromDynamic<T>(
      T Function(Object? item) fromDynamicItem) {
    return (Object? data) {
      if (data == null) {
        return null;
      } else if (data is Map<String, Object?>) {
        final result = <String, T>{};
        data.forEach((key, value) {
          result[key] = fromDynamicItem(value);
        });
        return result;
      }
      throw FormatException('can not parse to map: [$data]');
    };
  }

  static Object? Function(Map<String, T>? data) wrapToDynamic<T>(
      Object Function(T data) toDynamicItem) {
    return (Map<String, T>? data) {
      if (data == null) {
        return null;
      }
      final result = <String, dynamic>{};
      data.forEach((key, value) {
        result[key] = toDynamicItem(value);
      });
      return result;
    };
  }

  static bool equals<T, U>(Map<T, U>? a, Map<T, U>? b) {
    if (a == null) return b == null;
    if (b == null || a.length != b.length) return false;

    /// Check whether two references are to the same object.
    if (identical(a, b)) return true;
    for (final T key in a.keys) {
      if (!b.containsKey(key) || b[key] != a[key]) {
        return false;
      }
    }
    return true;
  }
}

typedef GetterHandler<T> = Object? Function(T data);
typedef SetterHandler<T> = void Function(T data, Object? value);
typedef ToDynamicHandler<T> = Object? Function(T data);
typedef FromDynamicHandler<T> = void Function(T data, Object? value);
typedef FieldReflectInfo<T> = Map<
    String,
    ({
      GetterHandler<T> getter,
      SetterHandler<T> setter,
      ToDynamicHandler<T> toDynamic,
      FromDynamicHandler<T> fromDynamic
    })>;

abstract class IDataBasic implements IData {
  final Map<String, Object?> _externalFields = {};

  IDataBasic();

  Map<String, Object?> getExternalFields() {
    return _externalFields;
  }

  Object? getExternalField(String name) {
    return _externalFields[name];
  }

  @override
  Object? operator [](String name) {
    return _externalFields[name];
  }

  void setExternalField(String name, Object? value) {
    _externalFields[name] = value;
  }

  @override
  void operator []=(String name, Object? value) {
    _externalFields[name] = value;
  }
}

class IDataField {
  final String key;

  const IDataField(this.key);
}

class IDataEnum {
  final String value;

  final String label;

  const IDataEnum(this.value, this.label);

  @override
  int get hashCode {
    return value.hashCode;
  }

  @override
  bool operator ==(Object? other) {
    return other is IDataEnum &&
        runtimeType == other.runtimeType &&
        value == other.value;
  }

  @override
  String toString() {
    return '$runtimeType($value-$label)';
  }
}


class TypeEnumResourceDuplicateNameStrategy extends IDataEnum implements IDataDynamic{
  static const NOT_ALLOW = TypeEnumResourceDuplicateNameStrategy('NOT_ALLOW','不允许重复');

  static const ALLOW = TypeEnumResourceDuplicateNameStrategy('ALLOW','允许重复');

  static const ALLOW_BUT_REMIND = TypeEnumResourceDuplicateNameStrategy('ALLOW_BUT_REMIND','重复时提醒');

  static const values = {
     'NOT_ALLOW':NOT_ALLOW,
     'ALLOW':ALLOW,
     'ALLOW_BUT_REMIND':ALLOW_BUT_REMIND,
  };

  const TypeEnumResourceDuplicateNameStrategy(super.value,super.label);

  static TypeEnumResourceDuplicateNameStrategy? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is String) {
      final result = values[data.toUpperCase()];
      if (result != null) {
        return result;
      }
      return TypeEnumResourceDuplicateNameStrategy(data, data);
    }
    throw FormatException('can not parse to TypeEnumResourceDuplicateNameStrategy: [$data]');
  }

  static Object? toDynamic(TypeEnumResourceDuplicateNameStrategy? result) {
    if (result == null) {
      return null;
    } else {
      return result.value;
    }
  }

  @override
  Object encodeDynamic() {
    return toDynamic(this)!;
  }
}

class FTypeUnit extends IDataField {
  static const createTime = FTypeUnit('createTime');
  static const id = FTypeUnit('id');
  static const isEnabled = FTypeUnit('isEnabled');
  static const modifyTime = FTypeUnit('modifyTime');
  static const name = FTypeUnit('name');
  static const remark = FTypeUnit('remark');
  const FTypeUnit(super.key);
}

final FieldReflectInfo<TypeUnit> _TypeUnit_fields = {
  "createTime": (
    getter: (data) => data._createTime,
    setter: (data, value) => data._createTime = value as String?,
    toDynamic: (data) {
      final formatter = StringHelper.toDynamic;
      return formatter(data._createTime);
    },
    fromDynamic: (data, value) {
      final parser = StringHelper.fromDynamic;
      data._createTime = parser(value);
    }
  ),
  "id": (
    getter: (data) => data._id,
    setter: (data, value) => data._id = value as int?,
    toDynamic: (data) {
      final formatter = IntHelper.toDynamic;
      return formatter(data._id);
    },
    fromDynamic: (data, value) {
      final parser = IntHelper.fromDynamic;
      data._id = parser(value);
    }
  ),
  "isEnabled": (
    getter: (data) => data._isEnabled,
    setter: (data, value) => data._isEnabled = value as TypeEnumSoftEnableIsEnable?,
    toDynamic: (data) {
      final formatter = TypeEnumSoftEnableIsEnable.toDynamic;
      return formatter(data._isEnabled);
    },
    fromDynamic: (data, value) {
      final parser = TypeEnumSoftEnableIsEnable.fromDynamic;
      data._isEnabled = parser(value);
    }
  ),
  "modifyTime": (
    getter: (data) => data._modifyTime,
    setter: (data, value) => data._modifyTime = value as String?,
    toDynamic: (data) {
      final formatter = StringHelper.toDynamic;
      return formatter(data._modifyTime);
    },
    fromDynamic: (data, value) {
      final parser = StringHelper.fromDynamic;
      data._modifyTime = parser(value);
    }
  ),
  "name": (
    getter: (data) => data._name,
    setter: (data, value) => data._name = value as String?,
    toDynamic: (data) {
      final formatter = StringHelper.toDynamic;
      return formatter(data._name);
    },
    fromDynamic: (data, value) {
      final parser = StringHelper.fromDynamic;
      data._name = parser(value);
    }
  ),
  "remark": (
    getter: (data) => data._remark,
    setter: (data, value) => data._remark = value as String?,
    toDynamic: (data) {
      final formatter = StringHelper.toDynamic;
      return formatter(data._remark);
    },
    fromDynamic: (data, value) {
      final parser = StringHelper.fromDynamic;
      data._remark = parser(value);
    }
  ),
};
class TypeUnit extends IDataBasic implements IDataDynamic {
  TypeUnit({
    String? createTime,
    int? id,
    TypeEnumSoftEnableIsEnable? isEnabled,
    String? modifyTime,
    String? name,
    String? remark
  }):
    _createTime = createTime,
    _id = id,
    _isEnabled = isEnabled,
    _modifyTime = modifyTime,
    _name = name,
    _remark = remark;

  static TypeUnit? fromDynamic(Object? dy) {
    if (dy == null) {
      return null;
    } else if (dy is Map<String, dynamic>) {
      final data = TypeUnit();
      dy.forEach((key, dynamicValue) {
        final fieldInfo = _TypeUnit_fields[key];
        if (fieldInfo == null) {
          data.setExternalField(key, dynamicValue);
          return;
        }
        fieldInfo.fromDynamic(data, dynamicValue);
      });
      return data;
    }
    throw FormatException('can not parse to TypeUnit: [$dy]');
  }

  static Map<String, dynamic>? toDynamic(TypeUnit? data) {
    if (data == null) {
      return null;
    }
    final result = <String, dynamic>{};
    data.getExternalFields().forEach((key, value) {
      if (value != null) {
        result[key] = value;
      }
    });
    _TypeUnit_fields.forEach((key, fieldInfo) {
      final dynamicValue = fieldInfo.toDynamic(data);
      if (dynamicValue != null) {
        result[key] = dynamicValue;
      }
    });
    return result;
  }

  @override
  Map<String, dynamic> encodeDynamic() {
    return toDynamic(this)!;
  }

  @override
  String toString() {
    return encodeDynamic().toString();
  }

  @override
  Object? operator [](String name) {
    var fieldInfo = _TypeUnit_fields[name];
    if (fieldInfo == null) {
      return super[name];
    }
    return fieldInfo.getter(this);
  }

  @override
  void operator []=(String name, Object? value) {
    var fieldInfo = _TypeUnit_fields[name];
    if (fieldInfo == null) {
      super[name] = value;
      return;
    }
    fieldInfo.setter(this, value);
  }

  String? _createTime;

  String get createTime{
    return _createTime!;
  }

  String? getCreateTime(){
    return _createTime;
  }

  set createTime(String data){
    _createTime = data;
  }

  void setCreateTime(String? data){
    _createTime = data;
  }

  int? _id;

  int get id{
    return _id!;
  }

  int? getId(){
    return _id;
  }

  set id(int data){
    _id = data;
  }

  void setId(int? data){
    _id = data;
  }

  TypeEnumSoftEnableIsEnable? _isEnabled;

  TypeEnumSoftEnableIsEnable get isEnabled{
    return _isEnabled!;
  }

  TypeEnumSoftEnableIsEnable? getIsEnabled(){
    return _isEnabled;
  }

  set isEnabled(TypeEnumSoftEnableIsEnable data){
    _isEnabled = data;
  }

  void setIsEnabled(TypeEnumSoftEnableIsEnable? data){
    _isEnabled = data;
  }

  String? _modifyTime;

  String get modifyTime{
    return _modifyTime!;
  }

  String? getModifyTime(){
    return _modifyTime;
  }

  set modifyTime(String data){
    _modifyTime = data;
  }

  void setModifyTime(String? data){
    _modifyTime = data;
  }

  String? _name;

  String get name{
    return _name!;
  }

  String? getName(){
    return _name;
  }

  set name(String data){
    _name = data;
  }

  void setName(String? data){
    _name = data;
  }

  String? _remark;

  String get remark{
    return _remark!;
  }

  String? getRemark(){
    return _remark;
  }

  set remark(String data){
    _remark = data;
  }

  void setRemark(String? data){
    _remark = data;
  }

}

Object? DynamicEncode(Object? info) {
  if (info == null) {
    return info;
  } else if (info is IDataDynamic) {
    return info.encodeDynamic();
  } else if (info is bool) {
    return BoolHelper.toDynamic(info);
  } else if (info is int) {
    return IntHelper.toDynamic(info);
  } else if (info is double) {
    return DoubleHelper.toDynamic(info);
  } else if (info is String) {
    return StringHelper.toDynamic(info);
  } else if (info is List) {
    return info.map((single) => DynamicEncode(single)).toList();
  } else if (info is Map) {
    final data = {};
    info.forEach((key, value) {
      data[DynamicEncode(key)] = DynamicEncode(value);
    });
    return data;
  } else if (info is Set) {
    final data = <dynamic>{};
    for (final value in info) {
      data.add(value);
    }
    return data;
  } else {
    throw FormatException('can not convertTo dynamic: ${info.runtimeType}');
  }
}

Future<List<TypeUnit>?> ApiResourceUnitGetAll([Object? data]) async{
  Object? result = await myRequest(
    method: "GET",
    url: "/api/resource/unit/getAll",
    data: DynamicEncode(data),
  );
  final parser = ListHelper.wrapFromDynamic<TypeUnit>((single){
    final handler = TypeUnit.fromDynamic;
    return handler(single)!;
  });
  return parser(result);
}
```

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
    <version>2.3</version>
    <configuration>
        <swaggerUrl>http://localhost:8110/v3/api-docs</swaggerUrl>
        <enumUrl>http://localhost:8110/enum/getAll</enumUrl>
        <outputTargets>
            <!--PC管理前端-->
            <outputTarget>
                <type>TYPESCRIPT</type>
                <apiImportPath>../utils/request</apiImportPath>
                <apiOutputFile>../src/api/index.tsx</apiOutputFile>
                <enumOutputFile>../src/api/enum.tsx</enumOutputFile>
            </outputTarget>
            <!--小程序管理前端-->
            <outputTarget>
                <type>TYPESCRIPT</type>
                <apiImportPath>../utils/request</apiImportPath>
                <apiOutputFile>../../../../TradeMini/miniprogram/api/index.ts</apiOutputFile>
                <enumOutputFile>../../../../TradeMini/miniprogram/api/enum.ts</enumOutputFile>
            </outputTarget>
            <!--App管理前端-->
            <outputTarget>
                <type>DART</type>
                <apiImportPath>package:trade_app/util/request.dart</apiImportPath>
                <apiOutputFile>../../../../TradeApp/lib/api/index.dart</apiOutputFile>
            </outputTarget>
        </outputTargets>
    </configuration>
</plugin>
```

在自己的SpringBoot项目中的pom.xml加入以上的插件

```bash
mvn swagger-code:codegen
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