
import java.util.ArrayList;

public class Tile {
	
	private int preTile;
	private Piece tilePiece;
	private ArrayList<Integer> adjTiles;
	
	public Tile(int tileId, Piece tilePiece, int preTile) {
		this.tilePiece = tilePiece;
		this.preTile = preTile;
		adjTiles = new ArrayList<Integer>();
		
	}
	
	public ArrayList<Integer> getAdjTiles(){
		return adjTiles;
	}
	
	public void setAdjTiles(ArrayList<Integer> adjTiles) {
		this.adjTiles = adjTiles;
	}
	
	public boolean isOccupied() {
		return tilePiece.getPieceVisibility();
	}
	
	public Piece getTilePiece() {
		return tilePiece;
	}
	
	public int getPreTile() {
		return preTile;
	}
	
	public void setPreTile(int preTile) {
		this.preTile = preTile;
	}
	
}
