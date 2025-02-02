## ecsimsw spring playground

### 1. 설 연휴 동안 Spring security 연습
- 회사에서 가장 분석하지 못한 코드가 Auth 였고, 이번에 앱의 SDK 버전에 따라 로그인 처리를 달리해야 하는 작업이 할당되었다.
- 설 연휴동안 이참에 Spring security 로 가상 요구 사항 개발해보기!
- 아래는 내 코드 기준

#### 인증 처리

1. AuthController
- 로그인 요청으로 사용자 아이디, 패스워드가 전달되면 이를 바탕으로 UsernamePasswordAuthenticationToken 를 생성한다.
- 이는 아직 인증되지 않은 상태임이 기록되어 있다. (authenticated=false)
- AuthenticationManager 가 AuthenticationProvider 에게 구체적인 인증 요청한다.

2. AuthenticationManager
- AuthenticationManager 는 정의한 AuthenticationProvider 를 갖고 있고,
- AuthenticationProvider 는 정의한 UserDetailService 를 갖고 있다.
- 이는 SecurityConfig 에서 미리 지정한다.
- AuthenticationManager.authenticate() 으로 인증이 요청되면,
- UserDetailService 는 DB 에서 사용자 정보를 가져오고, AuthenticationProvider 는 이를 바탕으로 요청한 회원 정보가 맞는지 인증 절차를 수행한다.

 3. UserDetailService
- UserDetailService 는 DB 에서 사용자 아이디, 암호화된 패스워드을 조회해서 UserDetails 로 전달한다.
- 기본 정보 외에 권한이나 관리자 여부를 함께 조회해서 넘기고자 CustomUserDetailService 와 CustomUserDetails 를 정의했다.
- UserDetailService 는 loadUserByUsername() 으로 로그인 요청한 사용자의 DB 정보를 UserDetails 로 감싸 전달한다.

 4. AuthenticationProvider
- AuthenticationProvider 는 전달받은 UserDetails 정보를 바탕으로 로그인 요청에서 사용한 아이디, 패스워드 등이 일치하는지 확인한다.
- 이때 미리 정의한 PasswordEncoder 를 사용하여, 정해진 암/복호화 방법으로 사용자 인증 여부를 확인한다.
- 아이디, 패스워드 외에 사용자의 권한이나 Blocking 상태를 확인하고자 CustomUserDetails 를 사용했다.

5. UsernamePasswordAuthenticationToken
- 인증에 성공하면 로그인 요청 정보가 담긴 UsernamePasswordAuthenticationToken 을 authenticated=true 로 변경하여 인증 완료를 표시한다.
- SecurityContextHolder 에 인증 정보가 저장되어, 이후 인증에 사용되었던 사용자 정보를 DB 조회 없이 꺼내 사용하거나,
- @PreAuthorize 와 같은 Spring security 기능을 쉽게 사용할 수 있도록 돕는다.
- SecurityContextHolder 는 ThreadLocal 로 관리된다.

#### 인가 처리
1. TokenController (OncePerRequestFilter)
- Header 에서 Jwt 꺼내서 토큰 유효성을 검증한다.
- 토큰의 Claim 에 저장된 사용자 정보를 확인하여 SecurityContextHolder 에 저장한다.
- 이후 편하게 사용자 정보를 확인하거나 사용자의 api 별 접근 권한을 확인하는데 사용된다.
- 이때 접근 권한이 동적으로 바뀜에 따라 권한 확인을 위한 DB 조회가 추가될 수 있다.
- 나는 권한이 실시간으로 바뀔 수 있고, 빠르게 반영되어야 하는 상황을 가정하였다.
- 따라서 토큰에 권한을 넣어 관리하는 것이 아닌, 매번 권한이 제한된 api 요청에서 DB 를 확인하여 사용자 권한을 확인하고 있다.

2. UserController
- 앞서 토큰과 DB 를 통해 확인한 사용자 정보를 통해, Api 접근 가능 여부가 결정된다.
- SecurityContextHolder 에서 사용자 정보를 가져와 활용 가능하다.