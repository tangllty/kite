package com.tang.kite.utils;

import com.tang.kite.session.entity.JavaAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Tang
 */
public class FieldsJavaTest {

    @Test
    public void getFieldName() {
        var fieldName = Fields.INSTANCE.getFieldName(JavaAccount::getId);
        assertEquals("id", fieldName);
    }

    @Test
    public void getField() {
        var field = Fields.INSTANCE.getField(JavaAccount::getId);
        assertEquals("id", field.getName());
    }

}
