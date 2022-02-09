# be-w56-java-was
56주차 간단 웹 서버 구현

# 공부한 부분 정리

## Reflection 이란

자바의 모든 클래스는 자신의 클래스 정보에 대한 것을 가지고 있는 Class 객체를 가지고 있는데 이것은 Foo.class 또는 instance.getClass() 의 방법으로 가져올 수 있다

여기에는 Field, Annotation, Method, Constructor, Parameter... 등 다양한 정보를 가져올 수 있는 API를 제공한다

자바에서는 이 Class의 API를 통해 가져온 정보들을 가지고 런타임때 접근해서 조작할 수 있도록하는 기능을 제공하는데 이것을 Reflection이라고 부른다

많은 것 중에서 이번 프로젝트에서 사용한 것만 다루도록 하겠다

## 사용방법

Method, Constructor, Field, Parameter, Annotation 객체가 reflect 패키지에 존재하는데 이것들을 이용해서 reflection을 사용할 수 있다

### Constructor

```java
//특정 클래스의 생성자를 가져와서 인스턴스화 시키기
try {
    Foo foo = Foo.class.getConstructor().newInstance();
} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
    e.printStackTrace();
}
```

### Annotation

```java
//특정 클래스에 붙은 어노테이션을 가져와서 타입을 알아낼 수 있다
Annotation[] declaredAnnotations = Foo.class.getDeclaredAnnotations();
for(Annotation annotation : declaredAnnotations) {
    System.out.println(annotation.annotationType());
}

//메서드에 붙은 어노테이션을 가져올 수 있다
Method[] methods = Foo.class.getMethods();
for(Method method : methods) {
    for(Annotation annotation : method.getAnnotations()) {
        System.out.println(annotation.annotationType());
    }
}

//파라미터에 붙은 어노테이션을 가져올 수 있다
Method[] methods = Foo.class.getMethods();
for(Method method : methods) {
    for(Parameter parameter : method.getParameters()) {
        for(Annotation annotation : parameter.getAnnotations()) {
            System.out.println(annotation.annotationType());
        }   
    }
}

//클래스 필드에 붙은 어노테이션을 가져올 수 있다
Field[] fields = Foo.class.getFields();
for(Field field :fields) {
    for(Annotation annotation : field.getAnnotations()) {
        System.out.println(annotation.annotationType());
    }
}
```

### Parameter

```java
//파라미터의 이름이나 타입을 가져올 수 있다
Method[] methods = Foo.class.getMethods();
for(Method method : methods) {
    for(Parameter parameter : method.getParameters()) {
        Class<?> type = parameter.getType();
        String name = parameter.getName();
    }
}
```

### Field

```java
//필드에 저장된 값을 원하는 타입으로 가져올 수 있다
try {
  Field[] fields = Foo.class.getFields();
  for (Field field : fields) {
      field.setAccessible(true); //private 필드인 경우에 설정해줘야 접근 가능
      "원하는 타입" o = field.get("원하는 타입");
  }
}catch (IllegalAccessException ex) {
  System.out.println(ex.getMessage());
}
```

### Method

```java
//reflection을 통해서 메서드 실행
try {
	Method method1 = Foo.class.getMethod("method1");
	method1.invoke(Foo.class);
} catch (NoSuchMethodException ex) {
    System.out.println(ex.getMessage());
} catch (InvocationTargetException | IllegalAccessException e) {
    e.printStackTrace();
}

//클래스에 해당하는 method 가져오기
Method[] methods = Foo.class.getMethods();
for(Method method : methods) {
    System.out.println(method.getName());
}
```

### 참고사항

- getMethods()  getDeclaredMethods()의 차이점 (getXXX, getDeclaredXXX을 뜻함)

  이 두 개의 차이점은 getXXX 는 public 접근자만 가져오게 되고 getDeclaredXXX은 private 접근자도 가져올 수 있다
  그러므로 자신이 private한 곳에 접근해야 한다면 getDeclaredXXX 메서드를 사용하면 된다

- getAnnotations() getDeclaredAnnotations()의 차이점

  getDeclaredAnnotation은 상속받은 어노테이션은 제외하고 가져온다

  getAnnotation은 상관없이 가져온다


## Reflection의 단점
- 컴파일타임때 제공해주는 타입 검사 기능을 제대로 누리지 못한다
- 코드가 지저분해지고 장황해진다 (try - catch 문과 에러처리를 해야한다)
- 일반 메서드를 부를 때 보다 Reflection을 통해서 호출되는 메서드가 느리다

---


## Redirect
>URL 리다이렉션(URL redirection, URL 넘겨주기)은 이용 가능한 웹 페이지를 하나 이상의 URL 주소로 만들어주는 월드 와이드 웹 기법이다. 
URL 포워딩(URL forwarding)이라고도 한다. 넘겨받은 URL을 웹 브라우저가 열려고 하면 다른 URL의 문서가 열리게 된다.
> <br>wikipedia 참조


이번 프로젝트에서 redirect를 사용한 부분은 회원가입이나 로그인을 했다면 
성공여부에 따라서 적당한 페이지로 보내주기 위해서 사용  

### 동작 순서
```
Client         Server
   |             |
   |------------>|GET /doc Http/1.1
   |             |
   |<------------|HTTP/1.1 302 Found
   |             |Location: /doc_new
   |             |
   |------------>|GET /doc_new HTTP/1.1
   |             |
   |<------------|HTTP/1.1 200 OK
   |             |
```

### 응답코드
리다이렉션을 위해서는 30X 의 응답 코드를 사용해서 응답한다  
301 ~ 308까지 존재하지만 이번 프로젝트에서는 302만 사용을 하였다  

`302 (임시 이동)` : 현재 서버가 다른 위치의 페이지로 요청에 응답하고 있지만 요청자는 향후 요청 시 원래 위치를 계속 사용해야 한다.

- **302 VS 307**  
  302와 307의 설명은 똑같이 되어있는 것을 위키피디아에서 볼 수 있다  
  - 302같은 경우에는 GET 메서드는 변경시키지 않고 다른 메서드는 변경시킬 수 있다  
  - 307같은 경우에는 메서드와 본문을 변경시키지 않고 사용한다

### 클라이언트에서 Redirect해주기
```js
window.location = 'http:://www.naver.com';
```
위와같이 클라이언트쪽에서 작성해준다면 서버의 도움없이 redirect를 클라이언트쪽에서 구현할 수 있다  
