package com.example.customlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ItemsDataAdapter extends BaseAdapter {

    Context context;


    private List<ItemData> items;

    private LayoutInflater inflater;

    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeItem((Integer) v.getTag());
        }
    };


    ItemsDataAdapter(Context context, List<ItemData> items) {
        this.context = context;
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void addItem(ItemData item) {
        this.items.add(item);
        notifyDataSetChanged();
    }


    public void removeItem(int position) {
        items.remove(position);



        FileWriter dataWriter = null;

        File dataFile = new File(context.getExternalFilesDir(null), "data.txt");
        dataFile.delete();

        try {
            dataWriter = new FileWriter(dataFile, true);

            for (ItemData item : items) {
                dataWriter.append(item.getTitle() + ";");
            }



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dataWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }






        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public ItemData getItem(int position) {
        if (position < items.size()) {
            return items.get(position);
        } else {
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_view, parent, false);
        }

        ItemData itemData = items.get(position);

        ImageView image = view.findViewById(R.id.icon);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        Button delBtn = view.findViewById(R.id.checkbox);

        image.setImageDrawable(itemData.getImage());
        title.setText(itemData.getTitle());
        subtitle.setText(itemData.getSubtitle());

        delBtn.setOnClickListener(myButtonClickListener);
        delBtn.setTag(position);
        return view;
    }
}