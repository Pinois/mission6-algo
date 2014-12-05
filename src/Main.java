import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Graph;
import net.datastructures.GraphAlgorithms;
import net.datastructures.Vertex;


public class Main {
	public static void main(String[] args) {
		// création et ouverture du fichier en lecture
		Fichier fichierR = new Fichier();
		fichierR.ouvrir("cities_small.txt", 'R');
		String ligneInput = null;
		Map<String, Vertex<String>> vertices = new HashMap<String, Vertex<String>>();
		Graph<String, Integer> graph = new AdjacencyMapGraph<String, Integer>(false);
		
		// Création du graph en lisant ligne par ligne le fichier
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
		
		// Exemple d'utilisation de l'algorithme de dijkstra pour calculer la distance entre une ville de départ et toutes les autres
		Vertex<String> villeDepart = vertices.get("12");
		// Algorithme de Dijkstra
		net.datastructures.Map<Vertex<String>, Integer> verticesDijkstra = GraphAlgorithms.shortestPathLengths(graph, villeDepart);
		for (Vertex<String> villeArrivee : verticesDijkstra.keySet()) {
			int coutVoyage = verticesDijkstra.get(villeArrivee);
			System.out.println(villeDepart.getElement() +" ==( "+ coutVoyage +" )==> "+ villeArrivee.getElement());
		}
	}
}
