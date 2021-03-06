package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Model.*;
import sample.Model.Cell;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This is the gameengine class that every class has an instance of in it
 */
public class GameEngine {
    //Properties

    //These are properties that is for FXML. No need to use them in uml diagram beacuse they don't effect the logic.
    @FXML private Button closeButton;
    private MediaPlayer mediaPlayer;
    @FXML private Slider soundControl;
    @FXML private Pane player_piece;
    @FXML private Pane player_piece_1;
    @FXML private Pane player_piece_2;
    @FXML private Pane player_piece_3;
    @FXML private Pane player_piece_4;
    @FXML private Pane player_piece_5;
    @FXML private Pane card_container;
    @FXML private Label card_title;
    @FXML private Label card_text;
    @FXML private Button skipbtn;
    @FXML private Label diceLabel;
    @FXML private Button buyButton;
    @FXML private Label turnlabel;
    @FXML private Button rollDice;
    @FXML private Button mortgageButton;
    @FXML private Button upgradeButton;

    //Trade popup elements
    @FXML private Button tradeButton;

    @FXML private GridPane property;
    @FXML private GridPane healthSystem;
    @FXML private GridPane money_grid;
    @FXML private GridPane name_grid;

    @FXML private Button completeTradeButton;
    @FXML private Button cancelTradeButton;
    @FXML private TextField offeredMoneyBox;
    @FXML private TextField requestedMoneyBox;
    @FXML private PasswordField buyerPasswordBox;
    @FXML private PasswordField sellerPasswordBox;
    @FXML private ListView buyerPropertiesListView;
    @FXML private ListView sellerPropertiesListView;
    @FXML private ComboBox clientsComboBox;

    //Mortgage popup elements
    @FXML private Button mortgageAcceptButton;
    @FXML private Button mortgageCancelButton;
    @FXML private Label totalMortgageEarnLabel;
    @FXML private Label totalMortgagePayLabel;
    @FXML private ListView propertiesListView;
    @FXML private ListView mortgagedPropertiesListView;

    //Build Popup elements
    @FXML private Button buildButton;
    @FXML private Button cancelButton;
    @FXML private Label houseCount;
    @FXML private Label hospitalCount;
    @FXML private ComboBox buildPropeties;

    //Build house
    @FXML private AnchorPane gamePlay;

    //Auction popup elements
    @FXML private Button auctionCompleteButton;
    @FXML private Text player1Label;
    @FXML private Text player2Label;
    @FXML private Text player3Label;
    @FXML private Text player4Label;
    @FXML private Text player1BidLabel;
    @FXML private Text player2BidLabel;
    @FXML private Text player3BidLabel;
    @FXML private Text player4BidLabel;
    @FXML private Slider player1BidSlider;
    @FXML private Slider player2BidSlider;
    @FXML private Slider player3BidSlider;
    @FXML private Slider player4BidSlider;

    //draggable game screen
    double xOffset = 0;
    double yOffset = 0;
    private  boolean startCellPassed = false;

    //Multiplayer scene elements
    @FXML private ChoiceBox player1TypeChoiceBox;
    @FXML private ChoiceBox player2TypeChoiceBox;
    @FXML private ChoiceBox player3TypeChoiceBox;
    @FXML private ChoiceBox player4TypeChoiceBox;
    @FXML private ChoiceBox player5TypeChoiceBox;
    @FXML private PasswordField player1PasswordBox;
    @FXML private PasswordField player2PasswordBox;
    @FXML private PasswordField player3PasswordBox;
    @FXML private PasswordField player4PasswordBox;
    @FXML private PasswordField player5PasswordBox;
    @FXML private TextField player1NameTextField;
    @FXML private TextField player2NameTextField;
    @FXML private TextField player3NameTextField;
    @FXML private TextField player4NameTextField;
    @FXML private TextField player5NameTextField;
    @FXML private Button player1ReadyButton;
    @FXML private Button player2ReadyButton;
    @FXML private Button player3ReadyButton;
    @FXML private Button player4ReadyButton;
    @FXML private Button player5ReadyButton;
    @FXML private Label player1ReadyStatusLabel;
    @FXML private Label player2ReadyStatusLabel;
    @FXML private Label player3ReadyStatusLabel;
    @FXML private Label player4ReadyStatusLabel;
    @FXML private Label player5ReadyStatusLabel;
    @FXML private Button multiplayerStartButton;
    @FXML private Button multiplayerBackButton;


    // These are the main properties in the uml diagram. These properties effects game logic
    private final int MAX_PLAYERS = 5; //Will be decided after pressing create game button
    private final int STARTING_MONEY = 1500;
    private final int MAX_BAN_TURN = 3;
    private int playerCount;
    private int botCount; //Newly added. Will be decided after pressing create game button
    private int turns; //Will be set to zero at the start of the game
    private ArrayList<Pane> pieces;
    private double gameVolume; //Current game volume. Will be 50% initially.

    private GameUI gameUI; //Reference to gameui component
    private GameMap gameMap; //Reference to the gamemap component
    private ArrayList<Player> players; //Reference to all players
    private Player currentPlayer; //Newly added. Current player of the turn
    private Dice dice; //Reference to the dice

    //Default constructor
    public GameEngine(){
        playerCount = 0;
        botCount = 0;
        turns = 0;
        pieces = null;
        gameVolume = 0.5;
        gameUI = new GameUI();
        gameMap = GameMap.getInstance();
        players = new ArrayList<>();
        currentPlayer = null;
        dice = new Dice();
    }

    public int getMaxPlayers(){ return MAX_PLAYERS;}
    public int getTurns(){ return turns;}
    public int getStartingMoney(){ return STARTING_MONEY;}

    public GameUI getGameUI(){ return gameUI; }
    public GameMap getMap(){ return gameMap; }
    public ArrayList<Player> getPlayers(){ return players; }
    public Dice getDice(){ return dice; }
    public Player getCurrentPlayer(){return currentPlayer; }
    public void setBotCount(int botCount){ this.botCount = botCount; }


    /**
     * This method starts the game by initializing the map and players
     * @param createPlayer
     */
    public void startGame(ArrayList<Player> createPlayer){ //Runs after deciding maxPlayers and
        System.out.println("There are " + playerCount + " players. " + (botCount) + " bot(s) can be added.");
        /*Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the number of bot: ");*/

        createMap();
        createPlayers(createPlayer);
        currentPlayer = players.get(0);
        turnlabel.setText("Round: " +  currentPlayer.getName());
    }

    public void updateUI(){
        for (Node node : property.getChildren()) {
            Label updateProperty =  (Label) node ;

            for( Player player : players ){

                for( Property p : player.getProperties() ){

                    if ( updateProperty.getText().contains( p.getName() ) ){
                        String updateLabel = p.getName() + " -> " + player.getName();
                        updateProperty.setText( updateLabel );
                    }

                }
            }
        }
    } // Updates the Property section

    /**
     * This methods checks if all players except one is bankrupt, if so ends the game
     * @return
     */
    public boolean finishGame(){
        int bankrupted = 0;
        for (Player p: players) {
            if(p.isBankrupt())
                bankrupted++;
        }
        return bankrupted >= players.size() - 1;
    }

    public void setGameVolume(double volume){
        gameVolume = volume;
    }

    public void muteGame(){
        gameVolume = 0;
    } //Make the volume 0


    public void manageProperties(){

        Cell currentPosition = currentPlayer.getPosition();

        if( currentPosition instanceof Property ){

            int price = ((Property) currentPlayer.getPosition()).getPrice();
            if(currentPlayer.buyProperty( (Property) currentPosition )){

                updateUI();
                updateMoneyUI();
            }
        }
    }

