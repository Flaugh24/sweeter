CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE message
(
  id      INT8 NOT NULL,
  file    VARCHAR(255),
  tag     VARCHAR(255),
  text    VARCHAR,
  user_id INT8,
  PRIMARY KEY (id)
);

CREATE TABLE user_role
(
  user_id INT8 NOT NULL,
  roles   VARCHAR(255)
);

CREATE TABLE usr
(
  id              INT8    NOT NULL,
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