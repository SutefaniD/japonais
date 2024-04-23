package HiraganaVirtualKeyboard;

import java.util.Scanner;

/**
 * Utilisation d'une matrice
 */
public class RomajiToHiragana {

	private static final String[] CONSONNES = {"k", "g", "s", "z", "j", "t",
			"d", "n", "h", "b", "p", "m", "r", "w", "nn", "ts", "f", "sh", "ch",
			"dj"};

	private static final String[] VOYELLES = {"a", "i", "u", "e", "o", "ya",
			"yu", "yo"};

	// ya, yu, yo as vowels to handle cases of sha,shu,sho,ja,ju,jo,cha,chu,cho
	// do not handle small kana with [x]+voyelles or [l]+voyelles:
	// small tsu: xtsu -> っ or ltsu -> っ
	// small kana: xa -> ぁ or xya -> ゃ etc.
	// small kana: la -> ぁ or lya -> ゃ etc.
	
	// pb: c+a is あ instead of cあ

	private static final String[][] KANA_MATRIX = {
			// VOYELLES
			{"あ", "い", "う", "え", "お", "や", "ゆ", "よ"}, // 0: voyelles
			// CONSONNES
			{"か", "き", "く", "け", "こ", "きゃ", "きゅ", "きょ"}, // 1: K
			{"が", "ぎ", "ぐ", "げ", "ご", "ぎゃ", "ぎゅ", "ぎょ"}, // 2: G
			{"さ", "し", "す", "せ", "そ", "しゃ", "しゅ", "しょ"}, // 3: S (pour しゃ on tape "s+ya) (-> SH)
			{"ざ", "じ", "ず", "ぜ", "ぞ", "じゃ", "じゅ", "じょ"}, // 4: Z (pour じゃ on tape "z+ya") (-> J)
			{"じゃ", "じ", "じゅ", "", "じょ"}, // 5: J (hepburn じゃ = J+a)
			{"た", "ち", "つ", "て", "と", "ちゃ", "ちゅ", "ちょ"}, // 6: T (pour ちゃ on tape "t+ya" (-> CH / TSu)
			{"だ", "ぢ", "づ", "で", "ど", "ぢゃ", "ぢゅ", "ぢょ"}, // 7: D (pour ぢゃ on tape "d+ya" (-> DJ)
			{"な", "に", "ぬ", "ね", "の", "にゃ", "にゅ", "にょ"}, // 8: N
			{"は", "ひ", "ふ", "へ", "ほ", "ひゃ", "ひゅ", "ひょ"}, // 9: H (/ Fu)
			{"ば", "び", "ぶ", "べ", "ぼ", "びゃ", "びゅ", "びょ"}, // 10: B
			{"ぱ", "ぴ", "ぷ", "ぺ", "ぽ", "ぴゃ", "ぴゅ", "ぴょ"}, // 11: P
			{"ま", "み", "む", "め", "も", "みゃ", "みゅ", "みょ"}, // 12: M
			{"ら", "り", "る", "れ", "ろ", "りゃ", "りゅ", "りょ"}, // 13: R
			{"わ", "", "", "", "を"}, // 14: W
			{"ん"}, // 15: NN
			{"", "", "つ"}, // 16: TS (TS+u)
			{"ふぁ", "", "ふ"}, // 17: F (F+u)
			{"しゃ", "し", "しゅ", "", "しょ"}, // 18: SH (hepburn: しゃ = SH+a)
			{"ちゃ", "ち", "ちゅ", "", "ちょ"}, // 19: CH (hepburn ちゃ = CH+a)
			{"ぢゃ", "ぢ", "ぢゅ", "", "ぢょ"}, // 20: DJ (hepburn ぢゃ = DJ+a)
	};

