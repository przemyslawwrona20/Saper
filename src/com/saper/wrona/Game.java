package com.saper.wrona;

import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Game extends JFrame implements MouseListener, ActionListener {
	private JMenu fileMenu, poziomMenu;
	private JMenuItem openItem, latwyItem, sredniItem, trudnyItem, exitItem;
	private JLabel liczbaBombLabel;

	private Board plansza;
	private Card[][] karty;

	/** true: if check the card first time */
	boolean isStart = false;

	/** true: if checked the bomb */
	boolean isFinish = false;

	private String pozostaloBomb = "Pozosta³o bomb ";

	public Game() {
		fileMenu = new JMenu("PLIK");

		openItem = new JMenuItem("NOWA GRA");
		openItem.addActionListener(this);
		fileMenu.add(openItem);

		poziomMenu = new JMenu("POZIOM");
		fileMenu.add(poziomMenu);

		latwyItem = new JMenuItem("£ATWY");
		latwyItem.addActionListener(this);
		poziomMenu.add(latwyItem);

		sredniItem = new JMenuItem("ŒREDNI");
		sredniItem.addActionListener(this);
		poziomMenu.add(sredniItem);

		trudnyItem = new JMenuItem("TRUDNY");
		trudnyItem.addActionListener(this);
		poziomMenu.add(trudnyItem);

		exitItem = new JMenuItem("WYJŒCIE");
		exitItem.addActionListener(this);
		fileMenu.add(exitItem);

		liczbaBombLabel = new JLabel(pozostaloBomb);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(liczbaBombLabel);
		setJMenuBar(menuBar);

		plansza = new Board();

		karty = new Card[plansza.getLiczbaWierszy()][plansza.getLiczbaKolumn()];
		setLatwy();
	}

	private void nowaGra() {
		isStart = false;
		isFinish = false;

		if (karty == null)
			return;

		removeCards();
		createNewCards();

		plansza = new Board(plansza.getLiczbaBomb(),
				plansza.getLiczbaWierszy(), plansza.getLiczbaKolumn());

		setLayout(new GridLayout(plansza.getLiczbaWierszy(),
				plansza.getLiczbaKolumn(), 5, 5));

		setSize(plansza.getLiczbaKolumn() * 45, plansza.getLiczbaWierszy() * 45);

		liczbaBombLabel.setText(pozostaloBomb
				+ Integer.toString(howMAnyBombs()));
		validate();
	}

	/** Usuwa karty z planszy */
	private void removeCards() {
		for (int i = 0; i < karty.length; i++) {
			for (int j = 0; j < karty[i].length; j++) {
				if (karty != null || karty[i][j] != null) {
					Card karta = karty[i][j];
					if (karta != null)
						remove(karta);
				}
			}
		}
	}

	/** Tworzy karty do nowej gry */
	private void createNewCards() {
		/** Create new card */
		karty = new Card[plansza.getLiczbaWierszy()][plansza.getLiczbaKolumn()];

		/** Put cart into array */
		for (int i = 0; i < plansza.getLiczbaWierszy(); i++) {
			for (int j = 0; j < plansza.getLiczbaKolumn(); j++) {
				Card karta = new Card(i, j);

				karta.addMouseListener(this);
				add(karta);
				karty[i][j] = karta;
			}
		}
	}

	/** Odkrywa pola wokó³ karty z któr¹ nie s¹siaduj¹ bomby */
	public void isZero(Card karta) {
		Stack<Card> stackKarty = new Stack<Card>();
		int wiersz = karta.getWiersz();
		int kolumna = karta.getKolumna();
		if (plansza.getValue(wiersz, kolumna) != 0)
			return;
		else {
			isZero(karta, stackKarty);
		}

		uncover(stackKarty);

	}

	private void isZero(Card karta, Stack<Card> stackKarty) {
		int wiersz = karta.getWiersz();
		int kolumna = karta.getKolumna();

		if (plansza.getValue(wiersz, kolumna) != 0)
			return;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				try {
					Card odkryjKarte = karty[wiersz - 1 + i][kolumna - 1 + j];
					if (!stackKarty.contains(odkryjKarte)) {
						stackKarty.add(odkryjKarte);
						isZero(odkryjKarte, stackKarty);
					}

				} catch (ArrayIndexOutOfBoundsException ex) {
				}

			}
		}
	}

	/** Sprawdza czy pod wskazan¹ kart¹ znajduje siê bomba */
	public void isGameOver(Card karta) {
		/**
		 * if true wskazane pole nie zawiera bomby mo¿na graæ dalej else
		 * wyszukuje pola gdzie s¹ bomby i je odkrywa gra zostaje przerwana
		 */
		if (plansza.getValue(karta.getWiersz(), karta.getKolumna()) != Board.BOOM) {
			return;

		} else {
			isFinish = true;
			ArrayList<Card> listKarta = plansza.whereAreBomb(karty);
			uncover(listKarta);
		}
	}

	/** Sprawdza ile pozosta³o kart do odkycia aby wygraæ */
	public int howManyCards() {
		int returnValue = 0;
		for (int i = 0; i < plansza.getLiczbaWierszy(); i++) {
			for (int j = 0; j < plansza.getLiczbaKolumn(); j++) {
				Card karta = karty[i][j];
				if (karta.isZakryta() && plansza.getValue(i, j) != Board.BOOM) {
					returnValue++;
				}
			}
		}
		return returnValue;
	}

	/** Sprawdza ile pozosta³o bomb */
	public int howMAnyBombs() {
		int liczbaFlag = 0;

		for (int i = 0; i < plansza.getLiczbaWierszy(); i++)
			for (int j = 0; j < plansza.getLiczbaKolumn(); j++) {
				if (karty[i][j].isFlaga())
					liczbaFlag++;
			}
		return plansza.getLiczbaBomb() - liczbaFlag;
	}

	/** Ustawia poziom ³atwy */
	public void setLatwy() {
		plansza.setLiczbaKolumn(GameInterface.liczbaKolumnLatwy);
		plansza.setLiczbaWierszy(GameInterface.LiczbaWierszyLatwy);
		plansza.setLiczbaBomb(GameInterface.liczbaBombLatwy);
		nowaGra();
	}

	/** Ustawia poziom œredni */
	public void setSredni() {
		plansza.setLiczbaKolumn(GameInterface.liczbaKolumnSredni);
		plansza.setLiczbaWierszy(GameInterface.LiczbaWierszySredni);
		plansza.setLiczbaBomb(GameInterface.liczbaBombSredni);
		nowaGra();
	}

	/** Ustawia poziom trudny */
	public void setTrudny() {
		plansza.setLiczbaKolumn(GameInterface.liczbaKolumnTrudny);
		plansza.setLiczbaWierszy(GameInterface.LiczbaWierszyTrudny);
		plansza.setLiczbaBomb(GameInterface.liczbaBombTrudny);
		nowaGra();
	}

	/** Odkrywa kartê */
	private void uncover(Card karta) {
		karta.odkryj(plansza.getValue(karta.getWiersz(), karta.getKolumna()));
		isZero(karta);
		isGameOver(karta);
	}

	/** Odkrywa listê kard */
	private void uncover(Collection<Card> listKarta) {
		for (Card karta : listKarta) {
			karta.odkryj(plansza.getValue(karta.getWiersz(), karta.getKolumna()));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Card karta = (Card) e.getSource();
		int wiersz = karta.getWiersz();
		int kolumna = karta.getKolumna();

		/** true if click right button of mouse && it is first click */
		if (e.getButton() == MouseEvent.BUTTON1 && !isStart) {
			isStart = true;
			plansza.generujBomby(wiersz, kolumna);
			plansza.start();

			uncover(karta);
		}
		/**
		 * true if click right button of mouse && it is not first click && did
		 * not click the bomb && card's flag is true
		 */
		else if (e.getButton() == MouseEvent.BUTTON1 && isStart && !isFinish
				&& !karta.isFlaga()) {

			uncover(karta);

			if (howManyCards() == 0) {
				isFinish = true;
			}
		}
		/**
		 * true if click left button of mouse && did not click the bomb && card
		 * is uncovered && card's flag is true
		 */
		else if (e.getButton() == MouseEvent.BUTTON3 && !isFinish
				&& karta.isZakryta() && karta.isFlaga()) {
			karta.setFlaga(false);
			karta.normal();
		}
		/**
		 * true if click left button of mouse && did not click the bomb && card
		 * is uncovered && card's flag is false
		 */
		else if (e.getButton() == MouseEvent.BUTTON3 && !isFinish
				&& karta.isZakryta() && !karta.isFlaga()) {
			karta.setFlaga(true);
			karta.chaneToFlagColor();
		}

		liczbaBombLabel.setText(pozostaloBomb
				+ Integer.toString(howMAnyBombs()));

		if (howManyCards() == 0)
			JOptionPane.showMessageDialog(null, "WYGRANA");

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		Game gra = new Game();
		gra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gra.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == openItem)
			nowaGra();
		else if (source == latwyItem)
			setLatwy();
		else if (source == sredniItem)
			setSredni();
		else if (source == trudnyItem)
			setTrudny();
		else if (source == exitItem)
			System.exit(0);
	}
}
