package sonu.com.palette.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import sonu.com.palette.R;

import java.text.DateFormat;
import java.util.Date;

import sonu.com.palette.data.db.model.Palette;

/**
 * Created by sonu on 1/13/2017.
 */

public class PalettesAdapter extends RecyclerView.Adapter<PalettesAdapter.MyViewHolder>{

    private Palette mPalettes[];
    private Context mContext;

    public PalettesAdapter(Context context, Palette palettes[]){
        mContext = context;
        mPalettes = palettes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_palettes, parent, false));
    }

    private class MarkedImageViewOnClickListener implements View.OnClickListener{
        private int mPosition;
        private boolean mMarked;
        MarkedImageViewOnClickListener(int position, boolean marked) {
            mPosition = position;
            mMarked = marked;
        }
        @Override
        public void onClick(View v) {
            if(mMarked) {
                ((ImageView) v)
                        .setImageDrawable(mContext.getDrawable(R.drawable.ic_bookmark_border_24dp));
            } else {
                ((ImageView) v)
                        .setImageDrawable(mContext.getDrawable(R.drawable.ic_bookmark_solid_24dp));
            }
            mMarked = !mMarked;
            mPalettes[mPosition].marked = mMarked;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Palette palette = mPalettes[position];
        holder.snoTextView.setText((position+1)+"");

        Date date = new Date(palette.timestamp);
        holder.timestampTextView.setText(DateFormat.getDateInstance().format(date)+"");

        holder.labelEditText.setText(palette.label);
        if(palette.marked) {
            holder.markedImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_bookmark_solid_24dp));
        } else {
            holder.markedImageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_bookmark_border_24dp));
        }

        holder.markedImageView.setOnClickListener(new MarkedImageViewOnClickListener(position, palette.marked));

        String colorLabels[] = palette.colorLabels;
        String colorHexs[] = palette.colorHexs;

        float density = mContext.getResources().getDisplayMetrics().density;

        holder.paletteHolder.removeAllViews();

        for (int i = 0;i < colorLabels.length; i++){
            View paletteElement = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_palette_element, null, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,(int)(4*density),0,(int)(4*density));
            paletteElement.setLayoutParams(layoutParams);

            View colorView = paletteElement.findViewById(R.id.colorView);
            TextView colorHex = (TextView) paletteElement.findViewById(R.id.colorHex);
            TextView colorLabel = (TextView) paletteElement.findViewById(R.id.colorLabel);

            ((GradientDrawable)colorView.getBackground()).setColor(Color.parseColor(colorHexs[i]));

            if(isColorDark(Color.parseColor(colorHexs[i]))) {
                ((GradientDrawable)colorView.getBackground()).setStroke((int)(1*mContext.getResources().getDisplayMetrics().density),lighten(Color.parseColor(colorHexs[i]),0.2));
            } else {
                ((GradientDrawable)colorView.getBackground()).setStroke((int)(1*mContext.getResources().getDisplayMetrics().density),darken(Color.parseColor(colorHexs[i]),0.2));
            }

            colorHex.setText(colorHexs[i]);
            colorLabel.setText(colorLabels[i]);

            holder.paletteHolder.addView(paletteElement);

            if(i != (colorLabels.length-1)) {
                View divider = new View(mContext);
                layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,(int)(1*density));
                layoutParams.setMargins((int)((16+36+12)*density),0,(int)(0*density),0);
                divider.setLayoutParams(layoutParams);
                divider.setBackgroundColor(Color.parseColor("#eeeeee"));
                holder.paletteHolder.addView(divider);
            }
        }
    }

    public boolean isColorDark(int color){
        double darkness = 1-(0.299*Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int)Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    @Override
    public int getItemCount() {
        return mPalettes.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView snoTextView,timestampTextView;
        public LinearLayout paletteHolder;
        public EditText labelEditText;
        public ImageView markedImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            snoTextView = (TextView) itemView.findViewById(R.id.snoTextView);
            paletteHolder = (LinearLayout) itemView.findViewById(R.id.paletteHolder);
            timestampTextView = (TextView) itemView.findViewById(R.id.timeStampTextView);
            labelEditText = (EditText) itemView.findViewById(R.id.labelEditText);
            markedImageView = (ImageView) itemView.findViewById(R.id.markedImageView);
        }
    }
}
