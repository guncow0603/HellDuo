INSERT INTO TB_USER (
    email, password, gender, age, phone_number, nickname, weight, height, role, name, specialization, experience, certifications, bio, point, rating, created_at, modified_at, user_status
) VALUES
      ('admin@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 32, '010-1111-1112', '관리자', 80.0, 185.0, 'ADMIN', '김철수', 'PILATES', 5, 'PT Level 1', '피트니스 전문가입니다.', 300, 4.8, NOW(), NOW(), 'ACTION'),
      ('user@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 32, '010-1111-1111', '유저1', 80.0, 185.0, 'USER', '김철수', 'PILATES', 5, 'PT Level 1', '피트니스 전문가입니다.', 300, 4.8, NOW(), NOW(), 'ACTION'),
      ('user3@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 35, '010-3333-3333', '유저3', 90.0, 190.0, 'USER', '박성민', 'CROSSFIT', 6, 'Crossfit Level 1', '크로스핏 전문가입니다.', 350, 4.9, NOW(), NOW(), 'ACTION'),
      ('trainer@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'WOMAN', 30, '010-4444-4444', '최은정', 58.0, 162.0, 'TRAINER', '최은정', 'PILATES', 5, '필라테스 Level 2', '필라테스 강사입니다.', 310, 4.6, NOW(), NOW(), 'ACTION'),
      ('trainer5@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 40, '010-5555-5555', '강호동', 85.0, 180.0, 'TRAINER', '강호동', 'REHABILITATION', 8, '재활운동 전문가', '재활 운동을 돕습니다.' , 400, 5.0, NOW(), NOW(), 'ACTION'),
      ('trainer6@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'WOMAN', 26, '010-6666-6666', '수지', 55.0, 160.0, 'TRAINER', '수지', 'NUTRITION', 3, '영양사 자격증', '영양 관리 전문입니다.' , 220, 4.5, NOW(), NOW(), 'ACTION'),
      ('trainer7@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 29, '010-7777-7777', '이승기', 70.0, 175.0, 'TRAINER', '이승기', 'FITNESS', 4, 'PT Level 2', '운동 목표를 달성하도록 돕습니다.' , 250, 4.7, NOW(), NOW(), 'ACTION'),
      ('trainer8@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'WOMAN', 34, '010-8888-8888', '김연아', 62.0, 168.0, 'TRAINER', '김연아', 'PILATES', 7, '필라테스 강사', '코어 및 유연성 강사입니다.' , 330, 4.8, NOW(), NOW(), 'ACTION'),
      ('trainer9@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 31, '010-9999-9999', '공유', 75.0, 178.0, 'TRAINER', '공유', 'CROSSFIT', 5, 'Crossfit Level 2', '고강도 크로스핏 훈련자입니다.' , 300, 4.6, NOW(), NOW(), 'ACTION'),
      ('trainer10@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'WOMAN', 27, '010-1010-1010', '송혜교', 57.0, 163.0, 'TRAINER', '송혜교', 'YOGA', 4, '명상 및 요가 강사', '요가와 명상 훈련을 제공합니다.' , 290, 4.5, NOW(), NOW(), 'ACTION'),
      ('trainer11@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 36, '010-1112-1112', '손흥민', 78.0, 182.0, 'TRAINER', '손흥민', 'REHABILITATION', 10, '재활 트레이너 자격증', '부상 후 회복 운동을 돕습니다.', 420, 5.0, NOW(), NOW(), 'ACTION'),
      ('trainer12@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'WOMAN', 33, '010-1212-1212', '한지민', 64.0, 170.0, 'TRAINER', '한지민', 'NUTRITION', 6, '영양 전문가', '식단 관리 및 영양 상담 전문가입니다.' , 310, 4.7, NOW(), NOW(), 'ACTION'),
      ('trainer13@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 28, '010-1313-1313', '박보검', 72.0, 177.0, 'TRAINER', '박보검', 'FITNESS', 3, '헬스 트레이너', '운동 입문자를 위한 강사입니다.' , 260, 4.5, NOW(), NOW(), 'ACTION'),
      ('trainer14@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'WOMAN', 30, '010-1414-1414', '신민아', 59.0, 164.0, 'TRAINER', '신민아', 'PILATES', 5, '필라테스 레벨 1', '기초부터 심화 과정까지 지도합니다.' , 280, 4.6, NOW(), NOW(), 'ACTION'),
      ('trainer15@email.com', '$2a$10$8vTpnPCx8BJn9I3Bei0st.ZT4lUbBxGwjctNY/lrEthUo8WC8yuPG', 'MAN', 39, '010-1515-1515', '마동석', 88.0, 188.0, 'TRAINER', '마동석', 'CROSSFIT', 9, 'Crossfit Expert', '최고의 강도로 훈련을 진행합니다.' , 400, 4.9, NOW(), NOW(), 'ACTION');


INSERT INTO tb_image_file (image_url, type, target_id) VALUES
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer1.png', 'PROFILE_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer10.png', 'PROFILE_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer11.png', 'PROFILE_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer12.png', 'PROFILE_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer13.png', 'PROFILE_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer14.png', 'PROFILE_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer15.png', 'PROFILE_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer2.png', 'PROFILE_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer3.png', 'PROFILE_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer4.png', 'PROFILE_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer5.png', 'PROFILE_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer6.png', 'PROFILE_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer7.png', 'PROFILE_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer8.png', 'PROFILE_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/profiles/trainer9.png', 'PROFILE_IMG', 15),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board1.png', 'BOARD_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board2.png', 'BOARD_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board3.png', 'BOARD_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board4.png', 'BOARD_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board5.png', 'BOARD_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board6.png', 'BOARD_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board7.png', 'BOARD_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board8.png', 'BOARD_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board9.png', 'BOARD_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board10.png', 'BOARD_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board11.png', 'BOARD_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board12.png', 'BOARD_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board13.png', 'BOARD_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board14.png', 'BOARD_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board15.png', 'BOARD_IMG', 15),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board16.png', 'BOARD_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board17.png', 'BOARD_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board18.png', 'BOARD_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board19.png', 'BOARD_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/boards/board20.png', 'BOARD_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png', 'PT_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png', 'PT_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt3.png', 'PT_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt4.png', 'PT_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt5.png', 'PT_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt6.png', 'PT_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt7.png', 'PT_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt8.png', 'PT_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt9.png', 'PT_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt10.png', 'PT_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt21.png', 'PT_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt22.png', 'PT_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt13.png', 'PT_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt14.png', 'PT_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt15.png', 'PT_IMG', 15),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt16.png', 'PT_IMG', 16),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt17.png', 'PT_IMG', 17),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt18.png', 'PT_IMG', 18),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt19.png', 'PT_IMG', 19),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt20.png', 'PT_IMG', 20),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png', 'PT_IMG', 21),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png', 'PT_IMG', 22),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png', 'PT_IMG', 23),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png', 'PT_IMG', 24),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt3.png', 'PT_IMG', 25),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt4.png', 'PT_IMG', 26),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt5.png', 'PT_IMG', 27),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt6.png', 'PT_IMG', 28),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt7.png', 'PT_IMG', 29),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt8.png', 'PT_IMG', 30),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt9.png', 'PT_IMG', 31),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt10.png', 'PT_IMG', 32),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt11.png', 'PT_IMG', 33),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt12.png', 'PT_IMG', 34),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt21.png', 'PT_IMG', 35),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt14.png', 'PT_IMG', 36),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt15.png', 'PT_IMG', 37),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt22.png', 'PT_IMG', 38),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt17.png', 'PT_IMG', 39),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt18.png', 'PT_IMG', 40),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt19.png', 'PT_IMG', 41),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt20.png', 'PT_IMG', 42),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png', 'PT_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png', 'PT_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt3.png', 'PT_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt4.png', 'PT_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt5.png', 'PT_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt6.png', 'PT_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt7.png', 'PT_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt8.png', 'PT_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt9.png', 'PT_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt10.png', 'PT_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt11.png', 'PT_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt12.png', 'PT_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt13.png', 'PT_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt14.png', 'PT_IMG', 15),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt15.png', 'PT_IMG', 16),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt16.png', 'PT_IMG', 17),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt17.png', 'PT_IMG', 18),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt18.png', 'PT_IMG', 19),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt19.png', 'PT_IMG', 20),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt20.png', 'PT_IMG', 21),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png', 'PT_IMG', 22),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png', 'PT_IMG', 23),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png', 'PT_IMG', 24),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png', 'PT_IMG', 25),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt3.png', 'PT_IMG', 26),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt4.png', 'PT_IMG', 27),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt5.png', 'PT_IMG', 28),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt6.png', 'PT_IMG', 29),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt7.png', 'PT_IMG', 30),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt8.png', 'PT_IMG', 31),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt9.png', 'PT_IMG', 32),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt10.png', 'PT_IMG', 33),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt11.png', 'PT_IMG', 34),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt12.png', 'PT_IMG', 35),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt13.png', 'PT_IMG', 36),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt14.png', 'PT_IMG', 37),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt21.png', 'PT_IMG', 38),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt16.png', 'PT_IMG', 39),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt22.png', 'PT_IMG', 40),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt18.png', 'PT_IMG', 41),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt19.png', 'PT_IMG', 42),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt20.png', 'PT_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/banners/banner1.png','BANNER_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/banners/banner2.png','BANNER_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt1.png','REVIEW_IMG', 1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt2.png','REVIEW_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt3.png','REVIEW_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt4.png','REVIEW_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt5.png','REVIEW_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt6.png','REVIEW_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt7.png','REVIEW_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt8.png','REVIEW_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt9.png','REVIEW_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt10.png','REVIEW_IMG',10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt11.png','REVIEW_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt12.png','REVIEW_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt13.png','REVIEW_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt14.png','REVIEW_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt7.png', 'REVIEW_IMG',1),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt8.png','REVIEW_IMG', 2),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt9.png','REVIEW_IMG', 3),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt10.png','REVIEW_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt11.png','REVIEW_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt12.png','REVIEW_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt13.png','REVIEW_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt14.png','REVIEW_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt15.png','REVIEW_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt16.png','REVIEW_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt17.png','REVIEW_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/ptImages/pt18.png','REVIEW_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications1.png', 'CERTS_IMG', 4),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications2.png', 'CERTS_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications3.png', 'CERTS_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications4.png', 'CERTS_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications5.png', 'CERTS_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications6.png', 'CERTS_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications7.png', 'CERTS_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications8.png', 'CERTS_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications9.png', 'CERTS_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications10.png', 'CERTS_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications1.png', 'CERTS_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications2.png', 'CERTS_IMG', 15),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications3.png', 'CERTS_IMG', 5),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications4.png', 'CERTS_IMG', 6),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications5.png', 'CERTS_IMG', 7),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications6.png', 'CERTS_IMG', 8),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications7.png', 'CERTS_IMG', 9),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications8.png', 'CERTS_IMG', 10),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications9.png', 'CERTS_IMG', 11),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications10.png', 'CERTS_IMG', 12),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications2.png', 'CERTS_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications3.png', 'CERTS_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications9.png', 'CERTS_IMG', 15),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications10.png', 'CERTS_IMG', 13),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications1.png', 'CERTS_IMG', 14),
                                                           ('https://hellduo.s3.ap-northeast-2.amazonaws.com/users/certifications/certifications2.png', 'CERTS_IMG', 15);


