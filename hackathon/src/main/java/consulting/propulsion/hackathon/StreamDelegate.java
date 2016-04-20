package consulting.propulsion.hackathon;

import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import client.StreamClientImpl;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.FlatActivityServiceImpl;

/**
 * This is an empty service implementation illustrating how to use a plain Java 
 * class as a BPMN 2.0 Service Task delegate.
 */
public class StreamDelegate implements JavaDelegate {
 
  private final Logger LOGGER = Logger.getLogger(StreamDelegate.class.getName());
  ClientConfiguration streamConfig = new ClientConfiguration();
  private StreamClient streamClient = new StreamClientImpl(streamConfig, "84dh574f6w8q", "mvnjntz889s8anqb7255nn7wzybyk7mwj4urtg7fqd9yhkmygcymsn5pm2mrm4hc");
  
  public void execute(DelegateExecution execution) throws Exception {
	// Instantiate a feed object
	  Feed feed = streamClient.newFeed("user", "gemeente");

	  // Create an activity service
	  FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);

	  // Add an activity to the feed, where actor, object and target are references to objects (`Eric`, `Hawaii`, `Places to Visit`)
	  SimpleActivity activity = new SimpleActivity();
	  activity.setActor("user:gemeente");
	  activity.setObject("Persoon:5");
	  activity.setVerb("schrijft in");
	  activity.setTarget("Adres:1");
	  SimpleActivity response = flatActivityService.addActivity(activity);
	  
	    LOGGER.info("\n\n  ...\n "
	            + "response=" + response.toString()
	            + " \n\n");	      
	    // Get activities from 5 to 10 (using offset pagination)
	  FeedFilter filter = new FeedFilter.Builder().build();
	  List<SimpleActivity> activities = flatActivityService.getActivities(filter).getResults();
	  for (int i=0; i<activities.size();i++){
		    LOGGER.info("\n\n  ...\n "
		            + "activity=" + activities.get(i).toString()
		            + " \n\n");	 
		  
	  }
      /**
       * Shutdown the client.
       */
      streamClient.shutdown();

  }
}
