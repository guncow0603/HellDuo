package com.hellduo.domain.pt.service;

import com.hellduo.domain.imageFile.service.ImageFileService;
import com.hellduo.domain.pt.dto.request.PTCreateReq;
import com.hellduo.domain.pt.dto.request.PTUpdateReq;
import com.hellduo.domain.pt.dto.response.PTCreateRes;
import com.hellduo.domain.pt.dto.response.PTUpdateRes;
import com.hellduo.domain.pt.dto.response.PTDeleteRes;
import com.hellduo.domain.pt.dto.response.PTReservRes;
import com.hellduo.domain.pt.dto.response.PTCompletedRes;
import com.hellduo.domain.pt.entity.PT;
import com.hellduo.domain.pt.entity.enums.PTSpecialization;
import com.hellduo.domain.pt.entity.enums.PTStatus;
import com.hellduo.domain.pt.repository.PTRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PTServiceTest {

    @Mock
    private PTRepository ptRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private ImageFileService imageFileService; // imageFileService mock 추가

    @InjectMocks
    private PTService ptService;

    private User trainer;
    private PTCreateReq ptCreateReq;

    @BeforeEach
    void setUp() {
        // 트레이너 사용자 설정 (빌더 패턴 사용)
        trainer = User.builder()
                .name("트레이너")
                .role(UserRoleType.TRAINER)
                .build();
        ReflectionTestUtils.setField(trainer, "id", 1L);  // id 설정

        // PT 생성 요청 설정 (record에 맞는 값 설정)
        ptCreateReq = new PTCreateReq(
                "PT 제목",  // 제목
                "설명",  // 설명
                LocalDateTime.parse("2024-12-27T12:00:00"),  // 예약 시간
                10000L,  // 가격
                PTSpecialization.CROSSFIT,  // 전문 분야
                37.5665,  // 위도
                126.9780,  // 경도
                "서울" ); // 주소
    }

    @Test
    void testPtCreate() {
        // given
        PT pt = PT.builder()
                .trainer(trainer)
                .status(PTStatus.UNRESERVED)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);  // id 설정
        when(ptRepository.save(any(PT.class))).thenReturn(pt);  // PT가 저장될 때 반환 값 설정

        // when
        PTCreateRes response = ptService.ptCreate(ptCreateReq, trainer);

        // then
        assertNotNull(response);
        assertEquals("PT가 생성 되었습니다.", response.msg());
        verify(ptRepository, times(1)).save(any(PT.class));
    }

    @Test
    void testPtUpdate() {
        // given
        PT pt = PT.builder()
                .trainer(trainer)
                .status(PTStatus.UNRESERVED)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);  // id 설정

        PTUpdateReq ptUpdateReq = new PTUpdateReq(
                "수정된 PT 제목",  // 제목
                "수정된 설명",  // 내용
                LocalDateTime.parse("2024-12-28T14:00:00"),  // 예약 시간
                15000L,  // 가격
                PTSpecialization.YOGA,  // 전문 분야
                37.5665,  // 위도
                126.9780,  // 경도
                "서울"  // 주소
        );

        when(ptRepository.findPTByIdWithThrow(1L)).thenReturn(pt);

        // when
        PTUpdateRes response = ptService.ptUpdate(1L, ptUpdateReq, trainer);

        // then
        assertNotNull(response);
        assertEquals("수정 완료", response.msg());
        verify(ptRepository, times(1)).findPTByIdWithThrow(1L);
    }

    @Test
    void testPtDelete() {
        // given
        PT pt = PT.builder()
                .trainer(trainer)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);  // id 설정

        // imageFileService의 deleteImages 메서드가 호출될 때 아무 동작도 하지 않도록 설정
        doNothing().when(imageFileService).deleteImages(anyLong(), anyString(), any(User.class));

        when(ptRepository.findPTByIdWithThrow(1L)).thenReturn(pt);

        // when
        PTDeleteRes response = ptService.ptDelete(1L, trainer);

        // then
        assertNotNull(response);
        assertEquals("삭제 완료", response.msg());
        verify(ptRepository, times(1)).delete(pt);
        verify(imageFileService, times(1)).deleteImages(1L, "pt", trainer); // deleteImages 메서드가 호출됐는지 확인
    }

    @Test
    void testPtReserv() {
        // given
        PT pt = PT.builder()
                .status(PTStatus.UNRESERVED)
                .price(10000L)
                .trainer(trainer)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);

        User user = User.builder()
                .role(UserRoleType.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user, "point", 20000L);

        when(ptRepository.findPTByIdWithThrow(1L)).thenReturn(pt);

        // Mocking RedissonClient to return a mock RLock object
        RLock mockLock = mock(RLock.class);
        when(redissonClient.getLock("ptReservLock:" + 1L)).thenReturn(mockLock);

        // when
        PTReservRes response = ptService.ptReserv(1L, user);

        // then
        assertNotNull(response);
        assertEquals("예약 완료 되었습니다.", response.msg());
        verify(ptRepository, times(1)).findPTByIdWithThrow(1L);
        verify(redissonClient, times(1)).getLock("ptReservLock:" + 1L);
        verify(mockLock, times(1)).unlock();  // Ensure unlock is called
    }

    @Test
    void testPtCompleted() {
        // given
        PT pt = PT.builder()
                .trainer(trainer)
                .status(PTStatus.SCHEDULED)
                .build();
        ReflectionTestUtils.setField(pt, "id", 1L);  // id 설정

        when(ptRepository.findPTByIdWithThrow(1L)).thenReturn(pt);

        // when
        PTCompletedRes response = ptService.ptCompleted(1L, trainer);

        // then
        assertNotNull(response);
        assertEquals("완료 처리 하였습니다.", response.smg());
        assertEquals(PTStatus.COMPLETED, pt.getStatus());
        verify(ptRepository, times(1)).findPTByIdWithThrow(1L);
    }
}
