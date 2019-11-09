package labs.com.usptodatabasegenerator.uspto.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Data
@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix="swagger")
public class SwaggerConfiguration {
	private String versionNumber;
	private String name;
	private String description;
	private String licence;
	private String licenceUrl;
	private String termsUrl;
	private String contactName;
	private String contactUrl;
	private String contactEmail;
	private String packageController;
	private String regexPathMapping;

	@Bean
	public Docket restApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(packageController))
				.paths(regex(regexPathMapping))
				.build()
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title(name.toUpperCase())
				.description(description)
				.version(versionNumber)
				.termsOfServiceUrl(termsUrl)
				.contact(new Contact(contactName, contactUrl, contactEmail))
				.license(licence)
				.licenseUrl(licenceUrl)
				.extensions(null).build();
	}
}
