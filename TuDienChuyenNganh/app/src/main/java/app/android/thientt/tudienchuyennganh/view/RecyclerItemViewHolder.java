package app.android.thientt.tudienchuyennganh.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.android.thientt.tudienchuyennganh.R;
import app.android.thientt.tudienchuyennganh.model.TTTDictionary;
import app.android.thientt.tudienchuyennganh.model.TTTWord;
import io.realm.RealmResults;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public RealmResults<TTTWord> TTTWords;
    public RealmResults<TTTDictionary> TTTDictionaries;
    private TextView tev_english, tev_vietnamese, tev_dictionary;
    public Context context;

    public RecyclerItemViewHolder(View itemView) {
        super(itemView);
        tev_vietnamese = (TextView) itemView.findViewById(R.id.tev_vietnamese);
        tev_english = (TextView) itemView.findViewById(R.id.tev_english);
        tev_dictionary = (TextView) itemView.findViewById(R.id.tev_dictionary);
        tev_english.setOnClickListener(this);
        tev_vietnamese.setOnClickListener(this);
    }

    public static RecyclerView.ViewHolder newInstance(View view, RealmResults<TTTWord> words, RealmResults<TTTDictionary> TTTDictionaries) {
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);
        holder.TTTWords = words;
        holder.TTTDictionaries = TTTDictionaries;
        holder.context = view.getContext();
        return holder;
    }

    public void setText(int position) {
        //tev_english.setTag(position); //for something i have not use
        tev_english.setText(TTTWords.get(position).getStrForeign());
        tev_vietnamese.setText(TTTWords.get(position).getStrVietnamese());
        tev_dictionary.setText(TTTDictionaries.get(TTTWords.get(position).getIdDictionary()).getStrName());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tev_english:
            case R.id.tev_vietnamese:
                String text = ((TextView) view).getText().toString();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(context.getString(R.string.translateviet), text);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view,R.string.clipboard_success, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}