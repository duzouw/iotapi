package cc.mrbird.febs.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author duzou
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Exists {

    /**
     * 默认 false 单卡，true 批量
     */
    boolean batch() default false;
}
