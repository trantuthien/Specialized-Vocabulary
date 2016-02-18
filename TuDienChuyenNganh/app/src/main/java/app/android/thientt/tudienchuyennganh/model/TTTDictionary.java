package app.android.thientt.tudienchuyennganh.model;

import io.realm.RealmObject;

/**
 * Created by ThienTT on 2/14/16.
 * object for dictionary
 */
public class TTTDictionary extends RealmObject {
    public static final String NAME = "strName";
    public static final String ID = "id";
    private String strName;
    private int id;

    public TTTDictionary(String strName, int id) {
        this.strName = strName;
        this.id = id;
    }

    public TTTDictionary() {
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
