/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package model.tests;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import utility.TestCommon;

/**
 * {@link TestCaseImpl} unit test.
 */
public class TestCaseImplTest {
   
   /**
    * Prove that the {@link TestCase#durationProperty()} is provided with a default value.
    */
   @Test public void shouldProvideDurationProperty(){
      TestCase testCase = new TestCaseImpl( "anything", Mockito.mock( TestClass.class ) );
      DoubleProperty duration = testCase.durationProperty();
      Assert.assertEquals( TestCaseImpl.DEFAULT_DURATION, duration.get(), TestCommon.precision() );
   }//End Method
   
   /**
    * Prove that the {@link TestCase#nameProperty()} is provided with the given name initially.
    */
   @Test public void shouldProvideNameProperty(){
      final String name = "anything";
      TestCase testCase = new TestCaseImpl( name, Mockito.mock( TestClass.class ) );
      StringProperty nameProperty = testCase.nameProperty();
      Assert.assertEquals( name, nameProperty.get() );
   }//End Method
   
   /**
    * Prove that the {@link TestCase#skippedProperty()} is provided with a default value.
    */
   @Test public void shouldProvideSkippedProperty(){
      TestCase testCase = new TestCaseImpl( "anything", Mockito.mock( TestClass.class ) );
      BooleanProperty skipped = testCase.skippedProperty();
      Assert.assertEquals( TestCaseImpl.DEFAULT_SKIPPED, skipped.get() );
   }//End Method
   
   /**
    * Prove that the {@link TestCase#statusProperty()} is provided with a default value.
    */
   @Test public void shouldProvideStatusProperty(){
      TestCase testCase = new TestCaseImpl( "anything", Mockito.mock( TestClass.class ) );
      ObjectProperty< TestResultStatus > status = testCase.statusProperty();
      Assert.assertEquals( TestCaseImpl.DEFAULT_STATUS, status.get() );
   }//End Method
   
   /**
    * Prove that the {@link TestCase#testClassProperty()} is provided with the given {@link TestClass} initially.
    */
   @Test public void shouldProvideTestClassProperty(){
      final TestClass testClass = Mockito.mock( TestClass.class );
      TestCase testCase = new TestCaseImpl( "anything", testClass );
      ObjectProperty< TestClass > testClassProperty = testCase.testClassProperty();
      Assert.assertEquals( testClass, testClassProperty.get() );
   }//End Method
   
   /**
    * Prove that the {@link TestCase#ageProperty()} is provided with the default age.
    */
   @Test public void shouldProvideAgeProperty(){
      TestCase testCase = new TestCaseImpl( "anything", Mockito.mock( TestClass.class ) );
      IntegerProperty ageProperty = testCase.ageProperty();
      Assert.assertEquals( TestCaseImpl.DEFAULT_AGE, ageProperty.get() );
   }//End Method
   
   /**
    * Prove that a null name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullNameInConstructor(){
      new TestCaseImpl( null, Mockito.mock( TestClass.class ) );
   }//End Method
   
   /**
    * Prove that an empty name is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectEmptyNameInConstructor(){
      new TestCaseImpl( "", Mockito.mock( TestClass.class ) );
   }//End Method
   
   /**
    * Prove that a name with only spaces is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectSpaceNameInConstructor(){
      new TestCaseImpl( "   ", Mockito.mock( TestClass.class ) );
   }//End Method
   
   /**
    * Prove that a null {@link TestClass} is rejected.
    */
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullTestClassInConstructor(){
      new TestCaseImpl( "anything", null );
   }//End Method

}//End Class
