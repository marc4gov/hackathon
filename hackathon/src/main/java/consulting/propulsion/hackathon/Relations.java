package consulting.propulsion.hackathon;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * This is an empty service implementation illustrating how to use a plain Java 
 * class as a BPMN 2.0 Service Task delegate.
 */
public class Relations implements JavaDelegate {
 
  private final Logger LOGGER = Logger.getLogger(Relations.class.getName());
  
  public void execute(DelegateExecution execution) throws Exception {
    
	  //OrientGraph graph = new OrientGraph("remote:192.168.99.100:32780/testDB");
	// AT THE BEGINNING
	  //OrientGraphFactory factory = new OrientGraphFactory("plocal:C:/temp/graph/db").setupPool(1,10);
	  OrientGraphFactory factory = new OrientGraphFactory("remote:192.168.99.100:32780/testDB").setupPool(1,10);

	  // EVERY TIME YOU NEED A GRAPH INSTANCE
	  OrientGraph graph = factory.getTx();
	  try {
		  //Create vertex
		  Vertex v1 = graph.addVertex(null);
		  // Add property
		  v1.setProperty("name", "v1");

		  Vertex v2 = graph.addVertex(null);;

		  // Create an Edge from v1 to v2
		  Edge e = graph.addEdge(null, v1, v2, "relationship");

	  } finally {
	     graph.shutdown();
	  }
    LOGGER.info("\n\n  ... LoggerDelegate invoked by "
            + "processDefinitionId=" + execution.getProcessDefinitionId()
            + ", activtyId=" + execution.getCurrentActivityId()
            + ", activtyName='" + execution.getCurrentActivityName() + "'"
            + ", processInstanceId=" + execution.getProcessInstanceId()
            + ", businessKey=" + execution.getProcessBusinessKey()
            + ", executionId=" + execution.getId()
            + " \n\n");
    
  }

}
