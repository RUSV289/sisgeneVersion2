package com.instituto.cuanto.sisgene.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.instituto.cuanto.sisgene.R;
import com.instituto.cuanto.sisgene.entities.TipoPreguntaUnicaItem;

import java.util.ArrayList;

/**
 * Created by Gustavo on 27/10/2015.
 */
public class TipoPreguntaUnicaAdapter extends BaseAdapter {

    ArrayList<TipoPreguntaUnicaItem> myList = new ArrayList<TipoPreguntaUnicaItem>();
    LayoutInflater inflater;
    Context context;


    public TipoPreguntaUnicaAdapter(Context context, ArrayList<TipoPreguntaUnicaItem> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public TipoPreguntaUnicaItem getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tipoPreguntaUnicaItem_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final TipoPreguntaUnicaItem currentTipoPreguntaUnicaItem = getItem(position);

        mViewHolder.tvTitle.setText(currentTipoPreguntaUnicaItem.getTitle());
        mViewHolder.tvDesc.setText(currentTipoPreguntaUnicaItem.getDescription());

        final int i = position;
        mViewHolder.tvDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //((ListItem) myItems.get(i)).caption = s.toString();
                currentTipoPreguntaUnicaItem.setDescription(s.toString());
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDesc;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.tvNombreEntrevistado);
            tvDesc = (TextView) item.findViewById(R.id.etRespuestaPregunta);
        }
    }

}
