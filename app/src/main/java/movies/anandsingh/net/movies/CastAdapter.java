package movies.anandsingh.net.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Fadwasa on 07/07/2018 AD.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder1>{
    private Context mContext;
    private ArrayList<CastInfo> castList;
    String sessionID,uID;


    public CastAdapter(Context mContext, ArrayList<CastInfo> castList,String sessionID,String uID){
        this.mContext = mContext;
        this.castList = castList;
        this.sessionID=sessionID;
        this.uID=uID;
    }

    @Override
    public CastAdapter.ViewHolder1 onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new CastAdapter.ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder1 holder, int position) {
        holder.title.setText(castList.get(position).getcName());
        Log.d("rrrr","eddd"+castList.get(position).getcName());

        String poster = "https://image.tmdb.org/t/p/w500" + castList.get(position).getcPoster();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.thumbnail);

    }



    @Override
    public int getItemCount(){
        return castList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;
        public ViewHolder1(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        CastInfo clickedDataItem = castList.get(pos);
                        Intent intent = new Intent(mContext, CastDetails.class);
                        intent.putExtra("cast", clickedDataItem );
                        intent.putExtra("sessionid", sessionID );
                        intent.putExtra("uid", uID );


                        mContext.startActivity(intent);
                    }
                }
            });

        }
    }
}
