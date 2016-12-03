/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import javafx.util.Pair;
import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundTriggerEvent;
import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link BuildResultStatusNotifier} is responsible for notifying changes to the
 * {@link uk.dangrew.jtt.model.jobs.BuildResultStatus} on {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
 */
public class BuildResultStatusNotifier {

   private final NotificationEvent notifiations;
   private final SoundTriggerEvent sounds;
   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link BuildResultStatusNotifier}.
    * @param database the {@link JenkinsDatabase} to monitor.
    */
   public BuildResultStatusNotifier( JenkinsDatabase database ) {
      this.database = database;
      
      this.notifiations = new NotificationEvent();
      this.sounds = new SoundTriggerEvent();
      this.database.jenkinsJobProperties().addBuildResultStatusListener( this::handleStatusChange );
   }//End Constructor
   
   /**
    * Method to handle the change in status.
    * @param job the {@link JenkinsJob} changed.
    * @param old the old build number and {@link BuildResultStatus}.
    * @param updated the updated build number and {@link BuildResultStatus}.
    */
   private void handleStatusChange( JenkinsJob job, Pair< Integer, BuildResultStatus > old, Pair< Integer, BuildResultStatus > updated ) {
      notifiations.fire( 
            new Event<>( new BuildResultStatusNotification( job, old.getValue(), updated.getValue() ) ) 
      );
      sounds.fire( new Event<>( new BuildResultStatusChange( old.getValue(), updated.getValue() ) ) );
   }//End Method
   
}//End Class
