# jasperreports

## setup
````
mvn spring-boot:run
````

## API
````
http://localhost:8080/swagger-ui.html
````

## upload
````
curl -X POST -F "file=@D:/projects/jasperreports/file/jasper_report_template.jrxml" http://localhost:8080/reports/upload
curl -X POST -F "file=@D:/projects/reports/sample_report.jasper" http://localhost:8080/reports/upload

SELECT * FROM jasper_reports;
````