package app.android.thientt.tudienchuyennganh.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import app.android.thientt.tudienchuyennganh.R;
import app.android.thientt.tudienchuyennganh.model.TTTDictionary;
import app.android.thientt.tudienchuyennganh.model.TTTWord;
import io.realm.RealmResults;

public class RecyclerWordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private RealmResults<TTTWord> tttWords;
    private RealmResults<TTTDictionary> tttDictionaries;

    public RecyclerWordAdapter(RealmResults<TTTWord> tttWords, RealmResults<TTTDictionary> tttDictionaries) {
        this.tttWords = tttWords;
        this.tttDictionaries = tttDictionaries;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return RecyclerItemViewHolder.newInstance(LayoutInflater.from(context).inflate(R.layout.recycler_tuvung_item, parent, false), tttWords, tttDictionaries);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        holder.setText(position);
    }

    public int getBasicItemCount() {
        return tttWords == null ? 0 : tttWords.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getBasicItemCount();
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}