//package com.javaclassec;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;

import javax.swing.JPanel;
import java.awt.*;

class Start {
	private JFrame frame = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Start() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame = new JFrame();
		this.frame.setBounds(100, 100, 700, 800);
		this.frame.setMinimumSize(new Dimension(700, 250));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		/**
		 *  Make the frame with it's own class.
		 */
		MainFrame mFrame = new MainFrame(this.frame);
		mFrame.prepare();
		mFrame.show();
		
	}
	
	/** =========================================================================================
	 *	Main Frame class that handles the main window mechanics.
	 *  ========================================================================================= */
	class MainFrame{
		private JFrame frame = null;
		private JPanel controlPanel = null;
		private BitDisplay bitDisplay = null;
		private JLabel rowLabel, columnLabel, valueLabel = null;
		private JSpinner rowSpinner, columnSpinner, valueSpinner, colorPicker = null;
		private JButton cursorSetterButton, valueSetterButton, clearButton = null;
		
		public MainFrame(JFrame super_farme){
			this.frame = super_farme;
			this.controlPanel = new JPanel();
			
			 /**
			  * Make GUI JObjects
			  */
			 this.rowLabel = new JLabel("Row: "); this.columnLabel = new JLabel("Column: "); this.valueLabel = new JLabel("Value: ");
		
		     this.rowSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 2, 1));
		     this.columnSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 15, 1));
		     this.valueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
		     String[] colors = {"Blue", "Red", "Green"};
		     this.colorPicker = new JSpinner(new SpinnerListModel(colors));
		     
		     this.cursorSetterButton = new JButton("GOTO"); 
		     this.valueSetterButton = new JButton("SET");
		     this.clearButton = new JButton("CLEAR");
		}
		public void prepare(){

			/**
		      * Set buttons listeners
		      */
		       this.valueSetterButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		                int row = (int)rowSpinner.getValue();
		                int column = (int)columnSpinner.getValue();
		                
		                switch (colorPicker.getValue().toString()){
			                case "Blue":{
			                	bitDisplay.fillCellColor = Color.BLUE;
			                }break;
			                case "Red":{
			                	bitDisplay.fillCellColor = Color.RED;
			                }break;
			                case "Green":{
			                	bitDisplay.fillCellColor = Color.GREEN;
			                }break;
		                }
		                
		                Position cursorPosition = bitDisplay.getCursorPosition(new Position(row, column));
		                if (cursorPosition.isValid()) {
		                    bitDisplay.setCursor(cursorPosition.getRow(), cursorPosition.getColumn());
		                    rowSpinner.setValue(cursorPosition.getRow());
		                    columnSpinner.setValue(cursorPosition.getColumn());
		                }
		                bitDisplay.setValue(row, column, (Integer)valueSpinner.getValue());
		                bitDisplay.repaint();
		            }
		        });
		        this.cursorSetterButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		                bitDisplay.setCursor((Integer)rowSpinner.getValue(), (Integer)columnSpinner.getValue());
		                bitDisplay.repaint();
		            }
		        });
		        this.clearButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		                bitDisplay.clear();
		                rowSpinner.setValue(0);
		                columnSpinner.setValue(0);
		                bitDisplay.repaint();
		            }
		        });
		        
		    /**
		     * Set the buttons location
		     */
	        
		    this.rowLabel.setLocation(30, 400);
	        this.rowLabel.setSize(50, 50);
	        this.controlPanel.add(this.rowLabel);
	        this.rowSpinner.setLocation(85, 415);
	        this.rowSpinner.setSize(50, 20);
	        this.controlPanel.add(this.rowSpinner);
	        this.columnLabel.setLocation(230, 400);
	        this.columnLabel.setSize(70, 50);
	        this.controlPanel.add(this.columnLabel);
	        this.columnSpinner.setLocation(305, 415);
	        this.columnSpinner.setSize(50, 20);
	        this.controlPanel.add(this.columnSpinner);
	        this.valueLabel.setLocation(430, 400);
	        this.valueLabel.setSize(70, 50);
	        this.controlPanel.add(this.valueLabel);
	        this.valueSpinner.setLocation(500, 415);
	        this.valueSpinner.setSize(50, 20);
	        this.controlPanel.add(this.valueSpinner);
		    this.controlPanel.setSize(60, 20);
		    this.controlPanel.add(this.colorPicker);
	        this.cursorSetterButton.setLocation(25, 450);
	        this.cursorSetterButton.setSize(130, 40);
	        this.controlPanel.add(this.cursorSetterButton);
	        this.valueSetterButton.setLocation(225, 450);
	        this.valueSetterButton.setSize(130, 40);
	        this.controlPanel.add(this.valueSetterButton);
		    this.clearButton.setLocation(425, 450);
		    this.clearButton.setSize(130, 40);
		    this.controlPanel.add(this.clearButton);
	
		}
		public void show(){
			/**
			 * Make BitDisplay and prepare
			 */
		     this.bitDisplay = new BitDisplay(3, 16);
		     this.bitDisplay.setLocation(0, 0);
		     this.bitDisplay.setSize(700, 400);
		     this.bitDisplay.setCursor(0, 0);
		
		     /**
		      * Prepare control panel
		      */
			 this.controlPanel.setSize(new Dimension(700, 120));
		     		        
			    /**
			     * adding panels to the JFrame
			     */
				this.frame.getContentPane().add(bitDisplay);
				this.frame.getContentPane().add(controlPanel, BorderLayout.SOUTH); //<- add control panel on the bottom
		}
	};
};


/**
 *	LCD display interface that the controller will use to interact with the logic.
 *	3 main functions that the controller should have is listed down below.
 */
