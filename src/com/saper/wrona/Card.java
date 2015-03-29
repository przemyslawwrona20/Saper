package com.saper.wrona;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Card extends JButton {
	private static final Color startColor = new Color(120, 180, 250),
			finishColor = Color.WHITE, color0 = Color.WHITE,
			color1 = new Color(120, 180, 250), color2 = new Color(80, 180, 20),
			color3 = Color.RED, color4 = Color.RED, color5 = Color.RED,
			color6 = Color.RED, color7 = Color.RED, color8 = Color.RED,
			color9 = Color.RED;
	private int line, column;
	private boolean zakryta = true;
	private boolean flag = false;

	public Card(int wiersz, int kolumna) {
		super();
		this.line = wiersz;
		this.column = kolumna;
		setBackground(startColor);
		setFont(new Font("Serif", Font.BOLD, 8));
	}

	/** Odkrywa kartê */
	public void odkryj(int value) {
		zakryta = false;
		Color color;
		switch (value) {
		case 0:
			color = color0;
			break;
		case 1:
			color = color1;
			break;
		case 2:
			color = color2;
			break;
		case 3:
			color = color3;
			break;
		case 4:
			color = color4;
			break;
		case 5:
			color = color5;
			break;
		case 6:
			color = color6;
			break;
		case 7:
			color = color7;
			break;
		case 8:
			color = color8;
			break;
		case 9:
			color = color9;
			break;
		default:
			color = Color.BLUE;
			break;
		}

		setBackground(finishColor);
		setForeground(color);

		if (value == Board.BOOM)
			setText("*");
		else
			setText(Integer.toString(value));
	}

	/** Zakrywa kartê */
	public void zakryj() {
		setBackground(startColor);
		setText("");
	}

	/** Ustawia flage na karte */
	public void chaneToFlagColor() {
		setBackground(Color.RED);
	}

	/** Zdejmujê flagê z karty */
	public void normal() {
		setBackground(startColor);
	}

	public boolean isZakryta() {
		return zakryta;
	}

	public int getWiersz() {
		return line;
	}

	public void setWiersz(int wiersz) {
		this.line = wiersz;
	}

	public int getKolumna() {
		return column;
	}

	public void setKolumna(int kolumna) {
		this.column = kolumna;
	}

	public void setZakryta(boolean zakryta) {
		this.zakryta = zakryta;
	}

	public boolean isFlaga() {
		return flag;
	}

	public void setFlaga(boolean flaga) {
		this.flag = flaga;
	}

}
