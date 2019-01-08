CREATE TABLE message
(
  id      SERIAL8,
  file    VARCHAR(255),
  tag     VARCHAR(255),
  text    VARCHAR,
  user_id INT8,
  PRIMARY KEY (id)
);

CREATE TABLE user_role
(
  user_id SERIAL8 NOT NULL,
  roles   VARCHAR(255)
);

CREATE TABLE usr
(
  id              SERIAL8,
  activation_code VARCHAR(255),
  active          BOOLEAN NOT NULL,
  email           VARCHAR(255),
  password        VARCHAR(255),
  username        VARCHAR(255),
  PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS message
  ADD
    FOREIGN KEY (user_id)
      REFERENCES usr;

ALTER TABLE IF EXISTS user_role
  ADD
    FOREIGN KEY (user_id)
      REFERENCES usr;