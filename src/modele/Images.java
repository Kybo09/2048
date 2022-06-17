package modele;
import java.awt.Color;

public class Images {

    private String path = "Assets";
    private static String theme = "/futur/";

    public Images(){
    }

    public void changeTheme(){
        if(theme.equals("/futur/")){
            theme = "/cartoon/";
        }
        else{
            theme = "/futur/";
        }
    }

    public String getTheme(){
        return theme;
    }

    public String getPath(){
        return this.path + theme;
    }

    public String getImage(int value){
        return path + this.theme + value + ".png";
    }

    public String getButton(String name){
        return path + this.theme + name + ".png";
    }

    public String getButtonHover(String name){
        return path + this.theme + name + "-hover.png";
    }

    public Color getBackgroundColor(){
        if(theme.equals("/futur/")){
            return new Color(27,11,37);
        }
        else{
            return new Color(11, 37, 11);
        }
    }

    public Color getAccentColor(){
        if(theme.equals("/futur/")){
            return new Color(204, 108, 240);
        }
        else{
            return new Color(113, 232, 109);
        }
    }

}
