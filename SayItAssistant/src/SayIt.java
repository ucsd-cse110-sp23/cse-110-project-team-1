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
    
    QuestionAnswer qaPrompt;
    
    JTextArea question;
    JTextArea answer;
    //int qID;
    String prefixQ = "Q: ";
    String prefixA = "A: ";

    //DO NOT pass in a null QuestionAnswer
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
        //qID = questionID;
        updateDisplay();
    }

    public void changeAnswer(String newAnswer){
        qaPrompt.setAnswer(newAnswer);
        // answer.setText(prefixA + newAnswer);
        
        updateDisplay();
    }

    public String getPrefixQ(){
        return prefixQ;
    }

    public String getPrefixA(){
        return prefixA;
    }

    public String getQuestionText(){
        return question.getText();
    }

    public String getAnswerText(){
        return answer.getText();
    }


    public void clearAnswer(){
        changeAnswer(null);
    }

    //clears display
    public void clearDisplay(){
        createQuestion(null, -1);
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

    // JChatGPT chatGPT;
    // JWhisper whisper;
    // JRecorder recorder;

    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);

    // JPanel history;
    
    // JChatGPT chatGPT, JWhisper whisper, JRecorder recorder, JPanel history
    MainPanel(){
        this.setPreferredSize(new Dimension(400, 20)); // set size of task
        this.setBackground(gray); // set background color of task
    
        this.setLayout(new BorderLayout()); // set layout of task

        qaPanel = new QAPanel(new QuestionAnswer());
        this.add(qaPanel, BorderLayout.CENTER);

        recButton = new JButton(startBlurb);
        this.add(recButton, BorderLayout.SOUTH);

        // this.chatGPT = chatGPT;
        // this.whisper = whisper;
        // this.recorder = recorder;
        // // recorder = new JRecorder();
        // this.history = history;
    }

    public QAPanel getQaPanel(){
        return qaPanel;
    }

    public JButton getRecButton() {
        return recButton;
    }

    public boolean getIsRec(){
        return isRec;
    }
    /*
     * Handles finishing recording and displayng to qaPanel 
     */
    // protected void finishRecording() {
    //     recorder.finish();
    //     String question;
    //     String answer;
    //     try {
    //         question = whisper.transcription(null);
    //         qaPanel.createQuestion(question,0);
    //         answer = chatGPT.run(question);
    //         qaPanel.changeAnswer(answer);
    //         History.addEntry(question, answer);
    //     } catch( IOException io) {
    //         io.printStackTrace();
    //         System.out.println("IO exception at Whisper transcription");
    //         qaPanel.changeAnswer("Sorry, we didn't quite catch that");
    //     } catch (InterruptedException ex) {
    //         ex.printStackTrace();
    //         System.out.println("Interupt exception chatGPT");
    //     }
    // }


    public void stopRecording(){
        recButton.setText(startBlurb);
        isRec = false;
    }

    public void startRecording(){
        recButton.setText(stopBlurb);
        isRec = true;
    }
    // public void changeRecording(){
    //     if (isRec){
    //         recButton.setText(startBlurb);
    //     } else {
    //         recButton.setText(stopBlurb);
    //     }
    //     isRec = !isRec;
    // }
}

class RecentQuestion extends JButton{
    QuestionAnswer questionAnswer;
    int maxCharLimit = 20;
    RecentQuestion(QuestionAnswer qa){
        questionAnswer = qa;
        if (qa.getQuestion().length() > maxCharLimit){
            this.setText(qa.getQuestion().substring(0, maxCharLimit) + "...");
        } else {
            this.setText(qa.getQuestion());
        }
    }

    QuestionAnswer getQuestionAnswer(){
        return questionAnswer;
    }
}

class PromptHistory extends JPanel{
    JLabel title;
    JPanel history;
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
        //TODO: figure out how to get ScrollPane to not collapse when the width becomes too large
        //right now am using 200 arbitrarily.
        histPane.setMinimumSize(new Dimension(200, HEIGHT));
        histPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        histPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(histPane, BorderLayout.CENTER);
        histPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void loadHist(){
        //TODO: implement loading history into list
        // String[] example = {"apple", "banana", "truffle", "death", "lasdjf askdjfasdjfagh woeg hiseroignsofjasdkjf kasda"};
        // history = new JList<String>(example);
        history = new JPanel();
        history.setLayout(new GridBagLayout());
        // history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
        // for (int i = 0; i < 50; i++){
        //     addQA(new QuestionAnswer(0,"hello " + i, "great"));
        //     addQA(new QuestionAnswer(0,"hello my name is not something you know " + i, "great"));
        // }
    }

    public JPanel getHistory(){
        return history;
    }

    public void addQA(QuestionAnswer qa){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        //c.weighty = 1.0;
        RecentQuestion recentQ = new RecentQuestion(qa);
        history.add(recentQ, c, 0);
        // recentQ.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        History.clear();
        //TODO: implement clearing history
    }

    public PromptHistory getPromptHistory(){
        return promptHistory;
    }
}

// the main app
public class SayIt extends JFrame{

    private MainPanel mainPanel;
    private JButton recButton;

    private SideBar sideBar;
    private JButton clearButton;

    boolean shouldFill = true;

    JChatGPT chatGPT;
    JWhisper whisper;
    JRecorder recorder;

    public MainPanel getMainPanel(){
        return mainPanel;
    }

    public SideBar getSideBar(){
        return sideBar;
    }

    // new MainPanel(new JChatGPT(), new JWhisper(), new JRecorder())
    public static void main(String[] args){
        new SayIt(new JChatGPT(), new JWhisper(), new JRecorder());
    }

    // MainPanel mainPanel
    public SayIt(JChatGPT chatGPT, JWhisper whisper, JRecorder recorder) {
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

        this.chatGPT = chatGPT;
        this.whisper = whisper;
        this.recorder = recorder;

        sideBar = new SideBar();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 0.25;
        this.add(sideBar, c);

        this.mainPanel = new MainPanel();
        //TODO: might change so that SayIt() doesn't need mainpanel passed in
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

    private void finishRecording() {
        recorder.finish();
        String question;
        String answer;
        QAPanel qaPanel = mainPanel.getQaPanel();
        try {
            question = whisper.transcription(null);
            qaPanel.createQuestion(question,0);
            answer = chatGPT.run(question);
            qaPanel.changeAnswer(answer);
            getSideBar().getPromptHistory().addQA(qaPanel.getQuestionAnswer());
            History.addEntry(question, answer);
        } catch( IOException io) {
            io.printStackTrace();
            System.out.println("IO exception at Whisper transcription");
            qaPanel.changeAnswer("Sorry, we didn't quite catch that");
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Interupt exception chatGPT");
        }
    }

    public void changeRecording(){
        if (mainPanel.getIsRec()){
            mainPanel.stopRecording();
            finishRecording();
        } else {
            recorder.start();
            mainPanel.startRecording();
        }
    }

    public void addListeners() {
        recButton.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeRecording();
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
