package com.hillspet.wearables.configuration;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import com.hillspet.wearables.common.constants.Constants;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@Component
@ApplicationPath(Constants.APPLICATION_PATH)
@Configuration
public class JerseyConfig extends ResourceConfig {

	private static final Logger LOGGER = LogManager.getLogger(JerseyConfig.class);

	public JerseyConfig() {

		// packages(Constants.WEARABLES_BASE_PACKAGE);

		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Path.class, true, true));
		provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
		provider.findCandidateComponents(Constants.WEARABLES_BASE_PACKAGE).forEach(beanDefinition -> {
			try {
				LOGGER.info("registering {} to jersey config", beanDefinition.getBeanClassName());
				register(Class.forName(beanDefinition.getBeanClassName()));
			} catch (ClassNotFoundException e) {
				LOGGER.warn("Failed to register: {}", beanDefinition.getBeanClassName());
			}
		});

		register(MultiPartFeature.class);
		configureSwagger();
	}

	private void configureSwagger() {
		register(ApiListingResource.class);
		register(SwaggerSerializers.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setContact("HPN");
		beanConfig.setVersion("1.0");
		beanConfig.setSchemes(new String[] { "http", "https" });
		beanConfig.setBasePath(Constants.CONTEXT_PATH.concat(Constants.APPLICATION_PATH));
		beanConfig.setResourcePackage(Constants.WEARABLES_RESOURCES_PACKAGE);
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
	}
}
