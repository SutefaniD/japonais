package HiraganaVirtualKeyboard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MainApp extends JFrame {

	private JTextArea inputTextArea;
	private JButton convertButton;
	private JPanel topPanel;
	private JTextArea outputTextArea;
	

	public MainApp() {

		super("Japanese Text Editor");

		inputTextArea = new JTextArea();
		JScrollPane topScrollPane = new JScrollPane(inputTextArea);
		topScrollPane.setPreferredSize(new Dimension(200, 50));
		
		convertButton = new JButton("Convertir");
		
		topPanel = new JPanel(new BorderLayout());
		topPanel.add(topScrollPane, BorderLayout.CENTER);
		topPanel.add(convertButton, BorderLayout.SOUTH);
		
		outputTextArea = new JTextArea();
		outputTextArea.setEditable(false);
		//outputTextArea.setFont(new Font("Serif", Font.CENTER_BASELINE, 20));
		outputTextArea.setFont(new Font("Meiryo", Font.PLAIN, 18));
		
		JScrollPane bottomScrollPane = new JScrollPane(outputTextArea);
				
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomScrollPane);
        getContentPane().add(splitPane);
		
        
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        Font[] allFonts = ge.getAllFonts();
//        for (Font font : allFonts) {
//            System.out.println("all fonts: " +font.getFontName(Locale.FRANCE));
//        }
        System.out.println("Font utilisée: " + outputTextArea.getFont());

		
        
		// Bouton pour convertir en Hiragana
		convertButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				convertToHiragana();
			}
		});
		
		// Conversion en tapant "Entrée"
		inputTextArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					convertToHiragana();
					e.consume(); // empêcher l'ajout d'un saut de ligne dans inputTextArea
				}
				
			}
		});


		
		// Ajouter un DocumentListener pour détecter les changements dans la JTextArea
//        inputTextArea.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                convertToHiragana();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                //convertToHiragana();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                //convertToHiragana();
//            }
//        });

	}
	
	private void convertToHiragana() {
		String inputText = inputTextArea.getText().trim().toLowerCase();
		System.out.println("Input Text: " + inputText); // Debug

		if (!inputText.isEmpty()) {
			RomajiToHiragana converter = new RomajiToHiragana();
			StringBuilder result = converter.convert(inputText);
			outputTextArea.append(result.toString() + "\n"); // Ajouter le contenu sur une nouvelle ligne dans outputTextArea
			inputTextArea.setText(""); // Effacer le contenu de inputTextArea
		}
		
	}

	private static void createAndShowGUI() {
		
		MainApp app = new MainApp();
		app.setTitle("Japanese Text Editor");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setSize(500, 300);

		app.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainApp::createAndShowGUI);

	}

}
