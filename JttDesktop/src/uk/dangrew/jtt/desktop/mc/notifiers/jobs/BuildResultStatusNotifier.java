/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltEvent;
import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltResult;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundTriggerEvent;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.model.event.structure.Event;

/**
 * The {@link BuildResultStatusNotifier} is responsible for notifying events on receipt of
 * {@link JobBuiltEvent}s.
 */
public class BuildResultStatusNotifier {
   
   private final NotificationEvent notifiations;
   private final SoundTriggerEvent sounds;
   
   /**
    * Constructs a new {@link BuildResultStatusNotifier}.
    */
   public BuildResultStatusNotifier() {
      this.notifiations = new NotificationEvent();
      this.sounds = new SoundTriggerEvent();
      
      new JobBuiltEvent().register( this::handleStatusChange );
   }//End Constructor
   
   /**
    * Method to handle the change in status.
    * @param event the {@link Event} with the {@link JobBuiltResult}.
    */
   private void handleStatusChange( Event< JobBuiltResult > event ) {
      JobBuiltResult change = event.getValue();
      notifiations.fire( 
            new Event<>( new BuildResultStatusNotification( change.getJenkinsJob(), change.getPreviousStatus(), change.getCurrentStatus() ) ) 
      );
      sounds.fire( new Event<>( new BuildResultStatusChange( change.getPreviousStatus(), change.getCurrentStatus() ) ) );
   }//End Method

}//End Class
