import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.ImageIcon;
@SuppressWarnings("unused")
public class PieceListener implements MouseListener {
	private HashMap<Integer, ArrayList<Integer>> possibleMoves;
	private Tile[] tile = new Tile[32];
	private ArrayList<Integer> tileIdKing; 
	private ArrayList<Integer> evilTileId;
	private ArrayList<Integer> leftTiles,rightTiles, topTiles, bottomTiles;
	private static int turn;
	private static final int GREEN = 1;
	private static final int WHITE = 0;
	private static final int WHITEICON = 0;
	private static final int GREENICON = 1;
	private static final int TRANSPARENT_WHITEICON = 2;
	private static final int TRANSPARENT_GREENICON = 3;
	private static int whitePiecesCount = 12;
	private static int greenPiecesCount = 12;
	private static boolean isChangeTurns = true;
	private boolean canDoubleEat = false;
	private int eater = -1;
	private boolean flagTurn = false;
	
	public PieceListener() {
		this.tile = Board.tile;
		
		init();
	}
	
	private void init() {
		tileIdKing = new ArrayList<Integer>(Arrays.asList(0,1,2,3,20,21,22,23));
		evilTileId = new ArrayList<Integer>(Arrays.asList(0,1,2,3,20,21,22,23,11,4,12,31,24,19));
		leftTiles = new ArrayList<Integer>(Arrays.asList(12,4,24,20));
		rightTiles = new ArrayList<Integer>(Arrays.asList(11,19,31,3));
		topTiles = new ArrayList<Integer>(Arrays.asList(0,1,2,3));
		bottomTiles = new ArrayList<Integer>(Arrays.asList(20,21,22,23));
		turn = 0;
	}
	private void showPossibleMoves(int i) {
		mapPossibleMoves();
		ArrayList<Integer> moves = possibleMoves.get(i);
		if(!tile[i].getTilePiece().getIsKing()) {
			moves = ordPiecesPossibleMoves(tile[i].getTilePiece().getCol(), moves, i);
		}
		moves = nonOccupiedPossibleMoves(moves);
		

		int col = tile[i].getTilePiece().getCol() == GREENICON ? TRANSPARENT_GREENICON : TRANSPARENT_WHITEICON;
		ImageIcon icon = tile[i].getTilePiece().getIcon(col);
		
		for(int j = 0;j<moves.size();j++) {
			Integer elem = moves.get(j);
			
			tile[elem].setPreTile(i);
			tile[elem].getTilePiece().setCol(col);
			tile[elem].getTilePiece().setPieceIcon(icon);
			tile[elem].getTilePiece().setPieceVisibility(true);
			tile[elem].getTilePiece().setVisible(true);
			tile[elem].getTilePiece().setEnabled(true);
			tile[elem].getTilePiece().addMouseListener(this);
			
			tile[elem].setAdjTiles(moves);
		}
		if(moves.size() != 0)
			disableMouseListener(moves);
	}
	
	private void disableMouseListener(ArrayList<Integer> moves) {
		
		for(int i = 0;i<32;i++) {
			if(!moves.contains(i)) {
				tile[i].getTilePiece().removeMouseListener(this);
			}
		}
	}
	
	private ArrayList<Integer> ordPiecesPossibleMoves(int color, ArrayList<Integer> moves, int tileId) {
		int col = color;
			if(moves.size() == 4) {
				if(turn == GREEN) {
					moves.remove(3);
					moves.remove(2);
				}else if (turn == WHITE) {
					moves.remove(0);
					moves.remove(0);
				}
				
			}else if(moves.size() == 2) {
				int[] exception = {0,1,2,21,22,23};
				boolean proceed = true;
				for(int a = 0;a<exception.length;a++) {
					if(tileId == exception[a]) {proceed = false; break;}
				}
				
				if(proceed) {
					if(col == GREENICON) {
						moves.remove(1);
					}else if (col == WHITEICON) {
						moves.remove(0);
					}
				}
			}
		
		return moves;
	}
	
