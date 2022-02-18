
CREATE TABLE registration_app.runner (
  id serial NOT NULL,
  name VARCHAR(50),
  b_day VARCHAR(100),
  mobile VARCHAR(15),
  address VARCHAR(1000),
  gender VARCHAR(20),
  race VARCHAR(20),
  tshirt_size VARCHAR(100),
  collection_place VARCHAR(100),


  CONSTRAINT pk_runner PRIMARY KEY (ID)
);