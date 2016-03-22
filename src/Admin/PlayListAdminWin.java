package Admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileSystemView;

import media.Playlist;

public class PlayListAdminWin extends JFrame implements Observateur{
	JTextArea text = new JTextArea();
	JSpinner text_rem = new JSpinner(new SpinnerNumberModel());
	JSpinner text_mod = new JSpinner(new SpinnerNumberModel());

	public PlayListAdminWin() {
		//on observe la playlist
		Playlist.addObs(this);
		
		
		this.setTitle("Page d'administration");
		this.setSize(600, 500);

		JPanel content = new JPanel();
		content.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		content.setBackground(Color.white);
		content.setLayout(new GridLayout(1, 2, 5, 5));
		content.add(new JScrollPane(text));

		JPanel colAdm = new JPanel();
		colAdm.setLayout(new GridLayout(8, 1));

		JButton button = new JButton();
		button.setText("add");
		button.setPreferredSize(new Dimension(100, 50));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
				fc.showOpenDialog(null);
				String path = fc.getSelectedFile().toString();
				if (path.endsWith(".mp3") || path.endsWith(".ogg")) {
					Playlist.add(fc.getSelectedFile().toString());
					System.out.println("on ajoute le fichier " + path);
				}

				else {
//					new JOptionPane("type de fichier non supporter");
				}
			}
		});

		JPanel addMusique = new JPanel();
		addMusique.add(button);
		JPanel delMusique = new JPanel();
		button = new JButton("del");
		button.setPreferredSize(new Dimension(100, 50));
		delMusique.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
		//					Playlist.remove(Integer.parseInt(text_rem.get));
				Playlist.remove((Integer)text_rem.getValue());
//				System.out.println("on veux suprimer le "+(Integer)text_mod.getValue());
			}
		});
		text_rem.setPreferredSize(new Dimension(50, 40));
		delMusique.add(text_rem);
		JPanel modMusique = new JPanel();
		button = new JButton("up");
		button.setPreferredSize(new Dimension(100, 50));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("up")){
					Playlist.swap((Integer)text_mod.getValue(),true);
					text_mod.setValue(new Integer((Integer)text_mod.getValue()-1));
				}
					
				else{
					Playlist.swap((Integer)text_mod.getValue(),false);
					text_mod.setValue(new Integer((Integer)text_mod.getValue()+1));
				}
					
			}
			
		});
		modMusique.add(button);
		button = new JButton("down");
		button.setPreferredSize(new Dimension(100, 50));
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
					if(e.getActionCommand().equals("up")){
						Playlist.swap((Integer)text_mod.getValue(),true);
						text_mod.setValue(new Integer((Integer)text_mod.getValue()-1));
					}
						
					else{
						Playlist.swap((Integer)text_mod.getValue(),false);
						text_mod.setValue(new Integer((Integer)text_mod.getValue()+1));
					}
						
			}
		});
		modMusique.add(button);
		text_mod.setPreferredSize(new Dimension(50, 40));
		modMusique.add(text_mod);
		colAdm.add(addMusique);
		colAdm.add(new JPanel());
		colAdm.add(delMusique);
		colAdm.add(new JPanel());
		colAdm.add(new JPanel());
		colAdm.add(new JPanel());
		colAdm.add(modMusique);

		colAdm.add(new JPanel());
		content.add(colAdm);

		this.setContentPane(content);
		// this.setLayout(new GridLayout(1, 2));
		// this.getContentPane().add(new JScrollPane());
		this.setVisible(true);
		// this.getContentPane().add();
	}

	class AddMusiqueListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class DelMusiqueListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class ModMusiqueListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class StopMusiqueListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}

	class NextMusiqueListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

		}

	}
	
	public void getPlaylist(){
		String s = Playlist.toString_();
		text.setText(s);
			
	}
	
	

}