INSERT INTO TB_Board (title, content, like_count, user_id) VALUES
                                                               ('헬스 첫 시작', '헬스장에서 첫 운동을 시작하며 경험한 것들에 대해 공유합니다. 운동을 처음 시작하신 분들을 위한 팁과 조언이 포함되어 있습니다.', 5, 1),
                                                               ('주 3회 운동으로 몸 변화', '주 3회 운동을 통해 몸에 어떤 변화가 일어났는지 기록해 봅니다. 꾸준히 운동을 하면서 느낀 점들을 공유합니다.', 6, 2),
                                                               ('PT와의 첫 만남', '퍼스널 트레이너(PT)와의 첫 만남에서 어떤 운동을 했고, 어떤 목표를 설정했는지 공유합니다.', 4, 3),
                                                               ('스쿼트와 데드리프트', '하체 운동의 기본, 스쿼트와 데드리프트에 대해 이야기합니다. 이 운동들이 하체 근력에 어떻게 도움을 주는지 설명합니다.', 7, 4),
                                                               ('유산소와 근력 운동의 조화', '유산소 운동과 근력 운동을 어떻게 균형 있게 해야 하는지에 대한 생각을 나눕니다.', 3, 5),
                                                               ('헬스와 식단의 중요성', '운동만큼 중요한 것이 식단입니다. 건강한 식습관과 운동을 병행하는 법에 대해 이야기합니다.', 8, 6),
                                                               ('헬스장에서 자주 하는 실수', '헬스장에서 자주 하는 실수와 그로 인해 발생할 수 있는 부상을 방지하는 방법을 공유합니다.', 2, 7),
                                                               ('체중 감소와 근육 증가', '체중을 감량하면서 근육량을 증가시키기 위한 운동 방법과 식단을 소개합니다.', 10, 8),
                                                               ('초보자를 위한 헬스 운동 루틴', '헬스를 처음 시작하는 사람들을 위한 운동 루틴을 추천합니다. 초보자도 따라할 수 있는 쉬운 운동들입니다.', 3, 9),
                                                               ('상체 운동 팁', '상체 운동에 대한 팁과 코칭을 제공합니다. 덤벨과 바벨을 활용한 상체 운동을 어떻게 하면 효과적으로 할 수 있는지 설명합니다.', 6, 10),
                                                               ('체력 향상을 위한 운동', '체력을 향상시키기 위한 운동과 훈련법에 대해 이야기합니다. 체력 향상은 모든 운동의 기초입니다.', 4, 11),
                                                               ('운동 전 스트레칭의 중요성', '운동 전 스트레칭을 하는 이유와 그 중요성에 대해 설명합니다. 스트레칭을 통해 운동 성과를 높일 수 있습니다.', 1, 12),
                                                               ('헬스장에서 부상 예방하기', '헬스장에서 운동 중 부상을 예방하는 방법에 대해 공유합니다. 올바른 자세와 적절한 휴식이 중요합니다.', 0, 13),
                                                               ('운동 동기 부여', '헬스를 지속할 수 있는 동기를 찾는 방법에 대해 이야기합니다. 운동을 할 때 필요한 마음가짐과 목표 설정 방법도 다룹니다.', 5, 14),
                                                               ('운동 후 회복과 휴식', '운동 후 회복을 위한 중요한 요소들, 예를 들어 충분한 수면과 영양 섭취에 대해 이야기합니다.', 9, 15);

