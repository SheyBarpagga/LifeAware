package com.comp7082.lifeaware;
//public class LoggingAspect {
//
//    public aspect LoggingAspect
//
//    {
//
//        pointcut onCreateExecution ():
//        execution( * com.comp7082.lifeaware.controllers.CaregiverActivity.onCreate(Bundle));
//
//        before():onCreateExecution() {
//        System.out.println("Entering onCreate");
//    }
//
//        after():onCreateExecution() {
//        System.out.println("Exiting onCreate: Caregiver/Patient data bundled");
//    }
//
//        pointcut getLocationAndSendSMSExecution ():
//        execution( * com.comp7082.lifeaware.MainFragment.getLocationAndSendSMS(String));
//
//        before():getLocationAndSendSMSExecution() {
//        System.out.println("Entering getLocationAndSendSMS");
//    }
//
//        after():getLocationAndSendSMSExecution() {
//        System.out.println("Exiting getLocationAndSendSMS: SMS sent");
//    }
//
//        pointcut replaceFragmentExecution ():
//        execution( * com.comp7082.lifeaware.controllers.CaregiverActivity.replaceFragment(androidx.fragment.app.Fragment))
//        ;
//
//        before():replaceFragmentExecution() {
//        System.out.println("Entering replaceFragment");
//    }
//
//        after():replaceFragmentExecution() {
//        System.out.println("Exiting replaceFragment: Patient data displayed");
//    }
//
//        pointcut patientConstructorExecution():
//            execution(com.comp7082.lifeaware.models.Patient.new(String));
//
//        before(): patientConstructorExecution() {
//            System.out.println("Patient constructor");
//    }
//
//        after(): patientConstructorExecution() {
//            System.out.println("Patient constructor finished");
//            Object[] arguments = thisJoinPoint.getArgs();
//            System.out.println("Patient id: " + arguments[0]);
//    }
//
//        pointcut caregiverConstructorExecution():
//            execution(com.comp7082.lifeaware.models.Caregiver.new(..));
//
//        before(): caregiverConstructorExecution() {
//            System.out.println("Caregiver constructor");
//    }
//
//        after(): caregiverConstructorExecution() {
//        System.out.println("Caregiver constructor finished");
//    }
//
//    }
//}
