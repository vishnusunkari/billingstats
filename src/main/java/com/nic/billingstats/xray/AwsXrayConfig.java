package com.nic.billingstats.xray;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;

@ConditionalOnProperty(
		value = "AWS_XRAY_ENABLED",
		havingValue = "true",
		matchIfMissing = false)
@Configuration
public class AwsXrayConfig {

	@Bean
	public Filter TracingFilter() {
		return new AWSXRayServletFilter("billingstats");

	}
}