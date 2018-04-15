import javax.swing.*;

@SuppressWarnings("serial")
public class Piece extends JLabel{
	private ImageIcon greenIcon, whiteIcon, greenIcon3, whiteIcon3;
	private  int iconWidth, iconHeight;

	private int col;
	private final int id;
	private int x,y;
	private boolean isVisible, isKing;
	private int hasEaten;
	
	private static final int WHITEICON = 0;
	private static final int GREENICON = 1;
	private static final int TRANSPARENT_WHITEICON = 2;
	private static final int TRANSPARENT_GREENICON = 3;
	
	public Piece(int col, int x, int y, int id, boolean isVisible, boolean isKing, int hasEaten) {
		this.col = col;
		this.x=x;
		this.y=y;
		this.id = id;
		this.isVisible = isVisible;
		this.isKing = isKing;
		this.hasEaten = hasEaten;
		
		initIcons();
		
		iconDimension(createPieceIcon(this.col));
		this.setBounds(this.x,this.y,iconWidth, iconHeight);
		this.setEnabled(isVisible);
		this.setVisible(isVisible);
		
	}
	
	private void initIcons() {
		greenIcon = new ImageIcon("src/images/green_piece.png");
		whiteIcon = new ImageIcon("src/images/white_piece.png");
		whiteIcon3 = new ImageIcon("src/images/white_piece3.png");
		greenIcon3 = new ImageIcon("src/images/green_piece3.png");
	}
	
	private ImageIcon createPieceIcon(int col) {
		
		switch(col) {
		case WHITEICON:
			this.setIcon(whiteIcon);
			return whiteIcon;
		case GREENICON:
			this.setIcon(greenIcon);
			return greenIcon;
		case TRANSPARENT_GREENICON:
			this.setIcon(greenIcon3);
			return greenIcon3;
		case TRANSPARENT_WHITEICON:
			this.setIcon(whiteIcon3);
			return whiteIcon3;
		default:
			return null;
			
		}
	}
	

	private void iconDimension(ImageIcon icon) {
		
		iconWidth = icon.getIconWidth();
		iconHeight = icon.getIconHeight();
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public void setPieceIcon(ImageIcon icon) {
		this.setIcon(icon);
	}
	
	public void setPieceVisibility(boolean flag) {
		isVisible = flag;
	}
	public boolean getPieceVisibility() {
		return isVisible;
	}
	
	public void setIsKing(boolean isKing) {
		this.isKing = isKing;
	}
	
	public boolean getIsKing() {
		return isKing;
	}
	
	public ImageIcon getIcon(int col) {
		switch(col) {
		case WHITEICON:
			return whiteIcon;
		case GREENICON:
			return greenIcon;
		case TRANSPARENT_WHITEICON:
			return whiteIcon3;
		case TRANSPARENT_GREENICON:
			return greenIcon3;
		default:
			System.out.println("invalid col value");
			return null;
		
		}	
	}
	
	public int getPieceId() {
		return id;
	}
	
	public void setTileEaten(int hasEaten) {
		this.hasEaten = hasEaten;
	}
	
	public int getTileEaten(){
		return hasEaten;
	}
	
	

}
