/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import uk.dangrew.jtt.desktop.main.JenkinsTestTracker;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jupa.file.protocol.JarJsonPersistingProtocol;
import uk.dangrew.jupa.json.marshall.ModelMarshaller;
import uk.dangrew.jupa.json.session.SessionManager;
import uk.dangrew.sd.logging.location.FileLocationProtocol;

/**
 * The {@link StatisticsConfigurationSessions} provides the {@link StatisticsConfiguration}s for the
 * system along with its {@link SessionManager}.
 */
public class StatisticsConfigurationSessions {

   static final String FOLDER_NAME = "uk.dangrew.jtt.configuration";
   static final String FILE_NAME = "statistics.json";
   
   private final StatisticsConfiguration configuration;
   private final SessionManager sessions;
   private final JarJsonPersistingProtocol configurationFileLocation;
   
   /**
    * Constructs a new {@link StatisticsConfigurationSessions}.
    * @param configuration the {@link StatisticsConfiguration} for the right panel.
    */
   public StatisticsConfigurationSessions( 
            StatisticsConfiguration configuration 
   ) {
      this( 
               configuration,
               new JarJsonPersistingProtocol( 
                        FOLDER_NAME, FILE_NAME, JenkinsTestTracker.class 
               )
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatisticsConfigurationSessions}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param protocol the {@link JarJsonPersistingProtocol} for the left {@link StatisticsConfiguration}.
    */
   StatisticsConfigurationSessions( 
            StatisticsConfiguration configuration, 
            JarJsonPersistingProtocol protocol 
   ) {
      this.configuration = configuration;
      this.configurationFileLocation = protocol;
      
      ModelMarshaller marshaller = constructMarshaller( configuration, protocol );
      marshaller.read();
      this.sessions = new SessionManager( marshaller );
      configureSessionManager( configuration, sessions );
   }//End Constructor
   
   /**
    * Method to construct the {@link ModelMarshaller} using {@link StatisticsConfigurationSessions}.
    * @param configuration the {@link StatisticsConfiguration} to persist.
    * @param locationProtocol the {@link JarJsonPersistingProtocol} for the persistence.
    * @return the {@link ModelMarshaller} constructed.
    */
   private ModelMarshaller constructMarshaller( 
            StatisticsConfiguration configuration, 
            JarJsonPersistingProtocol locationProtocol 
   ){
      StatisticsConfigurationPersistence persistence = new StatisticsConfigurationPersistence( configuration );
      return new ModelMarshaller( 
               persistence.structure(), 
               persistence.readHandles(), 
               persistence.writeHandles(), 
               locationProtocol 
      );
   }//End Method
   
   /**
    * Method to configure a {@link SessionManager} for the given {@link StatisticsConfiguration}.
    * @param configuration the {@link StatisticsConfiguration} to session.
    * @param session the {@link SessionManager} for controlling sessions.
    */
   private void configureSessionManager( StatisticsConfiguration configuration, SessionManager session ){
      session.triggerWriteOnChange( configuration.statisticBackgroundColourProperty() );
      session.triggerWriteOnChange( configuration.statisticTextColourProperty() );
      session.triggerWriteOnChange( configuration.statisticValueTextFontProperty() );
      session.triggerWriteOnChange( configuration.statisticDescriptionTextFontProperty() );
      session.triggerWriteOnChange( configuration.excludedJobs() );
   }//End Method
   
   /**
    * Method to shutdown the {@link SessionManager}s associated.
    */
   public void shutdownSessions(){
      sessions.stop();
   }//End Method
   
   /**
    * Method to determine whether this object uses the given {@link StatisticsConfiguration}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @return true if same as given.
    */
   public boolean uses( StatisticsConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   FileLocationProtocol configurationFileLocation(){
      return configurationFileLocation;
   }//End Method
   
}//End Class
