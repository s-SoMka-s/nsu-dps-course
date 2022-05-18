DROP TABLE IF EXISTS People;
DROP TYPE IF EXISTS Gender;

CREATE TYPE Gender AS ENUM ('Male', 'Female');
CREATE TABLE People (
                        Id integer NOT NULL,
                        Name text NOT NULL,
                        Gender gender NOT NULL,
                        FatherId integer,
                        MotherId integer,
                        SpouseId integer,
                        PRIMARY KEY (Id)
                        --FOREIGN KEY (FatherId) REFERENCES People(Id),
                        --FOREIGN KEY (MotherId) REFERENCES People(Id),
                        --FOREIGN KEY (SpouseId) REFERENCES People(Id)
);