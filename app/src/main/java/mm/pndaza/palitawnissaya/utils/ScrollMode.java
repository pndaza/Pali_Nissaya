package mm.pndaza.palitawnissaya.utils;

public enum ScrollMode {
    vertical, horizental;

    public static ScrollMode toScrollMode(String enumString){
        try {
return valueOf(enumString);
        } catch (Exception exception){
return vertical;
        }
    }
}
