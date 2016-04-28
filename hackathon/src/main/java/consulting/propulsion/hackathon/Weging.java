package consulting.propulsion.hackathon;

import java.util.Map;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;

import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

/**
 * This is an empty service implementation illustrating how to use a plain Java 
 * class as a BPMN 2.0 Service Task delegate.
 */
public class Weging implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(Weging.class.getName());

	public void execute(DelegateExecution execution) throws Exception {

		Map<String, Object> variables = execution.getVariables();
		int weging = 1;
		String kanaal = (String) variables.get("kanaal");
		LOGGER.info("kanaal = " + kanaal);
		String adres = (String) variables.get("postcode") + (String) variables.get("huisnummer");
		if (kanaal.equals("afnemer")) {
			weging = 5;
		} else if (kanaal.equals("website")) {
			weging = 3;
		} else {
			weging = 1;
		}
		LOGGER.info("weging = " + weging);
		execution.setVariable("score", weging + getRelations(adres));

		LOGGER.info("\n\n  ... LoggerDelegate invoked by "
				+ "processDefinitionId=" + execution.getProcessDefinitionId()
				+ ", activtyId=" + execution.getCurrentActivityId()
				+ ", activtyName='" + execution.getCurrentActivityName() + "'"
				+ ", processInstanceId=" + execution.getProcessInstanceId()
				+ ", businessKey=" + execution.getProcessBusinessKey()
				+ ", score=" + execution.getVariable("score")
				+ " \n\n");

	}

	@SuppressWarnings("unchecked")
	int getRelations(String adres) {
		OrientGraphFactory factory = new OrientGraphFactory("remote:130.211.107.28:2424/testDB", "admin", "propulsionaanpakfraude").setupPool(1,10);

		int aantal = 0;
		OrientGraph graph = factory.getTx();
		try {
			  for (Vertex v : graph.getVertices("naam", adres)) {
				for (Edge e : v.getEdges(Direction.BOTH, null)){
					//System.out.println("- Result: " + e.getId() + e.getLabel());
					aantal++;
				}		
			}
		} finally {
			graph.shutdown();
		}
		return aantal;
	}

}
