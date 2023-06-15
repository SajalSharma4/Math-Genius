import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MathGenius extends JFrame implements ActionListener{
	JFrame f1=new JFrame("Math Genius");

    private JLabel questionLabel, notificationLabel, selectionLabel;
    private JTextField answerField, nameTextField;
    private JButton selectButton,submitButton,okButton,scoreButton,backButton, okNameButton;
    private JRadioButton easyButton, mediumButton, hardButton;
    private ButtonGroup buttonGroup;
    private JPanel selectionPanel,buttonPanel,spacePanel1,questionPanel,submitPanel,notificationPanel;
    private int score = 0, answer=0, difficulty = 1, p_score;
    private String p_name,p_level="Easy"; 
    ScoreBoardDatabase sb=new ScoreBoardDatabase();
    
    public MathGenius() {
        start();
    	showNameDialog();
    }

    private void start() {
        f1.setSize(400, 300);
        f1.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f1.setVisible(true);
        f1.setLocationRelativeTo(null);
        f1.setLayout(new GridLayout(0, 1));
        
        selectionLabel = new JLabel("Select Your Difficulty");
        selectionLabel.setPreferredSize(new Dimension(80, 20));
        selectionLabel.setHorizontalAlignment(JLabel.CENTER);
        selectionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        easyButton = new JRadioButton("Easy");
        easyButton.setSelected(true);
        mediumButton = new JRadioButton("Medium");
        hardButton = new JRadioButton("Hard");
        easyButton.setPreferredSize(new Dimension(80,20));
        mediumButton.setPreferredSize(new Dimension(80,30));
        hardButton.setPreferredSize(new Dimension(80,20));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(easyButton);
        buttonGroup.add(mediumButton);
        buttonGroup.add(hardButton);
        
        selectButton = new JButton("Select");
        selectButton.setPreferredSize(new Dimension(100, 40));

        selectionPanel = new JPanel();
        selectionPanel.setPreferredSize(new Dimension(250, 50));
        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(250, 50));

        selectionPanel.add(easyButton);
        selectionPanel.add(mediumButton);
        selectionPanel.add(hardButton);
        buttonPanel.add(selectButton);

        f1.add(selectionLabel);
        f1.add(selectionPanel);
        f1.add(buttonPanel);

        easyButton.addActionListener(this);
        mediumButton.addActionListener(this);
        hardButton.addActionListener(this);
        selectButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==selectButton) {
        	game();
        }
        if(e.getSource()==easyButton) {
        	difficulty=1;
        	p_level="Easy";
        }
        if(e.getSource()==mediumButton) {
        	difficulty=2;
        	p_level="Medium";
        }
        if(e.getSource()==hardButton) {
        	difficulty=3;
        	p_level="Hard";
        }
        if(e.getSource()==submitButton) {
        	checkAnswer(answerField.getText());
            answerField.requestFocus();
        }
        if(e.getSource()==okButton) {
        	f1.dispose();
            dispose();
        }
        if(e.getSource()==backButton) {
        	f1.dispose();
            dispose();
            new MathGenius();
        }
        if(e.getSource()==okNameButton) {
        	String x=nameTextField.getText();
            p_name=x;
        	dispose();
        }
        if(e.getSource()==scoreButton) {
        	f1.dispose();
            dispose();
            try {
				JFrame f=new JFrame("Score Board");
				sb.setMainFrame(f);
		    	JLabel label=new JLabel(p_level+" Mode: ");
		    	f.add(label);
				f.add(sb.createResult(p_level));
				f.add(sb.createButtons());
	        	f.setLayout(new FlowLayout());
	        	f.setSize(400, 310);
	            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            f.setVisible(true);
	            f.setLocationRelativeTo(null);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
    }
    
    private void game(){
    	f1.getContentPane().removeAll();
    	f1.repaint();

        questionLabel = new JLabel("");
        questionLabel.setPreferredSize(new Dimension(100, 40));
        questionLabel.setHorizontalAlignment(JLabel.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        notificationLabel = new JLabel("");
        notificationLabel.setPreferredSize(new Dimension(300, 50));
        notificationLabel.setHorizontalAlignment(JLabel.CENTER);
        notificationLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        answerField=new JTextField();
        answerField.setPreferredSize(new Dimension(100, 40));
        answerField.setFont(new Font("Serif", Font.PLAIN, 18));

        submitButton=new JButton("Submit");
        submitButton.setPreferredSize(new Dimension(100, 40));

        spacePanel1 = new JPanel();
        spacePanel1.setPreferredSize(new Dimension(200, 20));

        questionPanel = new JPanel();
        questionPanel.setPreferredSize(new Dimension(250, 50));
        questionPanel.add(questionLabel);
        questionPanel.add(answerField);

        notificationPanel=new JPanel();
        notificationPanel.setPreferredSize(new Dimension(250, 50));
        notificationPanel.add(notificationLabel);
     
        submitPanel = new JPanel();
        submitPanel.setPreferredSize(new Dimension(250, 50));
        submitPanel.add(submitButton);

        f1.add(spacePanel1);
        f1.add(questionPanel);
        f1.add(notificationPanel);
        f1.add(submitPanel);
        answerField.requestFocus();

        generateQuestion();
        submitButton.addActionListener(this); 
    }
	
    private void checkAnswer(String userAnswer){
        try {
            int number = Integer.parseInt(userAnswer);
            boolean check=(number==answer);
            if(check){
                generateQuestion();
                answerField.setText("");
                score++;
            }else{
                showScoreDialog();
                p_score=score;
                try {
					sb.addScoreToDb(p_name,p_score,p_level);
				} catch (Exception e) {
					e.printStackTrace();
				} 
            }
        } catch (NumberFormatException e) {
            notificationLabel.setText("*Please enter a valid integer.");
            answerField.setText("");
        }
    }

    private void generateQuestion() {
        int a, b, operator;
        Random rand = new Random();
        switch (difficulty) {
            case 1: 
                a = rand.nextInt(10)+1;
                b = rand.nextInt(10)+1;
                operator = rand.nextInt(4); 
                switch (operator) {
                    case 0:
                        notificationLabel.setText("");
                        questionLabel.setText(a + " + " + b + " = ");
                        answer= a+b;
                        break;
                    case 1:
                        if(a>b){
                            notificationLabel.setText("");
                            questionLabel.setText(a + " - " + b + " = ");
                            answer= a-b;
                            break;
                        }else{
                            notificationLabel.setText("");
                            questionLabel.setText(b + " - " + a + " = ");
                            answer=b-a;
                            break;
                        }
                    case 2:
                        notificationLabel.setText("");
                        questionLabel.setText(a + " * " + b + " = ");
                        answer= a*b;
                        break;
                    case 3:
                        notificationLabel.setText("*Only write the numbers before decimals");
                        if(a>b){
                        questionLabel.setText(a + " / " + b + " = ");
                        answer=a/b;
                        break;
                        }else{
                            questionLabel.setText(b + " / " + a + " = ");
                            answer=b/a;
                        break;
                        }
                    default:
                        break;
                }
                break;
            case 2: 
                a = rand.nextInt(100) + 30;
                b = rand.nextInt(100) + 30;
                operator = rand.nextInt(2); 
                switch (operator) {
                    case 0:
                        notificationLabel.setText("");
                        questionLabel.setText(a + " + " + b + " = ");
                        answer= a + b;
                        break;
                    case 1:
                        notificationLabel.setText("");
                        if(a>b){
                            questionLabel.setText(a + " - " + b + " = ");
                            answer= a-b;
                            break;
                        }else{
                            questionLabel.setText(b + " - " + a + " = ");
                            answer=b-a;
                            break;
                        }
                }
                break;
            case 3: 
                a = rand.nextInt(30) + 20;
                b = rand.nextInt(20) + 16;
                operator = rand.nextInt(2);
                switch (operator) {
                    case 0:
                        notificationLabel.setText("");
                        questionLabel.setText(a + " * " + b + " = ");
                        answer= a * b;
                        break;
                    case 1:
                        notificationLabel.setText("*Only write the numbers before decimals");
                        int temp = a * b;
                        questionLabel.setText(temp + " / " + a + " = ");
                        answer= b;
                        break;
                }
                break;
        }
    }

    private void showScoreDialog() {
        JDialog dialog = new JDialog(this, "Game Over");
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(null);

        JLabel spaceLabel = new JLabel("    ");
        JLabel scoreLabel = new JLabel("You solved "+score+" question(s) correctly");
        scoreLabel.setPreferredSize(new Dimension(250, 80));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));

        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 40));
        
        backButton = new JButton("Start Again");
        backButton.setPreferredSize(new Dimension(100, 40));
        
        scoreButton = new JButton("Score Board");
        scoreButton.setPreferredSize(new Dimension(150, 40));
        
        scoreButton.addActionListener(this);
        okButton.addActionListener(this);
        backButton.addActionListener(this);
        
        dialog.add(scoreLabel);
        dialog.add(backButton);
        dialog.add(spaceLabel);
        dialog.add(okButton);
        dialog.add(scoreButton);
        dialog.setVisible(true);
    }

    private void showNameDialog() {
        JDialog dialog = new JDialog(this, "Game Start");
        dialog.setLayout(new FlowLayout());
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setPreferredSize(new Dimension(150, 60));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        
        final JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(300, 50));
        emptyLabel.setHorizontalAlignment(JLabel.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        
        nameTextField=new JTextField("Guest");
        nameTextField.setPreferredSize(new Dimension(100, 30));
        
        okNameButton = new JButton("OK");
        okNameButton.setPreferredSize(new Dimension(100, 40));
        okNameButton.addActionListener(this);
        
        dialog.add(nameLabel);
        dialog.add(nameTextField);
        dialog.add(okNameButton);
        dialog.setVisible(true);
    }
	    
    public static void main(String[] args) {
        new MathGenius();
    }
}