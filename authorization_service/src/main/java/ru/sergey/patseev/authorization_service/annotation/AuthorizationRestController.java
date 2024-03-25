package ru.sergey.patseev.authorization_service.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for marking REST controllers related to authorization.
 * It combines functionality of {@link RestController} and {@link RequestMapping}.
 * Can be used at the type level.
 */
@RestController
@RequestMapping
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorizationRestController {

	/**
	 * Alias for the {@link RequestMapping#path()} attribute.
	 *
	 * @return The path value for the controller.
	 */
	@AliasFor(annotation = RequestMapping.class, attribute = "path")
	String path() default "";
}
