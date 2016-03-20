package TestModules;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.calgen.wordbook.Models.Word;
import com.calgen.wordbook.R;

import java.util.List;

/**
 * Created by Gurupad on 08-Jul-15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.WordViewHolder> {

    List<Word> words;
    OnItemClickListener mItemClickListener;
    OnItemLongClickListener mItemLongClickListener;
    private final static String TAG = "RecyclerViewAdapter";
    private MultiSelector multiSelector = new MultiSelector();


    public RecyclerViewAdapter(List<Word> words) {
        this.words = words;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        WordViewHolder wordViewHolder = new WordViewHolder(view);
        return wordViewHolder;
    }

    @Override
    public void onBindViewHolder(WordViewHolder wordViewHolder, int position) {

        Word word = words.get(position);
        wordViewHolder.bindWordView(word);
        Log.d(TAG, "binding word" + word.word + "at position" + position);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class WordViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        CardView cardView;
        TextView tvWord;
        TextView tvDescription;
        private final String TAG = "WordViewHolder";
        private Word mWord;

        public WordViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            tvWord = (TextView) itemView.findViewById(R.id.tvWord);

            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
        }

        public void bindWordView(Word word) {
            mWord = word;
            tvWord.setText(word.word);
            tvDescription.setText(word.description);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClick(v, getPosition());
            }
            multiSelector.setSelected(this, true);
            return true;
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        public void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener onItemLongClickListener) {
        this.mItemLongClickListener = onItemLongClickListener;
    }
}


