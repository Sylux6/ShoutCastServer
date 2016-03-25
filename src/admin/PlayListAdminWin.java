package admin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import events.Observateur;
import media.FluxAudio;

public class PlayListAdminWin extends JFrame implements Observateur {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6988641537023091195L;
	JTextArea text = new JTextArea();
	JSpinner text_rem = new JSpinner(new SpinnerNumberModel());
	JSpinner text_mod = new JSpinner(new SpinnerNumberModel());
	JFileChooser fc= new JFileChooser();
	FluxAudio fa;
	
	
	
	public PlayListAdminWin(FluxAudio fa) {
		fa.getPlaylist().addObs(this);
		this.fa = fa;
		this.setTitle("Page d'administration");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel content = new JPanel();
		content.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		content.setBackground(Color.white);
		content.setLayout(new GridLayout(1, 2, 5, 5));
		JScrollPane a = new JScrollPane(text);
		content.add(a);

		JPanel colAdm = new JPanel();
		colAdm.setLayout(new GridLayout(8, 1));
		// ligne du bouton add
		JButton button = new JButton();
		button.setText("Add song");
		button.setPreferredSize(new Dimension(200, 50));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				
				fc.showOpenDialog(null);
				String path = null;
				if (fc.getSelectedFile() != null)
					path = fc.getSelectedFile().toString();

				if (path != null && (path.endsWith(".mp3") || path.endsWith(".ogg"))) {
					System.out.println(fc.getCurrentDirectory());
					fc.setCurrentDirectory(fc.getCurrentDirectory());
					fa.getPlaylist().add(fc.getSelectedFile().toString());
					System.out.println(path + " ADDED!");
				}

				else {
					// new JOptionPane("erreur type non supporter");
				}
			}
		});

		JPanel addMusique = new JPanel();
		addMusique.add(button);
		// fin ligne bouton add

		// ligne bouton delete
		JPanel delMusique = new JPanel();
		button = new JButton("Delete n°");
		button.setPreferredSize(new Dimension(100, 50));
		delMusique.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fa.getPlaylist().remove((Integer) text_rem.getValue());
			}
		});
		text_rem.setPreferredSize(new Dimension(50, 40));
		delMusique.add(text_rem);

		// fin ligne bouton delete

		// ligne bouton swap
		JPanel modMusique = new JPanel();
		button = new JButton("UP");
		button.setPreferredSize(new Dimension(100, 50));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("UP")) {
					fa.getPlaylist().swap((Integer) text_mod.getValue(), true);
					text_mod.setValue(new Integer((Integer) text_mod.getValue() - 1));
				}

				else {
					fa.getPlaylist().swap((Integer) text_mod.getValue(), false);
					text_mod.setValue(new Integer((Integer) text_mod.getValue() + 1));
				}

			}

		});
		modMusique.add(button);
		button = new JButton("DOWN");
		button.setPreferredSize(new Dimension(100, 50));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (e.getActionCommand().equals("DOWN")) {
					fa.getPlaylist().swap((Integer) text_mod.getValue(), true);
					text_mod.setValue(new Integer((Integer) text_mod.getValue() - 1));
				}

				else {
					fa.getPlaylist().swap((Integer) text_mod.getValue(), false);
					text_mod.setValue(new Integer((Integer) text_mod.getValue() + 1));
				}

			}
		});
		modMusique.add(button);
		text_mod.setPreferredSize(new Dimension(50, 40));
		modMusique.add(text_mod);

		// fin ligne bouton modification

		// ligne loop
		JPanel loop = new JPanel();
		JCheckBox cb = new JCheckBox("->loop");
		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (((JCheckBox) e.getSource()).isSelected()) {
					fa.getPlaylist().enableloop();
				} else {
					fa.getPlaylist().disableLoop();
				}
			}
		});
		loop.add(cb);
		// fin loop

		// ligne next
		JPanel nextSong = new JPanel();
		button = new JButton();
		button.setPreferredSize(new Dimension(100, 50));
		button.setText("NEXT");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				fa.enableNext();
			}
		});
		nextSong.add(button);
		// fin ligne next

		// ajout a la seconde colonne
		colAdm.add(addMusique);
		colAdm.add(new JPanel());
		colAdm.add(delMusique);
		colAdm.add(new JPanel());
		colAdm.add(nextSong);
		colAdm.add(loop);
		colAdm.add(modMusique);

		colAdm.add(new JPanel());
		content.add(colAdm);

		this.setContentPane(content);
		this.setVisible(true);
	}

	public void getPlaylist() {
		String s = fa.getPlaylist().toString();
		text.setText("Now playing: "+getCurrentSong()+"\n\n"+s);

	}

	public String getCurrentSong() {
		return fa.getPlaylist().getCurrent();
	}


}
