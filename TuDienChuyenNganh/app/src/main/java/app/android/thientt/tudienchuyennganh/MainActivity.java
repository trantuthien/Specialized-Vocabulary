package app.android.thientt.tudienchuyennganh;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import app.android.thientt.tudienchuyennganh.model.ExcelPoi;
import app.android.thientt.tudienchuyennganh.model.TLog;
import app.android.thientt.tudienchuyennganh.model.TTTDictionary;
import app.android.thientt.tudienchuyennganh.model.TTTWord;
import app.android.thientt.tudienchuyennganh.view.RecyclerWordAdapter;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final int SEARCH_TEXT_VIETNAMESENO = 0;
    private static final int SEARCH_TEXT_ENGLISH = 1;
    private static final int SEARCH_TEXT_VIETNAMESE = 2;
    private static final int SEARCH_TEXT_ALL = 3;

    private static final int ADDMORE = 123;
    private static final String TUDIENCHUYENNGANH = "TUDIENCHUYENNGANH";
    private static final String CHOSENDICTIONARY = "CHOSENDICTIONARY";
    private static final String TYPESEARCHING = "TYPESEARCHING";
    private static final String LOATFIRSTTIME = "LOATFIRSTTIME";
    private RecyclerView recyclerView;
    private RealmResults<TTTWord> TTTWords;
    private RealmResults<TTTDictionary> TTTDictionaries;
    private RecyclerWordAdapter mAdapter;
    private Integer[] chosenDictionary;
    private int typeSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddActivity();
            }
        });
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        String str = getSharedPreferences(MainActivity.TUDIENCHUYENNGANH, Context.MODE_PRIVATE).getString(MainActivity.CHOSENDICTIONARY, null);
        if (str != null) {
            TLog.d("lay thong tin duoc chon: " + str);
            chosenDictionary = new Gson().fromJson(str, new TypeToken<Integer[]>() {
            }.getType());
        }
        typeSearching = getSharedPreferences(MainActivity.TUDIENCHUYENNGANH, Context.MODE_PRIVATE).getInt(MainActivity.TYPESEARCHING, MainActivity.SEARCH_TEXT_VIETNAMESENO);

    }

    @Override
    protected void onResume() {
        super.onResume();
        readRealm(null, typeSearching);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        assert searchView != null;
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_type_searching:
                chooseTypeSearch();
                break;
            case R.id.action_settings:
                callChooseDictionary();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseTypeSearch() {
        new MaterialDialog.Builder(this)
                .title(R.string.action_type_searching)
                .items(R.array.type_searching)
                .itemsCallbackSingleChoice(typeSearching, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        getSharedPreferences(MainActivity.TUDIENCHUYENNGANH, Context.MODE_PRIVATE).edit().putInt(MainActivity.TYPESEARCHING, MainActivity.SEARCH_TEXT_VIETNAMESENO).commit();
                        typeSearching = which;
                        return true;
                    }
                })
                .positiveText(R.string.done_chosen)
                .show();
    }

    private void callChooseDictionary() {
        ArrayList<String> list = new ArrayList<>();
        if (TTTDictionaries != null && TTTDictionaries.size() > 0) {
            list.add(getString(R.string.str_all));
            for (TTTDictionary TTTDictionary : TTTDictionaries) {
                list.add(TTTDictionary.getStrName());
            }
        } else {
            list.add(getString(R.string.empty_array));
        }
        new MaterialDialog.Builder(this)
                .title(R.string.action_filter)
                .items(list)
                .itemsCallbackMultiChoice(chosenDictionary, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        String eee = new Gson().toJson(which);
                        getSharedPreferences(MainActivity.TUDIENCHUYENNGANH, Context.MODE_PRIVATE).edit().putString(MainActivity.CHOSENDICTIONARY, eee).commit();
                        chosenDictionary = which;
                        return true;
                    }
                })
                .positiveText(R.string.done_chosen)
                .show();
    }

    private void callAddActivity() {
        Intent intent = new Intent(this, AddActivity.class);
        startActivityForResult(intent, ADDMORE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDMORE) {
            readRealm(null, typeSearching);
        }
    }
    public void readRealm(String query, int type_search) {
        Realm realm = Realm.getInstance(MainActivity.this);
        TTTDictionaries = realm.where(TTTDictionary.class).findAll();
        if (TTTDictionaries != null && TTTDictionaries.size() > 0) {
            RealmQuery<TTTWord> query_words = realm.where(TTTWord.class);
            if (chosenDictionary != null && chosenDictionary.length > 0 && chosenDictionary[0] != 0) {
                for (int j = 0; j < chosenDictionary.length; j++) {
                    if (j != 0) {
                        query_words = query_words.or();
                    }
                    query_words = query_words.equalTo(TTTWord.IDTUDIEN, chosenDictionary[j] - 1);
                }
            }

            switch (type_search) {
                case MainActivity.SEARCH_TEXT_VIETNAMESENO:
                default:
                    TTTWords = query_words.contains(TTTWord.VIETNAMESENO, query, Case.INSENSITIVE).findAll();
                    break;
                case MainActivity.SEARCH_TEXT_ENGLISH:
                    TTTWords = query_words.contains(TTTWord.FOREIGN, query, Case.INSENSITIVE).findAll();
                    break;
                case MainActivity.SEARCH_TEXT_VIETNAMESE:
                    TTTWords = query_words.contains(TTTWord.VIETNAMESE, query, Case.INSENSITIVE).findAll();
                    break;
                case MainActivity.SEARCH_TEXT_ALL:
                    TTTWords = query_words.contains(TTTWord.VIETNAMESE, query, Case.INSENSITIVE).or().contains(TTTWord.VIETNAMESENO, query, Case.INSENSITIVE).or().contains(TTTWord.FOREIGN, query, Case.INSENSITIVE).findAll();
                    break;
            }
            if (TTTWords != null) {
                showTuvungsOnScreen();
            } else {
                TLog.e("co gi do ko on");
            }
        } else {
            addDictionaryTranslateviet();
        }
    }

    private MaterialDialog dialog;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            dialog.dismiss();
            Bundle bundle = message.getData();
            if (bundle != null) {
                String kequa = bundle.getString(MainActivity.LOATFIRSTTIME);
                if (kequa != null) {
                    Snackbar.make(recyclerView, kequa, Snackbar.LENGTH_LONG).show();
                    readRealm(null, typeSearching);
                }
            }
            return true;
        }
    });

    private void addDictionaryTranslateviet() {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.process)
                .content(R.string.waiting)
                .progress(true, 0)
                .show();
        final File file = getFileFromAsset();
        final String name = file.getName();
        final Messenger messenger = new Messenger(handler);
        Thread thread = new Thread() {
            @Override
            public void run() {
                String kequa = ExcelPoi.readExcelFile(MainActivity.this, file, name.substring(0, name.lastIndexOf(".")));
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(MainActivity.LOATFIRSTTIME, kequa);
                message.setData(bundle);
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private File getFileFromAsset() {
        File f = new File(getCacheDir() + "/kinh te-translateviet.com.xls");
        if (!f.exists()) try {
            InputStream is = getAssets().open("kinh te_translateviet.com.xls");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return f;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        TLog.d(query);
        mAdapter.notifyDataSetChanged();
        readRealm(query, typeSearching);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        TLog.d(newText);
        readRealm(newText, typeSearching);
        return false;
    }

    private void showTuvungsOnScreen() {
        mAdapter = new RecyclerWordAdapter(TTTWords, TTTDictionaries);
        recyclerView.setAdapter(new ScaleInAnimationAdapter(mAdapter));
    }
}
