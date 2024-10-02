package com.example.resistance.form.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.example.resistance.form.validator.ConfirmPassword.ConfirmPasswordValidator;

@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmPasswordValidator.class)
public @interface ConfirmPassword {
	String message() default "パスワードとパスワード(確認)が等しくありません";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String password();

	String confirmPassword();

	class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {
		private String passwordProperty;
		private String confirmPasswordProperty;
		private String message;

		@Override
		public void initialize(ConfirmPassword annotation) {
			passwordProperty = annotation.password();
			confirmPasswordProperty = annotation.confirmPassword();
			message = annotation.message();
		}

		@Override
		public boolean isValid(Object value, ConstraintValidatorContext context) {
			// 各フィールドから値を取得する
			BeanWrapper beanWrapper = new BeanWrapperImpl(value);
			String password = (String) beanWrapper.getPropertyValue(passwordProperty);
			String confirmPassword = (String) beanWrapper.getPropertyValue(confirmPasswordProperty);

			// パスワードが等しくなければエラーメッセージを表示する
			if (!password.equals(confirmPassword)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(message).addPropertyNode(passwordProperty)
						.addConstraintViolation();
				return false;
			}
			return true;
		}
	}
}
