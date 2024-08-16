# WOORI ARTE 전시 예매 및 중개 플랫폼

## 프로젝트 소개

**WOORI ARTE** 는 예술을 사랑하는 사람들을 위한 전시 예매 및 중개 플랫폼입니다.
- 전국의 다양한 전시를 쉽게 찾아 예약할 수 있습니다.
- 매력적인 전시 공간을 가진 임대사업자와 작품을 가진 작가를 연결해주는 매칭 시스템이 있습니다.
- 매칭이 완료된 임대사업자와 작가에게 많은 사람들에게 전시를 소개하고 개최할 수 있는 서비스를 제공합니다.

**[서비스 사용자]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/a3bf0386-5422-476b-a10f-b14b815b3b85)

- 클라이언트는 예매자, 사업자(작가, 임대사업자)로 2개 타입입니다.
- 회원가입은 예매자, 작가, 임대사업자가 각각 나누어져있습니다.
- 사이트는 예매자 사이트, 사업자 사이트로 나누어져있고, 예매자 사이트는 누구나 접근 가능하며, 사업자 사이트는 사업자 로그인을 한 후에 접근 가능합니다.


**[예매자]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/84177e95-6826-43ad-bf4c-98564ce05d1c)

- 예매자 사이트에서 진행중이거나 진행 예정인 전시를 조회할 수 있습니다.
- 원하는 전시에 대해 일정 금액을 결제하면 티켓을 발급받게 됩니다.
- 주요 기능: 
	- 회원가입, 로그인, 아이디 찾기 & 비밀번호 찾기 (이메일 인증번호)
	- 예매자 페이지 전시 조회, 전시 티켓 예매
	- 개인정보수정, 예매내역(관람예정, 관람완료), 예매 취소, 탈퇴하기

**[작가]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/362155ec-c2f5-4034-99b6-c357571f4e3e)

- 작가는 자신이 만든 작품을 사업자 사이트에 등록할 수 있고, 관리자 승인 후 작품이 사업자 사이트에 업로드 됩니다.
- 자신의 작품으로 전시를 개최하고싶은 공간이 있다면, 자신의 작품을 선택하여 임대사업자에게 매칭 신청을 보낼 수 있습니다.
- 주요 기능:
	- 회원가입, 로그인, 아이디 찾기 & 비밀번호 찾기(이메일 인증번호)
	- 사업자 페이지의 작품 및 공간 아이템 조회(날짜, 지역 필터링 조회 가능), 공간 아이템에 대해 매칭 신청
	- 작품 아이템 관리(작품 등록, 수정, 삭제), 매칭신청 현황(받은 제안, 신청 현황, 성사된 매칭), 개인정보 수정, 탈퇴하기

**[임대사업자]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/30f1090b-8905-4631-9c50-94933bf0812e)

- 임대사업자는 자신의 공간을 사업자 사이트에 등록할 수 있고, 관리자 승인 후 공간이 사업자 사이트에 업로드 됩니다.
- 자신의 공간에 전시를 개최하고싶은 작품이 있다면, 자신의 공간을 선택하여 작가에게 매칭 신청을 보낼 수 있습니다.
- 주요 기능:
	- 회원가입, 로그인, 아이디 찾기 & 비밀번호 찾기(이메일 인증번호)
	- 사업자 페이지의 작품 및 공간 아이템 조회(날짜, 지역 필터링 조회 가능), 작품 아이템에 대해 매칭 신청
	- 공간 아이템 관리(공간 등록, 수정, 삭제), 매칭신청 현황(받은 제안, 신청 현황, 성사된 매칭), 개인정보 수정, 탈퇴하기

**[관리자]**

- 주요 기능:
	- 매칭관리(매칭 조회 및 상태(수락대기중, 매칭취소, 매칭완료, 전시준비중) 관리), 완료된 매칭 전시 생성
	- 작품, 공간 아이템 등록 승인 및 거절
	- 등록된 전시 목록 조회(전시 정보 및 티켓 판매 수), 등록된 전시 정보수정

## 프로젝트 목표

내결함성, 고가용성 인프라를 구축하여 사용자들에게 안정적인 전자상거래 서비스를 제공하고자 하였습니다. 
이러한 안정적인 인프라를 바탕으로 WOORI ARTE는 예매자, 임대사업자, 작가 모두에게 최적의 서비스를 제공하고자 합니다. 예매자들이 다양한 전시를 쉽게 예약할 수 있고, 임대사업자와 작가가 원활하게 전시를 개최할 수 있도록 지원함으로써, 예술을 사랑하는 모든 이들이 만족할 수 있는 플랫폼을 구축하고자 하였습니다.

