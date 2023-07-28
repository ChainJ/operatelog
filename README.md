# operate-log

A common operation log util of Java version

## 模块说明

**operate-log-core**

操作日志核心二方包，用于实现操作日志主要组件，并生成对比差异

**operate-log-demo**

操作日志示例模块，包含其它模块核心示例程序

## 使用说明

```OperateLogUtil``` 工具通过对比对象字段差异，用于生成操作日志。正常情况下，用于对比的对象应该符合标准的JavaBean定义。

```OperateLogUtil``` 可以无侵入性地生成对象差异。
默认情况下，操作日志工具对所有的JavaBean字段进行对比并生成差异描述。

```java
/**
 * 用户信息
 */
public class User {
    /**
     * 用户ID
     */
    int id;
    /**
     * 用户名字
     */
    String name;
    /**
     * 用户年龄
     */
    Integer age;
    /**
     * 用户性别
     */
    byte gender;

    // getter and setter

    public static void main(String... args) {
        User user1 = new User();
        user1.id = 1;
        user1.name = "Jack";
        user1.age = 20;
        user1.gender = (byte) 1;

        User user2 = new User();
        user2.id = 2;
        user2.name = "Rose";
        user2.age = 18;
        user2.gender = (byte) 2;

        Map<String, Pair<String, String>> propertyDifferences = OperateLogUtil.diff(user1, user2);
        propertyDifferences.forEach((property, difference) -> {
            String pattern = "[%s]:[%s]->[%s] %n";
            System.out.printf(pattern, property, difference.getLeft(), difference.getRight());
        });

        /*
          The output should be:
          [id]:[1]->[2]
          [name]:[Jack]->[Rose]
          [age]:[20]->[18]
          [gender]:[1]->[2]
         */
    }
}
```

当然， **operate-log** 组件也支持通过注解和配置文件的方式，对操作日志进行自定义。

### 注解

用户可以借助 ```@OperateModel``` 和 ```@OperateField``` 两个注解自定义操作日志。
具体案例请看 ```operatelog.demo.AnnotationOperateLogDemo```

### 配置文件

用户可以借助配置文件自定义操作日志。
**operate-log** 组件支持 *yml* 和 *json* 两种格式的配置文件定义日志元数据。
具体案例请看 ```operatelog.demo.ConfigOperateLogDemo```

#### 配置项

使用配置文件时， **operate-log** 组件按照 环境变量->系统参数->配置文件 的顺序，读取前缀为 *operate_log* 的配置项。

用户可以根据需要，调整以下配置项

|        配置项         | 默认值                      | 说明              | 环境变量 | 系统参数 | 配置文件 | 
|:------------------:|--------------------------|-----------------|:----:|:----:|:----:|
|  config.file.path  | /operate-log.properties  | 基础配置文件路径        | Yes  | Yes  |  No  | 
|  meta.config.dir   | /operate-meta            | 日志元数据配置目录       | Yes  | Yes  | Yes  |
|  null.description  | 空                        | 空值描述            | Yes  | Yes  | Yes  |

