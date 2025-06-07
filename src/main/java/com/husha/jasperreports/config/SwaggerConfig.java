package com.husha.jasperreports.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(" مدیریت گزارشات جسپر - شرکت دانشوران سرمد")
                        .description("پلتفرمی برای پردازش و مدیریت گزارشات جسپر توسط شرکت دانشوران سرمد. [وب‌سایت شرکت](https://www.hushasoft.ir/)")
//                        .version("2.1.0")
                        .contact(new Contact()
                                .name("پشتیبانی دانشوران سرمد")
                                .url("https://www.hushasoft.ir/")
                                .email("support@hushasoft.ir")))
                .externalDocs(new ExternalDocumentation()
                        .description("مستندات رسمی گزارشات جسپر")
                        .url("https://community.jaspersoft.com/documentation"));
    }
}
