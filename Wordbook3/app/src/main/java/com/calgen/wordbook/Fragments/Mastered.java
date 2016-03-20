package com.calgen.wordbook.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calgen.wordbook.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by Gurupad Mamadapur on 21-Sep-15.
 */
public class Mastered extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mastered, container, false);

        // Create a Card
        Card card = new Card(getActivity(), R.layout.card_layout);

        // Create a CardHeader
        CardHeader header = new CardHeader(getActivity());
        header.setTitle("Hello world");

        card.setTitle("Simple card demo");
        CardThumbnail thumb = new CardThumbnail(getActivity());
        thumb.setDrawableResource(R.drawable.ic_launcher);

        card.addCardThumbnail(thumb);

        // Add Header to card
        card.addCardHeader(header);

        // Set card in the cardView
        CardView cardView = (CardView) rootView.findViewById(R.id.carddemo);
        cardView.setCard(card);

        return rootView;
    }
}
