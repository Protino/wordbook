package com.calgen.wordbook.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.calgen.wordbook.Models.Word;
import com.calgen.wordbook.R;

/**
 * Created by Gurupad on 29-Jun-15.
 */
public class NewWord extends Fragment {

    public Word word;
    boolean isInEditMode = false;
    public int location;
    public static EditText etWord;
    public static EditText etDescription;
    public static EditText etEtymo;
    public static EditText etExample;
    public static RadioGroup rgType;
    public static RadioButton rbPositve;
    public static RadioButton rbNeutral;
    public static RadioButton rbNegative;
    static String newWord;
    static String description;
    static byte type;
    static String example;
    static String etymo;

    private static final String TAG = "NewWord";

    addNewWordListener mCallback;

    public NewWord() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_word, container, false);
        initializeViews(rootView);
        try {
            //When a card is clicked in the home fragment
            location = getArguments().getInt("location");
            isInEditMode = true;
            setUpView();
            return rootView;
        } catch (Exception f) {
            Log.d(TAG, f.toString());
        }
        try {
            //When shared via other apps
            isInEditMode = false;
            etWord.setText(getArguments().getString("newword"));
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_word, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item selected and deal with it
        switch (item.getItemId()) {
            case R.id.action_done:
                newWord = new String(etWord.getText().toString().trim());
                if (!newWord.isEmpty() && isAlpha(newWord.toCharArray())) {
                    word = getNewWordObject();
                    if (isInEditMode)
                        Home.words.set(location, word);
                    else if (!isduplicate(newWord))
                        Home.words.add(word);
                    else {
                        Toast.makeText(getActivity(), newWord + " is already added", Toast.LENGTH_LONG).show();
                        return true;
                    }
                    Home.storeWord();
                    //mCallback.addNewWord(word);
                    getFragmentManager().popBackStackImmediate();

                } else
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isduplicate(String word) {
        Log.d(TAG, String.valueOf(Home.words.size()));
        for (int i = 0; i < Home.words.size(); i++) {
            if (Home.words.get(i).word.equals(word)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlpha(char[] chars) {

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                Toast.makeText(getActivity(), "That's a weird word", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (addNewWordListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement addNewWordListener");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("New Word");
    }

    @Override
    public void onDestroyView() {
        hide_keyboard(getActivity());
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public void hide_keyboard(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public Word getNewWordObject() {
        description = etDescription.getText().toString().trim();
        example = etExample.getText().toString().trim();
        getCheckedRadioButton();
        etymo = etEtymo.getText().toString().trim();
        return new Word(newWord, description, type, etymo, example);
    }

    public void getCheckedRadioButton() {
        switch (rgType.getCheckedRadioButtonId()) {

            case R.id.rbPositive:
                type = 1;
                break;
            case R.id.rbNeutral:
                type = 0;
                break;
            case R.id.rbNegative:
                type = -1;
                break;

            default:
                type = 0;
                break;
        }
    }


    private void setUpView() {
        etWord.setText(Home.editWord.word);
        etDescription.setText(Home.editWord.description);
        etExample.setText(Home.editWord.example);
        etEtymo.setText(Home.editWord.etymo);
        setCheckedRadioButton();
    }

    private void setCheckedRadioButton() {
        switch (Home.editWord.type) {
            case 1:
                rbPositve.setChecked(true);
                break;
            case 0:
                rbNeutral.setChecked(true);
                break;
            case -1:
                rbNegative.setChecked(true);
                break;
        }
    }

    private void initializeViews(View view) {
        etWord = (EditText) view.findViewById(R.id.etNewWord);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etEtymo = (EditText) view.findViewById(R.id.etEtymology);
        etExample = (EditText) view.findViewById(R.id.etExample);
        rgType = (RadioGroup) view.findViewById(R.id.rgType);
        rbPositve = (RadioButton) view.findViewById(R.id.rbPositive);
        rbNeutral = (RadioButton) view.findViewById(R.id.rbNeutral);
        rbNegative = (RadioButton) view.findViewById(R.id.rbNegative);
    }


    public interface addNewWordListener {
        void addNewWord(Word word);
    }
}
