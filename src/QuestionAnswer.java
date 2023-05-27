/*
 * Overall Wrapper class for a prompt
 * Includes ID, Command, Question, and Answer
 * No more setters and getters because that's literally public if you make getters and setters 
 * for every field and code bloat 
 */
class QuestionAnswer{
    public int qID;
    public String command;
    public String question;
    public String answer;

    /**
     * Creates a default question, answer, and qID tuple with null question, null answer, and null qID of -1
     */
    QuestionAnswer(){
        qID = -1;
        command = null;
        question = null;
        answer = null;
    }

    /**
     * Create new question, answer, qID tuple
     * @param qID -1 is the null qID
     * @param command set to null for no question
     * @param question set to null for no question
     * @param answer set to null for no answer
     */
    QuestionAnswer(int qID, String command, String question, String answer){
        this.qID = qID;
        this.command = command;
        this.question = question;
        this.answer = answer;
    }
}
