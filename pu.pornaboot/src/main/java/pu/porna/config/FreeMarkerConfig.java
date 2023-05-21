package pu.porna.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import no.api.freemarker.java8.Java8ObjectWrapper;

/**
 * Dit is een manier om in FreeMarker java.time objecten te formatteren.
 * @see https://github.com/lazee/freemarker-java-8
 * Dit schijnt ook te kunnen, zet dit in je properties:
 * {@code spring.freemarker.settings.object_wrapper=no.api.freemarker.java8.Java8ObjectWrapper(Configuration.VERSION_2_3_31)}
 */
@Configuration
public class FreeMarkerConfig implements BeanPostProcessor
{
@Override
public Object postProcessAfterInitialization( Object bean, String beanName ) throws BeansException
{

	if ( bean instanceof FreeMarkerConfigurer )
	{
		FreeMarkerConfigurer configurer = (FreeMarkerConfigurer) bean;
		configurer.getConfiguration().setObjectWrapper( new Java8ObjectWrapper( freemarker.template.Configuration.getVersion() ) );
	}
	return bean;
}

}
