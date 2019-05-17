import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;

public class AstarSearchAlgo{
	
		private ArrayList<Blocks> gameGrid ;
		private MapKnowledge[][] MapKB ;
		private int GoalRow = 0 ;
		private int GoalCol = 0 ;
		private Node[][] nodes;
		private List<Node> SolutionPath ;
		public static boolean PathSolutionGenerated = false ;
		
		public AstarSearchAlgo(int gRow , int gCol)
		{
			
			GoalRow = gRow;
			GoalCol = gCol;
			nodes = new Node[30][30] ;
		}
		
		public void run()
		{
			for(int r = 0 ; r < 29 ; r++)
			{
				for(int c = 0 ; c < 29 ; c++)
				{
					if(MapKB[r][c].isBlocked() == true)
					{
						System.out.println("BLOCKED WALL");
						nodes[r][c] = new Node(r, c, "r" + r + "c" + c, 10000000, MapKB[r][c].isBlocked()) ;	
					} else
					{
						nodes[r][c] = new Node(r, c, "r" + r + "c" + c, Math.hypot(GoalRow - r,GoalCol - c ), MapKB[r][c].isBlocked()) ;
						System.out.println("Heuristic value " + Math.hypot(GoalRow - r,GoalCol - c ));
					}
					
				}
			}
			
			
			
			
/**			for(int c = 0 ; c < 29 ; c++)
			{
					nodes[0][c].adjacencies = new Edge[] {
					new Edge(nodes[0][c + 1],MapKB[0][0 +1].getScoreImpact() ),
					new Edge(nodes[1][c],MapKB[1][c].getScoreImpact() ) };
			}
			
			for(int r = 1 ; r < 29 ; r++)
			{
					nodes[r][0].adjacencies = new Edge[] {
					new Edge(nodes[r + 1][0],MapKB[r + 1][0].getScoreImpact() ),
					new Edge(nodes[r][1],MapKB[r][1].getScoreImpact() ) };
			}
			**/
			
			for(int r = 0 ; r < 28 ; r++)
			{
				for(int c = 0 ; c < 28 ; c++)
				{
					nodes[r][c].adjacencies = new Edge[] {
					new Edge(nodes[r + 1][c],-MapKB[r + 1][c].getScoreImpact() ),
					new Edge(nodes[r][c + 1],-MapKB[r][c + 1].getScoreImpact() ) };
				}
			}
			
			showEdges();
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            AstarSearch(nodes[0][0],nodes[26][28]); //n1 to n13 (goal node)

            List<Node> path = printPath(nodes[26][28]);
            SolutionPath = path ;

           System.out.println("Path: " + path);
		}
		
		private void showEdges()
		{
			for(int r = 0 ; r < 28 ; r++)
			{
				for(int c = 0 ; c < 28 ; c++)
				{
					for(int e = 0 ;e < nodes[r][c].adjacencies.length; e++)
					{
						if(nodes[r][c].adjacencies[e].target.value == null)
						{
							System.out.println("PROBLEM HERE");
						} else
						{

							System.out.println("r" + r + "c" + c + "Edge " + e + " value " + nodes[r][c].adjacencies[e].target.value + " cost " + nodes[r][c].adjacencies[e].cost);
						}
					}
				}
			}
		}

        public static List<Node> printPath(Node target){
                List<Node> path = new ArrayList<Node>();
        
		        for(Node node = target; node!=null; node = node.parent){
		            path.add(node);
		        }

		        Collections.reverse(path);
		       
		        setPathSolutionGenerated(true);
		        return path;
        }

        public static void AstarSearch(Node source, Node goal){

                Set<Node> explored = new HashSet<Node>();

                PriorityQueue<Node> queue = new PriorityQueue<Node>(20, 
                        new Comparator<Node>(){
                                 //override compare method
                 public int compare(Node i, Node j){
                    if(i.f_scores > j.f_scores){
                        return 1;
                    }

                    else if (i.f_scores < j.f_scores){
                        return -1;
                    }

                    else{
                        return 0;
                    }
                 }

                 }
                );

                //cost from start
                source.g_scores = 0;

                queue.add(source);

                boolean found = false;

                while((!queue.isEmpty())&&(!found)){

                        //the node in having the lowest f_score value
                        Node current = queue.poll();

                        explored.add(current);

                        //goal found
                        if(current.value.equals(goal.value)){
                        		System.out.println("FOUND");
                                found = true;
                        }else {
                            //check every child of current node
                            
                            
                            for(Edge e : current.adjacencies){
                            	if(e != null){
                            		if(e.target.value != null) {
                            			System.out.println("Adj edge " + e.target.value);
                            		}else {
                            			System.out.println("Adj edge NULL POINTER IS HERE");
                            		}
                            		
                                    Node child = e.target;
                                    double cost = e.cost;
                                    double temp_g_scores = current.g_scores + cost;
                                    double temp_f_scores = temp_g_scores + child.h_scores;


                                    /*if child node has been evaluated and 
                                    the newer f_score is higher, skip*/
                                    
                                    if((explored.contains(child)) && 
                                            (temp_f_scores >= child.f_scores)){
                                            continue;
                                    }

                                    /*else if child node is not in queue or 
                                    newer f_score is lower*/
                                    
                                    else if((!queue.contains(child)) || (temp_f_scores < child.f_scores)){

                                            child.parent = current;
                                            child.g_scores = temp_g_scores;
                                            child.f_scores = temp_f_scores;

                                            if(queue.contains(child)){
                                                    queue.remove(child);
                                            }

                                            queue.add(child);

                                    }
                            	}

                            }
                        }



                }

        }

