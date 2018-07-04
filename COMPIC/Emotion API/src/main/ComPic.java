package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import rcaller.RCaller;
import rcaller.RCode;
import java.io.File;
import java.io.FileWriter;

public class ComPic extends JFrame {

	public static final String subscriptionKey = "edd4cd42d6de4ef28cf6c6b74da5df44";
	public static final String uriBase = "https://eastasia.api.cognitive.microsoft.com/face/v1.0/detect";

	private static final long serialVersionUID = 1L;

	private JFileChooser fc;
	private ImageIcon imgIcon;
	private ImageIcon imgIcon2;
	private JLabel imgLabel;
	private JLabel imgLabel2;
	private JButton open;
	private JButton visMulti;
	private JButton visOpen;
	private JButton visMenu;
	private JTextArea textArea;
	private JScrollPane sp;
	private ButtonListener btnListener;
	private VisualListener visListener;

	private class Panel1 extends JPanel {

		private static final long serialVersionUID = 1L;

		public Panel1() {

			fc = new JFileChooser();
			fc.setMultiSelectionEnabled(false);

			open = new JButton("1번 사진 열기");
			visMenu = new JButton("단일 사진 분석");
			visMulti = new JButton("다중시각화");
			visOpen = new JButton("2번 사진 열기");
			btnListener = new ButtonListener();
			visListener = new VisualListener();
			open.addActionListener(btnListener);
			visMulti.addActionListener(visListener);
			visOpen.addActionListener(visListener);
			visMenu.addActionListener(visListener);
			add(open);
			add(visMulti);
			add(visOpen);
			add(visMenu);
			visMulti.setEnabled(false);
			visOpen.setEnabled(false);
		}
	}

	private class Panel2 extends JPanel {

		private static final long serialVersionUID = 1L;

		public Panel2() {
			imgIcon = new ImageIcon();
			imgLabel = new JLabel();
			imgLabel.setIcon(imgIcon);
			add(imgLabel);
		}
	}
	private class Panel3 extends JPanel {

		private static final long serialVersionUID = 1L;

		public Panel3() {
			imgIcon2 = new ImageIcon();
			imgLabel2 = new JLabel();
			imgLabel2.setIcon(imgIcon2);
			add(imgLabel2);
		}
	}

	public void open() {

		Panel1 p1 = new Panel1();
		Panel2 p2 = new Panel2();
		Panel3 p3 = new Panel3();

		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(p3, BorderLayout.EAST);

		setTitle("사진을 모두 고르면, 비교시각화 버튼이 활성화 됩니다.");
		setSize(800, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		RCaller caller = new RCaller();
		caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
	}
	private class VisualListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == visMenu) {
				FaceRecognition fr = new FaceRecognition();
				fr.open();
			}
			
			if(e.getSource() == visMulti) {
		        try{ 
		            RCaller caller = new RCaller();     
		             
		            caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
		            
		            caller.addRCode("setwd('d:/example')");
		            caller.addRCode("library(jsonlite)");
		            caller.addRCode("library(httr)");
		            caller.addRCode("face<-fromJSON('faceresult.json')");
		            caller.addRCode("face2<-fromJSON('faceresult2.json')");
		            
		            caller.addRCode("age1 <- face$faceAttributes$age");
		            caller.addRCode("smile1 <- face$faceAttributes$smile*100");
		            caller.addRCode("bald1<- face$faceAttributes$hair$bald*100");
		            caller.addRCode("moustache1 <- face$faceAttributes$facialHair$moustache*100");
		            caller.addRCode("beard1 <- face$faceAttributes$facialHair$beard*100");
		            caller.addRCode("sideburns1 <- face$faceAttributes$facialHair$sideburns*100");
		            caller.addRCode("data1 <- c(age1,smile1,bald1,moustache1,beard1,sideburns1)");
		            caller.addRCode("names(data1) <- c('나이', '미소', '대머리','콧수염','턱수염','구레나룻')");

		            caller.addRCode("age2 <- face2$faceAttributes$age");
		            caller.addRCode("smile2 <- face2$faceAttributes$smile*100");
		            caller.addRCode("bald2<- face2$faceAttributes$hair$bald*100");
		            caller.addRCode("moustache2 <- face2$faceAttributes$facialHair$moustache*100");
		            caller.addRCode("beard2 <- face2$faceAttributes$facialHair$beard*100");
		            caller.addRCode("sideburns2 <- face2$faceAttributes$facialHair$sideburns*100");
		            caller.addRCode("data2 <- c(age2,smile2,bald2,moustache2,beard2,sideburns2)");
		            caller.addRCode("names(data2) <- c('나이', '미소', '대머리','콧수염','턱수염','구레나룻')");
		            caller.addRCode("rownames <- c('나이','미소', '대머리','콧수염','턱수염','구레나룻')");
		            caller.addRCode("mdata1 <- data1");
		            caller.addRCode("mdata2 <- data2");
		            caller.addRCode("fdata <- rbind(mdata1, mdata2)");
		            caller.addRCode("rownames(fdata) <- c('1st','2nd')");
		            
		            File file2 = caller.startPlot();
		            caller.addRCode("barplot.default(fdata,beside = T,col = rainbow(2),legend.text = c('사진1','사진2') )");
		            caller.addRCode("bplot <- barplot.default(fdata,beside = T,col = rainbow(2),legend.text = c('사진1','사진2'))");
		            caller.addRCode("grid()");
		            caller.addRCode("text(x=bplot, y=fdata-2, labels=fdata, col='black', cex = 0.8)");
		            caller.runAndReturnResult("print('fdata')");
		            
					imgIcon = new ImageIcon(file2.toString());
					Image temp = imgIcon.getImage(); // ImageIcon을 Image로 변환.
					Image newImg = temp.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(newImg); // Image로 ImageIcon
					VisualPanel vp = new VisualPanel();
					vp.create(icon);
		            
		        }catch (Exception ex){
		            System.out.println(ex.toString());
		        }
		    }
			