interface DisplayI {
	void clear();
	void setCursor(int row, int column);
    public void setValue(int row, int column, Integer value);
}
class BitDisplay extends CellDisplay implements DisplayI {
    private static final int cellsOnRow = 8;
    private int cursorRow = -1, cursorColumn = -1;

    public BitDisplay(int rows, int columns) {
        super(rows * BitDisplay.cellsOnRow, columns);
    }
    private boolean[] getFlagArray(int wartosc) {
        boolean[] valueFlags = new boolean[8];
        
        StringBuilder buffer = new StringBuilder(Integer.toBinaryString(wartosc));
        String digits = buffer.reverse().toString();

        for (int i = digits.length() - 1; i >= 0; --i) {
            if (digits.charAt(i) == '1') 
                valueFlags[i] = true;
            else 
                valueFlags[i] = false;
        }
        return valueFlags;
    }
    private void resetCursor() {
        wyczyscPola8(cursorRow, cursorColumn);
    }
    private void wyczyscPola8(int row, int column) {
        if (row != -1 && column != -1) {
            for (int r = row * cellsOnRow; r < (row + 1) * cellsOnRow; ++r) 
                clearCell(r, column);
        }
    }
    public Position getCursorPosition(Position prev) {
        int position = prev.getColumn() + prev.getRow() * 16 + 1;
        int row = position / 16;
        int column = position % 16;
        
        return (row == 3) ? new Position(-1, -1) : new Position(row, column);
    }
    @Override
    public void clear() {
        clearAll();
        setCursor(0, 0);
    }
    @Override
    public void setCursor(int row, int column) {
        resetCursor();

        for (int r = row * cellsOnRow; r < (row + 1) * cellsOnRow; ++r) 
            markCell(r, column);
        
        cursorRow = row;
        cursorColumn = column;
    }
    @Override
    public void setValue(int row, int column, Integer value) {
        boolean[] flagi = getFlagArray(value);
        wyczyscPola8(row, column); //////
        int i = 0;
        for (int r = row * BitDisplay.cellsOnRow; r < (row + 1) * BitDisplay.cellsOnRow; ++r) {
            if (flagi[i]){
                fillCell(r, column);
            }
            i++;
        }
    }
}

class Position {
    private final int row, column;
    
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }
    public boolean isValid() {
        return row >= 0 && column >= 0;
    }
}

/**
 *	Class that handles LCD mechanics such as drawing lines and individual cells. It uses the java.atw.Graphics for drawing things.
 */
class CellDisplay extends JPanel {
	public Color fillCellColor = Color.BLUE;
	
    private enum FLAG {
        empty,
        filled,
        marked
    }
    private final int columnsNumber, rowsNumber;
    private final FLAG[][] flags;
    private int cellSize, widthGrid, heightGrid, minX, maxX, minY, maxY, marginX, marginY;

    public CellDisplay(int rowsNumber, int columsNumber) {
        this.rowsNumber = rowsNumber;
        this.columnsNumber = columsNumber;
        this.flags = new FLAG[rowsNumber][columsNumber];
    }
    
    //override the JPanel method
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        count();
        fillBackground(g);
        drawCells(g);
        drawGrid(g);
    }
    public void markCell(int row, int column) {
        flags[row][column] = FLAG.marked;
    }
    public void fillCell(int row, int column) {
        flags[row][column] = FLAG.filled;
    }
    public void clearCell(int row, int column) {
        flags[row][column] = FLAG.empty;
    }
    public void clearAll() {
        for (int i = 0; i < rowsNumber; ++i) {
            for (int c = 0; c < columnsNumber; ++c)
                clearCell(i, c);
        }
    }
    private void count() {
        cellSize = getCellSideSize();
        widthGrid = columnsNumber * cellSize;
        heightGrid = rowsNumber * cellSize;
        marginX = (getWidth() - widthGrid) / 2;
        marginY = (getHeight() - heightGrid) / 2;
        minX = marginX;
        maxX = marginX + widthGrid;
        minY = marginY;
        maxY = marginY + heightGrid;
    }
    private void drawCells(Graphics g) {
        for (int i = 0, yPola = minY; i < rowsNumber; yPola += cellSize, i++) {
            for (int c = 0, xPola = minX; c < columnsNumber; xPola += cellSize, c++) {
                if (flags[i][c] == FLAG.filled) 
                    drawFilledCell(g, xPola, yPola, cellSize);
                else if (flags[i][c] == FLAG.marked) 
                    drawMarkedCells(g, xPola, yPola, cellSize);
            }
        }
    }
    private void drawFilledCell(Graphics g, int xCell, int yCell, int cellSize) {
        g.setColor(this.fillCellColor);
        g.fillRect(xCell, yCell, cellSize, cellSize);
    }
    private void drawMarkedCells(Graphics g, int xCell, int yCell, int cellSize) {
        g.setColor(Color.yellow);
        g.fillRect(xCell, yCell, cellSize, cellSize);
    }
    private void drawGrid(Graphics g) {
        g.setColor(Color.black);
        drawVerticalLine(g);
        drawHorizontalLine(g);
    }
    private void drawHorizontalLine(Graphics g) {
        for (int y = minY; y <= maxY; y += cellSize) {
            g.drawLine(minX, y,  maxX, y);
        }
    }
    private void drawVerticalLine(Graphics g) {
        for (int x = minX; x <= maxX; x += cellSize) {
            g.drawLine(x, minY,  x, maxY);
        }
    }
    private void fillBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
    private int getCellSideSize() {
        int x = getHeight() / rowsNumber;
        int y = getWidth() / columnsNumber;
        return Math.min(x, y);
    }
}