INSERT INTO tb_board_like (board_id, user_id) VALUES
                                               (1, 1), (1, 3), (1, 4), (1, 6), (1, 8),
                                               (2, 5), (2, 7), (2, 2), (2, 9), (2, 10),
                                               (3, 2), (3, 3), (3, 6), (3, 11), (3, 12),
                                               (4, 1), (4, 7), (4, 8), (4, 13), (4, 14),
                                               (5, 4), (5, 6), (5, 9), (5, 12), (5, 15),
                                               (6, 2), (6, 5), (6, 10), (6, 11), (6, 13),
                                               (7, 8), (7, 3), (7, 14), (7, 1), (7, 15),
                                               (8, 10), (8, 4), (8, 7), (8, 9), (8, 13),
                                               (9, 6), (9, 2), (9, 12), (9, 1), (9, 11),
                                               (10, 3), (10, 5), (10, 8), (10, 14), (10, 15),
                                               (11, 4), (11, 7), (11, 1), (11, 9), (11, 2),
                                               (12, 10), (12, 12), (12, 5), (12, 8), (12, 13),
                                               (13, 6), (13, 3), (13, 15), (13, 4), (13, 11),
                                               (14, 9), (14, 1), (14, 7), (14, 10), (14, 2),
                                               (15, 8), (15, 5), (15, 13), (15, 4), (15, 11),
                                               (1, 2), (1, 10), (2, 6), (2, 12), (3, 15),
                                               (3, 9), (4, 5), (4, 8), (5, 7), (5, 11),
                                               (6, 3), (6, 1), (7, 9), (7, 10), (8, 5),
                                               (8, 2), (9, 14), (9, 7), (10, 12), (10, 4),
                                               (11, 6), (11, 13), (12, 10), (13, 14), (14, 15),
                                               (15, 9), (15, 6), (1, 13), (2, 4), (3, 8),
                                               (4, 11), (5, 6), (6, 7), (7, 15), (8, 12);



