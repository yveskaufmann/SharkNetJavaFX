package net.sharksystem.sharknet.javafx.context;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A ViewContext field annotated with this annotation will be provided
 * with ViewContext Instance.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Context {
}
