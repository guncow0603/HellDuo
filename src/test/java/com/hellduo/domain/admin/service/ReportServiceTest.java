package com.hellduo.domain.admin.service;

import com.hellduo.domain.admin.dto.request.UserReportReq;
import com.hellduo.domain.admin.dto.response.GetReportListRes;
import com.hellduo.domain.admin.dto.response.UserReportRes;
import com.hellduo.domain.admin.entity.Report;
import com.hellduo.domain.admin.entity.enums.ReportReason;
import com.hellduo.domain.admin.repository.ReportRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser(Long id, String email, UserRoleType role) {
        User user = User.builder()
                .name("Test User")
                .email(email)
                .role(role)
                .userStatus(UserStatus.ACTION)
                .gender(Gender.MAN)
                .age(30)
                .phoneNumber("010-1234-5678")
                .nickname("TestNickname")
                .weight(70.0)
                .height(175.0)
                .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    @Test
    void reportCreate_shouldCreateReportSuccessfully() {
        // Given
        User reporterUser = createUser(1L, "reporter@example.com", UserRoleType.USER);
        User reportedUser = createUser(2L, "reported@example.com", UserRoleType.USER);

        UserReportReq req = new UserReportReq(2L, ReportReason.FRAUD, "Offensive behavior");

        when(userRepository.findUserByIdWithThrow(req.reportedUserId())).thenReturn(reportedUser);

        // When
        UserReportRes response = reportService.reportCreate(reporterUser, req);

        // Then
        assertEquals("신고가 완료되었습니다.", response.msg());
        verify(reportRepository, times(1)).save(any(Report.class));
    }

    @Test
    void getReportList_shouldReturnReportsForAdmin() {
        // Given
        User adminUser = createUser(1L, "admin@example.com", UserRoleType.ADMIN);
        User reporterUser = createUser(2L, "reporter@example.com", UserRoleType.USER);
        User reportedUser = createUser(3L, "reported@example.com", UserRoleType.USER);

        Report report = Report.builder()
                .reportedUser(reportedUser)
                .reporterUser(reporterUser)
                .reportReason(ReportReason.ABUSE)
                .content("Harassment")
                .build();

        when(reportRepository.findAll()).thenReturn(List.of(report));

        // When
        List<GetReportListRes> reportList = reportService.getReportList(adminUser);

        // Then
        assertEquals(1, reportList.size());
        GetReportListRes reportRes = reportList.get(0);
        assertEquals("reporter@example.com", reportRes.reporterEmail());
        assertEquals("reported@example.com", reportRes.reportedEmail());
        assertEquals("Harassment", reportRes.content());
        verify(reportRepository, times(1)).findAll();
    }

    @Test
    void getReportList_shouldThrowExceptionForNonAdminUser() {
        // Given
        User nonAdminUser = createUser(1L, "user@example.com", UserRoleType.USER);

        // When & Then
        assertThrows(RuntimeException.class, () -> reportService.getReportList(nonAdminUser));
    }
}
