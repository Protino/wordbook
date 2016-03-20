package com.calgen.wordbook.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.calgen.wordbook.Activity.MainActivity;
import com.calgen.wordbook.Models.Word;
import com.calgen.wordbook.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Gurupad on 29-Jun-15.
 */
public class Home extends Fragment implements View.OnClickListener {

    private static final String TAG = "Home";
    public static List<Word> words = new ArrayList<>();
    public static Word newWord;
    public static Word editWord;
    public static SharedPreferences sharedPrefs;
    public static SharedPreferences.Editor editor;
    private static FloatingActionButton fab;
    RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private MultiSelector multiSelector = new MultiSelector();
    private ModalMultiSelectorCallback mDeleteMode = new ModalMultiSelectorCallback(multiSelector) {

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            super.onCreateActionMode(actionMode, menu);
            getActivity().getMenuInflater().inflate(R.menu.contextual_home, menu);
            fab.hide();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            multiSelector.setSelectable(true);
            multiSelector.clearSelections();
            return super.onPrepareActionMode(actionMode, menu);
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            //multiSelector.clearSelections();
            multiSelector.setSelectable(false);
            initializeAdapter();
            fab.show();
            super.onDestroyActionMode(actionMode);
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            editor = sharedPrefs.edit();
            Gson gson = new Gson();

            switch (menuItem.getItemId()) {
                case R.id.action_delete:

                    actionMode.finish();

                    for (int i = words.size(); i >= 0; i--) {
                        if (multiSelector.isSelected(i, 0)) {
                            Word word = words.get(i);
                            words.remove(word);
                            recyclerView.getAdapter().notifyItemRemoved(i);
                        }
                    }

                    String json = gson.toJson(words);
                    editor.putString("Words", json);
                    editor.commit();
                    multiSelector.clearSelections();
                    multiSelector.setSelectable(false);
                    return true;
                default:
                    return false;
            }
        }
    };

    public Home() {
        // Required empty public constructor
    }

    public static void storeWord() {
        Gson gson = new Gson();
        editor = sharedPrefs.edit();
        String json = gson.toJson(words);
        editor.putString("Words", json);
        editor.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPrefs = (getActivity()).getSharedPreferences("Words", Context.MODE_PRIVATE);
        loadWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home, container, false);
        //Add reference to the floating button
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.show();
        //Get reference to the recycler view
        recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);
        //Get LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((ActionBarActivity) getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadWords();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //updateData();
        fab.show();
        initializeAdapter();
        Gson gson = new Gson();
        editor = sharedPrefs.edit();
        newWord = ((MainActivity) getActivity()).getNewWord();
        if (newWord != null) {
            if (!newWord.word.isEmpty()) {
                if (words.contains(newWord))
                    words.set(words.indexOf(newWord), newWord);
                else
                    words.add(newWord);
                storeWord();
                loadWords();
            }
        }
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Home");

        if (multiSelector != null) {
            if (multiSelector.isSelectable()) {
                if (mDeleteMode != null) {
                    mDeleteMode.setClearOnPrepare(false);
                    ((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
                }

            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.contextual_home, menu);
    }

    private void openSelectedWord(Word word, int location) {
        Bundle bundle = new Bundle();
        editWord = word;
        bundle.putInt("location", location);
        Fragment newFragment = new NewWord();
        newFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, newFragment).addToBackStack(null).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                Fragment newFragment = new NewWord();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
                break;
        }


    }

    private void initializeAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(words);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void loadWords() {
        if (sharedPrefs.contains("Words")) {
            String json = sharedPrefs.getString("Words", null);
            Type type = new TypeToken<ArrayList<Word>>() {
            }.getType();
            words = new ArrayList<>();
            words = new Gson().fromJson(json, type);
        }
    }

    public class WordViewHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {
        private final CardView cardView;
        private final TextView tvWord;
        private final TextView tvDescription;
        private final TextView tvType;
        private final String TAG = "WordViewHolder";
        public Word mWord;

        public WordViewHolder(View itemView) {
            super(itemView, multiSelector);

            itemView.setOnClickListener(this);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
            tvWord = (TextView) itemView.findViewById(R.id.tvWord);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
        }

        public void bindWordView(Word word) {
            mWord = word;
            tvWord.setText(word.word);
            tvDescription.setText(word.description);
            String resourceName = "neutral";
            switch (word.type) {
                case 1:
                    resourceName = "positive";
                    break;
                case 0:
                    resourceName = "neutral";
                    break;
                case -1:
                    resourceName = "negative";
                    break;
            }

            tvType.setBackgroundColor(getResources().getColor(getResources().getIdentifier(resourceName,"color",MainActivity.PACKAGE_NAME)));
        }

        @Override
        public void onClick(View v) {
            if (mWord == null)
                return;

            if (!multiSelector.tapSelection(this))
                openSelectedWord(mWord, words.indexOf(mWord));
        }

        @Override
        public boolean onLongClick(View v) {
            ((AppCompatActivity) getActivity()).startSupportActionMode(mDeleteMode);
            multiSelector.setSelected(this, true);
            return true;
        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<WordViewHolder> {

        private final static String TAG = "RecyclerViewAdapter";
        List<Word> words;

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
    }


}