		public MapKnowledge[][] getMapKB() {
			return MapKB;
		}

		public void setMapKB(MapKnowledge[][] mapKB) {
			MapKB = mapKB;
		}

		public int getGoalRow() {
			return GoalRow;
		}

		public void setGoalRow(int goalRow) {
			GoalRow = goalRow;
		}

		public int getGoalCol() {
			return GoalCol;
		}

		public void setGoalCol(int goalCol) {
			GoalCol = goalCol;
		}

		public List<Node> getSolutionPath() {
			return SolutionPath;
		}

		public void setSolutionPath(List<Node> solutionPath) {
			SolutionPath = solutionPath;
		}

		public boolean isPathSolutionGenerated() {
			return PathSolutionGenerated;
		}

		public static void setPathSolutionGenerated(boolean pathSolutionGenerated) {
			PathSolutionGenerated = pathSolutionGenerated;
		}
		
		
}

/**

        //h scores is the stright-line distance from the current city to Bucharest
        public static void main(String[] args){

                //initialize the graph base on the Romania map
                Node n1 = new Node("Arad",366);
                Node n2 = new Node("Zerind",374);
                Node n3 = new Node("Oradea",380);
                Node n4 = new Node("Sibiu",253);
                Node n5 = new Node("Fagaras",178);
                Node n6 = new Node("Rimnicu Vilcea",193);
                Node n7 = new Node("Pitesti",98);
                Node n8 = new Node("Timisoara",329);
                Node n9 = new Node("Lugoj",244);
                Node n10 = new Node("Mehadia",241);
                Node n11 = new Node("Drobeta",242);
                Node n12 = new Node("Craiova",160);
                Node n13 = new Node("Bucharest",0);
                Node n14 = new Node("Giurgiu",77);
 
                //initialize the edges

                //Arad
                n1.adjacencies = new Edge[]{
                        new Edge(n2,75),
                        new Edge(n4,140),
                        new Edge(n8,118)
                };
                 
                 //Zerind
                n2.adjacencies = new Edge[]{
                        new Edge(n1,75),
                        new Edge(n3,71)
                };
                 

                 //Oradea
                n3.adjacencies = new Edge[]{
                        new Edge(n2,71),
                        new Edge(n4,151)
                };
                 
                 //Sibiu
                n4.adjacencies = new Edge[]{
                        new Edge(n1,140),
                        new Edge(n5,99),
                        new Edge(n3,151),
                        new Edge(n6,80),
                };
                 

                 //Fagaras
                n5.adjacencies = new Edge[]{
                        new Edge(n4,99),

                        //178
                        new Edge(n13,211)
                };
                 
                 //Rimnicu Vilcea
                n6.adjacencies = new Edge[]{
                        new Edge(n4,80),
                        new Edge(n7,97),
                        new Edge(n12,146)
                };
                 
                 //Pitesti
                n7.adjacencies = new Edge[]{
                        new Edge(n6,97),
                        new Edge(n13,101),
                        new Edge(n12,138)
                };
                 
                 //Timisoara
                n8.adjacencies = new Edge[]{
                        new Edge(n1,118),
                        new Edge(n9,111)
                };
                 
                 //Lugoj
                n9.adjacencies = new Edge[]{
                        new Edge(n8,111),
                        new Edge(n10,70)
                };

                 //Mehadia
                n10.adjacencies = new Edge[]{
                        new Edge(n9,70),
                        new Edge(n11,75)
                };
                 
                 //Drobeta
                n11.adjacencies = new Edge[]{
                        new Edge(n10,75),
                        new Edge(n12,120)
                };

                 //Craiova
                n12.adjacencies = new Edge[]{
                        new Edge(n11,120),
                        new Edge(n6,146),
                        new Edge(n7,138)
                };

                //Bucharest
                n13.adjacencies = new Edge[]{
                        new Edge(n7,101),
                        new Edge(n14,90),
                        new Edge(n5,211)
                };
                 
                 //Giurgiu
                n14.adjacencies = new Edge[]{
                        new Edge(n13,90)
                };

                AstarSearch(n1,n13);

                List<Node> path = printPath(n13);

                        System.out.println("Path: " + path);


        }
**/