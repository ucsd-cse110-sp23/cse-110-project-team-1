import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.sound.sampled.LineUnavailableException;
// import javax.management.Query;
// import javax.sound.sampled.*;
import javax.swing.*;
// import javax.swing.event.ChangeEvent;

class QuestionAnswer{
    private int qID;

    private String question;
    private String answer;

    /**
     * Creates a default question, answer, and qID tuple with null question, null answer, and null qID of -1
     */
    QuestionAnswer(){
        qID = -1;
        question = null;
        answer = null;
    }

    /**
     * Create new question, answer, qID tuple
     * @param qID -1 is the null qID
     * @param question set to null for no question
     * @param answer set to null for no answer
     */
    QuestionAnswer(int qID, String question, String answer){
        this.qID = qID;
        this.question = question;
        this.answer = answer;
    }

    /**
     * sets qID for the question, answer, qID tuple
     * @param qID -1 is the null/no qID
     */
    public void setqID(int qID) {
        this.qID = qID;
    }

    /**
     * sets question for question, answer, qID tuple
     * @param question set to null for no question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * sets answer for the question, answer, qID tuple
     * @param answer set to null for no answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * @return -1 is null/no qID
     */
    public int getqID() {
        return qID;
    }

    /** 
     * @return String of the question asked
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return String of the answer to the question
     */
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

    /**
     * Creates new panel where the question and answer are displayed
     * @param questionAnswer pass in the QuestionAnswer of the question and answer to be displayed (-1 qID and null question and answer if no question asked)
     */
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

    /**
     * @return gets the QuestionAnswer object (qaPrompt) storing the displayed question and answer and their id
     */
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

    /**
     * create and display a new QuestionAnswer with no answer (null answer) on the question answer panel
     * @param newQuestion question to be stored
     * @param questionID id of questionAnswer (put -1 if no question)
     */
     public void createQuestion(String newQuestion, int questionID){
        qaPrompt = new QuestionAnswer(questionID, newQuestion, "");
        //qID = questionID;
        clearAnswer();

        updateDisplay();
    }

    /**
     * change the question and answer displayed to a new QuestionAnswer
     * @param newQaPrompt the QuestionAnswer whose question and answer we should display
     */
    public void changeQuestion(QuestionAnswer newQaPrompt){
        qaPrompt = newQaPrompt;
        //qID = questionID;
        updateDisplay();
    }

    /**
     * change the answer of the QuestionAnswer being displayed on the QAPanel
     * If qaPrompt is currently null, then it creates a new QuestionAnswer of null question and -1 qID
     * @param newAnswer the answer to set the displayed QuestionAnswer's answer to
     */
    public void changeAnswer(String newAnswer){
        //stop gap, might change
        if (qaPrompt == null){
            createQuestion(null, -1);
        }
        qaPrompt.setAnswer(newAnswer);
        // answer.setText(prefixA + newAnswer);
        
        updateDisplay();
    }

    /**
     * @return the words preceding each question when displayed (ex "Q: ")
     */
    public String getPrefixQ(){
        return prefixQ;
    }

    /**
     * @return the words preceding each answer when displayed in QAPanel (ex. "A: ")
     */
    public String getPrefixA(){
        return prefixA;
    }

    /**
     * @return exactly the string displayed in the JTextArea for the question (prefixQ + question)
     */
    public String getQuestionText(){
        return question.getText();
    }

    /**
     * @return exactly the string displayed in the JTextArea for the answer (prefixA + answer)
     */
    public String getAnswerText(){
        return answer.getText();
    }

    /**
     * @return sets the answer of the displayed QuestionAnswer (qaPrompt) to null
     */
    public void clearAnswer(){
        changeAnswer(null);
    }

    /**
     * sets the displayed QuestionAnswer to null and clears the display for the question and answer
     */
    public void clearDisplay(){
        changeQuestion(new QuestionAnswer());
    }

