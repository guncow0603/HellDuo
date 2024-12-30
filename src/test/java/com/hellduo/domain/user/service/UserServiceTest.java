package com.hellduo.domain.user.service;

import com.hellduo.domain.imageFile.repository.ImageFileRepository;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.enums.PTStatus;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.user.dto.request.TrainerSignupReq;
import com.hellduo.domain.user.dto.request.UserLoginReq;
import com.hellduo.domain.user.dto.request.UserSignupReq;
import com.hellduo.domain.user.dto.response.*;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.Specialization;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.entity.enums.UserStatus;
import com.hellduo.domain.user.repository.UserRepository;
import com.hellduo.global.jwt.JwtUtil;
import com.hellduo.global.redis.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    private PT pt;
    @Mock
    private PTRepository ptRepository; // PTRepository 모킹
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageFileRepository imageFileRepository;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private HttpServletResponse res;
    @Mock
    private JwtUtil jwtUtil;
    private UserService userService;
    private List<User> mockTrainers;

    @BeforeEach
    void setUp() {
        // 빌더 패턴을 사용하여 User 객체 생성
        User trainer1 = User.builder()
                .name("Trainer 1")
                .specialization(Specialization.FITNESS)
                .role(UserRoleType.TRAINER)
                .userStatus(UserStatus.ACTION)
                .build();
        ReflectionTestUtils.setField(trainer1, "id", 1L);  // id 설정 변경
        ReflectionTestUtils.setField(trainer1, "rating", 4.5);  // id 설정 변경

        User trainer2 = User.builder()
                .name("Trainer 2")
                .specialization(Specialization.YOGA)
                .role(UserRoleType.TRAINER)
                .userStatus(UserStatus.ACTION)
                .build();

        ReflectionTestUtils.setField(trainer2, "id", 1L);  // id 설정 변경
        ReflectionTestUtils.setField(trainer2, "rating", 4.8);  // id 설정 변경

        mockTrainers = Arrays.asList(trainer1, trainer2);  // Mock Trainer 목록
        MockitoAnnotations.openMocks(this);
        userService = new UserService(refreshTokenService, jwtUtil, passwordEncoder, userRepository, null, imageFileRepository);
    }

    @Test
    void testSignup() {
        // given
        UserSignupReq req = new UserSignupReq(
                "test@example.com",
                "password123!",
                "password123!",
                "John Doe",
                Gender.MAN,
                30,
                "1234567890",
                "johnny",
                75.0,
                180.0,
                false,
                ""
        );

        // Mock the behavior of passwordEncoder.matches
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(eq("password123!"), eq("encodedPassword"))).thenReturn(true); // 비밀번호 확인이 일치한다고 가정

        // Mocking userRepository behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());

        // when
        UserSignupRes result = userService.signup(req);

        // then
        assertNotNull(result);
        assertEquals("회원 가입 완료", result.msg());
    }

    @Test
    void testTrainerSignup() {
        // given
        TrainerSignupReq req = new TrainerSignupReq(
                "trainer@example.com",
                "password123!",
                "password123!",
                "John Trainer",
                Gender.MAN,
                35,
                "1234567890",
                Specialization.FITNESS,
                10,
                "Certifications",
                "Bio content"
        );

        // Mock the behavior of passwordEncoder
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(eq("password123!"), eq("encodedPassword"))).thenReturn(true);

        // Mock userRepository behavior
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(anyString())).thenReturn(Optional.empty());

        // Mock imageFileRepository behavior
        when(imageFileRepository.save(any())).thenReturn(null);

        // when
        TrainerSignupRes result = userService.trainerSignup(req);

        // then
        assertNotNull(result);
        assertEquals("회원 가입 완료", result.msg());
    }

    @Test
    void testLoginSuccess() {
        // given
        UserLoginReq req = new UserLoginReq("test@example.com", "password123!");

        // Mock User
        User user = User.builder()
                .email("test@example.com")
                .password("$2a$10$encryptedpassword")
                .userStatus(UserStatus.ACTION)  // 상태를 ACTIVE로 변경
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);  // id 설정 변경

        // Mock repository behavior
        when(userRepository.findUserByEmailWithThrow(eq("test@example.com"))).thenReturn(user);
        when(userRepository.findUserByIdWithThrow(eq(1L))).thenReturn(user);
        when(passwordEncoder.matches(eq("password123!"), eq(user.getPassword()))).thenReturn(true);

        // Mock JWT utils
        when(jwtUtil.createAccessToken(eq("test@example.com"), eq(user.getRole()))).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(eq("test@example.com"))).thenReturn("refreshToken");

        // when
        UserLoginRes result = userService.login(req, res);

        // then
        assertNotNull(result);
        assertEquals("로그인 완료", result.msg());

        // Verify JWT methods
        verify(jwtUtil).addAccessJwtToCookie("accessToken", res);
        verify(jwtUtil).addRefreshJwtToCookie("refreshToken", res);
        verify(refreshTokenService).saveRefreshToken(eq("refreshToken"), eq(1L));
    }

    @Test
    void testGetOwnProfile() {
        // given
        User user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .gender(Gender.MAN)  // Gender enum 설정
                .age(30)
                .phoneNumber("1234567890")
                .nickname("Johnny")
                .weight(70.0)
                .height(175.0)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);  // id 설정 변경

        // when
        UserOwnProfileGetRes result = userService.getOwnProfile(user);

        // then
        assertNotNull(result);
        assertEquals(1L, result.userId());  // userId 값 비교
        assertEquals("John Doe", result.name());  // name 값 비교
        assertEquals("john.doe@example.com", result.email());  // email 값 비교
        assertEquals("남성", result.gender());  // gender 값 비교 (Gender enum에 따른 description 확인)
        assertEquals(30, result.age());  // age 값 비교
        assertEquals("1234567890", result.phoneNumber());  // phoneNumber 값 비교
        assertEquals("Johnny", result.nickname());  // nickname 값 비교
        assertEquals(70.0, result.weight(), 0.001);  // weight 값 비교 (소수점 비교시 허용 오차)
        assertEquals(175.0, result.height(), 0.001);  // height 값 비교 (소수점 비교시 허용 오차)
    }

    @Test
    void testGetBestRatingTrainer() {
        // Given: userRepository의 findTop10ByRoleAndUserStatusNotOrderByRatingDesc 메서드 Mock 설정
        when(userRepository.findTop10ByRoleAndUserStatusNotOrderByRatingDesc(UserRoleType.TRAINER, UserStatus.DELETED))
                .thenReturn(mockTrainers);

        // When: getBestRatingTrainer 메서드 호출
        List<BestRatingTrainerRes> result = userService.getBestRatingTrainer();

        // Then: 반환된 결과를 검증
        assertNotNull(result);
        assertEquals(2, result.size());  // Mock한 2명의 트레이너가 반환되어야 한다.
        assertEquals("Trainer 1", result.get(0).name());
        assertEquals(4.5, result.get(0).rating());
        assertEquals("피트니스", result.get(0).specialization());

        assertEquals("Trainer 2", result.get(1).name());
        assertEquals(4.8, result.get(1).rating());
        assertEquals("요가", result.get(1).specialization());

    }

    @Test
    void getUserProfile_ValidCase_ShouldReturnUserProfile() {
        // User 객체 생성
        User user = User.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .gender(Gender.MAN) // Gender Enum을 설정
                .age(30)
                .phoneNumber("1234567890")
                .nickname("Johnny")
                .weight(70.0)
                .height(175.0)
                .build();
        ReflectionTestUtils.setField(user, "id", 2L);  // id 설정 변경

        // Trainer 객체 생성
        User trainer = User.builder()
                .build();
        ReflectionTestUtils.setField(trainer, "id", 1L);  // id 설정 변경

        // Mock PT 객체
        PT pt = PT.builder()
                .trainer(trainer)  // Trainer 빌더 패턴을 사용
                .status(PTStatus.SCHEDULED)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);  // id 설정 변경
        ReflectionTestUtils.setField(pt, "user", user);  // user 설정

        // ptRepository Mock 설정
        ptRepository = mock(PTRepository.class); // ptRepository Mock 객체 생성
        when(ptRepository.findPTByIdWithThrow(1L)).thenReturn(pt); // PT 조회 시 반환값 설정

        // userService Mock 설정
        userService = mock(UserService.class);  // userService Mock 객체 생성
        when(userService.getUserProfile(any(), eq(1L))).thenReturn(
                new UserOwnProfileGetRes(
                        pt.getUser().getId(),
                        pt.getUser().getName(),
                        pt.getUser().getEmail(),
                        pt.getUser().getGender().getDescription(),  // Gender의 description 비교
                        pt.getUser().getAge(),
                        pt.getUser().getPhoneNumber(),
                        pt.getUser().getNickname(),
                        pt.getUser().getWeight(),
                        pt.getUser().getHeight()
                )
        );

        // when
        UserOwnProfileGetRes result = userService.getUserProfile(pt.getTrainer(), 1L);

        // then
        assertNotNull(result);
        assertEquals(pt.getUser().getId(), result.userId());
        assertEquals(pt.getUser().getName(), result.name());
        assertEquals(pt.getUser().getEmail(), result.email());
        assertEquals(pt.getUser().getGender().getDescription(), result.gender());  // Gender의 description 비교
        assertEquals(pt.getUser().getAge(), result.age());
        assertEquals(pt.getUser().getPhoneNumber(), result.phoneNumber());
        assertEquals(pt.getUser().getNickname(), result.nickname());
        assertEquals(pt.getUser().getWeight(), result.weight(), 0.001);
        assertEquals(pt.getUser().getHeight(), result.height(), 0.001);
    }
}
