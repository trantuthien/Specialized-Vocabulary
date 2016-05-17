package app.android.thientt.tudienchuyennganh.model;

import android.content.Context;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import app.android.thientt.tudienchuyennganh.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ThienTT on 2/11/16.
 * This class use for read excel file
 */
public class ExcelPoi {
    public static String readExcelFile(Context context, File file, String filename) {
        try {
            int idDictionary = 0;
            Realm realm = Realm.getInstance(context);
            RealmResults<TTTDictionary> TTTDictionaries = realm.where(TTTDictionary.class).equalTo(TTTDictionary.NAME, filename).findAll();
            if (TTTDictionaries != null && TTTDictionaries.size() > 0) {
                return context.getString(R.string.exist_dictionary_name);
            } else {
                TTTDictionaries = realm.where(TTTDictionary.class).findAll();
            }
            if (TTTDictionaries != null)
                idDictionary = TTTDictionaries.size();

            realm.beginTransaction();
            TTTDictionary TTTDictionary = new TTTDictionary(filename, idDictionary);
            realm.copyToRealm(TTTDictionary);
            realm.commitTransaction();

            FileInputStream myInput = new FileInputStream(file);
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Iterator rowIter = mySheet.rowIterator();
            ArrayList<String> emp = new ArrayList<>();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    emp.add(myCell.toString());
                }
                /////////// HARD CODE FOR SIMPLE ///////////////
                if (emp.size() == 3) {
                    realm.beginTransaction();
                    TTTWord TTTWord = new TTTWord(emp.get(0), emp.get(1), emp.get(2), idDictionary);
                    realm.copyToRealm(TTTWord);
                    realm.commitTransaction();
                    emp.clear();
                }
            }
            return context.getString(R.string.finish_add_dictionary);
        } catch (Exception e) {
            TLog.e(e.toString());
        }
        return context.getString(R.string.error_unknown);
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
