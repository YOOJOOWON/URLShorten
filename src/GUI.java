
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class GUI {



    private Frame mainFrame;
    private Panel headerPanel;
    private Label Labelb0, Labelb1, Labelb2, Labelb3;
    
    private Panel inputPanel1, inputPanel2;
    private Panel resultPanel1, resultPanel2;
    private Label LabelMin, LabelMax;
    private TextField longInput, shortInput;
    private TextField longOutput, shortOutput;
    private Panel controlPanel;
    private Panel checkPanel;
    
    URLShortner sh;
	

    public GUI() throws Exception {
    	sh = new URLShortner();
    	initGUI();
    	
    	System.out.println(Base58.IntFromBase58("123") + " " + Base58.IntFromBase58("zyx"));
    	
    	mainFrame.setVisible(true);   	
    }
    
    void initGUI(){
    	
    	  mainFrame = new Frame("BTC Calculater");
          mainFrame.setSize(600, 400);
          mainFrame.setLayout(new GridLayout(12, 2));
          mainFrame.setLocationRelativeTo(null);

          mainFrame.addWindowListener(new WindowAdapter() {
              public void windowClosing(WindowEvent e) {
                  mainFrame.setVisible(false);
            	  System.out.println("haha");
            	  sh.save();
            	  System.exit(0);           
              }
          });
          
          
          headerPanel = new Panel();
          headerPanel.setLayout(new FlowLayout());

          Labelb0 = new Label();
          Labelb1 = new Label();
          Labelb2 = new Label();
          Labelb3 = new Label();
          
          LabelMin = new Label();
          LabelMax = new Label();
          
          longInput = new TextField(30);
          shortInput = new TextField(30);
          longOutput = new TextField(30);
          shortOutput = new TextField(30);
          
          Labelb1.setAlignment(Label.CENTER);
          Labelb2.setAlignment(Label.CENTER);
          Labelb3.setAlignment(Label.CENTER);

          controlPanel = new Panel();
          controlPanel.setLayout(new FlowLayout());
          checkPanel = new Panel();
          checkPanel.setLayout(new FlowLayout());
          inputPanel1 = new Panel();
          inputPanel1.setLayout(new FlowLayout());
          inputPanel2 = new Panel();
          inputPanel2.setLayout(new FlowLayout());

          resultPanel1 = new Panel();
          resultPanel1.setLayout(new FlowLayout());
          resultPanel2 = new Panel();
          resultPanel2.setLayout(new FlowLayout());
          
          
          Button btnReset = new Button("click");
          btnReset.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  try {
                	  update_longtoshort(longInput.getText());
                	  longInput.setText("");
                      
                  } catch (Exception e1) {
                      // TODO Auto-generated catch block
                      e1.printStackTrace();
                  }
              }
          });
          
          Button btnMail = new Button("click");
          btnMail.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                  try {
                	  update_shorttolong(shortInput.getText());
                	  shortInput.setText("");
                	  
                  } catch (Exception e1) {
                      // TODO Auto-generated catch block
                      e1.printStackTrace();
                  }
              }
          });


          
          mainFrame.add(Labelb0);
          mainFrame.add(Labelb1);
          

          mainFrame.add(inputPanel1);
          mainFrame.add(resultPanel1);
          mainFrame.add(inputPanel2);
          mainFrame.add(resultPanel2);
                    
          Labelb2.setText("result : " );
          Labelb3.setText("result : " );
          
          inputPanel1.add(LabelMin);
          inputPanel1.add(longInput);
          inputPanel1.add(btnReset);
          
          resultPanel1.add(Labelb2);
          resultPanel1.add(longOutput);
          resultPanel2.add(Labelb3);
          resultPanel2.add(shortOutput);

          inputPanel2.add(LabelMax);
          inputPanel2.add(shortInput);
          inputPanel2.add(btnMail);

          Labelb2.setText("Result : ");
          Labelb3.setText("Result : ");
          LabelMin.setText("Long  to Short : " );
          LabelMax.setText("Short to Long  : " );
          
          
          

    }
    
    
    void update_longtoshort(String name){   	

    	longOutput.setText(sh.LongToShort(name));
    	
    }
    void update_shorttolong(String name){    	    	

    	shortOutput.setText(sh.ShortToLong(name));
    }
	
	
}