	private ArrayList<Integer> nonOccupiedPossibleMoves(ArrayList<Integer> moves){
		int len = moves.size();
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i = 0;i<len;i++) {
			Integer elem = moves.get(i);
			if(!tile[(int) elem].isOccupied()) {
				list.add(elem);
			}else System.out.println(elem+" is occupied.");
		}
		
		return list;
	}
	
	private void removeAdjTile(ArrayList<Integer> list) {
		for(int i = 0;i<list.size();i++) {
			
			Integer elem = list.get(i);
			
			
			tile[elem].setPreTile(-1);
		
			tile[elem].getTilePiece().setPieceVisibility(false);
			tile[elem].getTilePiece().setVisible(false);
			tile[elem].getTilePiece().setEnabled(false);
			tile[elem].getTilePiece().removeMouseListener(this);
			tile[elem].getTilePiece().setTileEaten(-1);
			
		}
		
		
	}
	
	private boolean removePredecessorTile(int tileId) {
		
		int col = tile[tileId].getTilePiece().getCol() == GREENICON ? TRANSPARENT_GREENICON : TRANSPARENT_WHITEICON;
		ImageIcon icon = tile[tileId].getTilePiece().getIcon(col);
		
		tile[tileId].setPreTile(-1);
		tile[tileId].getTilePiece().setPieceIcon(icon);
		tile[tileId].getTilePiece().setCol(col);
		tile[tileId].getTilePiece().setPieceVisibility(false);
		tile[tileId].getTilePiece().setVisible(false);
		tile[tileId].getTilePiece().setEnabled(false);
		tile[tileId].getTilePiece().removeMouseListener(this);
		
		return tile[tileId].getTilePiece().getIsKing();
	}
	
	private void turnToRealPiece(int tileId, boolean isKing) {
		int col = tile[tileId].getTilePiece().getCol() == TRANSPARENT_GREENICON ? GREENICON : WHITEICON; 
		ImageIcon icon = tile[tileId].getTilePiece().getIcon(col);
		
		tile[tileId].getTilePiece().setCol(col);
		tile[tileId].getTilePiece().setIcon(icon);
		tile[tileId].getTilePiece().setTileEaten(-1);
		
		determineIfKing(tileId, isKing,col);
		
	}
	
	private void determineIfKing(int tileId, boolean isKing, int col) {
		boolean king = false;
		if(!tile[tileId].getTilePiece().getIsKing()) {
			for(int i = 0;i<tileIdKing.size();i++) {
				if((tileId == tileIdKing.get(i)) && (tileIdKing.get(i) <4) && (col == WHITEICON)) {
					tile[tileId].getTilePiece().setIsKing(true);
					king = true;
				}else if((tileId == tileIdKing.get(i)) && (tileIdKing.get(i) >19 && tileIdKing.get(i) < 24) && (col == GREENICON)) {
					tile[tileId].getTilePiece().setIsKing(true);
					king = true;
				}
			}
			
		}
		
		if(king) {System.out.println("King "+tileId);}else {
			tile[tileId].getTilePiece().setIsKing(isKing);
			
		}
	}
	private void changeTurns() {
			if(turn == WHITE)
				turn = GREEN;
			else
				turn = WHITE;
	}
	
	private void enableMouseListener() {
		if(turn == GREEN) {
			for(int i = 0;i<32;i++) {
				if(tile[i].getTilePiece().getCol() == GREENICON) {
					tile[i].getTilePiece().addMouseListener(this);
				}else {
					tile[i].getTilePiece().removeMouseListener(this);
				}
			}
		}else {
			for(int i = 0;i<32;i++) {
				if(tile[i].getTilePiece().getCol() == WHITEICON) {
					tile[i].getTilePiece().addMouseListener(this);
				}else {
					tile[i].getTilePiece().removeMouseListener(this);
				}
			}
		}
	}
	
	private ArrayList<Eat> howToCheckIfPieceCanEat(int col, int i) {
		ArrayList<Eat> eatList = new ArrayList<Eat>();
		
		ArrayList<Integer> moveList = possibleMoves.get(i);
		if(!tile[i].getTilePiece().getIsKing())
			moveList = ordPiecesPossibleMoves(tile[i].getTilePiece().getCol(),moveList,i);
		//System.out.println(i+" i: "+moveList);
		
		for(int j = 0;j<moveList.size();j++) {
			if(tile[moveList.get(j)].getTilePiece().getCol() == col && tile[moveList.get(j)].isOccupied()) {
					ArrayList<Integer> moveList2 = possibleMoves.get(moveList.get(j));
					//System.out.println(moveList.get(j)+" before: "+moveList2);
					if(!tile[i].getTilePiece().getIsKing()) {
						moveList2 = ordPiecesPossibleMoves(tile[moveList.get(j)].getTilePiece().getCol(),moveList2,moveList.get(j));
					}else if(tile[i].getTilePiece().getIsKing() && (evilTileId.contains(i)&& turn ==GREEN)) {
						moveList2.remove(0);
						moveList2.remove(0);
					}else if(tile[i].getTilePiece().getIsKing() && (evilTileId.contains(i)&& turn ==WHITE)) {
						if(moveList2.size() == 4) {
							moveList2.remove(3);
							moveList2.remove(2);
						}
					}
					
					//System.out.println(moveList.get(j)+" j: "+moveList2);
					
					if(!(moveList2.size() == 1) ) {
						//System.out.println("yo");
						if(!evilTileId.contains(moveList.get(j))) {
							//System.out.println("yo1");
							if(!tile[moveList2.get(j)].isOccupied() && moveList.size() > 1) {
								//System.out.println("yo2");
								eatList.add(new Eat(i, moveList.get(j), moveList2.get(j)));
							}else if(moveList.size() == 1)  {
							//	System.out.println("yo3");
								if(leftTiles.contains((Integer)i) && !tile[moveList2.get(j+1)].isOccupied()) {
									//System.out.println("yo4");
									eatList.add(new Eat(i, moveList.get(j), moveList2.get(j+1)));
								}else if(rightTiles.contains((Integer)i) && !tile[moveList2.get(j)].isOccupied()) {
									//System.out.println("yo5");
									eatList.add(new Eat(i, moveList.get(j), moveList2.get(j)));
								}
								
							}
						}
					}
			}
		}
		
		return eatList;
	}
	
	private ArrayList<ArrayList<Eat>> checkIfPieceCanEat(boolean secondWave, int anotherTile) {
		
		
		ArrayList<ArrayList<Eat>> eatArrList = new ArrayList<ArrayList<Eat>>();
		int start = 0;
		int end = 32;
		
		if(secondWave) {
			start = anotherTile;
			end = anotherTile+1;
		}
		for(int i = start;i<end;i++) {
			mapPossibleMoves();
			ArrayList<Eat> eatList = new ArrayList<Eat>();
			if(turn == WHITE && tile[i].getTilePiece().getCol() == WHITEICON) {
				eatList = howToCheckIfPieceCanEat(1,i);
				if(!eatList.isEmpty()) {
					eatArrList.add(eatList);
				}
					
				
			}else if(turn == GREEN && tile[i].getTilePiece().getCol() == GREENICON) {
				eatList = howToCheckIfPieceCanEat(0,i);
				if(!eatList.isEmpty()) {
					eatArrList.add(eatList);
				}
			}
			
			
		}
		return eatArrList;
	}
	
	private	boolean  compulsoryEat(boolean secondWave, int anotherTile) {
		boolean isNothingToEat = true;
		
		 
			    ArrayList<ArrayList<Eat>> eatArrList = checkIfPieceCanEat(secondWave, anotherTile);
				ArrayList<Integer> adj = getAllPotentialJumpTo(eatArrList);
				
			   for(int a = 0;a<eatArrList.size();a++) {
			        ArrayList<Eat> eatList = eatArrList.get(a);
			        for(int b = 0;b<eatList.size();b++) {
			        	Eat eat = eatList.get(b);
			        	System.out.println(eat.getEater() +"can jump to "+eat.getJumpTo());
			    		int col = tile[eat.getEater()].getTilePiece().getCol() == GREENICON ? TRANSPARENT_GREENICON : TRANSPARENT_WHITEICON;
			    		ImageIcon icon = tile[eat.getEater()].getTilePiece().getIcon(col);
			    		
			    			
			    			Integer elem = eat.getJumpTo();
			    			
			    			tile[elem].setPreTile(eat.getEater());
			    			tile[elem].getTilePiece().setCol(col);
			    			tile[elem].getTilePiece().setPieceIcon(icon);
			    			tile[elem].getTilePiece().setPieceVisibility(true);
			    			tile[elem].getTilePiece().setVisible(true);
			    			tile[elem].getTilePiece().setEnabled(true);
			    			tile[elem].getTilePiece().addMouseListener(this);
			    			tile[elem].getTilePiece().setTileEaten(eat.getFood());
			    			
			    			tile[elem].setAdjTiles(adj);
			    			isNothingToEat = false;
			        }	
			    }
			  
			   if(!adj.isEmpty())
				   disableMouseListener(adj);
			
		   return isNothingToEat;
		   
		    
	}
	
	private ArrayList<Integer> getAllPotentialJumpTo(ArrayList<ArrayList<Eat>> eatArrList){
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		
		for(int a = 0;a<eatArrList.size();a++) {
			for(int b = 0;b<eatArrList.get(a).size();b++) {
				list.add(eatArrList.get(a).get(b).getJumpTo());
			}
			
			
		}
		
		return list;
		
	}
	
	private void declareWinner() {
		if(greenPiecesCount == 0) {
			System.out.println("WHITE WINS!");
			System.exit(0);
		}else if(whitePiecesCount == 0) {
			System.out.println("GREEN WINS!");
			System.exit(0);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		for(int i = 0;i<32;i+=1) {
			if(e.getSource() == tile[i].getTilePiece() && e.getClickCount() == 1) {
				
				if(tile[i].getPreTile() == -1) {
					
					showPossibleMoves(i);
				}else{
					
					if(tile[i].getTilePiece().getTileEaten() != -1) {
						removePredecessorTile(tile[i].getTilePiece().getTileEaten());
						eater = i;
						if(turn == GREEN) {
							whitePiecesCount--;
						}else {
							greenPiecesCount--;
						}
						
						declareWinner();
						
					}
					tile[i].getAdjTiles().remove((Integer)i);
					
					removeAdjTile(tile[i].getAdjTiles());
					int pre = tile[i].getPreTile();
					
					boolean isKing = removePredecessorTile(pre);
					turnToRealPiece(i, isKing);
					tile[i].setPreTile(-1);
					pre = tile[i].getPreTile();
				// delete this part right here to preserve last version	
					if(isChangeTurns) {
						changeTurns();	
						enableMouseListener();
						flagTurn =false;
					}else {flagTurn = true;}
				// end
					isChangeTurns = compulsoryEat(canDoubleEat, eater);
					
					
					System.out.println("changeturns = "+isChangeTurns);
					// delete this part right here to preserve last version	
					if(isChangeTurns && flagTurn) {
						changeTurns();	
						enableMouseListener();
						isChangeTurns = compulsoryEat(canDoubleEat, eater);
					}
					// end
	
				}
				
				
			}
			
		}
		
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		for(int i = 0;i<32;i++) {
			if(e.getSource() == tile[i].getTilePiece()) {
				switch(tile[i].getTilePiece().getCol()) {
				case WHITEICON:
					tile[i].getTilePiece().setPieceIcon(new ImageIcon("src/images/white_piece2.png"));
					break;
				case GREENICON:
					tile[i].getTilePiece().setPieceIcon(new ImageIcon("src/images/green_piece2.png"));
					break;
				}
			}
		}
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		for(int i = 0;i<32;i++) {
			if(e.getSource() == tile[i].getTilePiece()) {
				switch(tile[i].getTilePiece().getCol()) {
				case WHITEICON:
					tile[i].getTilePiece().setPieceIcon(new ImageIcon("src/images/white_piece.png"));
					break;
				case GREENICON:
					tile[i].getTilePiece().setPieceIcon(new ImageIcon("src/images/green_piece.png"));
					break;
				}
			}
		}
		
		
		
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void mapPossibleMoves() {
		// in perspective of green pieces
		possibleMoves = new HashMap<Integer, ArrayList<Integer>>();
		
		possibleMoves.put(0, new ArrayList<Integer>(Arrays.asList(4,5)));
		possibleMoves.put(1, new ArrayList<Integer>(Arrays.asList(5,6)));
		possibleMoves.put(2, new ArrayList<Integer>(Arrays.asList(6,7)));
		possibleMoves.put(3, new ArrayList<Integer>(Arrays.asList(7)));
		possibleMoves.put(4, new ArrayList<Integer>(Arrays.asList(8,0)));
		possibleMoves.put(5, new ArrayList<Integer>(Arrays.asList(8,9,0,1)));
		possibleMoves.put(6, new ArrayList<Integer>(Arrays.asList(9,10,1,2)));
		possibleMoves.put(7, new ArrayList<Integer>(Arrays.asList(10,11,2,3)));
		possibleMoves.put(8, new ArrayList<Integer>(Arrays.asList(24,25,4,5)));
		possibleMoves.put(9, new ArrayList<Integer>(Arrays.asList(25,26,5,6)));
		possibleMoves.put(10, new ArrayList<Integer>(Arrays.asList(26,27,6,7)));
		possibleMoves.put(11, new ArrayList<Integer>(Arrays.asList(27,7)));
		possibleMoves.put(12, new ArrayList<Integer>(Arrays.asList(16,28)));
		possibleMoves.put(13, new ArrayList<Integer>(Arrays.asList(16,17,28,29)));
		possibleMoves.put(14, new ArrayList<Integer>(Arrays.asList(17,18,29,30)));
		possibleMoves.put(15, new ArrayList<Integer>(Arrays.asList(18,19,30,31)));
		possibleMoves.put(16, new ArrayList<Integer>(Arrays.asList(20,21,12,13)));
		possibleMoves.put(17, new ArrayList<Integer>(Arrays.asList(21,22,13,14)));
		possibleMoves.put(18, new ArrayList<Integer>(Arrays.asList(22,23,14,15)));
		possibleMoves.put(19, new ArrayList<Integer>(Arrays.asList(23,15)));
		possibleMoves.put(20, new ArrayList<Integer>(Arrays.asList(16)));
		possibleMoves.put(21, new ArrayList<Integer>(Arrays.asList(16,17)));
		possibleMoves.put(22, new ArrayList<Integer>(Arrays.asList(17,18)));
		possibleMoves.put(23, new ArrayList<Integer>(Arrays.asList(18,19)));
		possibleMoves.put(24, new ArrayList<Integer>(Arrays.asList(28,8)));
		possibleMoves.put(25, new ArrayList<Integer>(Arrays.asList(28,29,8,9)));
		possibleMoves.put(26, new ArrayList<Integer>(Arrays.asList(29,30,9,10)));
		possibleMoves.put(27, new ArrayList<Integer>(Arrays.asList(30,31,10,11)));
		possibleMoves.put(28, new ArrayList<Integer>(Arrays.asList(12,13,24,25)));
		possibleMoves.put(29, new ArrayList<Integer>(Arrays.asList(13,14,25,26)));
		possibleMoves.put(30, new ArrayList<Integer>(Arrays.asList(14,15,26,27)));
		possibleMoves.put(31, new ArrayList<Integer>(Arrays.asList(15,27)));
		
	}


}
