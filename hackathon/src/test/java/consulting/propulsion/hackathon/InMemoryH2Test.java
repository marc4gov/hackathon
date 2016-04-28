package consulting.propulsion.hackathon;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;
import org.camunda.connect.httpclient.HttpResponse;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import client.StreamClientImpl;
import io.getstream.client.StreamClient;
import io.getstream.client.config.ClientConfiguration;
import io.getstream.client.exception.StreamClientException;
import io.getstream.client.model.activities.SimpleActivity;
import io.getstream.client.model.feeds.Feed;
import io.getstream.client.model.filters.FeedFilter;
import io.getstream.client.service.FlatActivityServiceImpl;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.consulting.process_test_coverage.ProcessTestCoverage;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class InMemoryH2Test {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  private static final String PROCESS_DEFINITION_KEY = "hackathon";
  private final Logger LOGGER = Logger.getLogger(StreamDelegate.class.getName());
  ClientConfiguration streamConfig = new ClientConfiguration();
  private StreamClient streamClient = new StreamClientImpl(streamConfig, "84dh574f6w8q", "mvnjntz889s8anqb7255nn7wzybyk7mwj4urtg7fqd9yhkmygcymsn5pm2mrm4hc");
 

  static {
    LogFactory.useSlf4jLogging(); // MyBatis
  }

  @Before
  public void setup() {
    init(rule.getProcessEngine());
  }

  /**
   * Just tests if the process definition is deployable.
   */
  @Test
  @Deployment(resources = "dynamo.bpmn")
  public void testParsingAndDeployment() {
    // nothing is done here, as we just want to check for exceptions during deployment
  }

  @Test
  @Deployment(resources = "dynamo.bpmn")
  public void testHappyPath() throws IOException, StreamClientException {
	  /*
	  HttpConnector http = Connectors.getConnector(HttpConnector.ID);
	  HttpResponse response = http.createRequest()
			  .get()
			  .url("https://overheid.io/api/kvk?filters[postcode]=2314bj&filters[huisnummer]=16&ovio-api-key=ef124ce947e6ea350363312bc9ee370c7543498cb50f579e5675d53ae2dfb0de")
			  .execute();
	  System.out.println("http call:" + response.getResponse());
	  JSONObject obj = new JSONObject(response.getResponse());
	  String dossiernr = obj.getJSONObject("_embedded").getJSONArray("rechtspersoon").getJSONObject(0).getString("dossiernummer");
	  System.out.println("JSON:" + dossiernr );
	  String bsn = "183507083";
	  System.out.println("JSON:" + bsn.hashCode() );
	  System.out.println("JSON:" + dossiernr.hashCode() );
	  
	  
	  
	  
		// Instantiate a feed object
	  Feed feed = streamClient.newFeed("flat", "Adres");

	  // Create an activity service
	  FlatActivityServiceImpl<SimpleActivity> flatActivityService = feed.newFlatActivityService(SimpleActivity.class);

	  // Add an activity to the feed, where actor, object and target are references to objects (`Eric`, `Hawaii`, `Places to Visit`)
	  SimpleActivity activity = new SimpleActivity();
	  activity.setActor("flat:Adres");
	  activity.setObject("Persoon:6");
	  activity.setVerb("staat ingeschreven");
	  activity.setTarget("Adres:2");
	  SimpleActivity response2 = flatActivityService.addActivity(activity);
	  
	    LOGGER.info("\n\n  ...\n "
	            + "response=" + response2.toString()
	            + " \n\n");	      
	    // Get activities from 5 to 10 (using offset pagination)
	  FeedFilter filter = new FeedFilter.Builder().build();
	  List<SimpleActivity> activities = flatActivityService.getActivities(filter).getResults();
	  for (int i=0; i<activities.size();i++){
		    LOGGER.info("\n\n  ...\n "
		            + "activity=" + activities.get(i).toString()
		            + " \n\n");	 
		  
	  }

      streamClient.shutdown();
      */
  
	  
	  //ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
	  
	  // Now: Drive the process by API and assert correct behavior by camunda-bpm-assert
	  
	  // To generate the coverage report for a single tests add this line as the last line of your test method:
	  //ProcessTestCoverage.calculate(processInstance, rule.getProcessEngine());
	  /*
	  OrientGraphFactory factory = new OrientGraphFactory("remote:130.211.107.28:2424/testDB", "admin", "propulsionaanpakfraude").setupPool(1,10);

	  // EVERY TIME YOU NEED A GRAPH INSTANCE
	  OrientGraph graph = factory.getTx();

		  try {
			  for (Vertex v : graph.getVertices("naam", "2222AA22")) {
				  	for (Edge e : v.getEdges(Direction.BOTH, null)){
				  		System.out.println("- Result: " + e.getId() + e.getLabel());
				  	}		
				}
			  
		} finally {
			   graph.shutdown();
		}
  */
		  
		  //Vertex v1 = graph.addVertex("class:Adres");
		  //v1.setProperty("naam", "2222AA22");

		  //Vertex v2 = graph.addVertex("class:Persoon");;
		  //v2.setProperty("naam", "2234235");
		  // Create an Edge from v1 to v2
		  //Edge e = graph.addEdge(null, v1, v2, "Friend");
		  //Edge e = v1.addEdge("Ingeschreven", v2);

	  
  }
  

  @After
  public void calculateCoverageForAllTests() throws Exception {
    ProcessTestCoverage.calculate(rule.getProcessEngine());
  }  

}
