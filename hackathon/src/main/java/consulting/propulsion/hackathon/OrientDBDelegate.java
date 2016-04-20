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
public class OrientDBDelegate implements JavaDelegate {
 
  private final Logger LOGGER = Logger.getLogger(OrientDBDelegate.class.getName());
  
  public void execute(DelegateExecution execution) throws Exception {
	  OrientGraphFactory factory = new OrientGraphFactory("remote:192.168.99.100:32772/testDB", "admin", "admin").setupPool(1,10);

	  // EVERY TIME YOU NEED A GRAPH INSTANCE
	  OrientGraph graph = factory.getTx();
	  try {
		  //Create vertex

		  Vertex v1 = graph.addVertex("class:Adres");
		  v1.setProperty("name", execution.getVariable("postcode").toString() + execution.getVariable("huisnummer").toString() );

		  Vertex v2 = graph.addVertex("class:Persoon");;
		  v2.setProperty("name", execution.getVariable("bsn").toString().hashCode());
		  // Create an Edge from v1 to v2
		  //Edge e = graph.addEdge(null, v1, v2, "Friend");
		  Edge e = v1.addEdge("Ingeschreven", v2);
		
		  Iterable<Vertex> allVertices = graph.getVertices();
		  for (Vertex v : allVertices) {
			    System.out.println(v.toString());
		  }
	  } finally {
	     graph.shutdown();
	  }	  
    
  }

}
