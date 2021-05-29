-- V2__create_author_table.sql

CREATE TABLE registration_app.participants (
  id serial NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  age INT NOT NULL,
  mobile VARCHAR(15),
  mail VARCHAR(50),
  category VARCHAR(100),
  country VARCHAR(100),

  CONSTRAINT pk_participant PRIMARY KEY (ID)
);

CREATE TABLE registration_app.levels (
  id serial NOT NULL,
  level_name VARCHAR(50),
  active BOOLEAN,
  category VARCHAR(100),

  CONSTRAINT pk_level PRIMARY KEY (ID)
);


CREATE TABLE registration_app.participants_levels (
    participant_id INT,
    level_id INT,
    score INT,
    rank INT,
    result VARCHAR(50),
    CUSTOM_1 VARCHAR(50),
    CUSTOM_2 VARCHAR(50),
    CUSTOM_3 VARCHAR(50),
    CUSTOM_4 VARCHAR(50),
    CONSTRAINT fk_participant FOREIGN KEY(participant_id) REFERENCES registration_app.participants(id),
    CONSTRAINT fk_level FOREIGN KEY(level_id) REFERENCES registration_app.levels(id)
)