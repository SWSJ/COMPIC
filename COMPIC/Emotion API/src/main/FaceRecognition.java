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

public class FaceRecognition extends JFrame {

	public static final String subscriptionKey = "edd4cd42d6de4ef28cf6c6b74da5df44";
	public static final String uriBase = "https://eastasia.api.cognitive.microsoft.com/face/v1.0/detect";

	private static final long serialVersionUID = 1L;

	private JFileChooser fc;
	private ImageIcon imgIcon;
	private JLabel imgLabel;
	private JButton open;
	private JButton visHairColor;
	private JButton visEmotion;
	private JButton visSize;
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

			open = new JButton("사진 열기");
			visMenu = new JButton("2개사진비교");
			visHairColor = new JButton("머리칼 시각화");
			visEmotion = new JButton("감정 시각화");
			visSize = new JButton("얼굴크기 시각화");
			btnListener = new ButtonListener();
			visListener = new VisualListener();
			open.addActionListener(btnListener);
			visHairColor.addActionListener(visListener);
			visEmotion.addActionListener(visListener);
			visSize.addActionListener(visListener);
			visMenu.addActionListener(visListener);
			
			add(open);
			add(visHairColor);
			add(visEmotion);
			add(visSize);
			add(visMenu);
			visHairColor.setEnabled(false);
			visEmotion.setEnabled(false);
			visSize.setEnabled(false);
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
			textArea = new JTextArea();
			sp = new JScrollPane(textArea);
			sp.setPreferredSize(new Dimension(300, 600));
			add(sp);
		}
	}
	public void open() {

		Panel1 p1 = new Panel1();
		Panel2 p2 = new Panel2();
		Panel3 p3 = new Panel3();

		add(p1, BorderLayout.NORTH);
		add(p2, BorderLayout.CENTER);
		add(p3, BorderLayout.EAST);

		setTitle("사진을 고르면, MS Face Recognition API를 이용해 분석해드립니다");
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		RCaller caller = new RCaller();
		caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
	}
	private class VisualListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == visMenu) {
				ComPic cp = new ComPic();
				cp.open();
			}
			
			if(e.getSource() == visHairColor) {
		        try{ // 자바의 배열이 R로 가게 되면, 벡터가 되고, R의 벡터가 JAVA로 오면 배열이 된다.
		            //Creating an instance of class RCaller
		            RCaller caller = new RCaller();     
		             
		            //Important. Where is the Rscript?
		            //This is Rscript.exe in windows
		            caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
		            
		            caller.addRCode("setwd('d:/example')");
		            caller.addRCode("library(jsonlite)");
		            caller.addRCode("library(httr)");
		            caller.addRCode("library(plotrix)");
		            caller.addRCode("face<-fromJSON('faceresult.json')");
		            caller.addRCode("haircolor<-face$faceAttributes$hair$hairColor");
		            caller.addRCode("haircolor<- as.vector(haircolor[[1]])");
		            caller.addRCode("colorlist<- c(haircolor[1]$color)");
		            caller.addRCode("fixcolor <- colorlist");
		            caller.addRCode("for(i in 1:6) if(colorlist[i] == 'other') fixcolor[i] <- 'white'");
		            caller.addRCode("for(i in 1:6) if(colorlist[i] == 'blond') fixcolor[i] <- 'yellow'");
		            
		            File file1 = caller.startPlot();
		            caller.addRCode("pie3D(haircolor[[2]],labels = colorlist, main = '머리카락 색깔', col = fixcolor)");
		            caller.addRCode("legend('topright',colorlist, pch=15, col = fixcolor)");
		            caller.runAndReturnResult("print('haircolor')");
		            
					imgIcon = new ImageIcon(file1.toString());
					Image temp = imgIcon.getImage(); // ImageIcon을 Image로 변환.
					Image newImg = temp.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(newImg); // Image로 ImageIcon
					VisualPanel vp = new VisualPanel();
					vp.create(icon);
		            
		        }catch (Exception ex){
		            System.out.println(ex.toString());
		        }
		    }
			
			if(e.getSource() == visEmotion) {
				try{ // 자바의 배열이 R로 가게 되면, 벡터가 되고, R의 벡터가 JAVA로 오면 배열이 된다.
		            //Creating an instance of class RCaller
		            RCaller caller = new RCaller();     
		             
		            //Important. Where is the Rscript?
		            //This is Rscript.exe in windows
		            caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
		            
		            caller.addRCode("setwd('d:/example')");
		            caller.addRCode("library(jsonlite)");
		            caller.addRCode("library(httr)");
		            caller.addRCode("face<-fromJSON('faceresult.json')");
		            caller.addRCode("emotion <- face$faceAttributes$emotion");
		            caller.addRCode("emotion <- as.matrix(emotion)");
		            caller.addRCode("emotion");
		            caller.addRCode("names(emotion) <- c('화남','경멸','역겨움','공포','행복','중립','슬픔','놀람')");
		            caller.addRCode("myemotion <- c()");
		            caller.addRCode("for(i in 1:8) if(emotion[i]!=0) myemotion[i] <- names(emotion[i])");

		            File file1 = caller.startPlot();
		            caller.addRCode("pie(emotion, clockwise = TRUE, labels = myemotion, col = rainbow(8), main = '감정 비율')");
		            caller.runAndReturnResult("print('emotion')");
		            
					imgIcon = new ImageIcon(file1.toString());
					Image temp = imgIcon.getImage(); // ImageIcon을 Image로 변환.
					Image newImg = temp.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
					ImageIcon icon = new ImageIcon(newImg); // Image로 ImageIcon
					VisualPanel vp = new VisualPanel();
					vp.create(icon);
		            
		        }catch (Exception ex){
		            System.out.println(ex.toString());
		        }
		    }
			if(e.getSource() == visSize) {
				 try{ // 자바의 배열이 R로 가게 되면, 벡터가 되고, R의 벡터가 JAVA로 오면 배열이 된다.
			            //Creating an instance of class RCaller   
			             
			            RCaller caller = new RCaller();     
			            RCode test = new RCode();
			             
			            //Important. Where is the Rscript?
			            //This is Rscript.exe in windows
			            caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
			            
			            caller.addRCode("setwd('d:/example')");
			            caller.addRCode("library(jsonlite)");
			            caller.addRCode("library(httr)");
			            caller.addRCode("face<-fromJSON('faceresult.json')");
			            caller.addRCode("myface <- face$faceRectangle");
			            caller.addRCode("myface <- as.matrix(myface)");
			            caller.addRCode("gender <- face$faceAttributes$gender");

			            File file1 = caller.startPlot();
			            caller.addRCode("barplot(myface,col = 'green', main = '얼굴크기 및 비율')");
			            caller.addRCode("if(gender=='male') abline(h = 200)");
			            caller.addRCode("if(gender=='female') abline(h = 190)");
			            caller.runAndReturnResult("print('emotion')");
			            
						imgIcon = new ImageIcon(file1.toString());
						Image temp = imgIcon.getImage(); // ImageIcon을 Image로 변환.
						Image newImg = temp.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(newImg); // Image로 ImageIcon
						VisualPanel vp = new VisualPanel();
						vp.create(icon);
			            
			        }catch (Exception ex){
			            System.out.println(ex.toString());
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
							visHairColor.setEnabled(true);
							visEmotion.setEnabled(true);
							visSize.setEnabled(true);
							// Format and display the JSON response.
							String result = "REST Response:\n";

							String jsonString = EntityUtils.toString(entity).trim();
							FileWriter jsonfile = new FileWriter("d:\\example\\faceresult.json");
							jsonfile.write(jsonString);
							jsonfile.flush(); // stream 2배로 해주기. 있으면 좋음
							jsonfile.close();

							if (jsonString.charAt(0) == '[') {
								JSONArray jsonArray = new JSONArray(jsonString);
								result += jsonArray.toString(2) + "\n";
							}
							else if (jsonString.charAt(0) == '{') {
								JSONObject jsonObject = new JSONObject(jsonString);
								result += jsonObject.toString(2) + "\n";
							} else {
								result += jsonString + "\n";
							}
							textArea.setText(result);
						}
					}
					catch (Exception exception)
					{
						// Display error message.
						textArea.setText(exception.getMessage());
					}			
				}
			}
		}
	}
	public static void main(String[] args) {
		FaceRecognition fr = new FaceRecognition();
		fr.open();
		RCaller caller = new RCaller();
		caller.setRscriptExecutable("C:/Program Files/R/R-3.4.3/bin/x64/Rscript.exe");
	}
	
}
