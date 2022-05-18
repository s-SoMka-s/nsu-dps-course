CREATE TYPE gender AS ENUM ('male', 'female');
CREATE TABLE People (
    Id int NOT NULL,
    Name text NOT NULL,
    Gender gender NOT NULL,
    FatherId int,
    MotherId int,
    SpouseId int,
    PRIMARY KEY (Id),
    FOREIGN KEY (FatherId) REFERENCES People(Id),
    FOREIGN KEY (MotherId) REFERENCES People(Id),
    FOREIGN KEY (SpouseId) REFERENCES People(Id)
);