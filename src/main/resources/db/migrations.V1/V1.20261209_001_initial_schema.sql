IF NOT EXISTS (
    SELECT 1
    FROM sys.tables t
    INNER JOIN sys.schemas s
        ON s.schema_id = t.schema_id
    WHERE s.name = 'dbo'
      AND t.name = 'transaction'
)
BEGIN
CREATE TABLE dbo.[transaction]
(
    transactionId BIGINT IDENTITY(1,1) NOT NULL,
    amount        DECIMAL(19, 2) NOT NULL,
    scheduledDate DATE           NOT NULL,
    fee           DECIMAL(19, 2) NOT NULL,
    totalAmount   DECIMAL(19, 2) NOT NULL,

    createdAt     DATETIME2(7) NOT NULL
    CONSTRAINT DF_transaction_createdAt
    DEFAULT SYSUTCDATETIME(),

    updatedAt     DATETIME2(7) NOT NULL
    CONSTRAINT DF_transaction_updatedAt
    DEFAULT SYSUTCDATETIME(),

    CONSTRAINT PK_transaction
    PRIMARY KEY (transactionId),

    CONSTRAINT CK_transaction_amount
    CHECK (amount >= 0.01),

    CONSTRAINT CK_transaction_fee
    CHECK (fee >= 0.01)
    );
END
GO

CREATE OR ALTER TRIGGER dbo.TR_transaction_updatedAt
ON dbo.[transaction]
AFTER UPDATE
                          AS
BEGIN
    SET NOCOUNT ON;

UPDATE t
SET updatedAt = SYSUTCDATETIME()
    FROM dbo.[transaction] t
    INNER JOIN inserted i
ON t.transactionId = i.transactionId;
END
GO