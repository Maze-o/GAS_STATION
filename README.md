

<h1>해야할거</h1>

```
아무래도 주유소 정보를 메인으로 해야 하니까 이거에 대해 구상 하기
처음 접속 시에 주유소 정보를 띄우고
주유소 정보를 표시하는 버튼만 크게 만들어서
오피넷 api정보를 가져와서 
인포윈도우를 커스텀해 기름값, 장소 즐겨찾기 등 구현하기 (길찾기도 포함)
```

<h2>스타일</h2>

```
1. 홈 화면 버튼들 마우스 오버 시에 부트스트랩5 이용해서 툴팁 표시하기
2. 로고 이미지 정하기
3. 버튼 디자인 색 바꾸기
4. 왼쪽 오프캔버스 디자인 하기 (색, 부분 )


```

<h2>기능</h2>

```
내 장소 블럭 만들어서
집 주소 , 자주가는 장소 등등 커스텀해서 등록하기 (백엔드로 DB연동 필요, 로그인 강제)

상단 부분에 내 주소 ex) 대구광역시 -> 달성군 -> ??동 까지만 표시되게 하기 , 날씨 정보 표시

1. 카테고리 선택 후 인포윈도우 띄운 상태로 주소 검색 했을 때 인포윈도우 지우기
1-2. 주소 검색 시 위로 이동 버튼 제대로 만들기 (position제대로 잡기)
1-3. 주소 검색 시 밑에 나오는 페이지네이션 부분 다른 탭으로 이동 했을 시 안보이게 감추기
1-4. 최초 접속 시에 현재 위치 안나오게 하기
3. 장소 검색 후 리스트 커스텀 해서 길찾기 버튼 끼워넣기 (출발, 도착 버튼)
4. 길찾기 기능 구현 (탭에서 주소 정보 받은 후 카카오맵으로 링크 ) - https://devtalk.kakao.com/t/topic/113442
5. 주소 표시 버튼을 만들어서 (+, -컨트롤 위에 표시) 지도 클릭 했을 시에 내 현재 주소 표시 후 인포윈도우에 길찾기 도착, 출발 버튼 표시
6. 즐겨찾기 기능 구현 
```
















<h1>길찾기 구현 참고 사이트</h1>
https://developers.kakaomobility.com/docs/navi-api/directions/#%ED%98%B8%EC%B6%9C-%EB%B0%A9%EC%8B%9D
브이월드 <br>
REACT - https://codingapple.com/unit/react1-install-create-react-app-npx/ <br>

REACT 연동 - https://velog.io/@u-nij/Spring-Boot-React.js-%EA%B0%9C%EB%B0%9C%ED%99%98%EA%B2%BD-%EC%84%B8%ED%8C%85#1-spring-boot
 
지도 api - <br>
leaflet  <br>
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


