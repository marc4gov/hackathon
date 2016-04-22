package consulting.propulsion.hackathon;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

	  Map<String, Object> variables = execution.getVariables();
	  
	  OrientGraphFactory factory = new OrientGraphFactory("remote:130.211.107.28:2424/testDB", "admin", "propulsionaanpakfraude").setupPool(1,10);

	  OrientGraph graph = factory.getTx();
	  try {
		  String adres = execution.getVariable("postcode").toString() + execution.getVariable("huisnummer").toString();
		  Vertex v1 = graph.getVertices("naam", adres).iterator().next();
		  if (v1 == null) {
			  v1 = graph.addVertex("class:Adres");
			  v1.setProperty("naam", adres);			  
		  }
		  HashMap<String, Object> map = getDBInput(execution.getVariables());
		  
		  String vertexclass = (String) map.get("klasse");

		  Vertex v2 = graph.addVertex(vertexclass);
		  v2.setProperty("naam", (String) map.get("naam"));
		  v2.setProperty("afzender", (String) map.get("afzender"));
		  
		  // Create an Edge from v1 to v2
		  //Edge e = graph.addEdge(null, v1, v2, "Friend");
		  Edge e = graph.addEdge(null, v1, v2, (String) map.get("link"));
		  e.setProperty("datetime", new Date());
		  
		  // extra relations
		  String vertexclass2 = (String) map.get("klasse2");
		  if (vertexclass2 != null) {
			  Vertex v3 = graph.addVertex(vertexclass2);
			  if (vertexclass2.equals("class:Persoon")) {
				  v3.setProperty("naam", (String) map.get("afzender"));
				  Edge e1 = graph.addEdge(null, v2, v3, (String) map.get("link"));
				  e1.setProperty("datetime", new Date());
				  
			  } else {
				  String adresNiew = execution.getVariable("postcodeNieuw").toString() 
						  + execution.getVariable("huisnummerNieuw").toString();
				  v3.setProperty("naam", adresNiew);
				  
				  Vertex v4 = graph.getVertices("naam", (String) map.get("afzender")).iterator().next();
				  if (v4 == null) {
					  v4 = graph.addVertex("class:Persoon");
					  v4.setProperty("naam", (String) map.get("afzender"));			  
				  }
				  Edge e2 = graph.addEdge(null, v3, v4, (String) map.get("link"));
				  e2.setProperty("datetime", new Date());
				  Edge e3 = graph.addEdge(null, v1, v4, "Uitgeschreven");
				  e3.setProperty("datetime", new Date());
			  }
		  }
		
	  } finally {
	     graph.shutdown();
	  }	  
  }
  
  HashMap<String, Object> getDBInput (Map<String, Object >variableMap) {
	
	HashMap<String, Object> result = new HashMap<String, Object>();
	
	String kanaal = (String) variableMap.get("kanaal");
	if (kanaal.equals("afnemer")) {
		String betreft = (String) variableMap.get("betreft");
		String afnemertype = (String) variableMap.get("afnemertype");
		result.put("link", "Heeft");
		if (betreft.equals("overtreding")){
			result.put("klasse", "class:Overtreding");
		} else {
			result.put("klasse", "class:Uitkering");
		}
		result.put("naam", betreft);
		result.put("afzender", afnemertype);
	} else if (kanaal.equals("website")) {
		String categorie = (String) variableMap.get("meldingscategorie");
		String afzender = (String) variableMap.get("afzender");
		result.put("link", "Gemeld");
		result.put("klasse", "class:Melding");
		result.put("naam", categorie);
		result.put("afzender", afzender);
	} else if (kanaal.equals("loket")) {
		String intentie = (String) variableMap.get("intentie");
		switch(intentie){
			case "inschrijving": result.put("link", "Ingeschreven");
			case "uitschrijving": {
				result.put("link", "Uitgeschreven");
			}
			case "geboorte": { 
				result.put("link", "Geboorte");
				result.put("klasse2", "class:Persoon");
			}
			default: {
				result.put("link", "Ingeschreven");
				result.put("klasse2", "class:Adres");
			}
		}
		result.put("afzender", variableMap.get("bsn").hashCode());
		result.put("klasse", "class:Melding");
		result.put("naam", intentie);

	} else {
		result.put("klasse", "class:Melding");
		result.put("naam", "overig");
	}
	return result;
  }

}
