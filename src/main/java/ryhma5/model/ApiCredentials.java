package ryhma5.model;

public class ApiCredentials {
    private String applicationId;
    private String applicationSecret;

    // Setters
    public void setApplicationId(String applicationId){this.applicationId = applicationId;}
    public void setApplicationSecret(String applicationSecret){this.applicationSecret = applicationSecret;}

    // Getters
    public String getApplicationId() {return applicationId;}
    public String getApplicationSecret() {return applicationSecret;}
}