    private void openMortgagePopup(javafx.event.ActionEvent event) throws IOException {
        Stage mortgagePopup = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/mortgage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        mortgagePopup.setScene(new Scene(root));

        mortgagePopup.initModality(Modality.APPLICATION_MODAL);
        mortgagePopup.initOwner(mortgageButton.getScene().getWindow());
        mortgagePopup.show();

        for (Property p:currentPlayer.getProperties()) {
            if (!p.getMortgage()){
                propertiesListView.getItems().add(p.getName());
            }
            else{
                mortgagedPropertiesListView.getItems().add(p.getName());
            }
        }
        propertiesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(propertiesListView.getSelectionModel().getSelectedItem() == null)
                    return;
                for (Property p: currentPlayer.getProperties()) {
                    if(p.getName().equals(propertiesListView.getSelectionModel().getSelectedItem())){
                        if(currentPlayer.getMortgagedProperties().contains(p)){
                            totalMortgagePayLabel.setText((int)(Integer.parseInt(totalMortgagePayLabel.getText()) - p.getPrice()*0.55) + "");
                        }
                        else{
                            totalMortgageEarnLabel.setText((int)(Integer.parseInt(totalMortgageEarnLabel.getText()) + p.getPrice()*0.5) + "");
                        }
                        break;
                    }
                }
                mortgagedPropertiesListView.getItems().add(propertiesListView.getSelectionModel().getSelectedItem());
                propertiesListView.getItems().remove(propertiesListView.getSelectionModel().getSelectedItem());
            }
        });
        mortgagedPropertiesListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(mortgagedPropertiesListView.getSelectionModel().getSelectedItem() == null)
                    return;
                for (Property p: currentPlayer.getProperties()) {
                    if(p.getName().equals(mortgagedPropertiesListView.getSelectionModel().getSelectedItem())){
                        if(currentPlayer.getMortgagedProperties().contains(p)){
                            totalMortgagePayLabel.setText((int)(Integer.parseInt(totalMortgagePayLabel.getText()) + p.getPrice()*0.55) + "");
                        }
                        else{
                            totalMortgageEarnLabel.setText((int)(Integer.parseInt(totalMortgageEarnLabel.getText()) - p.getPrice()*0.5) + "");
                        }
                        break;
                    }
                }
                propertiesListView.getItems().add(mortgagedPropertiesListView.getSelectionModel().getSelectedItem());
                mortgagedPropertiesListView.getItems().remove(mortgagedPropertiesListView.getSelectionModel().getSelectedItem());
            }
        });

        mortgageAcceptButton.setOnAction(event1 -> {
            for (Property p:currentPlayer.getProperties()) {
                if(p.getMortgage()){
                    if (propertiesListView.getItems().contains(p.getName())){
                        currentPlayer.cancelMortgage(p);
                    }
                }
                else{
                    if (mortgagedPropertiesListView.getItems().contains(p.getName())){
                        currentPlayer.mortgage(p);
                    }
                }
            }

            updateMoneyUI();
            mortgagePopup.close();
        });
        mortgageCancelButton.setOnAction(event1 -> {
            mortgagePopup.close();
        });
    }

    @FXML private Label winnerLabel;
    private void openWinPopup() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/end.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        window.setScene(new Scene(root));

    }

    private void openTradePopup(javafx.event.ActionEvent event) throws IOException {
        Stage tradePopup = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/trade.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        tradePopup.setScene(new Scene(root));

        tradePopup.initModality(Modality.APPLICATION_MODAL);
        tradePopup.initOwner(tradeButton.getScene().getWindow());
        tradePopup.show();

        for (Property p:currentPlayer.getProperties()) {
            buyerPropertiesListView.getItems().add(p.getName());
        }
        for (Player p:players) {
            if (!p.isBankrupt() && p != currentPlayer){
                clientsComboBox.getItems().add(p.getName());
            }
        }
        clientsComboBox.setOnAction(eventComboBox -> {
            Player client = null;
            String clientName = (String) clientsComboBox.getSelectionModel().getSelectedItem();
            for (Player p:players) {
                if (p.getName().equals(clientName)){
                    client = p;
                    break;
                }
            }
            sellerPropertiesListView.getItems().clear();
            for (Property p:client.getProperties()) {
                sellerPropertiesListView.getItems().add(p.getName());
            }
        });
        completeTradeButton.setOnAction(eventCompleteTrade -> {
            Player client = null;
            String clientName = (String) clientsComboBox.getSelectionModel().getSelectedItem();
            for (Player p:players) {
                if (p.getName().equals(clientName)){
                    client = p;
                    break;
                }
            }
            if (client == null) return;
            if(buyerPasswordBox.getText().equals(currentPlayer.getPassword())){
                if(client instanceof Bot || sellerPasswordBox.getText().equals(client.getPassword())){
                    Property offeredProperty = null;
                    if (buyerPropertiesListView.getSelectionModel().getSelectedItem() != null){
                        for (Property p:currentPlayer.getProperties()) {
                            if (p.getName().equals((String) buyerPropertiesListView.getSelectionModel().getSelectedItem())){
                                offeredProperty = p;
                                break;
                            }
                        }
                    }

                    Property requestedProperty = null;
                    if (sellerPropertiesListView.getSelectionModel().getSelectedItem() != null) {
                        for (Property p : client.getProperties()) {
                            if (p.getName().equals((String) sellerPropertiesListView.getSelectionModel().getSelectedItem())) {
                                requestedProperty = p;
                                break;
                            }
                        }
                    }
                    Commerce commerce = new Commerce(currentPlayer, client, offeredProperty, requestedProperty,
                            Integer.parseInt(isNumeric(offeredMoneyBox.getText()) ? offeredMoneyBox.getText() : "0"), Integer.parseInt(isNumeric(requestedMoneyBox.getText()) ? requestedMoneyBox.getText() : "0"));
                    if (commerce.exchange()){
                        updateUI();
                        System.out.println("Exchange successfull");
                        updateMoneyUI();
                        tradePopup.close();
                    }
                    else{
                        System.out.println("Excahnge failed");
                    }
                }
            }
            else{
                System.out.println("Wrong password");
            }
        });
        cancelTradeButton.setOnAction(eventCancelTrade -> {
            tradePopup.close();
        });
    }

    /**
     * This method is used to make the auction interface and auction inbetween players
     * @throws IOException
     */
    private void auction() throws IOException {
        Stage auctionPopup = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/auction.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        auctionPopup.setScene(new Scene(root));

        auctionPopup.initModality(Modality.APPLICATION_MODAL);
        auctionPopup.initOwner(tradeButton.getScene().getWindow());
        auctionPopup.show();
        ArrayList<Player> bidders = new ArrayList<>();
        for (Player p: players) {
            if (p != currentPlayer && !(p instanceof Bot))
                bidders.add(p);
        }
        switch(bidders.size()) {
            case 4:
                player1Label.setText(bidders.get(0).getName());
                player2Label.setText(bidders.get(1).getName());
                player3Label.setText(bidders.get(2).getName());
                player4Label.setText(bidders.get(3).getName());
                player1BidSlider.setMax(bidders.get(0).getMoney() - 1);
                player2BidSlider.setMax(bidders.get(1).getMoney() - 1);
                player3BidSlider.setMax(bidders.get(2).getMoney() - 1);
                player4BidSlider.setMax(bidders.get(3).getMoney() - 1);
                break;
            case 3:
                player1Label.setText(bidders.get(0).getName());
                player2Label.setText(bidders.get(1).getName());
                player3Label.setText(bidders.get(2).getName());
                player1BidSlider.setMax(bidders.get(0).getMoney() - 1);
                player2BidSlider.setMax(bidders.get(1).getMoney() - 1);
                player3BidSlider.setMax(bidders.get(2).getMoney() - 1);
                player4BidSlider.setMax(0);
                player4Label.setText("-");
                break;
            case 2:
                player1Label.setText(bidders.get(0).getName());
                player2Label.setText(bidders.get(1).getName());
                player1BidSlider.setMax(bidders.get(0).getMoney() - 1);
                player2BidSlider.setMax(bidders.get(1).getMoney() - 1);
                player3BidSlider.setMax(0);
                player4BidSlider.setMax(0);
                player3Label.setText("-");
                player4Label.setText("-");
                break;
            case 1:
                player1Label.setText(bidders.get(0).getName());
                player1BidSlider.setMax(bidders.get(0).getMoney() - 1);
                player2BidSlider.setMax(0);
                player3BidSlider.setMax(0);
                player4BidSlider.setMax(0);
                player2Label.setText("-");
                player3Label.setText("-");
                player4Label.setText("-");
                break;
            default:
                auctionPopup.close();
                break;
        }


        player1BidSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                player1BidLabel.setText(((int)player1BidSlider.getValue()) + "");
            }
        });

        player2BidSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                player2BidLabel.setText(((int)player2BidSlider.getValue()) + "");
            }
        });

        player3BidSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                player3BidLabel.setText(((int)player3BidSlider.getValue()) + "");
            }
        });

        player4BidSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                player4BidLabel.setText(((int)player4BidSlider.getValue()) + "");
            }
        });

        Cell currentPosition = currentPlayer.getPosition();
        auctionCompleteButton.setOnAction(event -> {
            int max = Math.max(Math.max((int)player1BidSlider.getValue(), (int)player2BidSlider.getValue()) ,Math.max((int)player3BidSlider.getValue(), (int)player4BidSlider.getValue()));
            if(max == 0)
                System.out.println("Nobody buy this property");
            else{
                if(max == (int)player1BidSlider.getValue()){
                    if(bidders.get(0).buyAuction( (Property) currentPosition, max )){

                        updateUI();
                        updateMoneyUI();
                    }
                }
                else if(max == (int)player2BidSlider.getValue()){
                    if(bidders.get(1).buyAuction( (Property) currentPosition, max )){

                        updateUI();
                        updateMoneyUI();
                    }
                }
                else if(max == (int)player3BidSlider.getValue()){
                    if(bidders.get(2).buyAuction( (Property) currentPosition, max )){

                        updateUI();
                        updateMoneyUI();
                    }
                }
                else if(max == (int)player4BidSlider.getValue()){
                    if(bidders.get(3).buyAuction( (Property) currentPosition, max )){
                        updateUI();
                        updateMoneyUI();
                    }
                }
            }
            auctionPopup.close();
        });
    }

    /**
     * This method manages the game flow in terms of the players turn and their choices
     */
    public void gameFlow(){
        updateHealthUI();
        card_container.setVisible(false);
        card_text.setWrapText(true);
        card_title.setWrapText(true);
        skipbtn.setDisable( true );


        skipbtn.setOnAction(event -> {

            diceLabel.setText( "Dice: -" );

            if ( (currentPlayer.getPosition() instanceof Transportation || currentPlayer.getPosition() instanceof PublicService ) && !((Property) currentPlayer.getPosition()).hasOwner()){
                try {
                    auction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Skips bankrupt player
            if(currentPlayer.getMoney() < 0){
                currentPlayer.setBankrupt();
            }

            nextTurn();

            //Heals quarantine leaving players
            if(currentPlayer.getQuarantine() && currentPlayer.getBanTurn() <= 0 ){
                currentPlayer.setHealth(true);
                currentPlayer.setQuarantine(false);
                currentPlayer.setMove( new Healthy() );
                updateHealthUI();
            }

            //Skips quarantined player
            if(currentPlayer.getBanTurn() > 0 && currentPlayer.getQuarantine() && currentPlayer.getMove() instanceof Patient ){
                currentPlayer.setBanTurn(currentPlayer.getBanTurn() - 1);
                nextTurn();
            }

            //Skips bankrupt player
            if(currentPlayer.getMoney() < 0){
                nextTurn();
            }

            if(currentPlayer instanceof Bot)
                nextTurn();

            rollDice.setDisable(false);
            buyButton.setDisable(false);
            card_container.setVisible(false);
            handleBankruptcy();

        });

        tradeButton.setOnAction(event -> {

            try {
                openTradePopup(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mortgageButton.setOnAction(event -> {

            try {
                openMortgagePopup(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        upgradeButton.setOnAction(event -> {
            try {
                manageBuildings(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        buyButton.setOnAction(event -> {

            manageProperties();
        });

        buyButton.setOnAction(event -> {

            manageProperties();
        });

        rollDice.setOnAction( event -> {

            skipbtn.setDisable( false );
            movePlayer(dice.roll());

            rollDice.setDisable(true);
            Cell currentPosition = currentPlayer.getPosition();

            if ( currentPosition instanceof PublicService  && ((PublicService)currentPosition).hasOwner() ){

                int diceValue = dice.roll();

                ((PublicService) currentPosition).setMultiplier( diceValue );
                int rent = ((PublicService) currentPosition).calculateRent();

                currentPlayer.setMoney( currentPlayer.getMoney() - rent );

                Player owner = ((PublicService) currentPosition).getOwner();
                owner.setMoney(owner.getMoney() + rent);

                updateMoneyUI();
            }
            else if(currentPosition instanceof Taxation){

                buyButton.setDisable(true);

                ((Taxation) currentPosition).getMoneyFromUser(currentPlayer);

                updateMoneyUI();

            }else if(currentPosition instanceof StartCell){

                buyButton.setDisable(true);

                ((StartCell) currentPosition).payVisitors(currentPlayer);

                updateMoneyUI();

            }else if( currentPosition instanceof CardCell ){
                buyButton.setDisable(true);

                if(((CardCell) currentPosition).getType().equals("chance") ){
                    sample.Model.Card randomCard = gameMap.drawCard("Chance");
                    card_title.setText("Chance Card");
                    card_text.setText(randomCard.getMessage());

                    if(randomCard.getCardFunction().equals("payVisitor")){
                        randomCard.payVisitor(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getMoney")){
                        randomCard.getMoneyFromUser(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getPercent")){
                        randomCard.getMoneyFromUser(currentPlayer,0.10);
                    }else if(randomCard.getCardFunction().equals("vaccinate")){
                        randomCard.vaccinate(currentPlayer);
                    }else if(randomCard.getCardFunction().equals("infect")){
                        randomCard.makeInfected(currentPlayer);
                    }
                }

                if(((CardCell) currentPosition).getType().equals("community") ){
                    Card randomCard = gameMap.drawCard("CommunityChest");
                    card_title.setText("Community Chest Card");
                    card_text.setText(randomCard.getMessage());

                    if(randomCard.getCardFunction().equals("payVisitor")){
                        randomCard.payVisitor(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getMoney")){
                        randomCard.getMoneyFromUser(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getPercent")){
                        randomCard.getMoneyFromUser(currentPlayer,0.10);
                    }else if(randomCard.getCardFunction().equals("vaccinate")){
                        randomCard.vaccinate(currentPlayer);
                    }else if(randomCard.getCardFunction().equals("infect")){
                        randomCard.makeInfected(currentPlayer);
                    }
                }

                updateMoneyUI();
                card_container.setVisible(true);
            }else if( currentPosition instanceof Neighbourhood && ((Neighbourhood)currentPosition).hasOwner() ){

                int rent = ((Neighbourhood) currentPosition).calculateRent();
                currentPlayer.setMoney( currentPlayer.getMoney() - rent );

                Player owner = ((Neighbourhood) currentPosition).getOwner();
                owner.setMoney(owner.getMoney() + rent);

                updateMoneyUI();
                diceLabel.setText( "Dice: " + dice.roll() );

            }else if( currentPosition instanceof BeInfected){

                moveUIPiece(currentPlayer.getPiece(),65 + players.indexOf(currentPlayer) * 10 - 6,735 +  players.indexOf(currentPlayer) * 10 - 15);
                currentPlayer.setPosition(gameMap.getCells().get(10));
                currentPlayer.getPosition().addVisitor(currentPlayer);
                currentPlayer.setBanTurn(MAX_BAN_TURN);
                currentPlayer.setQuarantine(true);
                currentPlayer.setMoney(currentPlayer.getMoney() - 500);

                currentPlayer.setMove( new Patient() );

                updateHealthUI();
                updateMoneyUI();

            }else if( currentPosition instanceof CoronaTest ){
                if ( !currentPlayer.isHealthy() ){

                    moveUIPiece(currentPlayer.getPiece(),65,735);

                    currentPlayer.setPosition(gameMap.getCells().get(10));
                    currentPlayer.getPosition().addVisitor(currentPlayer);
                    currentPlayer.setBanTurn(MAX_BAN_TURN);
                    currentPlayer.setQuarantine(true);
                    currentPlayer.setMove( new Patient() );
                    updateHealthUI();
                }
            }else if(currentPosition instanceof Transportation && ((Transportation) currentPosition).hasOwner()){
                int rent = ((Transportation) currentPosition).calculateRent();
                currentPlayer.setMoney( currentPlayer.getMoney() - rent );

                Player owner = ((Transportation) currentPosition).getOwner();
                owner.setMoney(owner.getMoney() + rent);

                updateMoneyUI();
                diceLabel.setText( "Dice: " + dice.roll() );
            }


            if(startCellPassed){

                startCellPassed = false;
                currentPlayer.setMoney(currentPlayer.getMoney() + 200);

                updateMoneyUI();

            }

            handleInfection();
        });

    }//Update game state between turns


    /**
     * This method prepares the board and skips to the next turn
     */
    public void nextTurn(){
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1)%players.size());
        turnlabel.setText("Round: " +  currentPlayer.getName());

        //This is for bot player to move automatically
        if(currentPlayer instanceof Bot){
            rollDice.setDisable(true);
            skipbtn.setDisable(true);
            tradeButton.setDisable(true);
            upgradeButton.setDisable(true);
            mortgageButton.setDisable(true);
            buyButton.setDisable(true);
            movePlayer(dice.roll());
            Cell currentPosition = currentPlayer.getPosition();

            if ( currentPosition instanceof PublicService  && ((PublicService)currentPosition).hasOwner() ){

                int diceValue = dice.roll();

                ((PublicService) currentPosition).setMultiplier( diceValue );
                int rent = ((PublicService) currentPosition).calculateRent();


                currentPlayer.setMoney( currentPlayer.getMoney() - rent );

                Player owner = ((PublicService) currentPosition).getOwner();
                owner.setMoney(owner.getMoney() + rent);

                updateMoneyUI();
                diceLabel.setText( "Dice: " + dice.roll() );
            }
            else if(currentPosition instanceof Taxation){

                buyButton.setDisable(true);

                ((Taxation) currentPosition).getMoneyFromUser(currentPlayer);

                updateMoneyUI();

            }else if(currentPosition instanceof StartCell){

                buyButton.setDisable(true);

                ((StartCell) currentPosition).payVisitors(currentPlayer);

                updateMoneyUI();

            }else if( currentPosition instanceof CardCell ){
                buyButton.setDisable(true);

                if(((CardCell) currentPosition).getType().equals("chance") ){
                    Card randomCard = gameMap.drawCard("Chance");
                    card_title.setText("Chance Card");
                    card_text.setText(randomCard.getMessage());

                    if(randomCard.getCardFunction().equals("payVisitor")){
                        randomCard.payVisitor(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getMoney")){
                        randomCard.getMoneyFromUser(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getPercent")){
                        randomCard.getMoneyFromUser(currentPlayer,0.10);
                    }else if(randomCard.getCardFunction().equals("vaccinate")){
                        randomCard.vaccinate(currentPlayer);
                    }else if(randomCard.getCardFunction().equals("infect")){
                        randomCard.makeInfected(currentPlayer);
                    }
                    System.out.println("şans");
                }

                if(((CardCell) currentPosition).getType().equals("community") ){
                    Card randomCard = gameMap.drawCard("CommunityChest");
                    card_title.setText("Community Chest Card");
                    card_text.setText(randomCard.getMessage());

                    if(randomCard.getCardFunction().equals("payVisitor")){
                        randomCard.payVisitor(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getMoney")){
                        randomCard.getMoneyFromUser(currentPlayer,200);
                    }else if(randomCard.getCardFunction().equals("getPercent")){
                        randomCard.getMoneyFromUser(currentPlayer,0.10);
                    }else if(randomCard.getCardFunction().equals("vaccinate")){
                        randomCard.vaccinate(currentPlayer);
                    }else if(randomCard.getCardFunction().equals("infect")){
                        randomCard.makeInfected(currentPlayer);
                    }
                    System.out.println("kamu fonu");
                }

                updateMoneyUI();
                card_container.setVisible(true);
            }else if( currentPosition instanceof Neighbourhood && ((Neighbourhood)currentPosition).hasOwner() ){

                int rent = ((Neighbourhood) currentPosition).calculateRent();
                currentPlayer.setMoney( currentPlayer.getMoney() - rent );

                Player owner = ((Neighbourhood) currentPosition).getOwner();
                owner.setMoney(owner.getMoney() + rent);

                updateMoneyUI();
                diceLabel.setText( "Dice: " + dice.roll() );

            }else if( currentPosition instanceof BeInfected){

                moveUIPiece(currentPlayer.getPiece(),65,735);
                currentPlayer.setMove( new Patient() );

                currentPlayer.setPosition(gameMap.getCells().get(10));
                currentPlayer.getPosition().addVisitor(currentPlayer);

            }else if( currentPosition instanceof CoronaTest ){
                if ( !currentPlayer.isHealthy() ){

                    currentPlayer.setMove( new Patient() );
                    moveUIPiece(currentPlayer.getPiece(),65,735);

                    currentPlayer.setPosition(gameMap.getCells().get(10));
                    currentPlayer.getPosition().addVisitor(currentPlayer);
                    currentPlayer.setBanTurn(MAX_BAN_TURN);
                }
            }

            if(startCellPassed){

                startCellPassed = false;
                currentPlayer.setMoney(currentPlayer.getMoney() + 200);

                updateMoneyUI();

            }

            ((Bot) currentPlayer).decideBuilding();
            updateMoneyUI();
            updateUI();
            ((Bot) currentPlayer).decideBuyingProperty();
            updateMoneyUI();
            updateUI();
            ((Bot) currentPlayer).decideMortgagingProperty();
            updateMoneyUI();
            updateUI();

            handleInfection();
            //managePatients();

            nextTurn();

            if(currentPlayer.getBanTurn() > 0){
                currentPlayer.setBanTurn(currentPlayer.getBanTurn() - 1);
                nextTurn();
            }

            rollDice.setDisable(false);
            skipbtn.setDisable(false);
            tradeButton.setDisable(false);
            upgradeButton.setDisable(false);
            mortgageButton.setDisable(false);
            buyButton.setDisable(false);
            card_container.setVisible(false);

        }
        turns++;
        skipbtn.setDisable( true );
    } //Get to the next turn. Triggered by pressing next turn button.

    public void handleInfection(){

        Cell currentPosition = currentPlayer.getPosition();
        boolean virusExist = false;

        if(currentPosition.getName().equals("Quarantine"))
            return;

        if ( currentPosition.getVisitors().size() > 1 ){

            for ( Player player : currentPosition.getVisitors() ) {

                if ( !player.isHealthy() ){
                    virusExist = true;
                    break;
                }
            }

            if ( virusExist ){
                for ( Player player : currentPosition.getVisitors() ) {
                    player.setHealth( false );
                }
            }

        }


        if( currentPosition instanceof Neighbourhood ){
            Neighbourhood neighbourhood = (Neighbourhood)currentPosition;
            if( !(neighbourhood.getCoronaRisk() < Math.random()) ){
                currentPlayer.setHealth( false );
                currentPlayer.setInfectionTurn( turns );
            }
        }
        else if( currentPosition instanceof Transportation ){
            Transportation transportation = (Transportation) currentPosition;
            if( !(transportation.getCoronaRisk() < Math.random()) ){
                currentPlayer.setHealth( false );
                currentPlayer.setInfectionTurn( turns );

            }
        }


       updateHealthUI();
    } //Check the infection risk of the cell

    private void updateHealthUI() {
        int counter = 0;


        for (Node node : healthSystem.getChildren()) {
            Label updateHealth =  (Label) node ;

            Player player = players.get(counter);


                String updateLabel = player.getName() + "                     ";

                if ( player.isInQuarantine() ){
                    updateLabel = updateLabel + "Quarantine";
                }else if( !player.isHealthy() ){
                    updateLabel = updateLabel + "Infected";
                }else{
                    updateLabel = updateLabel + "Healhty";
                }
                updateHealth.setText( updateLabel );

            counter++;

        }
    }

    /**
     * This method is used to manage the houses of a player in terms of their number and checks eligibility for building hotel
     * @param neighbour
     */
    public void manageBuildings( Neighbourhood neighbour ){
        InputStream stream = null;
        try {
            if( neighbour.getHouseCount() == 4 ){
                stream = new FileInputStream(getClass().getResource("../imgs/hospital.png").getFile());
                neighbour.setCoronaRisk( neighbour.getCoronaRisk() * 0.5 );
            }
            else
                stream = new FileInputStream(getClass().getResource("../imgs/house.png").getFile());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        //Creating the image view
        ImageView imageView = new ImageView();
        //Setting image to the image view
        imageView.setImage(image);
        imageView.toFront();
        //Setting the image view parameters

        int appendSizeForY = 0;
        int appendSizeForX = 0;

        if( neighbour.getHouseCount() == 4 ){
            appendSizeForY = (int) neighbour.getY() + 10;
            appendSizeForX = (int) neighbour.getX() + 15;

        }else{
            appendSizeForY = (int) (neighbour.getHouseCount() * 20 + neighbour.getY() - 30);
            appendSizeForX = (int) (neighbour.getHouseCount() * 20 + neighbour.getX() - 25);
        }


        if( neighbour.getX() == 40 ){ // sol

            //Setting the image view parameters
            imageView.setX( 103 );
            imageView.setY( appendSizeForY );

            imageView.setRotate( 90 );

        }else if( neighbour.getX() == 755 ){ // sağ

            //Setting the image view parameters
            imageView.setX( 715 );
            imageView.setY( appendSizeForY );

            imageView.setRotate( 270 );

        }else if( neighbour.getY() == 755 ){ // alt

            //Setting the image view parameters
            imageView.setX( appendSizeForX );
            imageView.setY( 708 );

        }else if( neighbour.getY() == 35 ){ // üst

            //Setting the image view parameters
            imageView.setX( appendSizeForX );
            imageView.setY( 98 );

            imageView.setRotate( 180 );
        }

        imageView.setFitHeight(17);
        imageView.setFitWidth(17);
        imageView.setPreserveRatio(true);

        if ( neighbour.getHouseCount() == 4 ){
            int counter = 0;
            for( Node node : gamePlay.getChildren() ){
                if( node.getClass().toString().equals("image-view") ){
                    if( counter >= 13)
                        gamePlay.getChildren().remove( counter );
                }
                counter++;
            }
        }
        gamePlay.getChildren().add(imageView);

    }

    public void manageBuildings(javafx.event.ActionEvent event) throws IOException{
        Stage buildingPopup = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/buildhouse.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        buildingPopup.setScene(new Scene(root));

        buildingPopup.initModality(Modality.APPLICATION_MODAL);
        buildingPopup.initOwner(tradeButton.getScene().getWindow());
        buildingPopup.show();

        for (Property p:currentPlayer.canBuild()) {
            if ( ((Neighbourhood)p).getHouseCount() != 4 )
                buildPropeties.getItems().add( p.getName() );
        }

        cancelButton.setOnAction( event1 -> {
            buildingPopup.close();
        });

        buildButton.setOnAction( event1 -> {

            String house = (String) buildPropeties.getSelectionModel().getSelectedItem();
            Neighbourhood selectedProperty = null;

            for( Property p : currentPlayer.getProperties() ){
                if( p.getName().equals( house ) ){
                    selectedProperty = ( Neighbourhood ) p;
                }
            }

            if( currentPlayer.buildHouse( selectedProperty ) ){
                updateMoneyUI();
                manageBuildings( selectedProperty );
                buildingPopup.close();
            }

        });

    }

    /**
     * This method checks the bankruptcy status of players
     */
    public void handleBankruptcy(){

        int bankruptCount = 0;
        Player p = null;

        for(int i = 0; i < players.size(); i++){

            if(players.get(i).isBankrupt()){
                bankruptCount++;
            }else{
                p = players.get(i);
            }
        }

        if(bankruptCount == (MAX_PLAYERS - 1)){
            EndGame(p);
        }
    }

    /**
     * This method is used to declare the winner of the game
     * @param player
     */
    private void EndGame(Player player) {

        buyButton.setDisable(true);
        rollDice.setDisable(true);
        skipbtn.setDisable(true);
        mortgageButton.setDisable(true);
        upgradeButton.setDisable(true);
        tradeButton.setDisable(true);


        try {
            openWinPopup();
            winnerLabel.setText(player.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to manage the patients in terms of their remaning time in quarantine
     */
    public void managePatients(){
        for( Player player :  players){

            if ( (MAX_BAN_TURN * (MAX_PLAYERS - 1) <= player.getInfectionTurn() + 3) && !player.isInQuarantine() && !player.isHealthy() ){
                player.setBanTurn( 3 );
                moveUIPiece(player.getPiece(),65,735);
                player.setPosition(gameMap.getCells().get(10));
                player.getPosition().addVisitor(player);
            }else if( player.isInQuarantine() ){
                player.setBanTurn( player.getBanTurn() - 1 );
            }
        }

    }


    public void updateMoneyUI(){
        int playerCounter = 0;

        for (Node node : money_grid.getChildren()) {
            Label label = (Label) node;

            if(players.get(playerCounter).getMoney() >= 0){
                label.setText(players.get(playerCounter).getMoney() + "");
            }else{
                label.setText("Bankrupt");
            }

            playerCounter++;
        }

        playerCounter = 0;

        for (Node node : name_grid.getChildren()) {
            Label label = (Label) node;

            label.setText(players.get(playerCounter).getName() + " (" + players.get(playerCounter).getShape() + ") ");
            playerCounter++;
        }

    }


    public void createPlayers(ArrayList<Player> createPlayer){
        pieces = new ArrayList<>(Arrays.asList(player_piece,player_piece_1,player_piece_2,player_piece_3,player_piece_4,player_piece_5));
        ArrayList<String> shapes = new ArrayList<>( Arrays.asList( "hat", "car", "dog", "iron", "ship"));

        int counter = 0;
        for (Player p:createPlayer) {
            if(p instanceof Bot){
                botCount++;
                players.add(new Bot("bot" + botCount, pieces.get(0) , shapes.get(counter), "", gameMap.getCells().get(0)));
                pieces.remove(pieces.get(0));

            }
            else if(p instanceof Player){
                playerCount++;
                players.add(new Player(p.getName(), pieces.get(0), shapes.get(counter), p.getPassword(), gameMap.getCells().get(0)));
                pieces.remove(pieces.get(0));
            }
            counter++;
        }


        for(int i = 0; i <= players.size() - 1; i++){
            players.get(i).setMoney(STARTING_MONEY);
            moveUIPiece( players.get(i).getPiece(),gameMap.getCells().get(0).getX() + i * 10 - 6, gameMap.getCells().get(0).getY() + i * 10 );
        }

        updateMoneyUI();
        gameMap.getCells().get(0).setVisitors(players);
    }

    public void createMap(){
        gameMap.newMap();
        gameMap.addCell(new StartCell(735,735));
        gameMap.addCell(new Neighbourhood("Altındağ", 60,13, 0.2, "brown", 655, 755 ));
        gameMap.addCell(new CardCell("Community Chest","community", 590, 755 ));
        gameMap.addCell(new Neighbourhood("Sincan", 60,15, 0.19, "brown", 525, 755 ) );
        gameMap.addCell(new Taxation("Income Tax", 0.23, 460, 755 ));
        gameMap.addCell(new Transportation("Railroad", 200, 30, 0.1, "black", 395, 755 ));
        gameMap.addCell(new Neighbourhood("Kolej", 100,24, 0.12, "blue", 330, 755 ));
        gameMap.addCell(new CardCell("Chance","chance", 265, 755 ));
        gameMap.addCell(new Neighbourhood("Beşevler", 100, 27, 0.07, "blue" , 195, 755 ));
        gameMap.addCell(new Neighbourhood("Çayyolu", 120, 31, 0.1, "blue" , 135, 755 ));
        gameMap.addCell(new Quarantine("Quarantine" , 65, 730 ));
        gameMap.addCell(new Neighbourhood("Eryaman", 140, 35, 0.17, "purple" , 40, 655 ));
        gameMap.addCell(new PublicService("TEDAŞ", 150, 20, "white", 40, 590 ));
        gameMap.addCell(new Neighbourhood("Ostim", 140, 35, 0.15, "purple" , 40, 525 ));
        gameMap.addCell(new Neighbourhood("Beypazarı", 160,39, 0.05, "purple", 40, 460 ));
        gameMap.addCell(new Transportation("YHT", 200, 30, 0.13, "black", 40, 395));
        gameMap.addCell(new Neighbourhood("Dikmen", 180,40, 0.15, "orange", 40, 325 ));
        gameMap.addCell(new CardCell("Community Chest","community", 40, 265 ));
        gameMap.addCell(new Neighbourhood("Etimesgut", 180,42, 0.1, "orange", 40, 195 ));
        gameMap.addCell(new Neighbourhood("Gölbaşı", 200,45, 0.2, "orange", 40, 130 ));
        gameMap.addCell(new CoronaTest("Test Center" , 50, 50 ));
        gameMap.addCell(new Neighbourhood("Batıkent", 220, 47, 0.15, "red" , 135, 35 ));
        gameMap.addCell(new CardCell("Chance","chance", 195, 35 ));
        gameMap.addCell(new Neighbourhood("Yenimahalle", 220, 47, 0.2, "red", 260, 35  ));
        gameMap.addCell(new Neighbourhood("Mamak", 240, 50 , 0.01, "red", 330, 35 ));
        gameMap.addCell(new Transportation("Esenboğa", 300, 75, 0.21, "black", 395, 35 ));
        gameMap.addCell(new Neighbourhood("Sıhhiye", 260, 52, 0.14, "yellow", 460, 35  ));
        gameMap.addCell(new Neighbourhood("Emek", 260,52, 0.17, "yellow", 525, 35 ));
        gameMap.addCell(new PublicService("ASKİ", 150, 20, "white", 590, 35 ));
        gameMap.addCell(new Neighbourhood("Bilkent", 280, 55, 0.00000000000001, "yellow" , 655, 35 ));
        gameMap.addCell(new BeInfected("Go To Quarantine", 755, 35));
        gameMap.addCell(new Neighbourhood("Bahçelievler", 300, 57, 0.23, "green", 755, 135  ));
        gameMap.addCell(new Neighbourhood("Cebeci", 300,57, 0.14, "green", 755, 200 ));
        gameMap.addCell(new CardCell("Community Chest","community", 755, 260 ));
        gameMap.addCell(new Neighbourhood("Ulus", 320, 60, 0.09, "green" , 755, 325 ));
        gameMap.addCell(new Transportation("Aşti", 200, 45, 0.9, "black", 755, 395 ));
        gameMap.addCell(new CardCell("Chance","chance", 755, 460 ));
        gameMap.addCell(new Neighbourhood("Çankaya", 350,62, 0.15, "darkblue", 755, 525 ));
        gameMap.addCell(new Taxation("Luxury Tax", 0.23, 755, 590 ));
        gameMap.addCell(new Neighbourhood("Kızılay", 400, 65, 0.25, "darkblue", 755, 655 ));

        Chance payVisitorsCard = new Chance();
        payVisitorsCard.setMessage("Bank Pays 200 TL to the Visitor.");
        payVisitorsCard.setCardFunction("payVisitor");

        Chance getMoneyCard = new Chance();
        getMoneyCard.setMessage("Visitor pays 200 TL to the Bank.");
        getMoneyCard.setCardFunction("getMoney");

        Chance getPercentCard = new Chance();
        getPercentCard.setMessage("Visitor pays 10% of their money to the Bank.");
        getPercentCard.setCardFunction("getPercent");

        Chance vaccinateCard = new Chance();
        vaccinateCard.setMessage("Player is now vaccinated and healthy.");
        vaccinateCard.setCardFunction("vaccinate");

        Chance infectCard = new Chance();
        infectCard.setMessage("Player is now infected and not healthy.");
        infectCard.setCardFunction("infect");

        gameMap.addCards(payVisitorsCard);
        gameMap.addCards(getMoneyCard);
        gameMap.addCards(getPercentCard);
        gameMap.addCards(vaccinateCard);
        gameMap.addCards(infectCard);

        CommunityChest payVisitorsCardCommunity = new CommunityChest();
        payVisitorsCardCommunity.setMessage("Bank Pays 200 TL to the Visitor.");
        payVisitorsCardCommunity.setCardFunction("payVisitor");

        CommunityChest getMoneyCardCommunity = new CommunityChest();
        getMoneyCardCommunity.setMessage("Visitor pays 200 TL to the Bank.");
        getMoneyCardCommunity.setCardFunction("getMoney");

        CommunityChest getPercentCardCommunity = new CommunityChest();
        getPercentCardCommunity.setMessage("Visitor pays 10% of their money to the Bank.");
        getPercentCardCommunity.setCardFunction("getPercent");

        CommunityChest vaccinateCardCommunity = new CommunityChest();
        vaccinateCardCommunity.setMessage("Player is now vaccinated and healthy.");
        vaccinateCardCommunity.setCardFunction("vaccinate");

        CommunityChest infectCardCommunity = new CommunityChest();
        infectCardCommunity.setMessage("Player is now infected and not healthy.");
        infectCardCommunity.setCardFunction("infect");

        gameMap.addCards(payVisitorsCardCommunity);
        gameMap.addCards(getMoneyCardCommunity);
        gameMap.addCards(getPercentCardCommunity);
        gameMap.addCards(vaccinateCardCommunity);
        gameMap.addCards(infectCardCommunity);

        gameMap.shuffleCards();
    } //Initialize the map


    private void movePlayer(int amount){
        int position = (amount + gameMap.getCells().indexOf(currentPlayer.getPosition()));

        if(position >= gameMap.getCells().size()){
            startCellPassed = true;
        }

        position= position%gameMap.getCells().size();
        Cell c = gameMap.getCells().get(position);
        currentPlayer.getPosition().getVisitors().remove(currentPlayer);
        currentPlayer.setPosition(c);

        c.addVisitor(currentPlayer);

        diceLabel.setText( "Dice: " + amount );

        moveUIPiece( currentPlayer.getPiece(), c.getX() + players.indexOf(currentPlayer) * 10 - 6, c.getY() + players.indexOf(currentPlayer) * 10 - 15);
    }
    public void moveUIPiece(Pane piece,double x, double y){

        piece.setLayoutX(x);
        piece.setLayoutY(y);
    }

    Stage window;
    public Scene gameScene;
    public Scene pauseScene;

    public void playButtonPushed(javafx.event.ActionEvent event) throws IOException {

        Parent settingsParent = FXMLLoader.load(getClass().getResource("../view/playGame.fxml"));
        Scene settingsScene = new Scene( settingsParent );

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(settingsScene);
        window.show();
    }

    public void settingsButtonPushed(javafx.event.ActionEvent event) throws IOException{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/settings.fxml"));
        loader.setController(this);
        Parent settingsParent = (Parent) loader.load();
        Scene settingsScene = new Scene( settingsParent );

        if(window == null){
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(settingsScene);
            window.show();
        }else{
            window.setScene(settingsScene);
            window.show();
        }
    }

    public void howToPlayButtonPushed(javafx.event.ActionEvent event) throws IOException{

        //Parent settingsParent = FXMLLoader.load(getClass().getResource("../view/howToPlay.fxml"));
        //Scene settingsScene = new Scene( settingsParent );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/howToPlay.fxml"));
        loader.setController(this);
        Parent settingsParent = (Parent) loader.load();
        Scene settingsScene = new Scene( settingsParent );

        if(window == null){
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(settingsScene);
            window.show();
        }else{
            window.setScene(settingsScene);
            window.show();
        }
    }

    public void creditsButtonPushed(javafx.event.ActionEvent event) throws IOException{

        Parent settingsParent = FXMLLoader.load(getClass().getResource("../view/credits.fxml"));
        Scene settingsScene = new Scene( settingsParent );


        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(settingsScene);
        window.show();
    }

    public void closeButtonAction(javafx.event.ActionEvent event) throws IOException{
        // get a handle to the stage
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    public void singlePlayerButtonPushed(javafx.event.ActionEvent event) throws IOException{

        //Parent settingsParent = FXMLLoader.load(getClass().getResource("../view/game.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/game.fxml"));
        GameEngine engine = new GameEngine();
        loader.setController(this);
        Parent settingsParent = (Parent) loader.load();
        gameScene = new Scene( settingsParent );

        settingsParent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        settingsParent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.setX(event.getScreenX() - xOffset);
                window.setY(event.getScreenY() - yOffset);
            }
        });

        window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        window.show();
        System.out.println("Başladı: " + property);

        ArrayList<Player> start = new ArrayList<>();
        start.add(new Player("Player", ""));
        start.add(new Bot());
        start.add(new Bot());
        start.add(new Bot());
        start.add(new Bot());
        this.startGame(start);
        this.gameFlow();
    }

    public void continueButtonPushed(){//Scene scene
        window.setScene(gameScene);
    }

    public void multiPlayerButtonPushed(javafx.event.ActionEvent event) throws IOException{

        Parent settingsParent = FXMLLoader.load(getClass().getResource("../view/multi.fxml"));
        Scene settingsScene = new Scene( settingsParent );

        settingsParent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        settingsParent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                window.setX(event.getScreenX() - xOffset);
                window.setY(event.getScreenY() - yOffset);
            }
        });

        window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(settingsScene);
        window.show();

        player1TypeChoiceBox = (ChoiceBox) settingsScene.lookup("#player1TypeChoiceBox");
        player2TypeChoiceBox = (ChoiceBox) settingsScene.lookup("#player2TypeChoiceBox");
        player3TypeChoiceBox = (ChoiceBox) settingsScene.lookup("#player3TypeChoiceBox");
        player4TypeChoiceBox = (ChoiceBox) settingsScene.lookup("#player4TypeChoiceBox");
        player5TypeChoiceBox = (ChoiceBox) settingsScene.lookup("#player5TypeChoiceBox");
        player1PasswordBox = (PasswordField) settingsScene.lookup("#player1PasswordBox");
        player2PasswordBox = (PasswordField) settingsScene.lookup("#player2PasswordBox");
        player3PasswordBox = (PasswordField) settingsScene.lookup("#player3PasswordBox");
        player4PasswordBox = (PasswordField) settingsScene.lookup("#player4PasswordBox");
        player5PasswordBox = (PasswordField) settingsScene.lookup("#player5PasswordBox");
        player1NameTextField = (TextField) settingsScene.lookup("#player1NameTextField");
        player2NameTextField = (TextField) settingsScene.lookup("#player2NameTextField");
        player3NameTextField = (TextField) settingsScene.lookup("#player3NameTextField");
        player4NameTextField = (TextField) settingsScene.lookup("#player4NameTextField");
        player5NameTextField = (TextField) settingsScene.lookup("#player5NameTextField");
        player1ReadyButton = (Button) settingsScene.lookup("#player1ReadyButton");
        player2ReadyButton = (Button) settingsScene.lookup("#player2ReadyButton");
        player3ReadyButton = (Button) settingsScene.lookup("#player3ReadyButton");
        player4ReadyButton = (Button) settingsScene.lookup("#player4ReadyButton");
        player5ReadyButton = (Button) settingsScene.lookup("#player5ReadyButton");
        player1ReadyStatusLabel = (Label) settingsScene.lookup("#player1ReadyStatusLabel");
        player2ReadyStatusLabel = (Label) settingsScene.lookup("#player2ReadyStatusLabel");
        player3ReadyStatusLabel = (Label) settingsScene.lookup("#player3ReadyStatusLabel");
        player4ReadyStatusLabel = (Label) settingsScene.lookup("#player4ReadyStatusLabel");
        player5ReadyStatusLabel = (Label) settingsScene.lookup("#player5ReadyStatusLabel");
        multiplayerStartButton = (Button) settingsScene.lookup("#multiplayerStartButton");
        multiplayerBackButton = (Button) settingsScene.lookup("#multiplayerBackButton");

        ArrayList<Player> start = new ArrayList<>();

        player1TypeChoiceBox.setOnAction(event1 -> {
            if(player1TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player")){
                player1NameTextField.setVisible(true);
                player1PasswordBox.setVisible(true);
                player1ReadyButton.setVisible(true);
                player1ReadyStatusLabel.setText("Not Ready");
                player1ReadyStatusLabel.setTextFill(Color.web("#ddd05c")); //Yellow
            }
            else{
                player1NameTextField.setVisible(false);
                player1PasswordBox.setVisible(false);
                player1ReadyButton.setVisible(false);
                player1ReadyStatusLabel.setText("Ready");
                player1ReadyStatusLabel.setTextFill(Color.web("#61f1a9")); //Green
            }
        });
        player2TypeChoiceBox.setOnAction(event1 -> {
            if(player2TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player")){
                player2NameTextField.setVisible(true);
                player2PasswordBox.setVisible(true);
                player2ReadyButton.setVisible(true);
                player2ReadyStatusLabel.setText("Not Ready");
                player2ReadyStatusLabel.setTextFill(Color.web("#ddd05c")); //Yellow
            }
            else{
                player2NameTextField.setVisible(false);
                player2PasswordBox.setVisible(false);
                player2ReadyButton.setVisible(false);
                player2ReadyStatusLabel.setText("Ready");
                player2ReadyStatusLabel.setTextFill(Color.web("#61f1a9")); //Green
            }
        });
        player3TypeChoiceBox.setOnAction(event1 -> {
            if(player3TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player")){
                player3NameTextField.setVisible(true);
                player3PasswordBox.setVisible(true);
                player3ReadyButton.setVisible(true);
                player3ReadyStatusLabel.setText("Not Ready");
                player3ReadyStatusLabel.setTextFill(Color.web("#ddd05c")); //Yellow
            }
            else{
                player3NameTextField.setVisible(false);
                player3PasswordBox.setVisible(false);
                player3ReadyButton.setVisible(false);
                player3ReadyStatusLabel.setText("Ready");
                player3ReadyStatusLabel.setTextFill(Color.web("#61f1a9")); //Green
            }
        });
        player4TypeChoiceBox.setOnAction(event1 -> {
            if(player4TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player")){
                player4NameTextField.setVisible(true);
                player4PasswordBox.setVisible(true);
                player4ReadyButton.setVisible(true);
                player4ReadyStatusLabel.setText("Not Ready");
                player4ReadyStatusLabel.setTextFill(Color.web("#ddd05c")); //Yellow
            }
            else{
                player4NameTextField.setVisible(false);
                player4PasswordBox.setVisible(false);
                player4ReadyButton.setVisible(false);
                player4ReadyStatusLabel.setText("Ready");
                player4ReadyStatusLabel.setTextFill(Color.web("#61f1a9")); //Green
            }
        });
        player5TypeChoiceBox.setOnAction(event1 -> {
            if(player5TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player")){
                player5NameTextField.setVisible(true);
                player5PasswordBox.setVisible(true);
                player5ReadyButton.setVisible(true);
                player5ReadyStatusLabel.setText("Not Ready");
                player5ReadyStatusLabel.setTextFill(Color.web("#ddd05c")); //Yellow
            }
            else{
                player5NameTextField.setVisible(false);
                player5PasswordBox.setVisible(false);
                player5ReadyButton.setVisible(false);
                player5ReadyStatusLabel.setText("Ready");
                player5ReadyStatusLabel.setTextFill(Color.web("#61f1a9")); //Green
            }
        });
        player1TypeChoiceBox.getSelectionModel().select(0);
        player2TypeChoiceBox.getSelectionModel().select(0);
        player3TypeChoiceBox.getSelectionModel().select(0);
        player4TypeChoiceBox.getSelectionModel().select(0);
        player5TypeChoiceBox.getSelectionModel().select(0);

        player1ReadyButton.setOnAction(event1 -> {
            if(player1ReadyStatusLabel.getText().equals("Not Ready")){
                player1ReadyStatusLabel.setText("Ready");
                player1ReadyStatusLabel.setTextFill(Color.web("#61f1a9"));
            }
            else if(player1ReadyStatusLabel.getText().equals("Ready")){
                player1ReadyStatusLabel.setText("Not Ready");
                player1ReadyStatusLabel.setTextFill(Color.web("#ddd05c"));
            }
        });
        player2ReadyButton.setOnAction(event1 -> {
            if(player2ReadyStatusLabel.getText().equals("Not Ready")){
                player2ReadyStatusLabel.setText("Ready");
                player2ReadyStatusLabel.setTextFill(Color.web("#61f1a9"));
            }
            else if(player2ReadyStatusLabel.getText().equals("Ready")){
                player2ReadyStatusLabel.setText("Not Ready");
                player2ReadyStatusLabel.setTextFill(Color.web("#ddd05c"));
            }
        });
        player3ReadyButton.setOnAction(event1 -> {
            if(player3ReadyStatusLabel.getText().equals("Not Ready")){
                player3ReadyStatusLabel.setText("Ready");
                player3ReadyStatusLabel.setTextFill(Color.web("#61f1a9"));
            }
            else if(player3ReadyStatusLabel.getText().equals("Ready")){
                player3ReadyStatusLabel.setText("Not Ready");
                player3ReadyStatusLabel.setTextFill(Color.web("#ddd05c"));
            }
        });
        player4ReadyButton.setOnAction(event1 -> {
            if(player4ReadyStatusLabel.getText().equals("Not Ready")){
                player4ReadyStatusLabel.setText("Ready");
                player4ReadyStatusLabel.setTextFill(Color.web("#61f1a9"));
            }
            else if(player4ReadyStatusLabel.getText().equals("Ready")){
                player4ReadyStatusLabel.setText("Not Ready");
                player4ReadyStatusLabel.setTextFill(Color.web("#ddd05c"));
            }
        });
        player5ReadyButton.setOnAction(event1 -> {
            if(player5ReadyStatusLabel.getText().equals("Not Ready")){
                player5ReadyStatusLabel.setText("Ready");
                player5ReadyStatusLabel.setTextFill(Color.web("#61f1a9"));
            }
            else if(player5ReadyStatusLabel.getText().equals("Ready")){
                player5ReadyStatusLabel.setText("Not Ready");
                player5ReadyStatusLabel.setTextFill(Color.web("#ddd05c"));
            }
        });


        multiplayerStartButton.setOnAction(event1 -> {

            boolean flag = false;
            if(!player1ReadyStatusLabel.getText().equals("Ready")){
                player1ReadyStatusLabel.setTextFill(Color.web("#E74C3C"));
                flag = true;
            }
            if(!player2ReadyStatusLabel.getText().equals("Ready")){
                player2ReadyStatusLabel.setTextFill(Color.web("#E74C3C"));
                flag = true;
            }
            if(!player3ReadyStatusLabel.getText().equals("Ready")){
                player3ReadyStatusLabel.setTextFill(Color.web("#E74C3C"));
                flag = true;
            }
            if(!player4ReadyStatusLabel.getText().equals("Ready")){
                player4ReadyStatusLabel.setTextFill(Color.web("#E74C3C"));
                flag = true;
            }
            if(!player5ReadyStatusLabel.getText().equals("Ready")){
                player5ReadyStatusLabel.setTextFill(Color.web("#E74C3C"));
                flag = true;
            }
            if (flag)
                return;
            if(player1TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player"))
                start.add(new Player(player1NameTextField.getText(), player1PasswordBox.getText()));
            else
                start.add(new Bot());
            if(player2TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player"))
                start.add(new Player(player2NameTextField.getText(), player2PasswordBox.getText()));
            else
                start.add(new Bot());
            if(player3TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player"))
                start.add(new Player(player3NameTextField.getText(), player3PasswordBox.getText()));
            else
                start.add(new Bot());
            if(player4TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player"))
                start.add(new Player(player4NameTextField.getText(), player4PasswordBox.getText()));
            else
                start.add(new Bot());
            if(player5TypeChoiceBox.getSelectionModel().getSelectedItem().equals("Player"))
                start.add(new Player(player5NameTextField.getText(), player5PasswordBox.getText()));
            else
                start.add(new Bot());

            if(start.isEmpty())
                return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/game.fxml"));
            loader.setController(this);
            Parent sp = null;
            try {
                sp = (Parent) loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameScene = new Scene( sp );

            sp.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            sp.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    window.setX(event.getScreenX() - xOffset);
                    window.setY(event.getScreenY() - yOffset);
                }
            });

            window = (Stage)((Node)event1.getSource()).getScene().getWindow();
            window.setScene(gameScene);
            window.show();
            System.out.println("Multiplayer Başladı: " + property);

            this.startGame(start);
            this.gameFlow();
        });
        multiplayerBackButton.setOnAction(event1 -> {
            Parent sp = null;
            try {
                sp = FXMLLoader.load(getClass().getResource("../view/playGame.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene ss = new Scene( sp );

            Stage w = (Stage)((Node)event1.getSource()).getScene().getWindow();

            w.setScene(ss);
            w.show();
        });
    }

    public void backButtonPushed(javafx.event.ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/sample.fxml"));
        Parent settingsParent = (Parent) loader.load();
        Scene mainScene = new Scene(settingsParent);

        if(window == null){
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }else{
            window.setScene(pauseScene);
            window.show();
        }
    }

    public void gamePausedButtonPushed(javafx.event.ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/pause.fxml"));
        loader.setController(this);

        Parent settingsParent = (Parent) loader.load();
        pauseScene = new Scene( settingsParent );

        window.setScene(pauseScene);
        window.show();

    }

    public void adjustSoundButtonPushed(){


        soundControl.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

                mediaPlayer.volumeProperty().bindBidirectional( soundControl.valueProperty());
            }
        });
    }

    private boolean isNumeric(final String str) {

        // null or empty
        if (str.equals("")  || str == null || str.length() == 0) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}