INSERT INTO TB_PT (title, description, scheduled_date, price, latitude, longitude, address, trainer_id, user_id, status, specialization, created_at, modified_at)
VALUES
    ('전신 체력 강화', '전신 체력을 강화하기 위한 훈련 세션입니다.', '2025-01-01 10:00:00', 50000, 37.5890, 127.0010, '서울특별시 강남구 테헤란로 123, 3층', 4, 1, 'SCHEDULED', 'FITNESS', NOW(), NOW()),
    ('요가 기초', '기본적인 요가 동작을 배우는 세션입니다.', '2025-01-02 11:00:00', 30000, 37.5900, 127.0020, '서울특별시 마포구 홍익로 45, 2층', 5, 2, 'SCHEDULED', 'YOGA', NOW(), NOW()),
    ('필라테스 전환', '필라테스를 배우기 위한 기초적인 세션입니다.', '2025-01-03 12:00:00', 35000, 37.5910, 127.0030, '서울특별시 송파구 가락로 99, 1층', 6, 3, 'SCHEDULED', 'PILATES', NOW(), NOW()),
    ('하체 근육 강화 훈련', '하체 근육을 강화하기 위한 훈련 세션입니다.', '2025-01-04 13:00:00', 52000, 37.5920, 127.0040, '서울특별시 강서구 공항대로 229, 4층', 7, 1, 'SCHEDULED', 'FITNESS', NOW(), NOW()),
    ('기능성 훈련을 통한 부상 예방', '부상 예방을 위한 기능성 훈련 세션입니다.', '2025-01-05 14:00:00', 53000, 37.5930, 127.0050, '서울특별시 종로구 종로 45, 5층', 8, 2, 'SCHEDULED', 'REHABILITATION', NOW(), NOW()),
    ('몸매 교정을 위한 운동 세션', '몸매를 교정하고 균형을 맞추기 위한 운동 프로그램입니다.', '2025-01-06 15:00:00', 54000, 37.5940, 127.0060, '서울특별시 서초구 반포대로 100, 6층', 9, 3, 'SCHEDULED', 'REHABILITATION', NOW(), NOW()),
    ('전신 운동을 통한 체력 강화', '전신을 사용하는 운동으로 체력을 강화하는 세션입니다.', '2025-01-07 16:00:00', 51000, 37.5950, 127.0070, '서울특별시 중구 명동길 45, 7층', 10, 1, 'SCHEDULED', 'CROSSFIT', NOW(), NOW()),
    ('체력과 근력 향상 프로그램', '체력과 근력을 동시에 향상시키는 운동 프로그램입니다.', '2025-01-08 17:00:00', 55000, 37.5960, 127.0080, '서울특별시 강남구 압구정로 123, 8층', 11, 2, 'SCHEDULED', 'CROSSFIT', NOW(), NOW()),
    ('지구력 훈련 세션', '심폐지구력을 향상시키기 위한 유산소 훈련 세션입니다.', '2025-01-09 18:00:00', 50000, 37.5970, 127.0090, '서울특별시 용산구 이태원로 99, 9층', 12, 3, 'SCHEDULED', 'FITNESS', NOW(), NOW()),
    ('피트니스 고급', '고급 피트니스 운동을 배우는 세션입니다.', '2025-01-10 10:00:00', 60000, 37.5980, 127.0100, '서울특별시 강동구 천호대로 45, 10층', 13, 1, 'SCHEDULED', 'FITNESS', NOW(), NOW()),
    ('크로스핏 기본', '기본적인 크로스핏 훈련을 배우는 세션입니다.', '2025-01-11 11:00:00', 58000, 37.5990, 127.0110, '서울특별시 중랑구 용마산로 123, 11층', 14, 2, 'SCHEDULED', 'CROSSFIT', NOW(), NOW()),
    ('피트니스 체형 관리', '체형을 관리하기 위한 피트니스 세션입니다.', '2025-01-12 12:00:00', 49000, 37.6000, 127.0120, '서울특별시 마포구 연남로 56, 12층', 15, 1, 'SCHEDULED', 'FITNESS', NOW(), NOW()),
    ('필라테스 고급', '고급 필라테스 훈련을 배우는 세션입니다.', '2025-01-13 13:00:00', 55000, 37.6010, 127.0130, '서울특별시 서대문구 연세로 90, 13층', 4, 3, 'SCHEDULED', 'PILATES', NOW(), NOW()),
    ('영양학 및 체중 관리', '영양학에 기반한 체중 관리 세션입니다.', '2025-01-14 14:00:00', 47000, 37.6020, 127.0140, '서울특별시 동대문구 휘경로 25, 14층', 5, 2, 'SCHEDULED', 'NUTRITION', NOW(), NOW());

