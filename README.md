# 특정 고객 거래내역 조회 서비스 개발

## 목차
1. 개발 환경
2. 빌드 및 실행
3. API 기능명세
5. 문제해결 방법

## 개발 환경
- 개발 환경
    - IDE : Spring Tool Suite 4
    - OS : Mac OS X
    - Git
    - Maven
- 서버 개발 환경
    - Java 8
    - Spring Boot 2.3.1
    - Spring Data Redis
    - Junit 5
    - Redis Server 6.0.5

## 빌드 및 실행

    % cd accountmanager
    % mvn package
    % java -jar ./target/accountmanager-0.0.1-SNAPSHOT.jar

- 빌드를 위한 maven, Java는 먼저 설치되어 있어야 한다.
- 접속 URI : http://localhost:8080/
- swagger URI : http://localhost:8080/swagger-ui.html#
- 추가로 Redis 사용을 위해 다음의 주소와 포트에 Redis 가 실행 중이어야 한다. (src/main/resources/appliaction.properties에서 수정 가능)
    - 127.0.0.1:6379


## API 기능 명세

### Rest API 기반 쿠폰 시스템 API 명세
* API의 Body의 Content-Type : application/json

| Method | API | Parameter | 기능 |
|--|--|--|--|
|GET|/api/v1/accounts/maximums|-|첨부된 데이터 내 연도별 합계 금액이 가장 많은 고객 추출|
|GET|/api/v1/accounts/dormant|-|첨부된 데이터 내 연도별 거래가 없는 고객 내역 추출 |
|GET|/api/v1/branches|-|연도별 관리점 별 거래금액 합계가 큰 순서대로 출력|
|GET|/api/v1/branches/{brName}|지점명|지점명 입력 시 해당 지점의 거래 금액 합계 출력|

## 문제해결 방법
1. 시스템 구성
1.1. In memory DB
- Spring Batch를 통해 서버 구동 시 "resource"에 있는 지정된(프로퍼티 파일에 설정)파일을 읽어, in memory db인 redis에 적재
- FlatFileItemReader와 ItemWriter를 이용하여 데이터 등록

2.2. 입출력 구성
- 입력을 json 으로 입력받기 위해 REST API 로 구성
- 출력은 ResponseEntity를 통해 결과 코드와 결과를 json으로 반환

2. 모델 구성
2.1. 계좌 정보(Account) : 계좌 번호(PK), 계좌명, 관리점코드
 - 계좌 번호를 ID로 사용하여 "계좌 번호-계좌 정보" 키-값으로 사용하여, Spring Data Redis Repository를 이용해 관리
 
2.2. 관리점 정보(Branch) : 관리점 명(PK), 관리점 코드
- 관리점 명을 ID로 사용하여 "관리점명-관리점정보" 키-값으로 사용하여, Spring Data Redis Repository를 이용해 관리

2.3 거래 내역 정보(Transaction) : 계좌 번호, 거래 일시, 거래 금액, 수수료, 취소 여부
- 거래 내역 정보는 계좌 번호별 목록으로 관리
- 계좌 번호 별 목록으로 관리하므로, Redis SetOperations를 사용하는 TransactionRepository 구현

3. 문제 풀이
- 모든 문제의 거래 내역 중 취소된 거래는 제외한다.
3.1. 연도별 합계 금액이 가장 많은 고객 추출
- 대상 연도(2018년, 2019년) 별로 합계 금액(거래 금액 - 수수료) 계산

3.2. 연도별 거래 내역 없는 계좌 정보 추출
- 대상 연도(2018년, 2019년) 별로 거래 내역이 없는 계좌 정보 목록 구성

3.3. 연도별 관리점별 거래 금액 합계 내림 차순 정렬
- 관리점에 속한 계좌의 거래 내역의 거래 내역 합계를 내림차순으로 정렬

3.4 관리점명 입력 시 해당 지점의 거래 금액 합계 정보 추출
- 관리점 관리 Repository에서 관리점명을 키로 값을 찾아, 값이 존재하지 않거나 통폐합 지점인 경우 Optional.empty()를 반환하여 404 코드와 에러 메시지 반환
- 통폐합 관리점 정보는 속성 정보를 통해 설정하고, 통합한 관리점 검색 시 통합된 관리점의 정보도 출력


* API가 정상 동작하지 않을 경우, REDIS 서버의 current DB를 flush하고 재실행한다.
