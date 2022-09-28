/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dict;
import java.util.*;
/**
 *
 * @author Carlos
 */
public class CTrie {
    private ArrayList<Character> alphabet = new ArrayList<Character>(Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'a', 'Á', 'á', 'À', 'à', 'Ä', 'ä', 'Å', 'å', 'Æ', 'æ', 'Â', 'â', 'B', 'b', 'C', 'c', 'Č', 'č', 'Ç', 'ç', 'D', 'd', 'Ď', 'ď', 'E', 'e', 'É', 'é', 'È', 'è', 'Ě', 'ě', 'Ê', 'ê', 'Ë', 'ë', 'F', 'f', 'G', 'g', 'H', 'h', 'I', 'i', 'Í', 'í', 'Ì', 'ì', 'Ï', 'ï', 'Î', 'î', 'J', 'j', 'K', 'k', 'L', 'l', 'Ĺ', 'ĺ', 'Ľ', 'ľ', 'M', 'm', 'N', 'n', 'Ñ', 'ñ', 'Ň', 'ň', 'O', 'o', 'Ó', 'ó', 'Ò', 'ò', 'Ô', 'ô', 'Ö', 'ö', 'Ő', 'ő', 'Ø', 'ø', 'Œ', 'œ', 'P', 'p', 'Q', 'q', 'R', 'r', 'Ŕ', 'ŕ', 'Ř', 'ř', 'S', 's', 'Š', 'š', 'T', 't', 'Ť', 'ť', 'U', 'u', 'Ú', 'ú', 'Ù', 'ù', 'Ů', 'ů', 'Ü', 'ü', 'Ű', 'ű', 'Û', 'û', 'V', 'v', 'W', 'w', 'X', 'x', 'Y', 'y', 'Ý', 'ý', 'Z', 'z', 'Ž', 'ž'));
    private final Node root = new Node(false);
    public void insert(String word) {
        Node trav = root;
        int i = 0;
	while (i < word.length() && this.alphabet.indexOf(word.charAt(i)) != -1 && (word != "" && trav.values[this.alphabet.indexOf(word.charAt(i))] != null)) {
            int index = this.alphabet.indexOf(word.charAt(i)), j = 0;
            StringBuilder label = trav.values[index];

            while (j < label.length() && i < word.length() && label.charAt(j) == word.charAt(i)) {
                ++i;
                ++j;
            }

            if (j == label.length()) {
                trav = trav.children[index];
            } else {

                if (i == word.length()) {
                    Node existingChild = trav.children[index];
                    Node newChild = new Node(true);
                    StringBuilder remainingLabel = strCopy(label, j);
                    label.setLength(j);

                    trav.children[index] = newChild;
                    newChild.children[this.alphabet.indexOf(remainingLabel.charAt(0))] = existingChild;
                    newChild.values[this.alphabet.indexOf(remainingLabel.charAt(0))] = remainingLabel;
                } else {
                    StringBuilder remainingLabel = strCopy(label, j);

                    Node newChild = new Node(false);
                    StringBuilder remainingWord = strCopy(word, i);

                    Node temp = trav.children[index];

                    label.setLength(j);
                    trav.children[index] = newChild;
                    newChild.values[this.alphabet.indexOf(remainingLabel.charAt(0))] = remainingLabel;
                    newChild.children[this.alphabet.indexOf(remainingLabel.charAt(0))] = temp;
                    newChild.values[this.alphabet.indexOf(remainingWord.charAt(0))] = remainingWord;
                    newChild.children[this.alphabet.indexOf(remainingWord.charAt(0))] = new Node(true);
                }
                return;
            }
	}

	if (i < word.length()) {
            trav.values[this.alphabet.indexOf(word.charAt(i))] = strCopy(word, i);
            trav.children[this.alphabet.indexOf(word.charAt(i))] = new Node(true);
	} else {
            trav.isEnd = true;
        }
    }

    public int alSize() {
        return this.alphabet.size();
    }
    private StringBuilder strCopy(CharSequence str, int index) {
	StringBuilder result = new StringBuilder(100);
	while (index != str.length()) {
            result.append(str.charAt(index++));
	}
        return result;
    }

    public void print() {
        printUtil(root, new StringBuilder());
    }

    private void printUtil(Node node, StringBuilder str) {
	if (node.isEnd) {
            System.out.println(str);
	}

	for (int i = 0; i < node.values.length; ++i) {
            if (node.values[i] != null) {
		int length = str.length();
		str = str.append(node.values[i]);
		printUtil(node.children[i], str);
                str = str.delete(length, str.length());
            }
        }
    }

    public boolean search(String word) {
        int i = 0;
	Node trav = root;
        
        int m = 0;
        if (trav.values[this.alphabet.indexOf(word.charAt(i))] == null) {
            if (trav.values[this.alphabet.indexOf(word.charAt(i))+1] != null) {
                m+=1;
            }
        }
	while (i < word.length() && (trav.values[this.alphabet.indexOf(word.charAt(i))] != null || trav.values[this.alphabet.indexOf(word.charAt(i))+m] != null)) {
            int index = this.alphabet.indexOf(word.charAt(i))+m;
            StringBuilder label = trav.values[index];
            int j = 0;
            while (i < word.length() && j < label.length()) {
		if (word.charAt(i) != label.charAt(j)) {
                    if (this.alphabet.get(this.alphabet.indexOf(word.charAt(i))+m) != label.charAt(j)) {
                        return false;
                    }
		}

		++i;
		++j;
            }
            if (j == label.length() && i <= word.length()) {
		trav = trav.children[index];
            } else {
                return false;
            }
            m=0;
	}
        return i == word.length() && trav.isEnd;
    }
}

class Node {
	Node[] children = new Node[147];
	StringBuilder[] values = new StringBuilder[147];
	boolean isEnd;

	public Node(boolean isEnd) {
            this.isEnd = isEnd;
	}
}