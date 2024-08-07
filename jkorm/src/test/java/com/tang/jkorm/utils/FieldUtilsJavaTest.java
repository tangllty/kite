package com.tang.jkorm.utils;

import com.tang.jkorm.session.entity.JavaAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tang
 */
public class FieldUtilsJavaTest {

    @Test
    public void getFieldName() {
        var fieldName = FieldUtils.INSTANCE.getFieldName(JavaAccount::getId);
        assertEquals("id", fieldName);
    }

    @Test
    public void getField() {
        var field = FieldUtils.INSTANCE.getField(JavaAccount::getId);
        assertEquals("id", field.getName());
    }

}
