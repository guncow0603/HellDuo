# HellDuo

## 프로젝트 소개
>  HellDuo는 기존의 PT 시스템과 달리, 사용자가 원하는 장소에서 원하는 트레이너를 찾아 온라인으로 간편하게 예약할 수 있는 서비스입니다.


## 서비스 아키텍쳐
![image](https://github.com/user-attachments/assets/6f3f1a96-5e41-47c5-b6b8-198e690f4d20)

## ERD
![image (1)](https://github.com/user-attachments/assets/a9c09bd9-e630-485f-b70f-d2777c1cd02e)


## 주요 기능

1. **회원가입 및 로그인**
   - JWT 토큰을 활용한 로그인 지원

2. **프로필 관리**
   - 트레이너와 회원의 프로필 및 정보 관리

3. **예약 시스템**
   - 예약, 변경, 취소 기능 제공

4. **결제 시스템**
   - 토스 API를 활용한 온라인 결제 지원

5. **평가 시스템**
   - 후기 및 별점 평가 제공

6. **채팅/메시지**
   - 실시간 트레이너-회원 간 소통

7. **푸시 알림**
   - 채팅이 올 경우 알림 제공

8. **커뮤니티**
   - 회원 간 소통 지원

9. **트레이너 피티 생성 기능**
   - 트레이너가 PT 세션을 생성하고 관리

10. **리뷰 조회 기능**
    - 회원과 트레이너가 작성된 리뷰를 조회


## 사용 기술 스택
<details>
  <summary>BackEnd</summary>
 
* Java 17
* Spring boot 3.2.1
* Spring security 6.2.1
* JWT
* gradle 8.5
* QueryDSL 5.0.0
* spring data jpa 3.2.1
* spring data redis 3.2.1
* WebSocket 3.2.1
* STOMP
* toss API
* SSE
  
</details>

<details>
  <summary>FrontEnd</summary>
 
* HTML
* CSS
* JavaScript(jQuery)
* AJAX
* bootstrap 
* thymeleaf 
* js-cookie
  
</details>

<details>
  <summary>DB</summary>
 
* MySQL 8.2.0
* Redis
* h2 2.2.224
  
</details>

<details>
  <summary>infra</summary>
 
* AWS
  * EC2
  * S3
  * RDS
  * CodeDeploy
  * Application Load Balancer
  * Docker
* GitHub Actions
  
</details>

<details>
  <summary>Docs</summary>
 
* Jmeter 5.6.3

</details>

## 기술적 의사 결정
<details>
  <summary> 인증 및 인가</summary>

### JWT와 Redis 기반 리프레시 토큰 관리

1. **JWT (JSON Web Token) 사용**
    - **엑세스 토큰**과 **리프레시 토큰**을 사용하여 인증 및 인가를 관리합니다.
        - **엑세스 토큰**: 사용자 인증 정보를 담고 있으며, 짧은 유효기간을 가집니다. API 요청 시 헤더에 포함되어 인증을 처리합니다.
        - **리프레시 토큰**: 엑세스 토큰의 유효기간이 만료되었을 때, 새로운 엑세스 토큰을 발급하기 위해 사용됩니다. 리프레시 토큰은 상대적으로 긴 유효기간을 가집니다.
2. **토큰 발급 및 관리**
    - 사용자가 로그인 시 **엑세스 토큰**과 **리프레시 토큰**을 함께 발급하여 사용자의 세션을 관리합니다.
    - **엑세스 토큰**은 클라이언트 측에 저장되어, API 요청 시마다 서버로 전송되어 인증을 수행합니다.
    - **리프레시 토큰**은 서버에서만 안전하게 저장되며, 엑세스 토큰이 만료되면 리프레시 토큰을 사용하여 새로운 엑세스 토큰을 발급합니다.
3. **JWT의 장점**
    - **무상태성 (Stateless)**: JWT는 서버가 클라이언트의 세션 상태를 관리할 필요 없이 인증을 처리할 수 있어, 서버 부하를 줄이고 확장성을 높여줍니다.
4. **리프레시 토큰 관리: Redis 활용**
    - 리프레시 토큰은 **Redis**에 저장하여 관리합니다. Redis는 **빠르고 확장 가능한 데이터 저장소**로 리프레시 토큰을 저장하는 데 적합합니다.
    - 리프레시 토큰은 **Key-Value** 형태로 Redis에 저장됩니다.
        - **Key**: 사용자 ID 또는 세션 ID 등 유니크한 값
        - **Value**: 해당 사용자의 리프레시 토큰
5. **리프레시 토큰의 유효기간 관리**
    - 리프레시 토큰의 유효기간은 설정된 시간 동안 유지되며, 유효기간이 만료되거나 사용될 경우 Redis에서 삭제됩니다.
    - Redis의 **TTL (Time To Live)** 기능을 사용하여 리프레시 토큰에 유효기간을 설정할 수 있습니다. 만료된 토큰은 자동으로 삭제되어 보안성을 강화합니다.
6. **토큰 갱신 및 삭제**
    - 사용자가 엑세스 토큰을 갱신하려면, **리프레시 토큰**을 서버로 보내야 합니다.
    - 서버는 Redis에서 해당 리프레시 토큰을 조회하여 유효한지 확인하고, 유효하다면 새로운 엑세스 토큰을 발급합니다. 발급된 새로운 엑세스 토큰은 클라이언트에 전달됩니다.
    - Redis에 저장된 기존 리프레시 토큰은 **삭제**하거나 **갱신**될 수 있습니다.
    - 리프레시 토큰이 만료되거나 삭제된 경우, 사용자는 **재로그인**을 해야 합니다.
7. **Redis의 장점**
    - **고속 처리**: Redis는 메모리 기반 데이터베이스로 빠른 읽기/쓰기를 지원하여 리프레시 토큰의 유효성 검사와 갱신을 신속하게 처리할 수 있습니다.
    - **세션 공유**: 서버가 여러 대일 경우, Redis를 이용해 세션 상태를 공유할 수 있어 시스템 확장성을 높여줍니다.
    - **자동 만료**: TTL을 활용하여 리프레시 토큰에 유효기간을 설정할 수 있으며, 만료된 토큰은 자동으로 삭제되어 보안성을 강화합니다.

---

### 토큰 유효기간 설정

- **엑세스 토큰**: 1시간 (60 * 1000 * 60)
- **리프레시 토큰**: 3일 (60 * 1000L * 60 * 24 * 3)

이를 통해, **엑세스 토큰**은 상대적으로 짧은 시간 동안 유효하고, **리프레시 토큰**은 긴 유효기간을 제공하여 사용자 경험을 최적화하면서도 보안성을 유지할 수 있습니다. 
</details>

<details>
  <summary> 레디스 분산락을 활용한 피티 예약 및 게시글 좋아요 구현</summary>


### 1. 분산락(Distributed Lock) 개념

분산락은 여러 서버에서 동시에 자원에 접근할 때 발생할 수 있는 경쟁 조건(race condition)을 방지하기 위해 사용됩니다. 여러 인스턴스가 동일한 자원에 동시에 접근하려 할 때, 동시성 문제를 해결하고 데이터 일관성을 유지할 수 있도록 돕습니다. 이 과정에서 분산락은 자원의 충돌을 방지하고, 시스템의 안정성을 보장합니다.

### 2. 레디스를 활용한 분산락 구현

레디스는 빠르고 안정적인 분산락 처리 메커니즘을 제공하여, 분산 환경에서 자원의 동시 접근을 제어합니다. 이를 통해 여러 서버가 동일 자원에 접근할 때 발생할 수 있는 충돌을 방지할 수 있습니다.

### 3. 피티 예약에 대한 분산락 적용

피티 예약 시, 사용자가 예약을 진행하는 동안 분산락을 사용하여 동일한 피티에 대해 여러 사용자가 동시에 예약을 시도하지 않도록 보장합니다. 예를 들어, 한 사용자가 피티를 예약하는 동안, 다른 사용자가 동일 피티를 예약하려면 이전 예약이 완료될 때까지 대기해야 합니다. 이를 통해 중복 예약을 방지하고, 예약 처리의 일관성을 유지할 수 있습니다.

### 4. 게시글 좋아요에 대한 분산락 적용

게시글에 좋아요를 클릭하는 경우, 여러 사용자가 동시에 좋아요를 누를 수 있습니다. 이때 분산락을 적용하여 중복된 좋아요가 반영되지 않도록 하며, 게시글의 좋아요 수가 정확하게 반영되도록 합니다. 락을 통해 중복된 요청을 방지하고, 데이터의 일관성을 유지합니다.

### 5. 레디스를 활용한 분산락의 장점

- **고속 처리**: 레디스는 메모리 기반 데이터베이스로 빠른 데이터 처리 성능을 자랑합니다. 이로 인해 분산락의 성능이 매우 우수하며, 빠른 속도로 자원 접근을 제어할 수 있습니다.
- **신뢰성**: 락의 만료 시간을 설정할 수 있어 일정 시간이 지나면 락이 자동으로 해제되며, 시스템의 안정성을 높이고 데드락을 방지할 수 있습니다.
- **확장성**: 여러 서버 인스턴스가 존재하는 환경에서 레디스를 활용한 분산락을 통해 서버 간 상태 공유 및 자원 동기화를 손쉽게 관리할 수 있습니다.

### 6. 분산락 구현 흐름

1. **락 획득**: 특정 자원에 접근하기 전에, 레디스에서 락을 시도하여 자원에 대한 독점적인 접근을 확보합니다.
2. **작업 수행**: 락을 성공적으로 획득한 후, 해당 자원에 대해 필요한 작업을 수행합니다(예: 피티 예약, 게시글 좋아요).
3. **락 해제**: 작업이 완료되면 락을 해제하여 다른 사용자가 해당 자원에 접근할 수 있도록 합니다.
4. **락 만료**: 락의 유효시간을 설정하여 일정 시간이 지나면 락이 자동으로 해제되도록 하여, 시스템이 멈추지 않도록 합니다
</details>

<details>
  <summary> 캐싱을 통한 성능 최적화</summary>

### 1. **캐싱된 기능 구현**

캐싱 전략을 활용하여 반복적인 데이터 조회의 성능을 최적화합니다. 아래는 캐싱이 적용된 주요 기능들입니다:

- **피티 조회**: 사용자 현위치에 가장 가까운 피티를 조회할 때 캐싱을 활용하여 반복적인 조회 성능을 향상시킵니다. 현위치가 자주 바뀌지 않는 한 캐시된 데이터를 사용해 빠르게 결과를 반환합니다. 이를 통해 같은 데이터를 여러 번 조회할 때마다 데이터베이스 부하를 줄일 수 있습니다.
- **가장 좋아요 많은 게시물 조회**: 좋아요 수는 자주 변경되지 않기 때문에 이를 캐시하여 빠르게 조회할 수 있습니다. 캐시된 데이터를 사용하여 서버의 부하를 줄이고, 데이터베이스 요청을 최소화합니다.
- **평점순 트레이너 조회**: 트레이너의 평점 순으로 데이터를 조회할 때 캐시를 사용하여, 데이터가 자주 변하지 않는다면 캐시된 결과를 사용해 빠른 조회를 제공합니다. 이 기능은 트레이너의 정보가 빈번하게 변경되지 않으므로 캐시를 활용하는 데 효과적입니다.

### 2. **캐싱 전략**

캐싱 전략은 **읽기 전략**과 **쓰기 전략**으로 나눠집니다:

- **읽기 전략**: 데이터 조회 시 먼저 Redis에서 데이터를 요청하고, 만약 Redis에 데이터가 없으면 데이터베이스에서 데이터를 조회한 후 Redis에 저장합니다. 이 방식은 데이터베이스에 대한 부하를 줄이고 응답 속도를 개선하는 데 유리합니다.
- **쓰기 전략**: 데이터를 저장할 때는 서버에만 저장하며, 데이터베이스에 즉시 반영되도록 합니다. 이때, 캐시된 데이터도 업데이트하거나 삭제하여 일관성 있는 상태를 유지할 수 있습니다.

### 3. **장점**

캐싱을 통한 성능 최적화는 시스템에 여러 가지 장점을 제공합니다:

- **성능 개선**: 캐시된 데이터를 사용하여 데이터베이스 조회를 줄이고, 서버 응답 속도를 빠르게 하여 시스템 성능을 향상시킵니다. 자주 조회되는 데이터는 캐시로 처리하여 데이터베이스와 서버의 부담을 줄일 수 있습니다.
- **자원 절약**: 자주 조회되는 데이터를 캐시함으로써 데이터베이스 리소스를 절약하고, 서버에 대한 부하를 감소시켜 자원 관리가 효율적으로 이루어집니다.

### 4. **캐싱 전략의 효과적인 활용**

캐싱 전략을 효과적으로 활용하기 위한 방법은 다음과 같습니다:

- **TTL(Time-To-Live) 설정**: 캐시된 데이터가 유효한 시간 동안만 Redis에 저장되도록 TTL 값을 설정하여, 일정 시간이 지나면 캐시된 데이터가 자동으로 만료되도록 할 수 있습니다. 이를 통해 Redis에 불필요한 데이터가 쌓이는 것을 방지할 수 있습니다.
- **조건부 캐시 업데이트**: 캐시된 데이터가 자주 변경되지 않는 경우에만 캐시를 활용하고, 데이터를 갱신해야 할 경우에는 적절한 타이밍에 캐시를 업데이트하거나 삭제합니다. 이를 통해 최신 데이터와 캐시된 데이터를 동기화할 수 있습니다.

### 5. **단점 및 해결 방법**

캐싱을 사용함에 따라 발생할 수 있는 단점과 해결 방법은 다음과 같습니다:

- **Redis에 정보가 쌓일 수 있음**: 캐시된 데이터가 Redis에 계속 쌓이면 메모리 사용량이 증가할 수 있습니다. 이를 해결하기 위해 TTL을 설정하여, 일정 시간이 지나면 캐시된 데이터가 자동으로 만료되도록 하여 Redis의 메모리 사용을 최적화할 수 있습니다.
- **정보 수정이 지연될 수 있음**: 캐시된 데이터가 즉시 수정되지 않으면 최신 정보를 반영하는 데 지연이 발생할 수 있습니다. 이를 해결하기 위해 TTL 값을 설정하거나, 데이터 수정 시 캐시를 업데이트하는 로직을 추가하여 최신 정보가 반영되도록 할 수 있습니다.

이러한 방법들을 통해 캐시 시스템을 더욱 효과적으로 관리하고, 시스템 성능을 최적화할 수 있습니다.
</details>

<details>
  <summary> 도커를 활용한 블루-그린 배포 구현(무중단 배포)</summary>


### 1. **무중단 운영**

- 배포 중에도 사용자 서비스가 중단되지 않도록 보장.
- **블루-그린 배포**를 통해 현재 운영 중인 환경(블루)과 새 버전이 실행되는 환경(그린)을 동시에 운영.
    - 배포 준비 중 기존 **블루 환경**은 정상 작동하며 사용자는 영향을 받지 않음.
    - **그린 환경** 배포 완료 후 트래픽을 그린 환경으로 전환.
    - 장애 발생 시 **블루 환경**으로 즉시 롤백 가능.

---

### 2. **신속한 롤백 전략**

- 새 버전 배포 후 문제가 발생할 경우 즉시 이전 환경으로 복구.
- 블루-그린 배포 방식은 롤백 시간 최소화에 효과적.
    - 문제 발생 시 트래픽을 기존 **블루 환경**으로 전환하여 즉각 복구.
    - AWS CodeDeploy의 **자동 롤백** 기능을 활용해 장애 발생 시 자동 복구 가능.

---

### 3. **사용자 경험 최적화**

- 배포 과정에서 사용자에게 최소한의 영향 제공.
- 새 버전 준비 후 트래픽 전환만으로 즉시 서비스 제공 가능.
- **실시간 모니터링**으로 배포 상태를 점검하고 장애 시 신속 대응.
- 무중단 배포를 통해 배포 과정 중 사용자 경험 저하 최소화.

---

### 블루-그린 배포 전략 요약

- **안정성**: 기존 환경 유지하며, 새 환경 배포.
- **속도**: 배포 및 롤백 과정에서 빠른 대응 가능.
- **사용자 경험 개선**: 서비스 중단 없는 안정적 운영.
- **자동화**: GitHub Actions와 AWS CodeDeploy를 활용한 무중단 배포 자동화.

---

### 배포 과정 구현 (GitHub Actions + AWS CodeDeploy)

### 1. **GitHub Actions Workflow**

```yaml
yaml
코드 복사
name: CICD

on:
  push:
    branches: [ "main" ]

env:
  PROJECT_NAME: hellduo
  BUCKET_NAME: bluegreen-hellduo
  CODE_DEPLOY_APP_NAME: BlueGreen
  DEPLOYMENT_GROUP_NAME: BlueGreen

jobs:
  build-docker:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3

      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Generate application-prod.yml
        run: |
          echo "${{ secrets.YML_PROD }}" | base64 --decode > ./src/main/resources/application-prod.yml

      - name: Build with Gradle
        run: ./gradlew clean build -x test --stacktrace

      - name: Configure AWS
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: ap-northeast-2

      - name: Login to Amazon ECR
        id: login-ecr
        uses: aws-actions/amazon-ecr-login@v1

      - name: Build and Push Docker Image
        run: |
          docker build -t ${{ steps.login-ecr.outputs.registry }}/hellduo:${{ github.sha }} .
          docker push ${{ steps.login-ecr.outputs.registry }}/hellduo:${{ github.sha }}

      - name: Upload Deployment Package to S3
        run: |
          mkdir scripts
          echo '#!/bin/bash' > scripts/deploy.sh
          echo "docker pull ${{ steps.login-ecr.outputs.registry }}/hellduo:${{ github.sha }}" >> scripts/deploy.sh
          zip -r ${{ github.sha }}.zip scripts appspec.yml
          aws s3 cp ${{ github.sha }}.zip s3://$BUCKET_NAME/$PROJECT_NAME/${{ github.sha }}.zip

      - name: Start AWS CodeDeploy
        run: |
          aws deploy create-deployment \
            --application-name $CODE_DEPLOY_APP_NAME \
            --deployment-group-name $DEPLOYMENT_GROUP_NAME \
            --deployment-config-name CodeDeployDefault.OneAtATime \
            --s3-location bucket=$BUCKET_NAME,bundleType=zip,key=$PROJECT_NAME/${{ github.sha }}.zip

```

---

### 2. **Dockerfile**

```
dockerfile
코드 복사
FROM openjdk:17

ARG JAR_FILE=build/libs/HellDuo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

CMD ["sh", "-c", "java -jar -Dspring.profiles.active=prod app.jar"]

```

---

### 3. **AppSpec.yml**

```yaml
yaml
코드 복사
version: 0.0
os: linux

files:
  - source: /
    destination: /home/ubuntu/hellduo
    overwrite: yes

permissions:
  - object: /
    pattern: "**"
    owner: ubuntu
    group: ubuntu
    mode: 755

hooks:
  AfterInstall:
    - location: scripts/deploy.sh
      timeout: 200
      runas: root

```

---

### 결과

- **블루 환경**과 **그린 환경**을 분리하여 안정적인 무중단 배포 환경 구축.
- AWS CodeDeploy와 GitHub Actions를 활용하여 배포와 롤백을 자동화.
- 서비스 중단 없이 새로운 애플리케이션 버전을 사용자에게 제공.
</details>

<details>
  <summary> **채팅 시스템 구현**</summary>


- **WebSocket**: 클라이언트와 서버 간의 지속적인 양방향 연결을 유지하며, 실시간 통신을 지원합니다. WebSocket을 통해 클라이언트와 서버가 실시간으로 데이터를 주고받을 수 있습니다.
- **STOMP**: WebSocket 위에서 동작하는 메시징 프로토콜로, 메시지의 발행과 구독을 관리합니다. 이를 통해 여러 사용자가 동시에 구독할 수 있는 채팅방을 관리하고 메시지를 효율적으로 전달할 수 있습니다.

---

### **2. 시스템 설계**

- **채팅방 관리**:
    - **채팅방 생성**: 사용자가 다른 사용자와의 채팅방을 생성하면 `ChatRoom` 객체를 생성하여 저장합니다.
    - **채팅방 조회**: 사용자가 참여한 채팅방을 조회합니다.
    - **채팅방 삭제**: 사용자가 채팅방을 삭제할 수 있습니다. 이때, 해당 채팅방의 참여자만 삭제할 수 있도록 보안을 강화합니다.
- **메시지 관리**:
    - **메시지 전송**: 클라이언트가 채팅방에 메시지를 전송하면, 서버는 이를 처리하여 해당 채팅방에 구독된 다른 사용자에게 메시지를 전송합니다.
    - **메시지 조회**: 사용자가 채팅방에 전송된 메시지 목록을 조회할 수 있습니다.

---

### **3. 코드 구현**

### **ChatRoomService (채팅방 서비스)**

`ChatRoomService` 클래스는 채팅방을 생성, 조회, 삭제하는 기능을 제공합니다.

```java
java
코드 복사
@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomCreateRes createChatRoom(Long userId, ChatRoomCreateReq req) {
        User receiver = userRepository.findUserByIdWithThrow(req.receiverId());
        User sender = userRepository.findUserByIdWithThrow(userId);

        // 이미 채팅방이 존재하면 해당 채팅방 반환
        if (chatRoomRepository.existsByReceiverIdAndSenderId(receiver.getId(), sender.getId())) {
            ChatRoom rm = chatRoomRepository.findByReceiverIdAndSenderId(receiver.getId(), sender.getId()).get();
            return buildChatRoomCreateRes(rm);
        }

        // 새 채팅방 생성
        ChatRoom room = ChatRoom.builder()
            .name("채팅방")
            .sender(sender)
            .receiver(receiver)
            .build();

        chatRoomRepository.save(room);
        return buildChatRoomCreateRes(room);
    }

    private ChatRoomCreateRes buildChatRoomCreateRes(ChatRoom room) {
        UserRes sender = new UserRes(room.getSender().getId(), room.getSender().getNickname());
        UserRes receiver = new UserRes(room.getReceiver().getId(), room.getReceiver().getNickname());
        return new ChatRoomCreateRes(room.getId(), receiver, sender);
    }

    public ChatRoomDeleteRes deleteChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByIdWithThrow(roomId);
        if (!(chatRoom.getReceiver().getId().equals(userId) || chatRoom.getSender().getId().equals(userId))) {
            throw new NotChatRoomUserException(ChatRoomErrorCode.NOT_CHATROOM_USER);
        }
        chatRoomRepository.delete(chatRoom);
        return new ChatRoomDeleteRes("삭제가 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<ChatRoomListRes> getChatRoomList(Long userId) {
        List<ChatRoom> rooms = chatRoomRepository.findAllByReceiverIdOrSenderId(userId, userId);
        return rooms.stream()
                    .map(room -> new ChatRoomListRes(room.getId(), room.getSender().getNickname(), room.getReceiver().getNickname()))
                    .collect(Collectors.toList());
    }
}

```

### **ChatService (채팅 서비스)**

`ChatService` 클래스는 메시지 전송과 메시지 목록 조회를 담당합니다.

```java
java
코드 복사
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    public ChatMsgRes createChat(Long roomId, ChatMsgReq req, StompHeaderAccessor session) {
        ChatRoom chatRoom = findChatRoom(roomId);
        String tempName = session.getSessionAttributes().get("name").toString();
        User sender = findUserByName(tempName);

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(req.message())
                .build();

        chatRepository.save(chat);
        return new ChatMsgRes(chat.getId(), chat.getSender().getNickname(), chat.getContent(), chat.getCreatedAt());
    }

    public List<ChatListRes> getChatList(Long roomId) {
        List<Chat> chats = chatRepository.findAllByChatRoomId(roomId);
        return chats.stream()
                    .map(chat -> new ChatListRes(chat.getId(), chat.getSender().getNickname(), chat.getContent(), chat.getCreatedAt()))
                    .collect(Collectors.toList());
    }

    private ChatRoom findChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("해당하는 채팅방이 없습니다."));
    }

    private User findUserByName(String name) {
        return userRepository.findByNickname(name).orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }
}

```

---

### **4. WebSocket 및 STOMP 설정**

- **WebSocket 연결**: 서버는 WebSocket을 통해 클라이언트와 연결을 유지합니다. 클라이언트가 WebSocket에 연결하면, 서버는 STOMP를 통해 메시지를 관리합니다.
- **STOMP 메시지 매핑**: `@MessageMapping`과 `@SendTo` 어노테이션을 사용하여 메시지를 수신하고, 필요한 채팅방에 메시지를 발행합니다

---
</details>

<details>
  <summary> **이미지 리사이징**</summary>

### 1. **리사이징을 위한 `BufferedImage` 사용**

- **의사결정**: 이미지 리사이징을 위해 `BufferedImage`를 사용하여 이미지를 처리.
- **이유**: `BufferedImage`는 Java에서 이미지를 다루는 표준 클래스이며, 다양한 형식의 이미지를 읽고, 수정하며, 새로운 이미지를 생성할 수 있기 때문에 적합한 선택입니다. 이미지 리사이징을 할 때 `BufferedImage`를 활용하면 손쉽게 이미지를 메모리에서 처리할 수 있습니다.

### 2. **리사이징을 하는 이유**

- **목적**: S3에 업로드하는 이미지 파일의 크기를 조정하여 용량을 줄이고, 클라이언트 측에서 이미지를 더 효율적으로 불러올 수 있게 하려는 목적입니다.
    - **성능 최적화**: 원본 이미지가 너무 크면 파일 전송 시간과 로딩 속도가 길어져 성능에 악영향을 미칩니다. 리사이징을 통해 이미지를 필요한 크기로 조정하여 업로드 용량을 줄이고, 전송 속도와 클라이언트의 로딩 성능을 개선할 수 있습니다.
    - **저장 공간 절약**: 서버나 클라우드에서 저장할 수 있는 이미지 용량을 줄여 저장 공간을 효율적으로 사용하려는 목적입니다.
    - **호환성**: 다양한 화면 크기와 디바이스에 맞는 이미지를 제공하여 최적화된 사용자 경험을 제공하기 위함입니다.

### 3. **리사이징 로직**

- **의사결정**: `getScaledInstance` 메소드를 사용하여 이미지를 리사이징.
    
    ```java
    java
    코드 복사
    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
    
    ```
    
- **이유**:
    - `getScaledInstance`는 이미지 크기를 조정하는 간단한 방법을 제공하며, `SCALE_SMOOTH` 옵션은 리사이징할 때 부드러운 이미지 품질을 유지합니다. 이는 이미지 품질을 최대로 유지하면서 크기만 조정하려는 목적에 부합합니다.
    - 리사이징된 이미지를 새로운 `BufferedImage` 객체에 그려서, 리사이징된 이미지를 저장할 수 있도록 처리합니다. 이렇게 함으로써 메모리 내에서 이미지 크기만 조정하고 원본 이미지를 그대로 유지할 수 있습니다.

### 4. **리사이징된 이미지 처리**

- **의사결정**: 리사이징된 이미지를 새로운 `BufferedImage` 객체에 그려 저장.
    
    ```java
    java
    코드 복사
    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
    
    ```
    
- **이유**: `getGraphics().drawImage()`를 사용하여 리사이징된 이미지를 새로운 `BufferedImage`에 그립니다. 이는 원본 이미지를 수정하지 않고 리사이징된 이미지를 새롭게 생성할 수 있게 해줍니다. `BufferedImage` 객체는 이미지의 처리가 완료된 후, 파일로 저장하거나 클라우드 서비스에 업로드할 수 있기 때문에 이후 작업을 원활히 진행할 수 있습니다.

### 5. **결과 이미지 반환**

- **의사결정**: 리사이징된 이미지를 `BufferedImage`로 반환.
- **이유**: 리사이징된 이미지를 직접 반환하여 후속 작업에서 활용할 수 있게 해줍니다. `BufferedImage`는 파일로 저장하거나 다른 작업에 사용할 수 있는 형식이기 때문에 리사이징 후 후속 처리에 유용합니다.
</details>

<details>
  <summary> QueryDSL을 활용한 페이징 처리</summary>


### QueryDSL의 장점

1. **타입 세이프티**: QueryDSL은 정적 타입 기반으로 동작하므로 컴파일 타임에 오류를 잡을 수 있습니다. 쿼리 실행 전에 오류를 사전에 방지할 수 있습니다.
2. **가독성**: JPQL 문자열 기반 쿼리보다 가독성이 뛰어나며 유지보수가 쉽습니다.
3. **동적 쿼리 작성**: 조건부로 쿼리를 생성할 때 BooleanBuilder 또는 `where` 메서드를 활용해 복잡한 동적 쿼리도 간결하게 작성할 수 있습니다.
4. **재사용성**: 자주 사용되는 조건이나 정렬 규칙을 메서드로 만들어 재사용할 수 있습니다.
5. **통합성**: QueryDSL은 JPA뿐만 아니라 SQL, MongoDB 등 다양한 데이터베이스와의 통합도 지원합니다.

---

### 1. **`CustomPTRepositoryImpl` (PT 관련 페이징 처리)**

```java
java
코드 복사
@RequiredArgsConstructor
public class CustomPTRepositoryImpl implements CustomPTRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PTsReadRes> searchPTs(Pageable pageable, String keyword, PTSpecialization category) {
        // 동적 조건 생성
        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.trim().isEmpty()) {
            builder.and(QPT.pT.title.containsIgnoreCase(keyword)
                       .or(QPT.pT.description.containsIgnoreCase(keyword)));
        }
        if (category != null) {
            builder.and(QPT.pT.specialization.eq(category));
        }

        // 쿼리 작성 및 페이징 처리
        JPAQuery<PT> query = jpaQueryFactory
                .selectFrom(QPT.pT)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getSortOrder(pageable.getSort()));

        // 결과 매핑
        List<PTsReadRes> resultList = query.fetch().stream()
                .map(pt -> new PTsReadRes(
                        pt.getId(),
                        pt.getTitle(),
                        pt.getSpecialization() != null ? pt.getSpecialization().getName() : null,
                        pt.getScheduledDate(),
                        pt.getPrice(),
                        pt.getStatus().getDescription()
                ))
                .collect(Collectors.toList());

        // 결과 반환
        return new PageImpl<>(resultList, pageable, query.fetchCount());
    }

    private com.querydsl.core.types.OrderSpecifier<?> getSortOrder(Sort sort) {
        if (sort.isUnsorted()) {
            return QPT.pT.createdAt.asc(); // 기본 정렬
        }
        Sort.Order order = sort.iterator().next(); // 첫 번째 정렬 조건
        return switch (order.getProperty()) {
            case "title" -> order.isAscending() ? QPT.pT.title.asc() : QPT.pT.title.desc();
            case "createdAt" -> order.isAscending() ? QPT.pT.createdAt.asc() : QPT.pT.createdAt.desc();
            default -> order.isAscending() ? QPT.pT.id.asc() : QPT.pT.id.desc();
        };
    }
}

```

---

### 2. **`CustomBoardRepositoryImpl` (Board 관련 페이징 처리)**

```java
java
코드 복사
@Repository
@RequiredArgsConstructor
public class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardsReadRes> searchBoards(Pageable pageable, String keyword) {
        // 기본 쿼리
        JPAQuery<Board> query = jpaQueryFactory
                .selectFrom(QBoard.board)
                .where(QBoard.board.title.containsIgnoreCase(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        // 동적 정렬 추가
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                query.orderBy(new OrderSpecifier<>(
                        order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        new PathBuilder<>(Board.class, QBoard.board.getMetadata())
                                .get(order.getProperty(), Comparable.class)
                ));
            }
        }

        // 결과 조회 및 매핑
        List<BoardsReadRes> results = query
                .select(Projections.constructor(
                        BoardsReadRes.class,
                        QBoard.board.id,
                        QBoard.board.title,
                        QBoard.board.likeCount
                ))
                .fetch();

        // 결과 반환
        return PageableExecutionUtils.getPage(results, pageable, query::fetchCount);
    }
}

```

---

### **설명**

1. **CustomPTRepositoryImpl**:
    - **특화된 동적 조건**: PT의 제목(`title`)과 설명(`description`)에서 키워드로 검색하며, 카테고리(`specialization`) 필터를 동적으로 적용합니다.
    - **결과 매핑**: `PTsReadRes`로 변환하며, DTO에 필요한 정보만 추출합니다.
2. **CustomBoardRepositoryImpl**:
    - **기본 조건**: 제목(`title`)을 키워드로 검색합니다.
    - **동적 정렬**: `PathBuilder`를 사용하여 다양한 정렬 기준을 처리하며, 동적으로 `OrderSpecifier`를 추가합니다.
    - **결과 매핑**: `BoardsReadRes`로 변환하며, 게시판 ID, 제목, 좋아요 수 등을 제공합니다.

---

### QueryDSL 활용 시 이점

1. **페이징 처리 간소화**: `Pageable`을 사용하여 페이징 로직을 쉽게 통합할 수 있습니다.
2. **DTO 변환 간결화**: QueryDSL의 `Projections`를 사용하면 DTO 매핑이 간단합니다.
3. **동적 쿼리의 유연성**: 키워드, 정렬, 필터링 조건 등을 코드로 유연하게 제어할 수 있습니다.
4. **확장성**: 새로운 조건이나 정렬 규칙 추가가 쉽습니다.
</details>

<details>
  <summary> **실시간 알림 기능 구현**SSE(Server-Sent Events)를 사용</summary>


### **실시간 알림 기능 구현**SSE(Server-Sent Events)를 사용하여 서버에서 클라이언트로 실시간 메시지를 전달.

### **2. 주요 구성요소**

1. **`NotificationController`**
    - 클라이언트의 알림 구독 요청 처리 (`/api/v2/notifications/subscribe`).
    - 사용자별 SSE 연결(`SseEmitter`)을 관리하는 `sseEmitters` 맵에 연결 저장.
2. **`NotificationService`**
    - 새로운 SSE 연결 생성 및 관리.
    - 특정 사용자에게 메시지 전송.

---

### **3. 주요 흐름**

1. **구독 요청 처리**
    - 클라이언트가 `/subscribe` 엔드포인트로 GET 요청.
    - 서버는 `SseEmitter` 생성 후 연결을 맵에 저장하고 반환.
2. **알림 메시지 전송**
    - 특정 사용자 ID로 연결된 `SseEmitter`를 검색.
    - 연결된 클라이언트에 메시지 전달.
3. **연결 종료 관리**
    - 연결 완료, 타임아웃, 오류 발생 시 연결 삭제.

---

### **4. 코드 요약**

**컨트롤러**

```java
java
코드 복사
@GetMapping("/api/v2/notifications/subscribe")
public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    Long userId = userDetails.getUser().getId();
    return notificationService.subscribe(userId);
}

```

**서비스**

```java
java
코드 복사
public SseEmitter subscribe(Long userId) {
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    try {
        sseEmitter.send(SseEmitter.event().name("connect"));
    } catch (IOException e) {
        e.printStackTrace();
    }

    NotificationController.sseEmitters.put(userId, sseEmitter);

    sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));
    sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));
    sseEmitter.onError(e -> NotificationController.sseEmitters.remove(userId));

    return sseEmitter;
}

public void notifyMessage(Long receiverId) {
    if (NotificationController.sseEmitters.containsKey(receiverId)) {
        try {
            SseEmitter emitter = NotificationController.sseEmitters.get(receiverId);
            emitter.send(SseEmitter.event().name("createChatRoom").data("채팅 요청이 왔습니다."));
        } catch (Exception e) {
            NotificationController.sseEmitters.remove(receiverId);
        }
    }
}

```

---

### **5. 장점**

- 간단한 방식으로 서버에서 클라이언트로 실시간 알림 구현 가능.
- 사용자별 연결 관리.
</details>

<details>
  <summary>N+1 문제 해결(게시글 댓글 N+1 )</summary>

### 1. **N+1 문제란?**

N+1 문제는 JPA 또는 ORM(객체-관계 매핑)을 사용할 때 자주 발생하는 성능 문제입니다.

기본적으로 부모 엔티티를 조회한 후, 각 부모 엔티티에 연관된 자식 엔티티를 개별적으로 조회할 때 발생합니다.

예를 들어, 게시글(Board)와 댓글(Comment)의 관계에서 게시글 1개를 가져오는 데 필요한 1번의 쿼리와, 각 게시글의 댓글을 가져오기 위해 추가적으로 발생하는 N번의 쿼리로 인해 총 N+1번의 쿼리가 실행됩니다.

---

### 2. **코드에서 N+1 문제를 해결한 방법**

### 주요 해결 방법:

- *`JOIN FETCH`*를 사용하여 연관된 엔티티(댓글 리스트)를 한 번의 쿼리로 가져옵니다.

```java
java
코드 복사
@Query("SELECT b FROM Board b LEFT JOIN FETCH b.commentList WHERE b.id = :boardId")
Board findBoardByIdWithThrow(Long boardId);

```

- **`LEFT JOIN FETCH`**:
    - `Board` 엔티티를 조회할 때 `commentList`를 조인하여 즉시 로딩(Eager Loading)으로 가져옵니다.
    - 따라서 별도의 추가 쿼리 없이 `Board`와 연관된 댓글 데이터를 한 번의 쿼리로 가져올 수 있습니다.

---

### 3. **서비스 레이어에서 데이터 변환 및 반환**

서비스 코드에서는 조회된 데이터(엔티티)를 DTO(Data Transfer Object)로 변환하여 클라이언트에 전달합니다.

```java
java
코드 복사
@Transactional(readOnly = true)
public BoardReadRes getBoard(Long boardId) {
    // JOIN FETCH로 N+1 문제를 해결하며 Board 엔티티를 조회
    Board board = boardRepository.findBoardByIdWithThrow(boardId);

    // 조회된 Board 엔티티의 댓글 리스트를 CommentReadRes DTO로 변환
    List<CommentReadRes> commentReadResList = new ArrayList<>();
    for (Comment content : board.getCommentList()) {
        commentReadResList.add(new CommentReadRes(content));  // DTO 변환
    }

    // BoardReadRes DTO 생성 및 반환
    return new BoardReadRes(
            board.getId(),
            board.getLikeCount(),
            board.getTitle(),
            board.getContent(),
            board.getUser().getId(),
            commentReadResList
    );
}

```

### 주요 포인트:

1. **`findBoardByIdWithThrow`**: 한 번의 쿼리로 게시글과 댓글 리스트를 함께 조회.
2. **DTO 변환**: 클라이언트에 불필요한 정보를 노출하지 않도록 `CommentReadRes`와 `BoardReadRes`로 데이터를 변환.

---

### 5. **결과적으로 얻은 이점**

- **성능 최적화**: 데이터베이스에 대한 쿼리 요청 횟수를 줄여 성능 개선.
- **데이터 일관성**: 연관된 데이터를 한 번에 가져와 트랜잭션 내에서 일관된 데이터 보장.
- **코드 가독성**: 명확하고 효율적인 데이터 접근 방식을 사용.
</details>

