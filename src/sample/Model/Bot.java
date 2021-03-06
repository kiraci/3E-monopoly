package sample.Model;

//import com.sun.org.apache.xpath.internal.operations.Neg;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * This is the Bot class that extends the Player class to create non-human players
 */
public class Bot extends Player{

    //contructors
    public Bot(String name, Pane piece, String shape, String password, Cell cell){
        super(name, piece, shape, password, cell);
    }
    public Bot(){
    }
    //methods
    public Boolean decideBuyingProperty(){
        if(position instanceof Property)
            return buyProperty((Property) position);
        return false;
    }

    /**
     * This method is used to decide on mortgagingProperty by considering the money and a situation of RNG
     * @return Boolean Returns the success of the process
     */
    public Boolean decideMortgagingProperty()
    {
        if (money <= 0)
            return mortgage(properties.get((int)(Math.random() * (properties.size() - 1))));
        return false;
    }

    /**
     * This method is used to decide whether the bot should build on property or not
     * @return Boolean Returns the success of the process
     */
    public Boolean decideBuilding(){
        for (Property p:canBuild())
            return buildHouse((Neighbourhood) p);
        return false;
    }

    /**
     * This method is used to decide whether the bot should do a trade or not
     * @param botProperty
     * @param sellerProperty
     * @param botMoney
     * @param sellerMoney
     * @return Boolean Returns the success of the process
     */
    public Boolean decideTrading(Property botProperty, Property sellerProperty, int botMoney, int sellerMoney){
        if(canBuild().contains(botProperty))
            return false;
        if (sellerProperty == null){
            return botProperty.getPrice() + botMoney <= sellerMoney;
        }
        else if (botProperty == null){
            return botMoney <= sellerProperty.getPrice() + sellerMoney;
        }
        else{
            return botProperty.getPrice() + botMoney <= sellerProperty.getPrice() + sellerMoney;
        }
    }

}