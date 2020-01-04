/*
Mikhail Morozov
PROGRAMMING II 
ASSIGNMENT-1 
CALCULATOR
*/

/*
This calculator:
- supports the division with a fractional part 
- supports calculations with negative numbers 
- correctly shows an error when divide by zero 
- correctly displays an error when entering the number for the range of possible values 
- displays integers without fractional parts of 
- blocks input from the keyboard 
- locks the window size changes
*/

package calculator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.text.DecimalFormat;

public class Calculator extends Application {

    private TextField mainField = new TextField();
    private String[] letter={"7","8","9","+","4","5","6","-",
            "1","2","3","x","C","0","=","/"}; // Texts for buttons
    private Button[] clava=new Button[16];
    private String tmp=""; // For saving previos operation
    private double res=0; // For saving result
    private boolean newNum=true; // Showed start of new number

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("JavaFx Calculator");
        primaryStage.setResizable(false); // Cannot resize

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setCenter(centreGridPane());

        for (int i = 0; i < clava.length; i++) {
            actionOfButton(clava[i]);
        }
        
        System.out.println("This calculator:\n"+
        "- supports the division with a fractional part \n"+
        "- supports calculations with negative numbers \n"+
        "- correctly shows an error when divide by zero \n"+
        "- correctly displays an error when entering the number for the range of possible values \n"+
        "- displays integers without fractional parts of \n"+
        "- blocks input from the keyboard \n"+
        "- locks the window size changes");
        
        Scene scene = new Scene(border, 275, 205);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public HBox addHBox() {
        HBox hbox = new HBox();

        mainField.setMinSize(255, 30);
        mainField.setFocusTraversable(false);
        mainField.setAlignment(Pos.CENTER_RIGHT);
        mainField.setEditable(false); // Cannot edit

        hbox.setPadding(new Insets(15, 15, 15, 15));
        hbox.getChildren().add(mainField);

        return hbox;
    }

    public GridPane centreGridPane(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(0, 10, 10, 10));

        for (int i = 0; i < clava.length; i++) {
            clava[i]=new Button(letter[i]);
            clava[i].setMinSize(60,30);
            clava[i].setMaxSize(60,30);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid.add(clava[i*4+j],j,i);
            }
        }

        return grid;
    }

    public void calc(String text){
        switch (tmp){
            case "+" :
                res+=Double.parseDouble(mainField.getText());
                break;
            case  "-" :
                res-=Double.parseDouble(mainField.getText());
                break;
            case  "x" :
                DecimalFormat formatInt=new DecimalFormat("0");
                int len=formatInt.format(res).length();
                if(res<0) len--;
                if(len>15){ // Catch result out of range
                    res = 0;
                    tmp = "";
                    mainField.setText(String.valueOf("error"));
                }
                else{
                    res*=Double.parseDouble(mainField.getText());
                }
                break;
            case  "/" :
                if(Double.parseDouble(mainField.getText())<0.0000001){ // Catch division by 0
                    res = 0;
                    text="";
                    newNum=true;
                    mainField.setText(String.valueOf("error"));
                }
                else{
                    res= res/Double.parseDouble(mainField.getText());
                }
                break;
        }
        if(!text.equals("="))tmp=text;
        else tmp="=";
    }

    public void actionOfButton(Button button){ // Method for setting "on action" reaction to buttons
        switch (button.getText()){
            case "C":
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        res = 0;
                        tmp = "";
                        mainField.setText(creatigDF(res));
                        newNum=true;
                    }
                });
                break;
            case "=":
            case "+":
            case "x":
            case "/":
            case "-":
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        if(button.getText().equals("-") && newNum && !tmp.equals("=")){
                            mainField.setText(button.getText());
                            newNum=false;
                        }
                        else {
                            newNum = true;
                            if (tmp.equals("")) {
                                if (!mainField.getText().equals("")) {
                                    res = Double.parseDouble(mainField.getText());
                                    tmp = button.getText();
                                }
                            } else {
                                calc(button.getText());
                                if(!mainField.getText().equals("error")) mainField.setText(creatigDF(res));
                            }
                        }
                    }
                });
                break;
            default:
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        if(newNum){
                            mainField.setText(button.getText());
                            newNum=false;
                        }
                        else if(mainField.getText().length()<
                                ((mainField.getText().substring(0,1).equals("-")) ?  16 : 15)){ // Catch result out of range
                            mainField.setText(mainField.getText().concat(button.getText()));
                        }
                    }
                });
                break;
        }
    }

    private String creatigDF(double res){ // Assign the format of result
        DecimalFormat baseFormat=new DecimalFormat("0.0000000000");
        String tmp=baseFormat.format(res);
        String maskOfFormat="#,##0";
        int counter=10;
        for (int i = 1; i <= 10; i++) {
            if(tmp.charAt(tmp.length()-i)=='0') counter--;
            else break;
        }
        if(counter>0)maskOfFormat=maskOfFormat.concat(".");
        for (int i = 0; i < counter; i++) {
            maskOfFormat=maskOfFormat.concat("0");
        }
        DecimalFormat finalFormat=new DecimalFormat(maskOfFormat);
        tmp=finalFormat.format(res);
        return tmp;
    }

    public static void main(String[] args) {
        launch(args);
    }
}