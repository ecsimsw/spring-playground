# Playground

## 2. Service mesh 만들기
- 내부 통신할 때, 지금은 서비스별 port를 기준으로 목적지 주소를 찾는다.
- 이 경우는 스케일 아웃에 대비할 수 없고, Endpoint를 직접 알아야 해서 의존도 크다.
- 서비스 별 라우팅 경로를 갖고 있는 중간상을 구현하여 스케일 아웃에도 대응하고 직접 상대 포트를 몰라도 되게 만들자.
- 참고로 내부 서비스간 통신은 요청 시간이 암호화된 코드를 공유해서 복호화가 안되거나, 일정 시간 이상이면 응답하지 않는다.
- [RouteService](https://github.com/ecsimsw/my-spring-playground/blob/main/api-gateway/src/main/java/com/ecsimsw/gateway/service/RouteService.java)
- [ServiceMeshConfig](https://github.com/ecsimsw/my-spring-playground/blob/main/api-common/src/main/java/com/ecsimsw/common/config/ServiceMessConfig.java)
- [RouteController](https://github.com/ecsimsw/my-spring-playground/blob/main/api-gateway/src/main/java/com/ecsimsw/gateway/controller/RouteController.java)

#### 사용 시나리오
```
1. 각 프로젝트 application.properties에 service.name, server.port, 연결할 gateway 주소 선언한다.
2. 라우터에선 user에 등록된 엔드포인트 리스트를 읽고 목록 중 하나로 요청, 로드 밸런싱한다.
3. 내부 통신에 단순 서비스 명만 정의하는 식으로 통신 가능해지고, 서비스 간 직접 의존이 없어짐에 따라 동적 스케일 아웃이 가능해진다.
```

#### 서비스 엔드포인트 관리
```
1. 애플리케이션이 뜰 때 본인의 서비스 정보, 포트 정보를 Gateway 서버에 알린다.
2. Gateway 서버는 서비스별 주소와 포트 정보 리스트를 관리한다.
3. 서비스 별 통신에 Gateway에 서비스 정보만으로 전달이 가능하다.
4. 애플리케이션이 다운될 때 이를 Gateway 서버에 알려 리스트에서 제거한다.
```

## 1. Spring security 연습
- 회사에서 가장 분석하지 못한 코드가 Auth 였고, 이번에 앱의 SDK 버전에 따라 로그인 처리를 달리해야 하는 작업이 할당되었다.
- 설 연휴동안 이참에 Spring security로 가상 요구 사항 개발해보기!
- 아래는 내 코드 기준

#### 인증 처리

```
1. AuthController

- 로그인 요청으로 사용자 아이디, 패스워드가 전달되면 이를 바탕으로 UsernamePasswordAuthenticationToken를 생성한다. (authenticated=false)
- AuthenticationManager가 AuthenticationProvider에게 구체적인 인증 요청한다.

2. AuthenticationManager

- AuthenticationManager는 AuthenticationProvider를 갖고 있고, AuthenticationProvider는 UserDetailService를 갖고 있다.
- AuthenticationManager.authenticate()으로 인증이 요청되면, UserDetailService는 DB에서 사용자 정보를 가져온다.
- AuthenticationProvider는 이를 바탕으로 요청한 로그인 정보가 맞는지 인증 절차를 수행한다.

 3. UserDetailService

- UserDetailService는 DB 에서 사용자 아이디, 암호화된 패스워드을 조회해서 UserDetails로 전달한다.
- 기본 정보 외에 권한이나 관리자 여부를 함께 조회해서 넘기고자 CustomUserDetailService와 CustomUserDetails를 정의했다.
- UserDetailService는 loadUserByUsername()으로 로그인 요청한 사용자의 DB 정보를 UserDetails로 감싸 전달한다.

 4. AuthenticationProvider

- AuthenticationProvider는 전달받은 UserDetails 정보를 바탕으로 로그인 요청에서 사용한 아이디, 패스워드 등이 일치하는지 확인한다.
- 이때 미리 정의한 PasswordEncoder를 사용하여, 정해진 암/복호화 방법으로 Match 여부를 간단히 확인할 수 있다.
- 아이디, 패스워드 외에 사용자의 권한이나 Blocked 상태를 확인하고자 CustomUserDetails를 사용했다.

5. UsernamePasswordAuthenticationToken

- 인증에 성공하면 로그인 요청 정보가 담긴 UsernamePasswordAuthenticationToken을 authenticated=true로 변경하여 인증 완료를 표시한다.
- SecurityContextHolder에 인증 정보가 저장되어, 이후 인증에 사용되었던 사용자 정보를 DB 조회 없이 꺼내 사용하거나,
- @PreAuthorize와 같은 Spring security 기능을 쉽게 사용할 수 있도록 돕는다.
- SecurityContextHolder는 ThreadLocal로 관리된다.
```

#### 인가 처리
```
1. TokenController (OncePerRequestFilter)

- Header에서 Jwt를 꺼내 토큰 유효성을 검증한다.
- 토큰의 Claim에 저장된 사용자 정보를 확인하여 SecurityContextHolder에 저장한다.
- 이후 편하게 사용자 정보를 확인하거나 사용자의 api 별 접근 권한을 확인하는데 사용된다.
- 이때 접근 권한이 동적으로 바뀜에 따라 권한 확인을 위한 DB 조회가 추가될 수 있다.
- 나는 권한이 실시간으로 바뀔 수 있고, 빠르게 반영되어야 하는 상황을 가정하였다.
- 따라서 토큰에 권한을 넣어 관리하는 것이 아닌, 매번 권한이 제한된 api 요청에서 DB를 확인하여 사용자 권한을 확인하고 있다.

2. UserController

- 앞서 토큰과 DB를 통해 확인한 사용자 정보를 통해, Api 접근 가능 여부가 결정된다.
- SecurityContextHolder에서 사용자 정보를 가져와 활용 가능하다.
```
