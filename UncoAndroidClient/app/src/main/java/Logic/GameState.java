package Logic;

import Conexion.ClientConnector;

public class GameState {

    public static GameState game;
    public String qDefinition,
            playerName;
    public String[] answers;
    public int qNumber,
            qTime;

    private ClientConnector connector;
    private QuestionThread questionThread;
    private WaitingThread waitingThread;

    public GameState() {
        this.qDefinition = "";
        this.qNumber = 0;
        this.answers = new String[4];
        this.qTime = 0;
    }

    public static GameState getGameObject() {
        if (game == null) {
            game = new GameState();
        }
        return game;
    }

    public boolean connect(String ipAddress, String name) {
        connector = new ClientConnector(ipAddress);
        playerName = name;
        return connector.startConnection(new GameClientHandler());
    }

    public void sendAnswer(Integer index) {
        String sendAnswer = "sendAnswer:(" + playerName + ")(" + qNumber + ")(" + answers[index] + ")";
        //TODO: Ver respuesta score
        connector.makeRequest(sendAnswer);
        endQuestion();
    }

    //Manejo de Threads de Control de Pantallas
    public void setQuestionThread(QuestionThread questionThread) {
        this.questionThread = questionThread;
    }

    public void endQuestion() {
        questionThread.wake();
    }

    public void setWaitingThread(WaitingThread waitingThread) {
        this.waitingThread = waitingThread;
    }

    public void startQuestion() {
        waitingThread.wake();
    }

    public void endGame() {
        waitingThread.isLast();
        waitingThread.wake();
    }

    //TODO: crear juego para cliente

}
