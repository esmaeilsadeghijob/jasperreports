curl -X POST -F "file=@sample_report.jasper" http://localhost:8080/reports/upload

#curl -X GET "http://localhost:8080/reports/download?reportId=1&param1=value1&param2=value2"
#curl -X POST -F "file=@sample_report.jasper" http://localhost:8080/reports/upload
#curl -X GET "http://localhost:8080/reports/download?reportPath=sample_report.jasper" -o output.xlsx
#curl -X GET "http://localhost:8080/reports/download/excel?reportPath=sample_report.jasper" -o output.xlsx
#curl -X GET "http://localhost:8080/reports/download?reportPath=sample_report.jasper&username=Ali&date=2025-06-02" -o output.pdf
#curl -X GET "http://localhost:8080/reports/download?reportPath=sample_report.jasper" -o output.pdf

