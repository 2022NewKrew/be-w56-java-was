INSERT INTO USERS(USER_ID, PASSWORD, NAME, EMAIL)
VALUES ('admin', 'admin', '관리자', 'admin@kafe.com'),
       ('jynah92', '1111', '나준엽', 'jynah92@kakao.com');

INSERT INTO MEMOS(WRITER_ID, CONTENT, CREATED_DATE)
VALUES ('admin', '반갑습니다!', NOW());
