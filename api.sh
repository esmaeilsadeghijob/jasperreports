#curl -X POST -F "file=@D:/projects/jasperreports/file/jasper_report_template.jrxml"  -F "cid=123" -F "sid=456" -F "reportCode=789"   http://localhost:8080/jasperReports/upload
#curl -X GET -F "file=@D:/projects/jasperreports/file/jasper_report_template.jrxml"  -F "cid=123" -F "sid=456" -F "reportCode=789"   http://localhost:8080/preview/a2c1dc31-5fff-4511-bfda-209195bea633
curl -X GET http://localhost:8080/jasperReports/preview/a2c1dc31-5fff-4511-bfda-209195bea633 --output report_preview.jasper

#curl -X POST -F "file=@D:/projects/jasperreports/file/jasper_report_template.jrxml" http://localhost:8080/reports/upload
#curl -X GET "http://localhost:8080/reports/download-jasper?reportId=95b761c3-3108-4c3d-8be3-8504b5baa3ea" -o report.jasper
