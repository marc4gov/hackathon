package consulting.propulsion.hackathon;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdgeType;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import static org.junit.Assert.*;

import org.camunda.bpm.consulting.process_test_coverage.ProcessTestCoverage;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
public class InMemoryH2Test {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  private static final String PROCESS_DEFINITION_KEY = "hackathon";

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
  @Deployment(resources = "process.bpmn")
  public void testParsingAndDeployment() {
    // nothing is done here, as we just want to check for exceptions during deployment
  }

  @Test
  @Deployment(resources = "process.bpmn")
  public void testHappyPath() {
	  //ProcessInstance processInstance = processEngine().getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_KEY);
	  
	  // Now: Drive the process by API and assert correct behavior by camunda-bpm-assert
	  
	  // To generate the coverage report for a single tests add this line as the last line of your test method:
	  //ProcessTestCoverage.calculate(processInstance, rule.getProcessEngine());
	  
	  OrientGraphFactory factory = new OrientGraphFactory("remote:192.168.99.100:32781/testDB", "admin", "admin").setupPool(1,10);

	  // EVERY TIME YOU NEED A GRAPH INSTANCE
	  OrientGraph graph = factory.getTx();
	  try {
		  //Create vertex
		 
		  //OrientVertexType customer = graph.getVertexType("Customer");
		  //OrientEdgeType friendsEdge = graph.getEdgeType("Friend");
		  Vertex v1 = graph.addVertex("class:Customer");
		  v1.setProperty("name", "Blabla3");

		  Vertex v2 = graph.addVertex("class:Customer");;
		  v2.setProperty("name", "Duh3");
		  // Create an Edge from v1 to v2
		  //Edge e = graph.addEdge(null, v1, v2, "Friend");
		  Edge e = v1.addEdge("Friend", v2);
		
		  Iterable<Vertex> allVertices = graph.getVertices();
		  for (Vertex v : allVertices) {
			    System.out.println(v.toString());
		  }
	  } finally {
	     graph.shutdown();
	  }
  }

  @After
  public void calculateCoverageForAllTests() throws Exception {
    ProcessTestCoverage.calculate(rule.getProcessEngine());
  }  

}
