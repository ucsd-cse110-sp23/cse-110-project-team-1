import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

class QAPanel extends JPanel{
    Color green = new Color(188, 226, 158);
    JTextArea question;
    JTextArea answer;
    String qID;
    String defaultQ = "Q: ";
    String defaultA = "A: ";

    QAPanel(){
        setLayout(new GridLayout(2, 1));
        setBackground(green);

        question = new JTextArea(defaultQ);
        question.setLineWrap(true);
        this.add(question);
        question.setEditable(false);
        
        answer = new JTextArea(defaultA);
        answer.setLineWrap(true);
        this.add(answer);
        answer.setEditable(false);
    }

    public String getQuestionID(){
        return qID;
    }

    public String getAnswer(){
        return answer.getText();
    }
    public void changeAnswer(String newAnswer){
        answer.setText(defaultA + newAnswer);        
    }
    public void clearAnswer(){
        changeAnswer(defaultA);
    }

    public String getQuestion(){
        return question.getText();
    }

    /** use to change the displayed question, must input a questionID.
     * if there is no ID associated with it (example: displaying no question "Q:") use question ID = null**/
    public void changeQuestion(String newQuestion, String questionID){
        question.setText(defaultQ + newQuestion);
        qID = questionID;
        clearAnswer();
    }
    public void clearQuestion(){
        changeQuestion(defaultQ, null);
    }

}

class MainPanel extends JPanel{
    JButton recButton;
    boolean isRec = false;
    String startBlurb = "New Question";
    String stopBlurb = "Stop Recording";

    QAPanel qaPanel;

    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);
    
    MainPanel(){
        this.setPreferredSize(new Dimension(400, 20)); // set size of task
        this.setBackground(gray); // set background color of task
    
        this.setLayout(new BorderLayout()); // set layout of task

        qaPanel = new QAPanel();
        this.add(qaPanel, BorderLayout.CENTER);

        recButton = new JButton(startBlurb);
        this.add(recButton, BorderLayout.SOUTH);
    }

    public QAPanel getQaPanel(){
        return qaPanel;
    }

    public JButton getRecButton() {
        return recButton;
    }

    public void changeRecording(){
        if (isRec){
            recButton.setText(startBlurb);
            //stop recording
        } else {
            recButton.setText(stopBlurb);
            //start recording
        }
        isRec = !isRec;
        //TODO: implement stop and start recording
    }
}

class PromptHistory extends JPanel{
    JLabel title;
    JList<String> history;
    JScrollPane histPane;
    //TODO: switch out list for actual button with Question ID'S
    PromptHistory(){
        // history = new JList<String>(getHistory());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        title = new JLabel("Prompt History");
        this.add(title, BorderLayout.NORTH);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadHist(); //update list
        histPane = new JScrollPane(history);
        this.add(histPane, BorderLayout.CENTER);
        histPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        // this.add(history, BorderLayout.CENTER);
    }

    public void loadHist(){
        //TODO: implement loading history into list
        String[] example = {"apple", "banana", "truffle", "death", "lasdjf askdjfasdjfagh woeg hiseroignsofjasdkjf kasda"};
        history = new JList<String>(example);
    }
}

// the side panel
class SideBar extends JPanel{
    PromptHistory promptHistory;
    JButton clearButton;

    SideBar(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        promptHistory = new PromptHistory();
        this.add(promptHistory, BorderLayout.CENTER);
        promptHistory.setAlignmentX(Component.CENTER_ALIGNMENT);

        clearButton = new JButton("Clear All");
        this.add(clearButton, BorderLayout.SOUTH);
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public JButton getClearButton(){
        return clearButton;
    }

    public void clearHistory(){
        //TODO: implement clearing history
    }
}

// the main app
public class SayIt extends JFrame{

    private MainPanel mainPanel;
    private JButton recButton;

    private SideBar sideBar;
    private JButton clearButton;

    boolean shouldFill = true;

    public MainPanel getMainPanel(){
        return mainPanel;
    }

    public SideBar getSideBar(){
        return sideBar;
    }

    public static void main(String[] args){
        new SayIt();
    }

    public SayIt() {
        setTitle("SayIt Assistant");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setSize(400, 600);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (shouldFill) {
                        //natural height, maximum width
                        c.fill = GridBagConstraints.HORIZONTAL;
        }

        sideBar = new SideBar();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 0.25;
        this.add(sideBar, c);

        mainPanel = new MainPanel();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 4;
        c.gridx = 1;
        c.weighty = 1.0;
        c.weightx = 1.0;
        this.add(mainPanel, c);

        recButton = mainPanel.getRecButton();
        clearButton = sideBar.getClearButton();

        addListeners();
    }

    public void addListeners() {
        recButton.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.changeRecording();
            }
        }
        );
        clearButton.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sideBar.clearHistory();
            }
        }
        );
    }
}
