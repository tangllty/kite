package com.tang.jkorm.utils;

import com.tang.jkorm.session.entity.JavaAccount;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tang
 */
public class FieldUtilsJavaTest {

    @Test
    public void getFieldName() {
        String fieldName = FieldUtils.INSTANCE.getFieldName(JavaAccount::getId);
        assertEquals("id", fieldName);
    }

    @Test
    public void getField() {
        Field field = FieldUtils.INSTANCE.getField(JavaAccount::getId);
        assertEquals("id", field.getName());
    }

}