    /**
     * updates the display to show the current question and answer of the QuestionAnswer
     * will not display the question and answer if QuestionAnswer is null
     * will not display the question or answer if the respective field is null
     */
    public void updateDisplay(){
        if (getQuestionAnswer() == null){
            question.setText(prefixQ);
            answer.setText(prefixA);
            return;
        }
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

    Color gray = new Color(218, 229, 234);
    Color green = new Color(188, 226, 158);

    /**
     * creates and sets up the main panel that houses 
     * the panel that displays the question and answer
     * and the "New Question"/"Stop Recording" button
     */
    MainPanel(){
        this.setPreferredSize(new Dimension(400, 20)); // set size of task
        this.setBackground(gray); // set background color of task
    
        this.setLayout(new BorderLayout()); // set layout of task

        //Currently sets the Question Answer panel to display no question or answer upon the main panel being set up
        qaPanel = new QAPanel(new QuestionAnswer());
        this.add(qaPanel, BorderLayout.CENTER);

        recButton = new JButton(startBlurb);
        this.add(recButton, BorderLayout.SOUTH);
    }

    public String getRecStartBlurb(){ return startBlurb;}
    public String getRecStopBlurb(){ return stopBlurb;}

    /**
     * @return the panel that displays the quesiton and answer and has the QuestionAnswer displayed
     */
    public QAPanel getQaPanel(){
        return qaPanel;
    }

    /**
     * @return the "New Question"/"Stop Recording" button
     */
    public JButton getRecButton() {
        return recButton;
    }

    /**
     * @return whether the application is currently recording
     */
    public boolean getIsRec(){
        return isRec;
    }

    /**
     * sets the state (isRec) and the recButton text to when it has started recording
     */
    public void startRecording(){
        recButton.setText(stopBlurb);
        isRec = true;
    }

    /**
     * sets the state (isRec) and the recButton text to when it has stopped recording
     */
    public void stopRecording(){
        recButton.setText(startBlurb);
        isRec = false;
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
        } else if (qa.getQuestion().length() <= 0) {
            this.setText(" ");//to avoid the button from being too thin
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
        history.setBackground(new Color(0,0,0));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        history.add(new JPanel(), c);
        // for (int i = 0; i < 50; i++){
        //     addQA(new QuestionAnswer(0," ", "great"));
        //     addQA(new QuestionAnswer(0,"hello my name is not something you know " + i, "great"));
        // }
    }

    public JPanel getHistory(){
        return history;
    }

    public void addQA(QuestionAnswer qa){
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        // c.anchor = GridBagConstraints.NORTH;
        c.weightx = 1;
        // c.weighty = 0.00001;
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        //c.weighty = 1.0;
        RecentQuestion recentQ = new RecentQuestion(qa);
        history.add(recentQ, c, 0);
        // history.add(recentQ, 0);
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

    /**
     * @return panel housing the record button and 
     * the question/answer panel where the current question/answer are displayed
     */
    public MainPanel getMainPanel(){
        return mainPanel;
    }

    public SideBar getSideBar(){
        return sideBar;
    }

    /**
     * starts app
     * @param args
     */
    public static void main(String[] args){
        new SayIt(new JChatGPT(), new JWhisper(), new JRecorder());
    }

    // MainPanel mainPanel
    /**
     * starts and sets up application
     * @param chatGPT chatGPT used, use MockGPT for mock version of chatGPT
     * @param whisper 
     * @param recorder
     */
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

        //TODO: save the array list of tuples and use it to load prompt history
        History.initial();
    }

    private void finishRecording() {
        recorder.finish();
        String question;
        String answer;
        QAPanel qaPanel = mainPanel.getQaPanel();
        try {
            question = whisper.transcription(null);
            //TODO: make questionID be an actual questionID and update for each question
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
            finishRecording();
            mainPanel.stopRecording();
        } else {
            // recorder.start();
            mainPanel.startRecording();
            if (!recorder.start()){
                mainPanel.stopRecording();
                mainPanel.getQaPanel().changeAnswer("Please connect microphone");
            }
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