			if(e.getSource() == visOpen) {
					if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
						File file2 = fc.getSelectedFile();
						imgIcon2 = new ImageIcon(file2.toString());
						Image temp = imgIcon2.getImage(); // ImageIcon을 Image로 변환.
						Image newImg = temp.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(newImg); // Image로 ImageIcon

						imgLabel2.setIcon(icon);
						imgLabel2.repaint();

						HttpClient httpclient = new DefaultHttpClient();

						try
						{
							URIBuilder builder = new URIBuilder(uriBase);

							// Request parameters. All of them are optional.
							builder.setParameter("returnFaceId", "true");
							builder.setParameter("returnFaceLandmarks", "false");
							builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

							// Prepare the URI for the REST API call.
							URI uri = builder.build();
							HttpPost request = new HttpPost(uri);

							// Request headers.
							request.setHeader("Content-Type", "application/octet-stream");
							request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

							// Request body.
							FileEntity reqEntity = new FileEntity(file2, ContentType.APPLICATION_OCTET_STREAM);
							request.setEntity(reqEntity);

							// Execute the REST API call and get the response entity.
							HttpResponse response = httpclient.execute(request);
							HttpEntity entity = response.getEntity();
							
							if (entity != null)
							{
								visMulti.setEnabled(true);
								// Format and display the JSON response.
								String result = "REST Response:\n";

								String jsonString = EntityUtils.toString(entity).trim();
								FileWriter jsonfile = new FileWriter("d:\\example\\faceresult2.json");
								jsonfile.write(jsonString);
								jsonfile.flush(); // stream 2배로 해주기. 있으면 좋음
								jsonfile.close();
							}
						}
						catch (Exception exception)
						{
						}			
					}
		    }
		}
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == open) {
				if(fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					imgIcon = new ImageIcon(file.toString());
					Image temp = imgIcon.getImage(); // ImageIcon을 Image로 변환.
					Image newImg = temp.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(newImg); // Image로 ImageIcon

					imgLabel.setIcon(icon);
					imgLabel.repaint();

					HttpClient httpclient = new DefaultHttpClient();

					try
					{
						URIBuilder builder = new URIBuilder(uriBase);

						// Request parameters. All of them are optional.
						builder.setParameter("returnFaceId", "true");
						builder.setParameter("returnFaceLandmarks", "false");
						builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");

						// Prepare the URI for the REST API call.
						URI uri = builder.build();
						HttpPost request = new HttpPost(uri);

						// Request headers.
						request.setHeader("Content-Type", "application/octet-stream");
						request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

						// Request body.
						FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
						request.setEntity(reqEntity);

						// Execute the REST API call and get the response entity.
						HttpResponse response = httpclient.execute(request);
						HttpEntity entity = response.getEntity();
						
						if (entity != null)
						{
							visOpen.setEnabled(true);
							// Format and display the JSON response.
							String result = "REST Response:\n";

							String jsonString = EntityUtils.toString(entity).trim();
							FileWriter jsonfile = new FileWriter("d:\\example\\faceresult.json");
							jsonfile.write(jsonString);
							jsonfile.flush(); // stream 2배로 해주기. 있으면 좋음
							jsonfile.close();

						}
					}
					catch (Exception exception)
					{
					}			
				}
			}
		}
	}
	public static void main(String[] args) {
		ComPic cp = new ComPic();
		cp.open();
		RCaller caller = new RCaller();
		caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
	}
	
}
