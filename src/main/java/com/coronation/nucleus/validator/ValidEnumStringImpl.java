package com.coronation.nucleus.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import java.util.Collection;
import java.util.Iterator;

public class ValidEnumStringImpl implements ConstraintValidator<ValidEnumString, Object> {
    private Class<? extends Enum<?>> enumClass;

    public ValidEnumStringImpl() {
    }

    public void initialize(ValidEnumString constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            boolean valid = this.isValidObject(value);
            boolean isDefaultMessage = "".equals(context.getDefaultConstraintMessageTemplate());
            if (!valid && isDefaultMessage) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid ").append(this.enumClass.getSimpleName()).append(" specified. Valid values are : ").append(String.join(" ", this.getEnumPossibleValues(this.enumClass)));
                ConstraintViolationBuilder constraintViolationBuilder = context.buildConstraintViolationWithTemplate(stringBuilder.toString());
                constraintViolationBuilder.addConstraintViolation();
            }

            return valid;
        }
    }

    private boolean isValidObject(Object value) {
        Enum<?>[] enumConstants = (Enum[])this.enumClass.getEnumConstants();
        boolean valid = true;
        if (value.getClass().isArray()) {
            if (!value.getClass().getComponentType().isPrimitive()) {
                Object[] values = (Object[])((Object[])value);
                Object[] var5 = values;
                int var6 = values.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    Object aValue = var5[var7];
                    if (aValue != null && !this.isValidEnumString(aValue.toString(), enumConstants)) {
                        valid = false;
                        break;
                    }
                }
            } else {
                valid = false;
            }
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            Collection<Object> values = (Collection)value;
            Iterator var10 = values.iterator();

            while(var10.hasNext()) {
                Object aValue = var10.next();
                if (aValue != null && !this.isValidEnumString(aValue.toString(), enumConstants)) {
                    valid = false;
                    break;
                }
            }
        } else if (!this.isValidEnumString(value.toString(), enumConstants)) {
            valid = false;
        }

        return valid;
    }

    private boolean isValidEnumString(String value, Enum<?>[] enumConstants) {
        Enum[] var3 = enumConstants;
        int var4 = enumConstants.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Enum<?> anEnum = var3[var5];
            if (value.equals(anEnum.name())) {
                return true;
            }
        }

        return false;
    }

    private String[] getEnumPossibleValues(Class<? extends Enum<?>> clazz) {
        Enum<?>[] enumConstants = (Enum[])clazz.getEnumConstants();
        String[] enumNames = new String[enumConstants.length];

        for(int x = 0; x < enumConstants.length; ++x) {
            enumNames[x] = enumConstants[x].name();
        }

        return enumNames;
    }
}