INSERT INTO TB_PT (title, description, scheduled_date, price, latitude, longitude, address, trainer_id, user_id, status, specialization, created_at, modified_at)
VALUES
    ('전신 체력 강화', '전신 체력을 강화하기 위한 훈련 세션입니다.', '2025-01-01 10:00:00', 50000, 37.5890, 127.0010, '서울특별시 강남구 테헤란로 123, 3층', 4, 1, 'COMPLETED', 'FITNESS', NOW(), NOW()),
    ('요가 기초', '기본적인 요가 동작을 배우는 세션입니다.', '2025-01-02 11:00:00', 30000, 37.5900, 127.0020, '서울특별시 마포구 홍익로 45, 2층', 5, 2, 'COMPLETED', 'YOGA', NOW(), NOW()),
    ('필라테스 전환', '필라테스를 배우기 위한 기초적인 세션입니다.', '2025-01-03 12:00:00', 35000, 37.5910, 127.0030, '서울특별시 송파구 가락로 99, 1층', 6, 3, 'COMPLETED', 'PILATES', NOW(), NOW()),
    ('하체 근육 강화 훈련', '하체 근육을 강화하기 위한 훈련 세션입니다.', '2025-01-04 13:00:00', 52000, 37.5920, 127.0040, '서울특별시 강서구 공항대로 229, 4층', 7, 1, 'COMPLETED', 'FITNESS', NOW(), NOW()),
    ('기능성 훈련을 통한 부상 예방', '부상 예방을 위한 기능성 훈련 세션입니다.', '2025-01-05 14:00:00', 53000, 37.5930, 127.0050, '서울특별시 종로구 종로 45, 5층', 8, 2, 'COMPLETED', 'REHABILITATION', NOW(), NOW()),
    ('몸매 교정을 위한 운동 세션', '몸매를 교정하고 균형을 맞추기 위한 운동 프로그램입니다.', '2025-01-06 15:00:00', 54000, 37.5940, 127.0060, '서울특별시 서초구 반포대로 100, 6층', 9, 3, 'COMPLETED', 'REHABILITATION', NOW(), NOW()),
    ('전신 운동을 통한 체력 강화', '전신을 사용하는 운동으로 체력을 강화하는 세션입니다.', '2025-01-07 16:00:00', 51000, 37.5950, 127.0070, '서울특별시 중구 명동길 45, 7층', 10, 1, 'COMPLETED', 'CROSSFIT', NOW(), NOW()),
    ('체력과 근력 향상 프로그램', '체력과 근력을 동시에 향상시키는 운동 프로그램입니다.', '2025-01-08 17:00:00', 55000, 37.5960, 127.0080, '서울특별시 강남구 압구정로 123, 8층', 11, 2, 'COMPLETED', 'CROSSFIT', NOW(), NOW()),
    ('지구력 훈련 세션', '심폐지구력을 향상시키기 위한 유산소 훈련 세션입니다.', '2025-01-09 18:00:00', 50000, 37.5970, 127.0090, '서울특별시 용산구 이태원로 99, 9층', 12, 3, 'COMPLETED', 'FITNESS', NOW(), NOW()),
    ('피트니스 고급', '고급 피트니스 운동을 배우는 세션입니다.', '2025-01-10 10:00:00', 60000, 37.5980, 127.0100, '서울특별시 강동구 천호대로 45, 10층', 13, 1, 'COMPLETED', 'FITNESS', NOW(), NOW()),
    ('크로스핏 기본', '기본적인 크로스핏 훈련을 배우는 세션입니다.', '2025-01-11 11:00:00', 58000, 37.5990, 127.0110, '서울특별시 중랑구 용마산로 123, 11층', 14, 2, 'COMPLETED', 'CROSSFIT', NOW(), NOW()),
    ('피트니스 체형 관리', '체형을 관리하기 위한 피트니스 세션입니다.', '2025-01-12 12:00:00', 49000, 37.6000, 127.0120, '서울특별시 마포구 연남로 56, 12층', 15, 1, 'COMPLETED', 'FITNESS', NOW(), NOW()),
    ('필라테스 고급', '고급 필라테스 훈련을 배우는 세션입니다.', '2025-01-13 13:00:00', 55000, 37.6010, 127.0130, '서울특별시 서대문구 연세로 90, 13층', 4, 3, 'COMPLETED', 'PILATES', NOW(), NOW()),
    ('영양학 및 체중 관리', '영양학에 기반한 체중 관리 세션입니다.', '2025-01-14 14:00:00', 47000, 37.6020, 127.0140, '서울특별시 동대문구 휘경로 25, 14층', 5, 2, 'COMPLETED', 'NUTRITION', NOW(), NOW());

