package cn.senninha.sserver.lang.codec;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MessageWrapperAnnotation {
	public int cmd() default 0;
}
