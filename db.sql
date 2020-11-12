create schema bank;
set schema 'bank';

CREATE TYPE idType AS enum ('DL', 'SID') ;

CREATE TYPE transactionKey AS enum ('DEBIT','CREDIT','TRANSFER') ;

CREATE TABLE "Person"
(
    "PersonId" SERIAL unique,
    "FirstName" VARCHAR(20) NOT NULL,
    "LastName" VARCHAR(20) NOT NULL,
    "Address" VARCHAR(100) NOT NULL,
    "StateIdType" idType DEFAULT 'DL',
    "StateId" VARCHAR(20) unique,
    "State" VARCHAR(3) NOT NULL,
    CONSTRAINT "PK_Person" PRIMARY KEY  ("PersonId")
);

CREATE TABLE "User"
(
    "UserId" SERIAL unique,
    "PersonId" INT not null,
    "UserName" VARCHAR(30) NOT NULL,
    "Password" VARCHAR(40) NOT NULL,
    "Employee" BOOLEAN DEFAULT 'FALSE',
    CONSTRAINT "PK_UserId" PRIMARY KEY  ("UserId")
);

CREATE TABLE "Account"
(
    "AccountId" SERIAL unique,
    "UserId" INTEGER NOT NULL,
    "Balance" real NOT NULL,
    CONSTRAINT "PK_Account" PRIMARY KEY  ("AccountId")
);


CREATE TABLE "Transaction"
(
    "TransactionId" SERIAL,
    "AccountId" INTEGER NOT NULL,
    "TransactionType" transactionKey NOT NULL,
    "Amount" real NOT NULL,
    "TargetAccount" INTEGER,
    "TransactionTime" TIMESTAMP not null,
    CONSTRAINT "PK_Transaction" PRIMARY KEY  ("TransactionId")
);


ALTER TABLE "User" ADD CONSTRAINT "FK_PersonId"
    FOREIGN KEY ("PersonId") REFERENCES "Person" ("PersonId") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "Account" ADD CONSTRAINT "FK_UserId"
    FOREIGN KEY ("UserId") REFERENCES "User" ("UserId") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "Transaction" ADD CONSTRAINT "FK_TransactionId"
    FOREIGN KEY ("AccountId") REFERENCES "Account" ("AccountId") ON DELETE NO ACTION ON UPDATE NO ACTION;

