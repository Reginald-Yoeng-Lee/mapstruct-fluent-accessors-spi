# mapstruct-fluent-accessors-spi

> This project is forked from [com.github.nilyin:mapstruct-fluent-accessors-spi](https://github.com/nilyin/mapstruct-fluent-accessors-spi).
> For now almost all the features are the same as the original one. The subtle differences will be illustrated later.

### Installation

First, add the dependency

```xml
<dependency>
    <groupId>io.github.reginald-yoeng-lee</groupId>
    <artifactId>mapstruct-fluent-accessors-spi</artifactId>
    <version>1.0.0</version>
</dependency>
```

Next, add the path in AnnotationProcessor

```xml
<annotationProcessorPaths>
	<path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${org.mapstruct.version}</version>
    </path>
    <path>
        <groupId>io.github.reginald-yoeng-lee</groupId>
        <artifactId>mapstruct-fluent-accessors-spi</artifactId>
        <version>1.0.0</version>
    </path>
</annotationProcessorPaths>
```

### Usage

For an object contains a field named `foo`, if a method meets all these conditions:
* has the same name as the field (i.e. `foo()`), with the prefix `get` (i.e. `getFoo()`), or with the prefix `is` (i.e. `isFoo()`)
* has no parameter
* declares the same type as the field

it'll be taken as the getter of that field.

### Differences with the original project

_Comparing is between the version 1.0.0 of this project and the version 1.0.3 of the [forked project](https://github.com/nilyin/mapstruct-fluent-accessors-spi/releases/tag/1.0.3)_

Almost all the features are the same but a small bug is fixed.

Consider we have a pojo like:
```java
public class Pojo {
    @Min(5) // Type annotation
    private int foo;

    public int foo() {
        return foo;
    }
}
```
If the `@Min` is a type annotation, in the original project, the method `foo()` will be considered **NOT** the getter of `foo` incorrectly.

The problem comes up from the method `isSamePropertyNameExist(ExecutableElement getterOrSetterMethod)`. In the original project, it uses  
`element.asType().toString().equals(getterOrSetterMethod.getReturnType().toString())`  
to check whether the type of field and the type of the return type of the method are the same. However, since the filed is annotated by the
_Type Annotation_ `@Min`, the value of `element.asType().toString()` is actually **@Min(5) java.lang.Integer** which contains the info of
the annotation, while the value of `getterOrSetterMethod.getReturnType().toString()` is **java.lang.Integer**.

To fix this tricky bug, the types comparing instead uses  
`typeUtils.isSameType(element.asType(), getterOrSetterMethod.getReturnType())`  
while the `typeUtils` is inherited from the parent class `DefaultAccessorNamingStrategy`.
See [Types#isSameType(TypeMirror t1, TypeMirror t2)](https://docs.oracle.com/en/java/javase/17/docs/api/java.compiler/javax/lang/model/util/Types.html#isSameType(javax.lang.model.type.TypeMirror,javax.lang.model.type.TypeMirror))