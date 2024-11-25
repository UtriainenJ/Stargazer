package ryhma5.model;

public class UserPreferences {

    private String textFieldText;
    private String date;

    public UserPreferences(String textFieldText, String date){
        this.textFieldText = textFieldText;
        this.date = date;
    }

    // getters
    public String getTextFieldText() { return textFieldText; }
    public String getDateStart(){ return date; }

    // setters
    public void  setTextFieldText (String cityName){ this.textFieldText = cityName; }
    public void setDateStart (String date){ this.date = date; }
}