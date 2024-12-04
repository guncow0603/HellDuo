-- 더미 데이터 삽입
INSERT INTO TB_USER (email, password, gender, age, phone_number, nickname, weight, height, role, name, specialization, experience, certifications, bio, deleted, point)
VALUES
-- 일반 유저
('user1@example.com', 'password1', 'MAN', 25, '010-1111-2221', 'nickname1', 70.5, 175.0, 'USER', '김유저', NULL, NULL, NULL, '안녕하세요! 김유저입니다.', FALSE, 100),
('user2@example.com', 'password2', 'WOMAN', 30, '010-1111-2222', 'nickname2', 55.0, 165.0, 'USER', '이유저', NULL, NULL, NULL, '반갑습니다! 이유저입니다.', FALSE, 200),
('user3@example.com', 'password3', 'MAN', 28, '010-1111-2223', 'nickname3', 68.0, 178.0, 'USER', '박유저', NULL, NULL, NULL, '열심히 운동 중입니다.', FALSE, 150),
('user4@example.com', 'password4', 'WOMAN', 24, '010-1111-2224', 'nickname4', 50.0, 160.0, 'USER', '최유저', NULL, NULL, NULL, '건강해지고 싶어요.', FALSE, 120),
('user5@example.com', 'password5', 'MAN', 35, '010-1111-2225', 'nickname5', 80.0, 180.0, 'USER', '정유저', NULL, NULL, NULL, '운동 초보입니다.', FALSE, 80),

-- 트레이너
('trainer1@example.com', 'password6', 'MAN', 40, '010-2222-3331', 'trainer1', 85.0, 185.0, 'TRAINER', '김트레이너', 'FITNESS', 10, 'PT 자격증', '경력 10년의 트레이너입니다.', FALSE, 300),
('trainer2@example.com', 'password7', 'WOMAN', 32, '010-2222-3332', 'trainer2', 58.0, 162.0, 'TRAINER', '이트레이너', 'YOGA', 8, '요가 자격증', '요가 전문 트레이너입니다.', FALSE, 250),
('trainer3@example.com', 'password8', 'MAN', 29, '010-2222-3333', 'trainer3', 78.0, 177.0, 'TRAINER', '박트레이너', 'PILATES', 5, '필라테스 자격증', '필라테스 강사입니다.', FALSE, 200),
('trainer4@example.com', 'password9', 'WOMAN', 38, '010-2222-3334', 'trainer4', 60.0, 170.0, 'TRAINER', '최트레이너', 'NUTRITION', 12, '영양사 자격증', '영양학 전문가입니다.', FALSE, 400),
('trainer5@example.com', 'password10', 'MAN', 45, '010-2222-3335', 'trainer5', 90.0, 190.0, 'TRAINER', '정트레이너', 'CROSSFIT', 15, '크로스핏 자격증', '크로스핏 지도자입니다.', FALSE, 500);