INSERT INTO TB_PT (title, description, scheduled_date, price, latitude, longitude, address, trainer_id, user_id, status, specialization, created_at, modified_at)
VALUES
    ('전신 체력 강화', '전신 체력을 강화하기 위한 훈련 세션입니다.', '2025-01-01 10:00:00', 50000, 37.5890, 127.0010, '서울특별시 강남구 테헤란로 123, 3층', 4, 1, 'UNRESERVED', 'FITNESS', NOW(), NOW()),
    ('요가 기초', '기본적인 요가 동작을 배우는 세션입니다.', '2025-01-02 11:00:00', 30000, 37.5900, 127.0020, '서울특별시 마포구 홍익로 45, 2층', 5, 2, 'UNRESERVED', 'YOGA', NOW(), NOW()),
    ('필라테스 전환', '필라테스를 배우기 위한 기초적인 세션입니다.', '2025-01-03 12:00:00', 35000, 37.5910, 127.0030, '서울특별시 송파구 가락로 99, 1층', 6, 3, 'UNRESERVED', 'PILATES', NOW(), NOW()),
    ('하체 근육 강화 훈련', '하체 근육을 강화하기 위한 훈련 세션입니다.', '2025-01-04 13:00:00', 52000, 37.5920, 127.0040, '서울특별시 강서구 공항대로 229, 4층', 7, 1, 'UNRESERVED', 'FITNESS', NOW(), NOW()),
    ('기능성 훈련을 통한 부상 예방', '부상 예방을 위한 기능성 훈련 세션입니다.', '2025-01-05 14:00:00', 53000, 37.5930, 127.0050, '서울특별시 종로구 종로 45, 5층', 8, 2, 'UNRESERVED', 'REHABILITATION', NOW(), NOW()),
    ('몸매 교정을 위한 운동 세션', '몸매를 교정하고 균형을 맞추기 위한 운동 프로그램입니다.', '2025-01-06 15:00:00', 54000, 37.5940, 127.0060, '서울특별시 서초구 반포대로 100, 6층', 9, 3, 'UNRESERVED', 'REHABILITATION', NOW(), NOW()),
    ('전신 운동을 통한 체력 강화', '전신을 사용하는 운동으로 체력을 강화하는 세션입니다.', '2025-01-07 16:00:00', 51000, 37.5950, 127.0070, '서울특별시 중구 명동길 45, 7층', 10, 1, 'UNRESERVED', 'CROSSFIT', NOW(), NOW()),
    ('체력과 근력 향상 프로그램', '체력과 근력을 동시에 향상시키는 운동 프로그램입니다.', '2025-01-08 17:00:00', 55000, 37.5960, 127.0080, '서울특별시 강남구 압구정로 123, 8층', 11, 2, 'UNRESERVED', 'CROSSFIT', NOW(), NOW()),
    ('지구력 훈련 세션', '심폐지구력을 향상시키기 위한 유산소 훈련 세션입니다.', '2025-01-09 18:00:00', 50000, 37.5970, 127.0090, '서울특별시 용산구 이태원로 99, 9층', 12, 3, 'UNRESERVED', 'FITNESS', NOW(), NOW()),
    ('피트니스 고급', '고급 피트니스 운동을 배우는 세션입니다.', '2025-01-10 10:00:00', 60000, 37.5980, 127.0100, '서울특별시 강동구 천호대로 45, 10층', 13, 1, 'UNRESERVED', 'FITNESS', NOW(), NOW()),
    ('크로스핏 기본', '기본적인 크로스핏 훈련을 배우는 세션입니다.', '2025-01-11 11:00:00', 58000, 37.5990, 127.0110, '서울특별시 중랑구 용마산로 123, 11층', 14, 2, 'UNRESERVED', 'CROSSFIT', NOW(), NOW()),
    ('피트니스 체형 관리', '체형을 관리하기 위한 피트니스 세션입니다.', '2025-01-12 12:00:00', 49000, 37.6000, 127.0120, '서울특별시 마포구 연남로 56, 12층', 15, 1, 'UNRESERVED', 'FITNESS', NOW(), NOW()),
    ('필라테스 고급', '고급 필라테스 훈련을 배우는 세션입니다.', '2025-01-13 13:00:00', 55000, 37.6010, 127.0130, '서울특별시 서대문구 연세로 90, 13층', 4, 3, 'UNRESERVED', 'PILATES', NOW(), NOW()),
    ('영양학 및 체중 관리', '영양학에 기반한 체중 관리 세션입니다.', '2025-01-14 14:00:00', 47000, 37.6020, 127.0140, '서울특별시 동대문구 휘경로 25, 14층', 5, 2, 'UNRESERVED', 'NUTRITION', NOW(), NOW());




