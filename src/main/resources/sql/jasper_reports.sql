CREATE TABLE jasper_reports
(
    id            UUID PRIMARY KEY,
    cid           INT NOT NULL,
    sid           INT NOT NULL,
    reportcode    INT,
    reportcontent BYTEA, -- ذخیره فایل JRXML یا JASPER
    fixed         BOOLEAN DEFAULT FALSE
);
