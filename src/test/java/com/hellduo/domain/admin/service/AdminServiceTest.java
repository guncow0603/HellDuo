package com.hellduo.domain.admin.service;

import com.hellduo.domain.admin.dto.request.NoticeReq;
import com.hellduo.domain.admin.dto.request.NoticeUpdateReq;
import com.hellduo.domain.admin.dto.response.*;
import com.hellduo.domain.admin.entity.Notice;
import com.hellduo.domain.admin.repository.NoticeRepository;
import com.hellduo.domain.user.entity.User;
import com.hellduo.domain.user.entity.enums.Gender;
import com.hellduo.domain.user.entity.enums.UserRoleType;
import com.hellduo.domain.user.entity.enums.UserStatus;
import com.hellduo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNotice_adminUser_createsNotice() {
        // given
        User admin = User.builder().role(UserRoleType.ADMIN).build();

        NoticeReq noticeReq = mock(NoticeReq.class);
        when(noticeReq.title()).thenReturn("Test Title");
        when(noticeReq.content()).thenReturn("Test Content");

        Notice savedNotice = Notice.builder().title("Test Title").content("Test Content").user(admin).build();

        when(noticeRepository.save(any(Notice.class))).thenReturn(savedNotice);

        // when
        NoticeRes response = adminService.createNotice(noticeReq, admin);

        // then
        assertEquals("공지사항이 생성되었습니다.", response.msg());
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }


    @Test
    void getNoticeList_returnsNoticeList() {
        // given
        Notice notice1 = Notice.builder().title("Notice 1").build();
        ReflectionTestUtils.setField(notice1, "id", 1L);

        Notice notice2 = Notice.builder().title("Notice 2").build();
        ReflectionTestUtils.setField(notice2, "id", 2L);

        List<Notice> noticeList = List.of(notice1, notice2);
        when(noticeRepository.findAll()).thenReturn(noticeList);

        // when
        List<GetNoticeListRes> response = adminService.getNoticeList();

        // then
        assertEquals(2, response.size());
        assertEquals("Notice 1", response.get(0).title());
        assertEquals("Notice 2", response.get(1).title());
    }

    @Test
    void getNotice_validId_returnsNotice() {
        // given
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Test Content")
                .user(user)
                .build();
        ReflectionTestUtils.setField(notice, "id", 1L);

        when(noticeRepository.findNoticeByIdWithThrow(1L)).thenReturn(notice);

        // when
        GetNoticeRes response = adminService.getNotice(1L);

        // then
        assertEquals("Test Notice", response.title());
        assertEquals("Test Content", response.content());
        assertEquals(Long.valueOf(1L), response.userId());
        assertEquals(Long.valueOf(1L), response.noticeId()); // Notice의 ID 값도 검증
    }

    @Test
    void updateNotice_adminUser_updatesNotice() {
        // given
        User admin = User.builder().role(UserRoleType.ADMIN).build();

        Notice notice = Notice.builder().title("Old Title").content("Old Content").build();
        ReflectionTestUtils.setField(notice, "id", 1L);

        NoticeUpdateReq updateReq = mock(NoticeUpdateReq.class);
        when(updateReq.title()).thenReturn("New Title");
        when(updateReq.content()).thenReturn("New Content");

        when(noticeRepository.findNoticeByIdWithThrow(1L)).thenReturn(notice);

        // when
        UpdateNoticeRes response = adminService.updateNotice(admin, 1L, updateReq);

        // then
        assertEquals("공지사항 수정완료", response.msg());
        assertEquals("New Title", notice.getTitle());
        assertEquals("New Content", notice.getContent());
    }

    @Test
    void deleteNotice_adminUser_deletesNotice() {
        // given
        User admin = User.builder().role(UserRoleType.ADMIN).build();

        doNothing().when(noticeRepository).deleteById(1L);

        // when
        DeleteNoticeRes response = adminService.deleteNotice(admin, 1L);

        // then
        assertEquals("공지사항이 삭제되었습니다.", response.msg());
        verify(noticeRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUserList_adminUser_returnsUserList() {
        // given
        User admin = User.builder().role(UserRoleType.ADMIN).build();

        List<User> users = new ArrayList<>();
        User user = User.builder()
                .name("John Doe")
                .email("john@example.com")
                .role(UserRoleType.USER)
                .userStatus(UserStatus.ACTION)
                .gender(Gender.MAN) // Gender 설정 추가
                .age(30)             // 나이 설정
                .phoneNumber("010-1234-5678") // 전화번호 설정
                .nickname("Johnny")          // 닉네임 설정
                .weight(70.0)                   // 체중 설정
                .height(175.0)                  // 키 설정
                .build();
        users.add(user);

        ReflectionTestUtils.setField(user, "id", 1L);

        when(userRepository.findAll()).thenReturn(users);

        // when
        List<GetUserListRes> response = adminService.getUserList(admin);

        // then
        assertEquals(1, response.size());
        assertEquals("John Doe", response.get(0).name());
        assertEquals("john@example.com", response.get(0).email());
        assertEquals("남성", response.get(0).gender()); // MALE의 Description이 "남성"이라 가정
        assertEquals(30, (int) response.get(0).age()); // Integer에서 int로 변환
        assertEquals("010-1234-5678", response.get(0).phoneNumber());
        assertEquals("Johnny", response.get(0).nickname());
        assertEquals(70.0, response.get(0).weight());
        assertEquals(175.0, response.get(0).height());
        assertEquals(UserStatus.ACTION, response.get(0).userStatus());
    }




}