	public StringBuilder convert(String input) {

		StringBuilder resultat = new StringBuilder();
		int i = 0;

		while (i < input.length()) {
			char lettre = input.charAt(i);

			// Cas 1: lettre = voyelle simple "a", "i", "u", "e", "o"
			if (isVowel(lettre)) {
				resultat.append(
						KANA_MATRIX[0][findVowelIndex(String.valueOf(lettre))]);
				i++; // Avancer au prochain caractère
			}
			// Cas 2: lettre = "y" + voyelle (voyelle double "ya", "yu", "yo")
			else if (lettre == 'y' && i < input.length() - 1
					&& isVowel(input.charAt(i + 1))) {
				String deuxLettres = "y" + input.charAt(i + 1);
				resultat.append(KANA_MATRIX[0][findVowelIndex(deuxLettres)]);
				i += 2; // Avancer de 2 caractères
			}

			// Cas 3: lettre = consonne simple ( "k", "g", "s", "z", "j", "t",
			// "d", "n",
			// "h",
			// "b", "p", "m", "r", "w", "f") OU 'c' (pour gérer cas 'ch'
			// ensuite)
			else if (isConsonant(lettre) || lettre == 'c') {
				// Récupérer la lettre suivante si elle existe
				char lettre2 = (i < input.length() - 1)
						? input.charAt(i + 1)
						: '\0'; // '\0' retourne nul
				System.out.println("lettre2:" + lettre2);
				// Cas 3-1: Consonne suivie d'une voyelle
				if (isVowel(lettre2)) {
					resultat.append(KANA_MATRIX[findConsonantIndex(
							String.valueOf(lettre)) + 1][findVowelIndex(
									String.valueOf(lettre2))]);
					i += 2; // Avancer de 2 caractères
					System.out.println("consIndex: "
							+ findConsonantIndex(String.valueOf(lettre)) + 1);
				}
				// Cas 3-2: Consonne suivie d'une voyelle double ("ya", "yu",
				// "yo")
				else if (lettre2 == 'y' && i < input.length() - 2
						&& isVowel(input.charAt(i + 2))) {
					String deuxLettres = "y" + input.charAt(i + 2);
					resultat.append(KANA_MATRIX[findConsonantIndex(
							String.valueOf(lettre)) + 1][findVowelIndex(
									deuxLettres)]);
					i += 3; // Avancer de 3 caractères
					System.out.println("consIndex: "
							+ findConsonantIndex(String.valueOf(lettre)) + 1);
				}
				// Cas 3-3: Consonne répétée: "nn" -> ん
				else if (lettre == 'n' && lettre2 == 'n') {
					resultat.append(KANA_MATRIX[15][0]);
					i += 2;
				}
				// Cas 3-4: consonne répétée: Cas de petit tsu
				else if (lettre == lettre2) {
					resultat.append("っ");
					i++; // Avancer au caractère suivant
				}
				// Cas 3-5: Consonne double ("sh", "ch" ou "dj") + voyelle
				else if ((((lettre == 's' || lettre == 'c') && lettre2 == 'h')
						|| (lettre == 'd' && lettre2 == 'j'))
						&& i < input.length() - 2
						&& isVowel(input.charAt(i + 2))) {
					String deuxLettres = String.valueOf(lettre)
							+ String.valueOf(lettre2);
					resultat.append(KANA_MATRIX[findConsonantIndex(deuxLettres)
							+ 1][findVowelIndex(
									String.valueOf(input.charAt(i + 2)))]);
					i += 3; // Avancer de 3 caractères
				}
				// Autres cas: ne pas convertir (erreur?)
				else {
					resultat.append(lettre);
					i++; // Avancer au prochain caractère
				}

			}
			// tous les autres caractères apparaissent tels quels
			else {
				resultat.append(lettre);
				i++;
			}
		}

		return resultat;
	}

	private int findConsonantIndex(String consonne) {
		for (int i = 0; i < CONSONNES.length; i++) {
			if (CONSONNES[i].equals(consonne)) {
				return i;
			}
		}
		return -1;
	}

	private int findVowelIndex(String voyelle) {
		for (int i = 0; i < VOYELLES.length; i++) {
			if (VOYELLES[i].equals(voyelle)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isConsonant(char c) {
		for (String consonant : CONSONNES) {
			if (consonant.equals(String.valueOf(c))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isVowel(char c) {

		for (String vowel : VOYELLES) {
			if (vowel.equals(String.valueOf(c))) {
				return true;
			}
		}

		return false;
	}

	// pour tester
	public static void main(String[] args) {
		System.out.println("Saisir les lettres: ");

		try (Scanner sc = new Scanner(System.in)) {
			String input = sc.next();

			RomajiToHiragana converter = new RomajiToHiragana();
			StringBuilder resultat = converter.convert(input);

			System.out.println("Input : " + input);
			System.out.println("Résultat: " + resultat);
		}
	}

}