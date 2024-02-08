import '${importPath}';

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

  static Object? toDynamic(String? data) {
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

<#list enumList as singlEnum>
class ${singlEnum.name} extends IDataEnum implements IDataDynamic{
<#list singlEnum.constantList as constant>
  static const ${constant.value} = ${singlEnum.name}('${constant.value}','${constant.label}')

</#list>
  static const values = {
    <#list singlEnum.constantList as constant>
     '${constant.value}':${constant.value},
    </#list>
  }

  const ${singlEnum.name}(super.value,super.label);

  static ${singlEnum.name}? fromDynamic(Object? data) {
    if (data == null) {
      return null;
    } else if (data is String) {
      final result = values[data.toUpperCase()];
      if (result != null) {
        return result;
      }
      return ${singlEnum.name}(data, data);
    }
    throw FormatException('can not parse to ${singlEnum.name}: [$data]');
  }

  static Object? toDynamic(${singlEnum.name}? result) {
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
};
</#list>

<#list typeList as singleType>
class F${singleType.name} extends IDataField {
  <#list singleType.fieldList as field>
  static const ${field.name} = F${singleType.name}('${field.name}');
  </#list>
  const F${singleType.name}(super.key);
}

class ${singleType.name} extends IDataBasic implements IDataDynamic {
  static final FieldReflectInfo<User> _fields = {
    <#list singleType.fieldList as field>
      "${field.name}": (
        getter: (data) => data._${field.name},
        setter: (data, value) => data._${field.name} = value as ${field.type}?,
        toDynamic: (data) {
          final formatter = ${field.formatter};
          return formatter(data._${field.name});
        },
        fromDynamic: (data, value) {
          final parser = ${field.parser};
          data._${field.name} = parser(value);
        }
      ),
    </#list>
  };

  ${singleType.name}({
  <#list singleType.fieldList as field>
    ${field.type}? ${field.name}<#sep>,</#sep>
  </#list>
  }):<#list singleType.fieldList as field>
    _${field.name} = ${field.name}<#sep>,</#sep></#list>;

  static ${singleType.name}? fromDynamic(Object? dy) {
    if (dy == null) {
      return null;
    } else if (dy is Map<String, dynamic>) {
      final data = ${singleType.name}();
      dy.forEach((key, dynamicValue) {
        final fieldInfo = _fields[key];
        if (fieldInfo == null) {
          data.setExternalField(key, dynamicValue);
          return;
        }
        fieldInfo.fromDynamic(data, dynamicValue);
      });
      return data;
    }
    throw FormatException('can not parse to ${singleType.name}: [$dy]');
  }

  static Map<String, dynamic>? toDynamic(${singleType.name}? data) {
    if (data == null) {
      return null;
    }
    final result = <String, dynamic>{};
    data.getExternalFields().forEach((key, value) {
      if (value != null) {
        result[key] = value;
      }
    });
    _fields.forEach((key, fieldInfo) {
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
    var fieldInfo = _fields[name];
    if (fieldInfo == null) {
      return super[name];
    }
    return fieldInfo.getter(this);
  }

  @override
  void operator []=(String name, Object? value) {
    var fieldInfo = _fields[name];
    if (fieldInfo == null) {
      super[name] = value;
      return;
    }
    fieldInfo.setter(this, value);
  }

  <#list singleType.fieldList as field>
  ${field.type}? _${field.name};

  ${field.type} get ${field.name}{
    return _${field.name}!;
  }

  ${field.type}? get${field.upperName}(){
    return _${field.name};
  }

  set ${field.name}(${field.type} data){
    _${field.name} = data;
  }

  void set${field.upperName}(${field.type}? data){
    _${field.name} = data;
  }

  </#list>
}
</#list>


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
    throw FormatException('can not convertTo dynamic: ${r'${info.runtimeType}'}');
  }
}

<#list apiList as singleApi>
Future<${singleApi.responseType}?> ${singleApi.name}([Object? data]) async{
  Object? result = await myRequest(
    method: "${singleApi.method}",
    url: "${singleApi.url}",
    data: DynamicEncode(data),
  );
  final parser = ${singleApi.responseParser};
  return parser(result);
}
</#list>