INSERT INTO tb_comment (content, board_id, user_id) VALUES
                                                        ('첫 번째 게시물에 대한 댓글입니다.', 1, 1),
                                                        ('정말 도움이 되는 글이네요! 감사합니다.', 1, 2),
                                                        ('저도 헬스 시작하려고 하는데 참고할게요.', 1, 3),
                                                        ('주 3회 운동, 저도 시도해봐야겠네요.', 2, 4),
                                                        ('꾸준히 운동하는 게 정말 중요한 것 같아요.', 2, 5),
                                                        ('PT와의 첫 만남이라니, 기대되네요!', 3, 6),
                                                        ('퍼스널 트레이너 추천받고 싶습니다.', 3, 7),
                                                        ('스쿼트는 정말 하체에 최고인 것 같아요.', 4, 8),
                                                        ('스쿼트와 데드리프트로 하체 근육이 많이 좋아졌습니다.', 4, 9),
                                                        ('유산소와 근력 운동의 밸런스를 잘 맞춰야겠네요.', 5, 10),
                                                        ('좋은 정보 감사합니다!', 5, 11),
                                                        ('헬스에서 식단이 정말 중요하죠.', 6, 12),
                                                        ('식단 관리가 운동만큼 힘들어요 ㅠㅠ', 6, 13),
                                                        ('헬스장에서 실수 많이 해봤네요. 조심해야겠어요.', 7, 14),
                                                        ('부상을 방지하는 게 정말 중요하죠.', 7, 15),
                                                        ('체중 감량하면서 근육량도 늘리기 힘들던데 대단합니다.', 8, 1),
                                                        ('초보자를 위한 운동 루틴 정말 좋아요.', 9, 2),
                                                        ('운동 루틴 잘 참고하겠습니다!', 9, 3),
                                                        ('상체 운동 팁 유익하네요. 덤벨 활용법 배워갑니다.', 10, 4),
                                                        ('상체 운동도 중요하죠! 감사합니다.', 10, 5),
                                                        ('체력 향상을 위한 훈련이 필요하네요.', 11, 6),
                                                        ('운동 전 스트레칭은 꼭 해야 하는 것 같아요.', 12, 7),
                                                        ('스트레칭 덕분에 부상이 줄어들었어요.', 12, 8),
                                                        ('헬스장에서 부상 당한 적이 있어서 더 조심해야겠어요.', 13, 9),
                                                        ('부상을 예방하는 정보 감사합니다.', 13, 10),
                                                        ('운동 동기 부여가 필요했는데 이 글이 큰 도움이 됐어요.', 14, 11),
                                                        ('꾸준히 하다 보면 동기부여도 생기겠죠.', 14, 12),
                                                        ('운동 후 휴식이 정말 중요하죠.', 15, 13),
                                                        ('충분한 수면과 영양 섭취 기억해야겠네요.', 15, 14),
                                                        ('댓글로 좋은 의견 남겨주셔서 감사합니다.', 1, 15),
                                                        ('저도 헬스 열심히 해봐야겠어요.', 2, 1),
                                                        ('운동에 대해 잘 정리된 글이네요!', 3, 2),
                                                        ('하체 근력에 더 신경을 써야겠네요.', 4, 3),
                                                        ('유산소와 근력 운동의 밸런스 유지가 어렵네요.', 5, 4),
                                                        ('헬스와 식단 관리법 정말 유익해요.', 6, 5),
                                                        ('부상 예방을 위한 자세 교정이 필요하겠네요.', 7, 6),
                                                        ('초보자 루틴 너무 좋아요! 따라 해볼게요.', 9, 7),
                                                        ('상체 근력 운동 덕분에 팔 힘이 좋아졌어요.', 10, 8),
                                                        ('체력 훈련법 정말 감사합니다!', 11, 9);


INSERT INTO tb_notice (title, content, admin_id) VALUES
                                                    ('사이트 점검 안내',
                                                     '안녕하세요, 헬듀오 운영팀입니다.
                                                      시스템 점검으로 인해 내일 오전 3시부터 오전 5시까지 약 2시간 동안 서비스가 일시 중단될 예정입니다.
                                                      원활한 서비스 제공을 위한 점검 작업이오니 이용에 불편을 드리게 된 점 양해 부탁드립니다.
                                                      작업이 완료되는 즉시 서비스를 정상화할 예정이며, 이용에 참고해주시길 바랍니다. 감사합니다.',
                                                     1),

                                                    ('신규 기능 업데이트',
                                                     '안녕하세요, 헬듀오 운영팀입니다.
                                                      이번에 저희 서비스에 신규 알림 기능이 추가되었습니다.
                                                      이 기능을 통해 회원님들은 주요 공지사항, 이벤트 소식 및 계정 관련 알림을 더욱 빠르고 편리하게 확인할 수 있습니다.
                                                      새로운 알림 기능은 설정 메뉴에서 활성화 및 비활성화가 가능하며, 원하는 알림 유형을 선택할 수도 있습니다.
                                                      앞으로도 더 나은 서비스를 제공하기 위해 계속해서 노력하겠습니다. 많은 관심 부탁드립니다.',
                                                     1),

                                                    ('정기 점검 공지',
                                                     '안녕하세요, 헬듀오 운영팀입니다.
                                                      다음 주 월요일에 정기 시스템 점검이 예정되어 있습니다.
                                                      점검 시간은 오전 2시부터 오전 6시까지로, 이 시간 동안 일부 서비스가 원활하게 작동하지 않을 수 있습니다.
                                                      점검 작업은 시스템 성능 개선과 보안 강화를 위해 이루어지며, 더욱 안정적인 서비스를 제공하기 위함입니다.
                                                      점검 이후에도 혹시나 발생할 수 있는 오류는 고객센터로 알려주시면 빠르게 처리하겠습니다.
                                                      이용에 불편을 드려 죄송하며, 양해 부탁드립니다.',
                                                     1),

                                                    ('보안 패치 적용',
                                                     '안녕하세요, 헬듀오 운영팀입니다.
                                                      최근 보안 위협에 대응하기 위해 중요한 보안 패치가 적용되었습니다.
                                                      이번 패치는 회원님의 계정 정보와 서비스 데이터를 보다 안전하게 보호하기 위한 작업으로,
                                                      최신 보안 기술을 도입하여 외부 침입 및 해킹 시도를 방지하도록 강화되었습니다.
                                                      안전한 사용을 위해 회원님들께서는 비밀번호를 정기적으로 변경하고, 보안 설정을 점검해주시길 권장드립니다.
                                                      저희 헬듀오는 앞으로도 회원님의 정보 보호를 최우선으로 생각하며, 더욱 안전하고 신뢰할 수 있는 서비스를 제공하기 위해 최선을 다하겠습니다.
                                                      감사합니다.',
                                                     1);