## 개발 기간

2024.03.18 ~ 2024.05.08

## 팀원 구성
| 이름   | 역할                                                                                 | GitHub                                   |
|--------|--------------------------------------------------------------------------------------|------------------------------------------|
| 이대원 | 백엔드(도메인 CRUD, JWT, Email 인증), 인프라(AWS 3-tier 설계)                        | [no-wead](https://github.com/no-wead)    |
| 김영현 | 백엔드(도메인 CRUD, 결제 API)                                                        | [kim-young-hyun](https://github.com/kim-young-hyun) |
| 장지은 | 백엔드(도메인 CRUD, S3 사진 저장 API), 인프라, DevOps                                | [jieunjea](https://github.com/jieunjea)  |
| 진민호 | 인프라, DevOps                                                                       | [minho](https://github.com/mlnho)        |
| 이유경 | 프론트엔드                                                                           | [ugyeong0u0](https://github.com/ugyeong0u0) |

## 개발환경
**Front-end**: HTML, CSS, JavaScript, React.js

**Back-end**: JDK 17, SpringBoot 3.2.4, Spring Data JPA, Spring Security, Redis, Gradle, KG이니시스  API

**Infra**: AWS(Route 53, WAF, EC2, ECS, ECR, RDS, ElasticCache, S3, CloudWatch, CloudInformation, SNS, EventBridge, CodeDeploy),
 Docker, Jenkins, Github Webhook, Nginx, Grafana
 
**Tools**: IntelliJ, VSCode, Postman, DBeaver, MobaXterm, Slack, Notion

## 프로젝트 컨벤션
### API
- URI
	-   동사 + 하이픈(-) + 명사
	-   소문자
	-   마지막에 / 안넣기
	-   URI에 작성되는 영어를 복수형으로 작성한다.
	-   확장자 사용 x
-   메소드명: 동사 + 명사
    -   카멜케이스
    -   컨트롤러 서비스 리포지토리 통일
-   참고자료
    -   [https://dev-cool.tistory.com/32](https://dev-cool.tistory.com/32)

### DB

-   데이터베이스, 테이블, 뷰, 컬럼 등 모든 것 소문자로.
-   단어 사이는 underscores(_)로 구분한다. (snake_case)
-   약어를 사용하지 않는다.
    -   ex : mid_nm 대신에 middle_name 사용
-   예약어는 피한다.
    -   ex : user, local, table 등을 이름으로 사용하지 않는다.
-   데이터를 보유하는 테이블, 뷰 및 기타 관계는 복수형이 아닌 단수형 이름을 가져야 한다.
-   primary key는 → **`테이블명`** + **`_`** + **`id`**
-   나머지 필드는 모두 이름만. ex) name, company

### 브랜치 전략
- git-flow 전략을 선택하였습니다
	- main 브랜치는 배포를 위해 사용하는 브랜치입니다.
	- develop 브랜치는 개발 과정에서 사용하는 브랜치입니다. 기능 개발이 완료된 코드로 관리되며 프로젝트의 가장 최신 코드로 유지됩니다. 개발 완료된 기능은 CI/CD를 통해 자동 배포됩니다.
	- feat 브랜치는 기능 개발 시 기능마다 develop 브랜치에서 분기하여 기능 개발 완료 후 develop 브랜치에 PR을 통해 merge하였습니다.

### 커밋 메시지
> **[Feat] 커밋메시지 컨벤션 ✅**

[type]: Feat, Fix, Docs, Style, Refactor, Test, Chore etc..

참고자료
- [https://techblog-hyunjun.tistory.com/21](https://techblog-hyunjun.tistory.com/21)
-   [](https://velog.io/@shin6403/Git-git-%EC%BB%A4%EB%B0%8B-%EC%BB%A8%EB%B2%A4%EC%85%98-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)[https://velog.io/@shin6403/Git-git-커밋-컨벤션-설정하기](https://velog.io/@shin6403/Git-git-%EC%BB%A4%EB%B0%8B-%EC%BB%A8%EB%B2%A4%EC%85%98-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0)

## 서비스 아키텍처
![](https://github.com/no-wead/fisa-oheazy/assets/124620452/5c20e684-b53e-44ee-9945-72f2ed00b15e)

- 프론트엔드는 비동기 방식의 React.js로 구현, 서버는 Nginx  사용
- 백엔드는 Spring Boot를 이용한 restful API와 결제 시스템 구현
- 클라이언트 정보와 전시 등의 정보를 Mysql에 저장
- Redis를 사용하여 email 인증번호와 JWT refresh 토큰에 대해 만료기간 설정하여 저장
- Amazon CloudWatch와 Grafana를 이용하여 CPU, Memory, 활성화된 EC2 개수, 트래픽 수 모니터링 시스템 구축

## CI/CD 아키텍처
![](https://github.com/no-wead/fisa-oheazy/assets/124620452/a435b178-1705-437f-83d7-6d2fb3c0ed09)

- 모든 어플리케이션은 일관된 환경으로 확장성, 이식성을 용이하게 하기 위해 도커 이미지 파일로 배포
- 이미지 버전관리와 컨테이너를 이용한 배포, 오토스케일링을 위해 ECR, ECS 사용
- Github Webhook을 이용해 Git Repository에 생긴 변경사항 인식하여 Jenkins에서 빌드, 도커 이미지로 변환 후 ECR로 도커 이미지 파일 배포
-  ECR - Code Deploy - ECS 를 이용하여 지속적인 서비스 제공을 위한 무중단 배포 방식 중 블루/그린 배포 구현

## 인프라 아키텍처
![](https://github.com/no-wead/fisa-oheazy/assets/124620452/1b555c61-2dd8-4a27-8c83-ca5be7c7be29)

- 웹 프로젝트이므로 유지보수, 확장성, 보안을 고려해 3티어 아키텍처로 구성
- 전자 상거래 서비스에서 중요한 부분인 대용량 트래픽, 내결함성, 고가용성, 보안을 적용한 아키텍처 구성
	- 로드밸런싱을 배치해 트래픽 분산, 오토스케일링 그룹과 가용영역을 나누어 배치
	- 클라이언트로부터 들어온 요청을 Nginx의 reverse proxy를 통해 백엔드 서버와 프론트엔드 서버로 분산
	- 외부의 IP 접근이 불가능하도록 서버를 Private Subnet에 구성, 운영관리를 위한 Bastion Host 배치
	- DB를 primary, standby로 나누어 이중화 구성
	- WAF를 사용해 SQL Injection, XSS, 해외 IP 차단
- Amazon CloudWatch, AWS Lambda를 사용해 서버에 대한 이상 지표 발생 대비 Slack 알람 시스템 구성

## DB 모델링
![](https://github.com/no-wead/fisa-oheazy/assets/124620452/22e91fb8-1d2e-441e-bd80-2eba38b3f4ba)


## 서비스 기능
**[예매자 메인 페이지]**

![image](https://github.com/no-wead/fisa-oheazy/assets/124620452/3146bee6-cf3a-4547-b52a-7337f47a74e4)

**[전시 상세 페이지]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/9235649f-c597-48b4-bcc3-59d4be09d5e5)

**[로그인 페이지]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/8a6486d4-73c2-4639-90a4-4e889f8003a6)

**[마이페이지 - 예매 내역]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/688e4fb3-80e7-439a-8949-ba8f5fc5d8fd)

**[사업자 메인페이지 - 공간]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/355980ac-f9c5-4ce4-a28c-0fecf24f2987)

**[작품 아이템 상세 조회]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/e41419a3-21e7-44d2-a3d7-9395dd2106c1)


**[매칭 신청 - 아이템 선택]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/b8943b74-163a-4e6b-a033-b66af6539562)

**[아이템 관리]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/2ceed883-8428-4d03-b62e-117627aa2c45)

**[아이템 등록]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/38691236-4e55-4d3d-bd02-b9e20770d5f0)


**[아이템 수정]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/40b5687b-28dc-4694-9fa0-8f36392b7773)

**[매칭신청 현황]**

![](https://github.com/no-wead/fisa-oheazy/assets/124620452/9c99fa79-1fc4-441d-bd22-5b1439d92cf6)

## 프로젝트 소감
### 이대원
“협업을 진행하는 방법을 배울 수 있었고, 직접 구현하고 연결하는 과정에서  
고려할 사항들과 마주치는 에러들 덕분에 발전할 수 있었습니다.”

### 장지은
“백, 프론트, 인프라 모두 경험을 해봐야 더 좋은 개발자가 될 수 있다는 것을 배웠으며,  
AWS 서버를 구축하며 다양한 아키텍쳐에 대해 생각하는 것이 가장 재미있었습니다.”

### 김영현
“힘들었지만, 그만큼 보람이 있었습니다.”

### 진민호
“다양한 인프라 도구 및 AWS 서비스를 다뤄볼 수 있어서 좋았습니다.＂

### 이유경 
“한 달이라는 짧은 기간 동안 많은 어려움을 겪었지만, 이 시간을 통해  역량을 시험하고 키울 수 있는 소중한 기회였습니다. 이 경험은 앞으로의 도전에 큰 자신감을 심어주었습니다.”
