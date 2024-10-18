# -
주유소 정보 프로젝트

https://ziszini.tistory.com/121 /카카오 지도 api 적용

<h3>프로젝트 설계</h3>


패지키 구조 - https://velog.io/@jsb100800/Spring-boot-directory-package
'

개념(조직화,관계도,그림,무슨 기능을 넣을것인지) -> 논리(탭에 대한 필요한 세부항목) -> 물리(개발환경 구축)
draw.io

주유소 정보 만들 예정.

구상  -      
웹을 들어가면 지도로 시작. (내 근처 주유소 정보 바로 뜨게)      
마음에 드는 주유소 즐겨찾기기능.      
즐겨찾기 주유소만 뜨도록 설정하는 탭 만들기. [로그인 구현 필요 (간단?하게 할까 고민중..)]      
주유소를 클릭하면 주유소에 대한 정보 (네이버 주유소 정보 링크)  
고속도로 별 주유소 가격 (어려울수도. 보류)

<h2>개발환경</h2>
java - jdk11 https://kjchoi.co.kr/17 <br/>
spring boot - io.start.spring 사이트를 이용하면 쉽게 프로젝트 템플릿 파일을 얻을 수 있다 <br/>
- 프로젝트 import https://data-make.tistory.com/655 (gradle project로 import 해야함) <br/>
git 연동 - https://crong-cat.tistory.com/entry/STS-github-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0github-%EC%B2%98%EC%9D%8C-commit<br/>
빌드구성 - gradle <br/>
편집툴 - STS (이클립스 튜닝판) <br/>
DBMS - MariaDB Heidi sql db명 :  gas-db, table 명 : user<br/>
api - 주유소, 지도 <br/>
DB Connect - JPA https://velog.io/@seulki412/Spring-Boot-JPA-%EC%97%B0%EA%B2%B0%ED%95%98%EA%B8%B0-DB-%EC%98%A4%EB%9D%BC%ED%81%B4-%EC%97%B0%EA%B2%B0-Entity-Id-JPQL  (DB연결) <br/>
View Template - thymeleaf https://lifere.tistory.com/entry/Spring-Boot-Thymeleaf-%ED%83%80%EC%9E%84%EB%A6%AC%ED%94%84-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-%EB%B0%8F-%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0  (타임리프 연결)<br/>
front - css, javascript<br/>
API - https://www.data.go.kr/data/15076636/openapi.do(주유소), https://tmapapi.tmapmobility.com/  (지도 - 티맵)      
API test - swagger https://lucas-owner.tistory.com/28 <br/>
배포파일 - BootWar <br/>
배포서버 - <br/>
AWS EC2 - AWS 정식 서버 버젼, 1년 무료후 유료(유료가 비쌈. 몇만원대)<br/>
AWS 라이트세일 - AWS 가상서버 버전, 3달무료 후 한달에 5천원 발생. 사용량 만큼이므로 잠시 정지시켜 두거나, 필요없을시 지워도 됨. <br/>

https://blog.naver.com/nissisoft21/222802517122 - STS 설정

https://golf-dev.tistory.com/39 - API설계


HEADER - 로고 :  지도,검색,로그인,로그아웃,즐겨찾기

BODY - 지도


https://jong-bae.tistory.com/32

롬복 오류 ->  https://velog.io/@ibd1229/Spring-Boot-3.0-Slf4j-%EC%82%AC%EC%9A%A9%EC%8B%9Clog-cannot-be-resolved-%EC%97%90%EB%9F%AC-Lombok-%EC%9D%B8%EC%8B%9D-%EC%95%88%EB%90%A8-%EC%84%A4%EC%B9%98%ED%96%88%EB%8A%94%EB%8D%B0%EB%8F%84-%EC%95%88%EB%90%A8-%ED%95%B4%EA%B2%B0

참고할 로그인 디자인 - 원티드

참고 지도 사이트 - https://map.kakao.com/


