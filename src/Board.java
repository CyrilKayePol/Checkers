import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Board extends JPanel{
	public static Piece[] piece = new Piece[32];
	public static Tile[] tile = new Tile[32];
	private PieceListener pl;
	private static final int WHITEICON = 0;
	private static final int GREENICON = 1;
	private static final int TRANSPARENT_WHITEICON = 2;
	private static final int TRANSPARENT_GREENICON = 3;
	
	public Board() {
		setLayout(null);
		setPreferredSize(new Dimension(800,560));
		init();
		
	}
	
	
	private void init() {
		pl = new PieceListener();
		placePiecesAndTiles();
		
	}
	
	private void placePiecesAndTiles() {
		int x = 110;
		int y = 5;
		int col = GREENICON;
		boolean isVisible = true;
		
		for(int i = 0;i<=31;i++) {
			
			if(i<=3) {
				x = (i==0) ? 110 : 200+x;
				
			}else if(i >= 4 && i< 8) {
				x = (i==4) ? 10 : 200+x;
				y = 70;
			}else if(i>=8 && i <12) {
				x = (i==8) ? 110 : 200+x;
				y = 135;
			}else if(i>=12 && i <16) {
				col = WHITEICON;
				x = (i==12) ? 10 : 200+x;
				y = 325;
			}else if(i>=16 && i <20) {
				
				x = (i==16) ? 110 : 200+x;
				y = 390;
			}else if(i>=20 && i<24){
				
				x = (i==20) ? 10 : 200+x;
				y = 455;
			}else if(i>=24 && i<28){
				col = TRANSPARENT_GREENICON;
				x = (i==24) ? 10 : 200+x;
				y = 200;
				isVisible = false;
			}else {
				col = TRANSPARENT_WHITEICON;
				x = (i==28) ? 110 : 200+x;
				y = 265;
			}
			
			piece[i] = new Piece(col,x,y,i,isVisible,false,-1);
			
			if(col == 0 && isVisible)
				piece[i].addMouseListener(pl);
			
			tile[i] = new Tile(i,piece[i],-1);
			this.add(piece[i]);	
			
		}
	}
	
	
	
	Image board_image = new ImageIcon("src/images/board.png").getImage();
	public void paintComponent(Graphics g) {
		g.drawImage(board_image, 0, 0, getWidth(), getHeight(), this);
	}

	
	
}

