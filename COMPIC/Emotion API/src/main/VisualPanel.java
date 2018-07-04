package main;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import rcaller.RCaller;
import rcaller.RCode;

public class VisualPanel extends JFrame {
	private JButton test;
	private ImageIcon imgIcon;
	private JLabel imgLabel;
	
	private class Panel2 extends JPanel {

		private static final long serialVersionUID = 1L;

		public Panel2(ImageIcon a) {
			imgLabel = new JLabel();
			imgLabel.setIcon(a);
			add(imgLabel);
		}
	}

	public void create(ImageIcon b){
			Panel2 p2 = new Panel2(b);

			add(p2, BorderLayout.CENTER);

			setTitle("시각화 결과");
			setSize(600, 600);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
	}
	
	public static void main(String[] args) {

	    }

	}
	

