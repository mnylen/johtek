import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Main {
	
	static Evaluator ce; // current evaluator
	static Evaluator oe; // our evaluator
	static Evaluator ye; // your evaluator
	
	
	class PositionComparator implements Comparator<Position> {
		public int compare(Position p1, Position p2) {
			return (int)(ce.eval(p1) - ce.eval(p2));
		}
	}
	
	static double alphabeta(Position p, int depth, double alpha, double beta, int player) {
		// 0 tries to maximize, 1 tries to minimize
	    if (p.winner == -1) return -1E10-depth; // prefer to win sooner
	    if (p.winner == +1) return +1E10+depth; // and lose later
				    
		if(depth == 0) return ce.eval(p);
		Vector<Position> P = p.getNextPositions();
		Collections.sort(P, (new Main()).new PositionComparator());
		if(player == 0) Collections.reverse(P);
		
		//System.out.println("Pelaaja: "+player+" koko: "+P.size());
		//System.out.println("Ensimmäinen: "+eval(P.elementAt(0)));
		//System.out.println("Viimeinen: "+eval(P.lastElement()));
		
		if(player == 0) {
			for(int i = 0; i < P.size(); ++i) {
				alpha = Math.max(alpha, alphabeta(P.elementAt(i),depth-1,alpha,beta,1));
				if(beta <= alpha) break;
			}
			return alpha;
		}
		
		for(int i = 0; i < P.size(); ++i) {
			beta = Math.min(beta,alphabeta(P.elementAt(i),depth-1,alpha,beta,0));
			if(beta <= alpha) break;
		}
		
		return beta;
	}
	
	static double minmax(Position p, int depth, int player) {
		double alpha = -Double.MAX_VALUE, beta = Double.MAX_VALUE;
		return alphabeta(p,depth,alpha,beta,player);
		/*
		//return (p.whiteToMove ? -1 : 1) * eval(p);
		if(depth <= 0) return eval(p);
		
		double val = eval(p);
		if(Math.abs(val) > 1e8) return val; // prevent king exchange :/
		
		double alpha = p.whiteToMove ? 1e12 : -1e12;
		
		Vector<Position> P = p.getNextPositions();
		
		for(int i = 0; i < P.size(); ++i) {
			if(p.whiteToMove) {
				alpha = Math.min(alpha, minmax(P.elementAt(i),depth-1));
			} else alpha = Math.max(alpha, minmax(P.elementAt(i),depth-1));
		}
		
		return alpha;
		*/
	}
	
	public static void main(String[] args) {
		// you get the white pieces, we play the black pieces
		oe = new OurEvaluator();
		ye = new YourEvaluator();
		//ye = new OurEvaluator();
		
		int depth = 5;
		
		Position p = new Position();
		p.setStartingPosition();
		p.print();
		
		System.out.println("Eval: "+oe.eval(p));
		
		long ms = System.currentTimeMillis();
		
		while(true) {
			ce = oe;
			//System.out.println("Minmax: "+minmax(p,3,(p.whiteToMove?1:0)));
			ce = ye;
			Vector<Position> P = p.getNextPositions();
			
			double evaluation = (new OurEvaluator()).eval(p);
			
			if(p.winner == +1) {
				System.out.println("White won.");
				break;
			} 
			
			if(p.winner == -1) {
				System.out.println("Black won.");
				break;
			}
			
			if(P.size() == 0) {
				System.out.println("No more available moves");
				break;
			}
			
			Position bestPosition = new Position();
			if(p.whiteToMove) {
				ce = ye;
				double max = -1./0.;
				for(int i = 0; i < P.size(); ++i) {
					double val = minmax(P.elementAt(i),depth,1);
					if(max < val) {
						bestPosition = P.elementAt(i);
						max = val;
					}
				}
			} else {
				ce = oe;
				double min = 1./0.;
				for(int i = 0; i < P.size(); ++i) {
					double val = minmax(P.elementAt(i),depth,0);
					if(min > val) {
						bestPosition = P.elementAt(i);
						min = val;
					}
				}
			}
			
			assert p.whiteToMove != bestPosition.whiteToMove;
			p = bestPosition;
			p.print();
			
			long curtime = System.currentTimeMillis();
			System.out.println("Move took "+((curtime-ms)/1000.0)+" seconds");
			ms = curtime;
		}
		
		/*
		for(int i = 0; i < 60; ++i) {
			System.out.println("----------------");
			Vector<Position> P = p.getNextPositions();
			p = P.elementAt((int)(Math.random()*P.size()));
			p.print();
			System.out.println("Eval: "+eval(p));
		}
		*/
	}
}