INSERT INTO tb_review (title, content, rating, pt_id, trainer_id) VALUES
                                                                      ('최고의 PT 프로그램',
                                                                       '이 PT 프로그램은 매우 유익했습니다. 트레이너의 지도와 세심한 관리 덕분에 목표를 빠르게 달성할 수 있었습니다.',
                                                                       4.5,
                                                                       15,  -- pt_id (PT 프로그램 15)
                                                                       4),  -- trainer_id (트레이너 4)

                                                                      ('트레이너가 훌륭해요',
                                                                       '트레이너 분의 친절하고 세심한 관리로 운동을 즐기게 되었습니다. 항상 긍정적인 피드백을 주셔서 더욱 동기부여가 되었습니다.',
                                                                       5.0,
                                                                       16,  -- pt_id (PT 프로그램 16)
                                                                       5),  -- trainer_id (트레이너 5)

                                                                      ('PT로 체중 감량 성공',
                                                                       '이 프로그램 덕분에 체중을 10kg 감량하는데 성공했습니다. 매우 체계적이고 효율적인 프로그램이었습니다.',
                                                                       4.7,
                                                                       17,  -- pt_id (PT 프로그램 17)
                                                                       6),  -- trainer_id (트레이너 6)

                                                                      ('운동을 즐겁게 할 수 있었어요',
                                                                       '운동이 힘들고 지루할 줄 알았는데, 트레이너의 도움 덕분에 즐겁게 운동할 수 있었습니다. 감사합니다.',
                                                                       4.3,
                                                                       18,  -- pt_id (PT 프로그램 18)
                                                                       7),  -- trainer_id (트레이너 7)

                                                                      ('무리하지 않아서 좋았어요',
                                                                       '트레이너님이 항상 제 상태를 체크하고 무리가 가지 않도록 조절해주셔서 매우 안전하게 운동할 수 있었습니다.',
                                                                       4.6,
                                                                       19,  -- pt_id (PT 프로그램 19)
                                                                       8),  -- trainer_id (트레이너 8)

                                                                      ('운동 효과가 확실하게 나타났어요',
                                                                       '짧은 시간 안에 체형 변화가 느껴졌습니다. 트레이너님의 철저한 피드백 덕분에 효율적으로 운동할 수 있었습니다.',
                                                                       4.8,
                                                                       20,  -- pt_id (PT 프로그램 20)
                                                                       9),  -- trainer_id (트레이너 9)

                                                                      ('피로가 풀렸어요',
                                                                       '이 PT는 정말 피로 회복에 도움이 되었습니다. 트레이너님의 지도 덕분에 몸이 가벼워진 느낌이 듭니다.',
                                                                       4.4,
                                                                       21,  -- pt_id (PT 프로그램 21)
                                                                       10),  -- trainer_id (트레이너 10)

                                                                      ('운동이 즐겁습니다',
                                                                       '운동을 즐길 수 있게 해주신 트레이너님께 감사드립니다. 처음에는 힘들었지만, 점점 재미있어졌습니다.',
                                                                       4.2,
                                                                       22,  -- pt_id (PT 프로그램 22)
                                                                       11),  -- trainer_id (트레이너 11)

                                                                      ('PT 프로그램이 체형 변화에 도움을 주었어요',
                                                                       'PT 프로그램 덕분에 복부와 하체의 체형 변화가 확실히 느껴졌습니다. 트레이너님의 프로그램이 아주 효과적이었어요.',
                                                                       4.6,
                                                                       23,  -- pt_id (PT 프로그램 23)
                                                                       12),  -- trainer_id (트레이너 12)

                                                                      ('꾸준히 운동할 수 있도록 도와주셨어요',
                                                                       '트레이너님의 격려 덕분에 운동을 꾸준히 할 수 있었고, 목표를 달성했습니다. 더 좋은 결과를 위해 계속할 예정입니다.',
                                                                       4.9,
                                                                       24,  -- pt_id (PT 프로그램 24)
                                                                       13),  -- trainer_id (트레이너 13)

                                                                      ('체력이 많이 늘었어요',
                                                                       '체력도 늘고, 운동을 시작한 지 1개월 만에 확실한 변화를 느꼈습니다. 매우 만족하고 있습니다.',
                                                                       4.7,
                                                                       25,  -- pt_id (PT 프로그램 25)
                                                                       14),  -- trainer_id (트레이너 14)

                                                                      ('근육량 증가',
                                                                       '근육량을 늘리는 데 큰 도움이 되었습니다. 트레이너님이 체계적으로 운동을 지도해주셔서 효과를 봤습니다.',
                                                                       5.0,
                                                                       26,  -- pt_id (PT 프로그램 26)
                                                                       4),  -- trainer_id (트레이너 4)

                                                                      ('더 나은 결과를 위해 노력하고 있습니다',
                                                                       '트레이너님이 제공하는 맞춤형 운동 프로그램 덕분에 점차 나아지는 결과를 느끼고 있습니다.',
                                                                       4.3,
                                                                       27,  -- pt_id (PT 프로그램 27)
                                                                       5),  -- trainer_id (트레이너 5)

                                                                      ('목표를 달성했어요',
                                                                       '목표 체중을 달성할 수 있었고, 몸이 가벼워지는 기분이 들었습니다. 트레이너님 덕분이에요.',
                                                                       4.8,
                                                                       28,  -- pt_id (PT 프로그램 28)
                                                                       6);  -- trainer_id (트레이너 6)


