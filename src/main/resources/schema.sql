CREATE TABLE IF NOT EXISTS POST_OFFICES
(
    IND  INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    NAME VARCHAR(250)                             NOT NULL,
    CONSTRAINT PK_POST_OFFICES PRIMARY KEY (IND)
);

CREATE TABLE IF NOT EXISTS POST_ITEMS
(
    ID         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    IND        INTEGER                                 NOT NULL,
    TYPE_ITEM  INTEGER                                 NOT NULL,
    ADDRESS_ID INTEGER                                 NOT NULL,
    SENDER     VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_POST_ITEMS PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS ADDRESSES
(
    ID           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    IND          INTEGER                                  NOT NULL,
    CITY         VARCHAR(250)                             NOT NULL,
    STREET       VARCHAR(250)                             NOT NULL,
    HOUSE_NUMBER INTEGER                                  NOT NULL,
    FLAT_NUMBER  INTEGER                                  NOT NULL,
    CONSTRAINT ADDRESSES PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DELIVERY_STAGE
(
    ID                 INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    OFFICE_ID          INTEGER                                  NOT NULL,
    ITEM_ID            INTEGER                                  NOT NULL,
    STATUS_DELIVERY_ID INTEGER                                  NOT NULL,
    TIME_DELIVERY      DATETIME,
    CONSTRAINT DELIVERY_STAGE PRIMARY KEY (ID)
);


CREATE TABLE IF NOT EXISTS TYPE_POST_ITEM
(
    ID             INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    TYPE_POST_ITEM VARCHAR(40)                              NOT NULL,
    ALIAS          VARCHAR(40),
    CONSTRAINT TYPE_POST_ITEM PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS STATUS_DELIVERY
(
    ID     INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    STATUS VARCHAR(40)                              NOT NULL,
    CONSTRAINT STATUS_DELIVERY PRIMARY KEY (ID)
);