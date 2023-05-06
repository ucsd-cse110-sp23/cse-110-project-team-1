import java.awt.*;
import java.awt.event.*;
import java.io.*;

// import javax.management.Query;
// import javax.sound.sampled.*;
import javax.swing.*;
// import javax.swing.event.ChangeEvent;

class QuestionAnswer{
    private int qID;

    private String question;
    private String answer;

    QuestionAnswer(){
        qID = -1;
        question = null;
        answer = null;
    }

    QuestionAnswer(int qID, String question, String answer){
        this.qID = qID;
        this.question = question;
        this.answer = answer;
    }

    public void setqID(int qID) {
        this.qID = qID;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getqID() {
        return qID;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }    
}

class QAPanel extends JPanel{
    Color green = new Color(188, 226, 158);
    
    QuestionAnswer qaPrompt;//TODO: refactor to use QuestionAnswer
    
    JTextArea question;
    JTextArea answer;
    //int qID;
    String prefixQ = "Q: ";
    String prefixA = "A: ";

    QAPanel(QuestionAnswer questionAnswer){
        setLayout(new GridLayout(2, 1));
        setBackground(green);

        qaPrompt = questionAnswer;

        question = new JTextArea();
        question.setLineWrap(true);
        this.add(question);
        question.setEditable(false);
        
        answer = new JTextArea();
        answer.setLineWrap(true);
        this.add(answer);
        answer.setEditable(false);

        updateDisplay();
    }

    //gets the information directly from qaPrompt, has the actual question, answer, and id.
    public QuestionAnswer getQuestionAnswer(){
        return qaPrompt;
    }

    public int getQuestionID(){
        return qaPrompt.getqID();
    }

    public String getQuestion(){
        return qaPrompt.getQuestion();
    }

    public String getAnswer(){
        return qaPrompt.getAnswer();
    }


    public void setQuestionID(int id) {
        //qID = id;
        qaPrompt.setqID(id);
    }

    /** use to CREATE a new question and to display a question, must input a questionID.
     * if there is no ID associated with it (example: displaying no question "Q:") use question ID = -1**/
    public void createQuestion(String newQuestion, int questionID){
        qaPrompt = new QuestionAnswer(questionID, newQuestion, "");
        //qID = questionID;
        clearAnswer();

        updateDisplay();
    }

    /** use to CHANGE the displayed question, must input a questionID.
     * if there is no ID associated with it (example: displaying no question "Q:") use question ID = -1**/
    public void changeQuestion(QuestionAnswer newQaPrompt){
        qaPrompt = newQaPrompt;
        qaPrompt.setQuestion(qaPrompt.getQuestion());
        //qID = questionID;
        clearAnswer();

        updateDisplay();
    }

    public void changeAnswer(String newAnswer){
        qaPrompt.setAnswer(newAnswer);
        // answer.setText(prefixA + newAnswer);
        
        updateDisplay();
    }



    public String getQuestionText(){
        return question.getText();
    }

    public String getAnswerText(){
        return answer.getText();
    }


    public void clearAnswer(){
        changeAnswer("");
    }

    //clears display
    public void clearDisplay(){
        createQuestion(prefixA, -1);
    }

    public void updateDisplay(){
        if (getQuestion() != null){
            question.setText(prefixQ + getQuestion());
        } else {
            question.setText(prefixQ);
        }

        if (getAnswer() != null){
            answer.setText(prefixA + getAnswer());
        } else {
            answer.setText(prefixA);
        }
    }
}

class MainPanel extends JPanel{
    JButton recButton;
    boolean isRec = false;
    String startBlurb = "New Question";
    String stopBlurb = "Stop Recording";

    QAPanel qaPanel;

    JRecorder recorder;

    PromptHistory history;

    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);
    
    MainPanel(){
        history = new PromptHistory();
        this.setPreferredSize(new Dimension(400, 20)); // set size of task
        this.setBackground(gray); // set background color of task
    
        this.setLayout(new BorderLayout()); // set layout of task

        qaPanel = new QAPanel(new QuestionAnswer());
        this.add(qaPanel, BorderLayout.CENTER);

        recButton = new JButton(startBlurb);
        this.add(recButton, BorderLayout.SOUTH);

        recorder = new JRecorder();
    }

    public QAPanel getQaPanel(){
        return qaPanel;
    }

    public JButton getRecButton() {
        return recButton;
    }

    /*
     * Handles finishing recording and displayng to qaPanel 
     */
    private void finishRecording() {
        recorder.finish();
        String question;
        String answer;
        // PromptHistory history = new PromptHistory();
        // try {
            // question = JWhisper.transcription(null);
            question = "this is the test";
            history.saveQuestion(question);
            qaPanel.createQuestion(question,0);
            // answer = JChatGPT.run(question);
            answer = "test answer";
            qaPanel.changeAnswer(answer);
            History.addEntry(question, answer);
        // } catch( IOException io) {
        //     io.printStackTrace();
        //     System.out.println("IO exception at Whisper transcription");
        // } catch (InterruptedException ex) {
        //     ex.printStackTrace();
        //     System.out.println("Interupt exception chatGPT");
        // }
    }

    public void changeRecording(){
        if (isRec){
            recButton.setText(startBlurb);
            finishRecording();
        } else {
            recButton.setText(stopBlurb);
            recorder.start();
        }
        isRec = !isRec;
    }
}

class PromptHistory extends JPanel{
    JLabel title;
    JPanel history;
    JScrollPane histPane;
    //TODO: switch out list for actual button with Question ID'S
    PromptHistory(){
        history = new JPanel(new GridLayout(10,1,10,5));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        title = new JLabel("Prompt History");
        this.add(title, BorderLayout.NORTH);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton button1 = new JButton("button1");
        history.add(button1);

        loadHist(); //update list
        histPane = new JScrollPane(history);
        this.add(histPane, BorderLayout.CENTER);
        histPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // PromptHistory(){
    //     history = new JPanel();
    //     history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS)); // add this line
    //     setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    //     title = new JLabel("Prompt History");
    //     this.add(title, BorderLayout.NORTH);
    //     title.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    //     JButton button1 = new JButton("button1");
    //     history.add(button1);
    
    //     loadHist(); //update list
    //     histPane = new JScrollPane(history);
    //     this.add(histPane, BorderLayout.CENTER);
    //     histPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    //     history.setPreferredSize(new Dimension(200, 400)); // for example
    // }

    public void loadHist(){
        //TODO: implement loading history into list
        // String[] example = {"apple", "banana", "truffle", "death", "lasdjf askdjfasdjfagh woeg hiseroignsofjasdkjf kasda"};
        // history = new JList<String>(example);
    }

    public void saveQuestion(String questionName){
        JButton button1 = new JButton(questionName);
        history.add(button1);
        history.revalidate();
        history.repaint();
        // histPane.add(history);
        histPane.setViewportView(history);
        histPane.revalidate();
        histPane.repaint();
    }
    // public void saveQuestion(String questionName){

    //     JButton button1 = new JButton(questionName);
    //     history.add(button1);
    //     history.revalidate();
    //     history.repaint();
    //     histPane.setViewportView(history);
    //     histPane.revalidate();
    //     histPane.repaint();
    // }
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
        History.clear();
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
        // setVisible(true);
        setSize(600, 600); //400, 600
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setUndecorated(false);
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

        setVisible(true);

        recButton = mainPanel.getRecButton();
        clearButton = sideBar.getClearButton();

        addListeners();

        History.initial();
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
