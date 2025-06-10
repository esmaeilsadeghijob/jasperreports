CREATE TABLE jasper_reports_params
(
    id        UUID PRIMARY KEY,
    report_id UUID REFERENCES jasper_reports (id) ON DELETE CASCADE, -- اتصال به گزارش مربوطه
    paramname VARCHAR(200) NOT NULL,
    paramtype INT          NOT NULL                                  -- نوع پارامتر (مثلاً: 1=String, 2=Integer, 3=Date)
);
