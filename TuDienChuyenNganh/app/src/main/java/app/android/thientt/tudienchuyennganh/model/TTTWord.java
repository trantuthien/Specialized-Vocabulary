package app.android.thientt.tudienchuyennganh.model;

import io.realm.RealmObject;

/**
 * Created by ThienTT on 2/11/16.
 * object for the word of dictionary
 */
public class TTTWord extends RealmObject {
    private String strForeign, strVietnamese, strVietnameNo;
    private int idDictionary;
    public static String FOREIGN = "strForeign";
    public static String VIETNAMESENO = "strVietnameNo";
    public static String VIETNAMESE = "strVietnamese";
    public static String IDTUDIEN = "idDictionary";

    public TTTWord(String strForeign, String strVietnamese, String strVietnameNo, int idDictionary) {
        this.strForeign = strForeign;
        this.strVietnamese = strVietnamese;
        this.strVietnameNo = strVietnameNo;
        this.idDictionary = idDictionary;
    }

    public String getStrForeign() {
        return strForeign;
    }

    public void setStrForeign(String strForeign) {
        this.strForeign = strForeign;
    }

    public String getStrVietnamese() {
        return strVietnamese;
    }

    public void setStrVietnamese(String strVietnamese) {
        this.strVietnamese = strVietnamese;
    }

    public String getStrVietnameNo() {
        return strVietnameNo;
    }

    public void setStrVietnameNo(String strVietnameNo) {
        this.strVietnameNo = strVietnameNo;
    }

    public int getIdDictionary() {
        return idDictionary;
    }

    public void setIdDictionary(int idDictionary) {
        this.idDictionary = idDictionary;
    }

    public TTTWord() {

    }
}
