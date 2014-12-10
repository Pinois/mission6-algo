import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Vertex;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


public class Main {
	public static void main(String[] args) {
		// cr�ation et ouverture du fichier en lecture
		Fichier fichierR = new Fichier();
		fichierR.ouvrir(args[0], 'R');
		String ligneInput = null;
		Map<String, Vertex<String>> vertices = new HashMap<String, Vertex<String>>();
		Graph<String, Integer> graph = new AdjacencyMapGraph<String, Integer>(false);

		// Cr�ation du graph en lisant ligne par ligne le fichier
		while((ligneInput = fichierR.lire()) != null){
			String[] champs = ligneInput.split("\t");
			if(champs.length != 3)
				continue;
			Vertex<String> u; 
			if((u = vertices.get(champs[0])) == null){
				u = graph.insertVertex(champs[0]);
				vertices.put(champs[0], u);
			}
			Vertex<String> v; 
			if((v = vertices.get(champs[1])) == null){
				v = graph.insertVertex(champs[1]);
				vertices.put(champs[1], v);
			}
			graph.insertEdge(u, v, Integer.parseInt(champs[2]));
		}

		// Exemple d'utilisation de l'algorithme de dijkstra pour calculer la distance entre une ville de d�part et toutes les autres
		// Algorithme de Dijkstra
		net.datastructures.PositionalList<Edge<Integer>> modifiedGraph = GraphAlgorithms.MST(graph);
		Fichier fichierW = new Fichier();
		fichierW.ouvrir(args[1], 'W');
		SparseMultigraph<Integer, String> g = new SparseMultigraph<Integer, String>();
		System.out.println("Ville 1 - Ville 2 - Poids");
		for (Edge<Integer> edge : modifiedGraph) {
			int vertex1 = Integer.valueOf((String)edge.getVertex()[0].getElement());
			int vertex2 = Integer.valueOf((String)edge.getVertex()[1].getElement());
			g.addVertex(vertex1);
			g.addVertex(vertex2);
			g.addEdge(vertex1+":"+vertex2+":"+edge.getElement(), vertex1, vertex2);
			if(Integer.parseInt((String) edge.getVertex()[0].getElement()) < 10){
				fichierW.ecrire(edge.getVertex()[0].getElement()+"   "+edge.getVertex()[1].getElement()+"  "+edge.getElement());
				System.out.println("   "+edge.getVertex()[0].getElement()+"         "+edge.getVertex()[1].getElement()+"      "+edge.getElement());
			}
			else{
				System.out.println("   "+edge.getVertex()[0].getElement()+"        "+edge.getVertex()[1].getElement()+"      "+edge.getElement());
				fichierW.ecrire(edge.getVertex()[0].getElement()+"  "+edge.getVertex()[1].getElement()+"  "+edge.getElement());
			}
		}
		fichierW.fermer();
		Layout<Integer, String> layout = new ISOMLayout(g);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		layout.setSize(new Dimension(width-100,height-100));
		
		BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(width,height)); //Sets the viewing area size
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<String, String>() {
            @Override
            public String transform(String c) {
                return c.split(":")[2];
            }
        });
        
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv); 
        frame.pack();
        frame.setVisible(true);
	}
}
