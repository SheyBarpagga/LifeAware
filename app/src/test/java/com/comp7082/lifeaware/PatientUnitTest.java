package com.comp7082.lifeaware;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.comp7082.lifeaware.models.Patient;


public class PatientUnitTest {
    @Test
    public void createValid() {
        Patient patient = mock(Patient.class);

        String testName     = "test name";
        String testAge      = "22";
        String testId       = "s9f0sf90fs0ss09uf3";

        when(patient.getAge()).thenReturn(testAge);
        when(patient.getId()).thenReturn(testId);
        when(patient.getName()).thenReturn(testName);

        assertEquals(patient.getName(), testName);
        assertEquals(patient.getAge(), testAge);
        assertEquals(patient.getId(), testId);

        assertFalse("Patient name is empty", patient.getName().isEmpty());
        assertFalse("Patient id is empty", patient.getId().isEmpty());
        assertFalse("Patient age is empty", patient.getAge().isEmpty());
    }
}
