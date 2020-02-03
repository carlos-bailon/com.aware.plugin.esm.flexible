package com.aware.plugin.esm.flexible;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aware.utils.IContextCard;

public class ContextCard implements IContextCard {

    //Constructor used to instantiate this card
    public ContextCard() {
    }

    private TextView mTitleTextView = null;

    @Override
    public View getContextCard(Context context) {
        //Load card layout
        View card = LayoutInflater.from(context).inflate(R.layout.card_esm_flexible, null);
        mTitleTextView = card.findViewById(R.id.tv_card);

        //Return the card to AWARE/apps
        return card;
    }
}
