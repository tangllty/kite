package com.tang.kite.utils;

import com.tang.kite.session.entity.JavaAccount;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tang
 */
public class ReflectsJavaTest {

    @Test
    public void getFieldName() {
        var fieldName = Reflects.getFieldName(JavaAccount::getId);
        assertEquals("id", fieldName);
    }

    @Test
    public void getField() {
        var field = Reflects.getField(JavaAccount::getId);
        assertEquals("id", field.getName());
    }

}
