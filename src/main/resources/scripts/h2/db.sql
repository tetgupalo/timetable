CREATE TABLE IF NOT EXISTS "USER" (
  USER_ID   INT AUTO_INCREMENT,
  GROUP_ID  INT,
  USER_NAME VARCHAR(60)  NOT NULL,
  PASSWORD  VARCHAR(200) NOT NULL,
  ROLE      VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS "LECTURER" (
  LECTURER_ID INT AUTO_INCREMENT,
  USER_ID     INT         NOT NULL,
  DEGREE      VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS "GRADUATE" (
  GRADUATE_ID    INT AUTO_INCREMENT,
  LESSON_ID      INT         NOT NULL,
  USER_ID        INT         NOT NULL,
  LECTURER_ID    INT         NOT NULL,
  GRADUATE_NAME  VARCHAR(60) NOT NULL,
  GRADUATE_SCORE VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS "GROUP" (
  GROUP_ID           INT AUTO_INCREMENT,
  GROUP_NAME         VARCHAR(60) NOT NULL,
  AMOUNT_OF_STUDENTS INT         NOT NULL,
  GROUP_YEAR         INT         NOT NULL
);

CREATE TABLE IF NOT EXISTS "LESSON" (
  LESSON_ID   INT AUTO_INCREMENT,
  LECTURER_ID INT         NOT NULL,
  LESSON_NAME VARCHAR(60) NOT NULL,
  LESSON_TIME VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS "LESSONS_FOR_GROUPS" (
  LESSON_ID INT NOT NULL,
  GROUP_ID  INT NOT NULL
);