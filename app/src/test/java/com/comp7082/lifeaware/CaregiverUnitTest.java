package com.comp7082.lifeaware;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.comp7082.lifeaware.models.Caregiver;
import com.comp7082.lifeaware.models.Patient;

public class CaregiverUnitTest {
    @Test
    public void createValid() {
        Caregiver caregiver = mock(Caregiver.class);

        String testName     = "test name";
        String testPhone    = "+6055765768";
        String testId       = "s9f0sf90fs0ss09uf3";

        when(caregiver.getPhoneNumber()).thenReturn(testPhone);
        when(caregiver.getId()).thenReturn(testId);
        when(caregiver.getName()).thenReturn(testName);

        assertEquals(caregiver.getName(), testName);
        assertEquals(caregiver.getPhoneNumber(), testPhone);
        assertEquals(caregiver.getId(), testId);

        assertFalse("Patient name is empty", caregiver.getName().isEmpty());
        assertFalse("Patient id is empty", caregiver.getId().isEmpty());
        assertFalse("Patient age is empty", caregiver.getPhoneNumber().isEmpty());
    }
}
