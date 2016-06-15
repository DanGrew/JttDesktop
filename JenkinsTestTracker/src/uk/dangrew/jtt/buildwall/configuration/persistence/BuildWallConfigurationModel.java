/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.conversion.ColorConverter;

/**
 * The {@link BuildWallConfigurationModel} provides the model to read from and write to when converting
 * a {@link BuildWallConfiguration} to and from json data.
 */
class BuildWallConfigurationModel {
   
   private final BuildWallConfiguration configuration;
   private final JenkinsDatabase database;
   private final ColorConverter colorConverter;
   
   private List< JenkinsJob > jobBuffer;
   private List< BuildWallJobPolicy > policyBuffer; 
   
   /**
    * Constructs a new {@link BuildWallConfigurationModel}.
    * @param configuration the {@link BuildWallConfiguration} being serialized.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    */
   BuildWallConfigurationModel( BuildWallConfiguration configuration, JenkinsDatabase database ) {
      if ( configuration == null || database == null ) {
         throw new NullPointerException( "Arguments must not be null." );
      }
      
      this.configuration = configuration;
      this.database = database;
      this.colorConverter = new ColorConverter();
      
      this.jobBuffer = new ArrayList<>();
      this.policyBuffer = new ArrayList<>();
   }//End Constructor

   /**
    * {@link BuildWallConfiguration#numberOfColumns()} setter.
    * @param key the json key.
    * @param value the value.
    */
   void setColumns( String key, Integer value ) {
      configuration.numberOfColumns().set( value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#numberOfColumns()} getter.
    * @param key the json key.
    * @return the value.
    */
   Integer getColumns( String key ){
      return configuration.numberOfColumns().get();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobPanelDescriptionProvider()} setter.
    * @param key the json key.
    * @param value the value.
    */
   void setDescriptionType( String key, JobPanelDescriptionProviders descriptionType ) {
      configuration.jobPanelDescriptionProvider().set( descriptionType );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobPanelDescriptionProvider()} getter.
    * @param key the json key.
    * @return the value.
    */
   JobPanelDescriptionProviders getDescriptionType( String key ){
      return configuration.jobPanelDescriptionProvider().get();
   }//End Method
   
   /**
    * Method to get the number of {@link JenkinsJob}s that will be placed in the structure.
    * @param key the json key.
    * @return the number of {@link JenkinsJob}s to put in the structure.
    */
   Integer getNumberOfJobs( String key ) {
      return configuration.jobPolicies().size();
   }//End Method
   
   /**
    * Method to clear the buffers used when reading and writing.
    */
   private void clearBuffers(){
      jobBuffer.clear();
      policyBuffer.clear();
   }//End Method
   
   /**
    * Method to indicate {@link JenkinsJob} {@link BuildWallJobPolicy}s are being written. This will
    * prepare the {@link JenkinsJob}s and {@link BuildWallJobPolicy}s to write.
    * @param keyString the json key.
    */
   void startWritingJobs( String keyString ) {
      clearBuffers();
      
      configuration.jobPolicies().forEach( ( key, value ) -> {
            jobBuffer.add( key );
            policyBuffer.add( value );
      } );
   }//End Method
   
   /**
    * Method to indicate that {@link JenkinsJob} and {@link BuildWallJobPolicy}s are being parsed.
    * This will clear any existing buffered data.
    * @param key the json key.
    */
   void startParsingJobs( String key ) {
      clearBuffers();
   }//End Method
   
   /**
    * Method to set the name of a {@link JenkinsJob} in a key value pair. These are ordered but do not
    * need to be parsed in tandem. If multiple {@link JenkinsJob}s are parsed they will be buffered. The
    * name will also be searched for in the {@link JenkinsDatabase}. If it does not exist a new {@link JenkinsJob}
    * is created and stored.
    * @param key the json key.
    * @param value the name of the {@link JenkinsJob}.
    */
   void setJobName( String key, String value ) {
      JenkinsJob job = database.getJenkinsJob( value );
      if ( job == null ) {
         job = new JenkinsJobImpl( value );
         database.store( job );
      }
      
      if ( policyBuffer.isEmpty() ) {
         jobBuffer.add( job );
      } else {
         configuration.jobPolicies().put( job, policyBuffer.remove( 0 ) );
      }
   }//End Method
   
   /**
    * Method to get the name of the next {@link JenkinsJob} buffered to write.
    * @param key the json key.
    * @return the {@link JenkinsJob} name or null if no further buffered.
    */
   String getJobName( String key ){
      if ( jobBuffer.isEmpty() ) {
         return null;
      }
      
      return jobBuffer.remove( 0 ).nameProperty().get();
   }//End Method
   
   /**
    * Method to set the {@link BuildWallJobPolicy} of a {@link JenkinsJob} in a key value pair. These are ordered but do not
    * need to be parsed in tandem. If multiple {@link BuildWallJobPolicy}s are parsed they will be buffered. 
    * @param key the json key.
    * @param value the {@link BuildWallJobPolicy} read.
    */
   void setJobPolicy( String key, BuildWallJobPolicy value ) {
      if ( jobBuffer.isEmpty() ) {
         policyBuffer.add( value );
      } else {
         configuration.jobPolicies().put( jobBuffer.remove( 0 ), value );
      }
   }//End Method
   
   /**
    * Method to get the next buffered {@link BuildWallJobPolicy} to write.
    * @param key the json key.
    * @return the {@link BuildWallJobPolicy} or null if no more.
    */
   BuildWallJobPolicy getJobPolicy( String key ) {
      if ( policyBuffer.isEmpty() ) {
         return null;
      }
      
      return policyBuffer.remove( 0 );
   }//End Method
   
   /**
    * Method to generally set the family of a {@link Font} {@link ObjectProperty}.
    * @param property the {@link Font} {@link ObjectProperty}.
    * @param family the family to use.
    */
   private void setPropertyFontFamily( ObjectProperty< Font > property, String family ) {
      Font current = property.get();
      property.set( Font.font( family, current.getSize() ) );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobNameFont()} family setter.
    * @param key the json key.
    * @param value the family.
    */
   void setJobNameFontFamily( String key, String value ) {
      setPropertyFontFamily( configuration.jobNameFont(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#buildNumberFont()} family setter.
    * @param key the json key.
    * @param value the family.
    */
   void setBuildNumberFontFamily( String key, String value ) {
      setPropertyFontFamily( configuration.buildNumberFont(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#completionEstimateFont()} family setter.
    * @param key the json key.
    * @param value the family.
    */
   void setCompletionEstimateFontFamily( String key, String value ) {
      setPropertyFontFamily( configuration.completionEstimateFont(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#detailFont()} family setter.
    * @param key the json key.
    * @param value the family.
    */
   void setDetailFontFamily( String key, String value ) {
      setPropertyFontFamily( configuration.detailFont(), value );
   }//End Method
   
   /**
    * Method to generally set the size of the {@link Font} in the {@link ObjectProperty} given.
    * @param property the {@link Font} {@link ObjectProperty} to set the size.
    * @param size the size to set.
    */
   private void setPropertyFontSize( ObjectProperty< Font > property, Double size ) {
      Font current = property.get();
      property.set( Font.font( current.getFamily(), size ) );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobNameFont()} size setter.
    * @param key the json key.
    * @param value the size.
    */
   void setJobNameFontSize( String key, Double value ) {
      setPropertyFontSize( configuration.jobNameFont(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#buildNumberFont()} size setter.
    * @param key the json key.
    * @param value the size.
    */
   void setBuildNumberFontSize( String key, Double value ) {
      setPropertyFontSize( configuration.buildNumberFont(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#completionEstimateFont()} size setter.
    * @param key the json key.
    * @param value the size.
    */
   void setCompletionEstimateFontSize( String key, Double value ) {
      setPropertyFontSize( configuration.completionEstimateFont(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#detailFont()} size setter.
    * @param key the json key.
    * @param value the size.
    */
   void setDetailFontSize( String key, Double value ) {
      setPropertyFontSize( configuration.detailFont(), value );
   }//End Method
   
   /**
    * Method to generally set the {@link Color} on an {@link ObjectProperty} by converting the hex.
    * @param property the {@link ObjectProperty} to set on.
    * @param value the hex representation.
    */
   private void setColourProperty( ObjectProperty< Color > property, String value ) {
      property.set( colorConverter.hexToColor( value ) );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobNameColour()} setter.
    * @param key the json key.
    * @param value the hex representation of the {@link Color}.
    */
   void setJobNameFontColour( String key, String value ) {
      setColourProperty( configuration.jobNameColour(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#buildNumberColour()} setter.
    * @param key the json key.
    * @param value the hex representation of the {@link Color}.
    */
   void setBuildNumberFontColour( String key, String value ) {
      setColourProperty( configuration.buildNumberColour(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#completionEstimateColour()} setter.
    * @param key the json key.
    * @param value the hex representation of the {@link Color}.
    */
   void setCompletionEstimateFontColour( String key, String value ) {
      setColourProperty( configuration.completionEstimateColour(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#detailColour()} setter.
    * @param key the json key.
    * @param value the hex representation of the {@link Color}.
    */
   void setDetailFontColour( String key, String value ) {
      setColourProperty( configuration.detailColour(), value );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobNameFont()} family getter.
    * @param key the json key.
    * @return the family.
    */
   String getJobNameFontFamily( String key ) {
      return configuration.jobNameFont().get().getFamily();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#buildNumberFont()} family getter.
    * @param key the json key.
    * @return the family.
    */
   String getBuildNumberFontFamily( String key ) {
      return configuration.buildNumberFont().get().getFamily();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#completionEstimateFont()} family getter.
    * @param key the json key.
    * @return the family.
    */
   String getCompletionEstimateFontFamily( String key ) {
      return configuration.completionEstimateFont().get().getFamily();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#detailFont()} family getter.
    * @param key the json key.
    * @return the family.
    */
   String getDetailFontFamily( String key ) {
      return configuration.detailFont().get().getFamily();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobNameFont()} size getter.
    * @param key the json key.
    * @return the size.
    */
   Double getJobNameFontSize( String key ) {
      return configuration.jobNameFont().get().getSize();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#buildNumberFont()} size getter.
    * @param key the json key.
    * @return the size.
    */
   Double getBuildNumberFontSize( String key ) {
      return configuration.buildNumberFont().get().getSize();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#completionEstimateFont()} size getter.
    * @param key the json key.
    * @return the size.
    */
   Double getCompletionEstimateFontSize( String key ) {
      return configuration.completionEstimateFont().get().getSize();
   }//End Method   
   
   /**
    * {@link BuildWallConfiguration#detailFont()} size getter.
    * @param key the json key.
    * @return the size.
    */
   Double getDetailFontSize( String key ) {
      return configuration.detailFont().get().getSize();
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#jobNameColour()} getter.
    * @param key the json key.
    * @return the {@link Color} as a hexadecimal representation.
    */
   String getJobNameFontColour( String key ) {
      return colorConverter.colorToHex( configuration.jobNameColour().get() );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#buildNumberColour()} getter.
    * @param key the json key.
    * @return the {@link Color} as a hexadecimal representation.
    */
   String getBuildNumberFontColour( String key ) {
      return colorConverter.colorToHex( configuration.buildNumberColour().get() );
   }//End Method
   
   /**
    * {@link BuildWallConfiguration#completionEstimateColour()} getter.
    * @param key the json key.
    * @return the {@link Color} as a hexadecimal representation.
    */
   String getCompletionEstimateFontColour( String key ) {
      return colorConverter.colorToHex( configuration.completionEstimateColour().get() );
   }//End Method   
   
   /**
    * {@link BuildWallConfiguration#detailColour()} getter.
    * @param key the json key.
    * @return the {@link Color} as a hexadecimal representation.
    */
   String getDetailFontColour( String key ) {
      return colorConverter.colorToHex( configuration.detailColour().get() );
   }//End Method
}//End Class
