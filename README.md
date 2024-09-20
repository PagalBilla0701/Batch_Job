       INSERT INTO COPS_FEE_WAIVER (
    ID, 
    N_CARDNUM, 
    F_STATUS, 
    F_LATE_FEE_ELIGIBLE, 
    X_ANNUAL_FEE_REQUESTED, 
    D_ANNUAL_FEE_REQ_DATE, 
    F_LATE_FEE_REQUESTED, 
    D_LATE_FEE_REQ_DATE, 
    X_ANNUAL_FEE_ELIGIBLE, 
    X_FILENAME, 
    D_CREAT, 
    D_UPD, 
    X_CREAT, 
    X_UPD, 
    F_LATEST
) 
VALUES (
    101,                                   -- ID
    '1234567890123456',                    -- N_CARDNUM
    'ACTIVE',                              -- F_STATUS
    'YES',                                 -- F_LATE_FEE_ELIGIBLE
    '500.00',                              -- X_ANNUAL_FEE_REQUESTED
    TO_DATE('2023-09-01', 'YYYY-MM-DD'),   -- D_ANNUAL_FEE_REQ_DATE
    '100.00',                              -- F_LATE_FEE_REQUESTED
    TO_DATE('2023-09-05', 'YYYY-MM-DD'),   -- D_LATE_FEE_REQ_DATE
    '750.00',                              -- X_ANNUAL_FEE_ELIGIBLE
    'fee_waiver_file.csv',                 -- X_FILENAME
    SYSDATE,                               -- D_CREAT
    SYSDATE,                               -- D_UPD
    'SYSTEM_USER',                         -- X_CREAT
    'SYSTEM_USER',                         -- X_UPD
    'YES'                                  -- F_LATEST
);
