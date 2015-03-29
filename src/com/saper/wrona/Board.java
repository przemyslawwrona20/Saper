package com.saper.wrona;

import java.util.ArrayList;
import java.util.Random;

/** Plansza do gry w Sapera */
public class Board {
	/** Bomba */
	public static final int BOOM = -1;

	/** Liczba bomb */
	private int liczbaBomb;

	/** Liczba wierszy */
	private int liczbaWierszy;

	/** Liczba kolumn */
	private int liczbaKolumn;

	/** Plansza do gry */
	private int[][] plansza;

	public Board() {
	}

	/**
	 * @param liczbaBomb
	 *            liczba bomb
	 * @param liczbaWierszy
	 *            liczba wierszy
	 * @param liczbaKolumn
	 *            liczba kolumn
	 */
	public Board(int liczbaBomb, int liczbaWierszy, int liczbaKolumn) {
		super();
		this.liczbaBomb = liczbaBomb;
		this.liczbaWierszy = liczbaWierszy;
		this.liczbaKolumn = liczbaKolumn;
		plansza = new int[liczbaWierszy][liczbaKolumn];
	}

	/**
	 * Losowo rozmieszcza bomby na planszy W polu o wierszu withoutWiersz i
	 * kolumnie withoutKolumna nie pojawi siê bomba
	 * 
	 * @param withoutWiersz
	 *            wiersz w którym ma nie byæ bomby
	 * @param withoutKolumna
	 *            komurka w którje ma nie byæ bomby
	 */
	public void generujBomby(int withoutWiersz, int withoutKolumna) {
		Random random = new Random();
		int licznik = 0;
		while (licznik < liczbaBomb) {
			int wiersz = random.nextInt(liczbaWierszy);
			int kolumna = random.nextInt(liczbaKolumn);

			if (wiersz == withoutWiersz && kolumna == withoutKolumna)
				continue;

			if (jestZajety(wiersz, kolumna) == false) {
				plansza[wiersz][kolumna] = Board.BOOM;
				licznik++;
			}
		}
	}

	/**
	 * Losowo rozmieszcza bomby na planszy
	 */
	public void generujBomby() {
		generujBomby(-1, -1);
	}

	/**
	 * Sprawdza czy dana komurka zawiera bombê
	 * 
	 * @param wiersz
	 *            numer wiersza
	 * @param kolumna
	 *            numer kolumny
	 * @return -1: gdy komurka zawiera bombê 0: gdy nie ma bomby lub jest poza
	 *         plansz¹
	 */
	public boolean jestZajety(int wiersz, int kolumna) {
		/** Jf true: komorka znajduje sie poza plansza */
		if (wiersz < 0 || kolumna < 0 || wiersz > (liczbaWierszy - 1)
				|| kolumna > (liczbaKolumn - 1))
			return false;
		/** Jf true: w komórce znajduje sie bomba */
		else if (plansza[wiersz][kolumna] == Board.BOOM)
			return true;
		/** Jf true: w komórce nie ma bomb i nalezy do planszy */
		else
			return false;
	}

	/**
	 * Sprawdza ile bomb otacza dan¹ komurkê
	 * 
	 * @param wiersz
	 *            numer wiersza
	 * @param kolumna
	 *            numer kolumny
	 * @return liczba bomb
	 */

	public int ileBomb(int wiersz, int kolumna) {
		ArrayList<Boolean> values = new ArrayList<Boolean>();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				values.add(jestZajety(wiersz - 1 + i, kolumna - 1 + j));
			}
		}

		int licznik = 0;
		if (plansza[wiersz][kolumna] == Board.BOOM)
			licznik--;
		for (boolean value : values) {
			if (value == true)
				licznik++;
		}
		return licznik;
	}

	/**
	 * 
	 * @param wiersz
	 *            Wiersz odkywanego pola
	 * @param kolumna
	 *            Kolumna odkrywanego pola
	 * @return Zwraca wartoœæ jaka znajduje siê w polu
	 */
	public int odkryjBox(int wiersz, int kolumna) {
		if (jestZajety(wiersz, kolumna) == true)
			return Board.BOOM;
		else
			return ileBomb(wiersz, kolumna);
	}

	public void start() {
		for (int i = 0; i < liczbaWierszy; i++) {
			for (int j = 0; j < liczbaKolumn; j++) {
				plansza[i][j] = odkryjBox(i, j);
			}
		}
	}

	/**
	 * @param karty
	 *            referencja do tablcy z kartami
	 * @return Lista kart które zawieraj¹ bombe
	 */
	public ArrayList<Card> whereAreBomb(Card[][] karty) {
		ArrayList<Card> listBomb = new ArrayList<Card>();
		for (int i = 0; i < liczbaWierszy; i++) {
			for (int j = 0; j < liczbaKolumn; j++) {
				if (plansza[i][j] == -1)
					listBomb.add(karty[i][j]);
			}
		}
		return listBomb;
	}

	/**
	 * Wypisuje pola planszy w konsoli
	 */
	public void display() {
		for (int i = 0; i < liczbaWierszy; i++) {
			System.out.println();
			for (int j = 0; j < liczbaKolumn; j++) {
				if (plansza[i][j] == Board.BOOM)
					System.out.print("-1");
				else
					System.out.print(plansza[i][j]);
			}
		}
	}

	/**
	 * @param wiersz
	 *            wiersz z którego pobierana jest wartoœæ
	 * @param kolumna
	 *            kolumna z którejpobierana jest wartoœæ
	 * @return wartoœæ kturan znajduje siê w komurce
	 */
	public int getValue(int wiersz, int kolumna) {
		return plansza[wiersz][kolumna];
	}

	public int getLiczbaBomb() {
		return liczbaBomb;
	}

	public void setLiczbaBomb(int liczbaBomb) {
		this.liczbaBomb = liczbaBomb;
	}

	public int getLiczbaWierszy() {
		return liczbaWierszy;
	}

	public void setLiczbaWierszy(int liczbaWierszy) {
		this.liczbaWierszy = liczbaWierszy;
	}

	public int getLiczbaKolumn() {
		return liczbaKolumn;
	}

	public void setLiczbaKolumn(int liczbaKolumn) {
		this.liczbaKolumn = liczbaKolumn;
	}

	public int[][] getPlansza() {
		return plansza;
	}

}
