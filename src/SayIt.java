import java.awt.*;
import java.awt.event.*;
import java.io.*;

// import javax.management.Query;
// import javax.sound.sampled.*;
import javax.swing.*;
// import javax.swing.event.ChangeEvent;

import org.javatuples.Triplet;


class QAPanel extends JPanel{
    private final String EMPTY_QUESTION = null;
    private final String EMPTY_COMMAND = null;
    private final int EMPTY_ID = -1;

    Color green = new Color(188, 226, 158);
    
    QuestionAnswer qaPrompt;
    
    private JTextArea question;
    private JTextArea answer;
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
        return qaPrompt.qID;
    }

    public String getQuestion(){
        return qaPrompt.question;
    }

    public String getAnswer(){
        return qaPrompt.answer;
    }


    public void setQuestionID(int id) {
        //qID = id;
        qaPrompt.qID = id;
    }

    /**
     * create and display a new QuestionAnswer with no answer (null answer) on the question answer panel
     * @param newQuestion question to be stored
     * @param questionID id of questionAnswer (put -1 if no question)
     */
     public void createQuestion(String command, String newQuestion, int questionID){
        qaPrompt = new QuestionAnswer(questionID, command,newQuestion, "");
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
            createQuestion(EMPTY_COMMAND, EMPTY_QUESTION, EMPTY_ID);
        }
        qaPrompt.answer = newAnswer;
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
    JButton dltButton;
    boolean isRec = false;
    String startBlurb = "New Question";
    String stopBlurb = "Stop Recording";
    String deletBlurd = "Delete";

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

        dltButton = new JButton(deletBlurd);
        this.add(dltButton, BorderLayout.EAST);
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

    public JButton getdltButton(){
        return dltButton;
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
        if (qa.question.length() > maxCharLimit){
            this.setText(qa.question.substring(0, maxCharLimit) + "...");
        } else if (qa.question.length() <= 0) {
            this.setText(" ");//to avoid the button from being too thin
        } else {
            this.setText(qa.question);
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
    AccountMediator histClass;

    PromptHistory(){
        // history = new JList<String>(getHistory());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        title = new JLabel("Prompt History");
        this.add(title, BorderLayout.NORTH);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadHistUI(); //update list
        histPane = new JScrollPane(history);
        //TODO: figure out how to get ScrollPane to not collapse when the width becomes too large
        //right now am using 200 arbitrarily.
        histPane.setMinimumSize(new Dimension(200, HEIGHT));
        histPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        histPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(histPane, BorderLayout.CENTER);
        histPane.setAlignmentX(Component.CENTER_ALIGNMENT);
    }


    public void loadHistUI(){
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
        history.add(new JPanel(), c,-1);
        // for (int i = 0; i < 50; i++){
        //     addQA(new QuestionAnswer(0," ", "great"));
        //     addQA(new QuestionAnswer(0,"hello my name is not something you know " + i, "great"));
        // }
    }

    public JPanel getHistory(){
        return history;
    }

    /**
     * @param qa object of QuestionAnswer type with question and answer
     * @return newly added question button
     */
    public RecentQuestion addQA(QuestionAnswer qa){
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

        return recentQ;
    }

    public void dltQuestion(RecentQuestion currQ){
        //delete the currQ Jbutton on promptHistory
        history.remove(currQ);
        revalidate();
    }

    /**
     * only does the ui part of clearing questions (removes all buttons)
     */
    public void clearAll() {
        //delete all buttons
        Component[] components = history.getComponents();
        for (Component component : components) {
            if (component instanceof RecentQuestion) {
                history.remove(component);
            }
        }
        revalidate();
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

    public PromptHistory getPromptHistory(){
        return promptHistory;
    }

    /**
     * only does the UI part of clearing the buttons from prompt history
     */
    public void clearHistory(){
        promptHistory.clearAll();
    }
}

// the main app
public class SayIt extends JFrame{

    private MainPanel mainPanel;
    private JButton recButton;
    private JButton dltButton;

    private SideBar sideBar;
    private JButton clearButton;
    private static RecentQuestion currQ;

    boolean shouldFill = true;

    JChatGPT chatGPT;
    JWhisper whisper;
    JRecorder recorder;

    // testing purpose
    //int i;

    AccountMediator histClass;

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
        new SayIt(new JChatGPT(), new JWhisper(), new JRecorder(), null);
    }

    // MainPanel mainPanel
    /**
     * starts and sets up application
     * @param chatGPT chatGPT used, use MockGPT for mock version of chatGPT
     * @param whisper 
     * @param recorder
     */
    public SayIt(JChatGPT chatGPT, JWhisper whisper, JRecorder recorder, String saveFile) {
        //i = 0;

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
        histClass = new AccountMediator();

        sideBar = new SideBar();
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridx = 0;
        c.weighty = 1.0;
        c.weightx = 0.25;
        this.add(sideBar, c);
        // sideBar.promptHistory.loadHist(saveFile);

        // Load history and add listener
        //TODO fix HISTORY and Command
        for (Triplet<Integer,String,String> entry : histClass.initial(saveFile)) {
            RecentQuestion recentQ = sideBar.promptHistory.addQA(new QuestionAnswer(entry.getValue0(), null,entry.getValue1(), entry.getValue2()));
            addListenerToRecentQ(recentQ);
        }

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
        dltButton = mainPanel.getdltButton();

        dltButton.setEnabled(false);

        if (histClass.entries.size() == 0) {
            clearButton.setEnabled(false);
        }

        addListeners();

    }

    private void addListenerToRecentQ(RecentQuestion recentQ){
        recentQ.addMouseListener(
            new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println(recentQ.getQuestionAnswer().question);
                    showPromptHistQuestionOnQAPrompt(recentQ);
                }
            }
        );
    }

    private RecentQuestion finishRecording() {
        recorder.finish();
        String question;
        String answer;
        Parser parser;
        QAPanel qaPanel = mainPanel.getQaPanel();
        try {
            question = whisper.transcription(null);
            parser = new Parser(question);
            parser.Parse();

            System.out.println(question);
            System.out.println(parser.command);

            if (parser.command == null) {
                qaPanel.createQuestion(parser.COMMAND_NOT_FOUND, "Invalid Command Entered", 0);
                answer = parser.COMMAND_NOT_FOUND;
                qaPanel.changeAnswer(answer);
                RecentQuestion recentQ = getSideBar().getPromptHistory().addQA(qaPanel.getQuestionAnswer());
                addListenerToRecentQ(recentQ);
                currQ = recentQ;

                dltButton.setEnabled(true);
                clearButton.setEnabled(true);

                qaPanel.setQuestionID(histClass.addEntry(question, answer));
                return recentQ;

            } else if (parser.command.equals(parser.QUESTION)) {
                qaPanel.createQuestion(parser.QUESTION,parser.getPrompt(),0); //TODO accept/pase command
                answer = chatGPT.run(parser.getPrompt());
                qaPanel.changeAnswer(answer);

                RecentQuestion recentQ = getSideBar().getPromptHistory().addQA(qaPanel.getQuestionAnswer());
                addListenerToRecentQ(recentQ);
                currQ = recentQ;

                dltButton.setEnabled(true);
                clearButton.setEnabled(true);

                qaPanel.setQuestionID(histClass.addEntry(question, answer));
                return recentQ;
            } else if (parser.command.equals(parser.DELETE)) {
                deleteClicked();
                return currQ;
            } else if (parser.command.equals(parser.CLEAR_ALL)) {
                clearClicked();
                return currQ;
            } 
            
            // answer = chatGPT.run(question);
            // answer = "test answer " + i;
            // i++;
            // qaPanel.changeAnswer(answer);
            // int numEntriesJson = History.initial(null).size();

            // RecentQuestion recentQ = getSideBar().getPromptHistory().addQA(qaPanel.getQuestionAnswer());
            // addListenerToRecentQ(recentQ);
            // set the current question on screen to be 
            // currQ = recentQ;
            // set the delete button to be enabled (can delete now)
            // dltButton.setEnabled(true);
            // clearButton.setEnabled(true);

            // qaPanel.setQuestionID(histClass.addEntry(question, answer));

            return currQ;
        } catch( IOException io) {
            io.printStackTrace();
            System.out.println("IO exception at Whisper transcription");
            qaPanel.changeAnswer("Sorry, we didn't quite catch that");
            return null;
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.out.println("Interupt exception chatGPT");
            return null;
        }
    }
    
    /*
     * This method sets the RecentQustion(button) 
     * that is showing QuestionAnswer in QAPanel 
     */
    public static RecentQuestion setCurrQ(RecentQuestion recentQ){
        currQ = recentQ;
        return recentQ;
    }

    /*
     * This method get the RecentQustion(button) in QAPanel 
     */
    public static RecentQuestion getCurrQ(){
        return currQ;
    }

    /**
     * @return return newly added question button. 
     * If no question button is created, return null
     */
    public RecentQuestion changeRecording(){
        RecentQuestion recentQ;
        if (mainPanel.getIsRec()){
            recentQ = finishRecording();
            mainPanel.stopRecording();
            return recentQ;
        } else {
            // recorder.start();
            mainPanel.startRecording();
            if (!recorder.start()){
                mainPanel.stopRecording();
                mainPanel.getQaPanel().changeAnswer("Please connect microphone");
            }

            return null;
        }
    }

    public AccountMediator getHistClass(){
        return histClass;
    }

    public void showPromptHistQuestionOnQAPrompt(RecentQuestion recentQ){
        SayIt.setCurrQ(recentQ);
        QuestionAnswer toDisplay = recentQ.getQuestionAnswer();
        QAPanel qaPanel = mainPanel.getQaPanel();
        qaPanel.changeQuestion(toDisplay);
        dltButton.setEnabled(true);
    }

    public void deleteClicked(){
        if(currQ != null){
            histClass.removeEntry(currQ.getQuestionAnswer().qID);
            sideBar.getPromptHistory().dltQuestion(currQ);
            mainPanel.qaPanel.changeQuestion(new QuestionAnswer());
            currQ = null;
            dltButton.setEnabled(false);
            if (histClass.entries.size() == 0 ) {
                clearButton.setEnabled(false);
            }
        }
    }

    public void clearClicked(){
        histClass.clear();
        sideBar.clearHistory();        
        mainPanel.qaPanel.changeQuestion(new QuestionAnswer());
        currQ = null;
        clearButton.setEnabled(false);
        dltButton.setEnabled(false);
    }

    public void addListeners() {
        recButton.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // int numEntriesJson = History.initial(null).size();
                // get the added prompt button
                RecentQuestion recentQ =  changeRecording();
                // update the QApanel when clicking the question button
                if(recentQ != null) {
                    addListenerToRecentQ(recentQ);
                }
            }
        }
        );
        clearButton.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                histClass.clear();
                sideBar.clearHistory();
                
                mainPanel.qaPanel.changeQuestion(new QuestionAnswer());
                currQ = null;
                clearButton.setEnabled(false);
                dltButton.setEnabled(false);
            }
        }
        );
        dltButton.addActionListener(
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteClicked();
            }
        }
        );
    }
}

