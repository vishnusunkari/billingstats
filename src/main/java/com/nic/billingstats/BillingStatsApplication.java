package com.nic.billingstats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import com.amazonaws.xray.proxies.apache.http.HttpClientBuilder;

@Slf4j
@EnableSwagger2
@SpringBootApplication
public class BillingStatsApplication {

    @Value("${swagger.service.title}")
    private String swaggerServiceTitle;

    @Value("${swagger.api.version}")
    private String swaggerApiVersion;

    @Value("${swagger.contact}")
    private String[] swaggerContact;

    public static void main(String[] args) {
        SpringApplication.run(BillingStatsApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        //return new RestTemplate();
        return new RestTemplate(clientHttpRequestFactory());
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        log.info("Custom RestTemplate ClientHttpRequestFactory for X-Ray Tracing");
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().useSystemProperties().build());
        factory.setReadTimeout(20000);
        factory.setConnectTimeout(7000);
        factory.setConnectionRequestTimeout(7000);
        return factory;
    }

    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerServiceTitle)
                        .version(swaggerApiVersion)
                        .contact(new Contact(swaggerContact[0], swaggerContact[1], swaggerContact[2]))
                        .build());
    }

}
