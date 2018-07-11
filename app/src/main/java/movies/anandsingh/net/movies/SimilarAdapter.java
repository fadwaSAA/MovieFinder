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
 * Created by Fadwasa on 09/07/2018 AD.
 */

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.ViewHolder2> {
    private Context mContext;
    private ArrayList<MovieDetails> movieList;
    String sessionID1,UID1;



    public SimilarAdapter(Context mContext, ArrayList<MovieDetails> movieList,String sessionID1,String UID1){
        this.mContext = mContext;
        this.movieList = movieList;
        this.sessionID1=sessionID1;
        this.UID1=UID1;
        Log.d("moviwee","SimilarAdapter" );

    }

    @Override
    public SimilarAdapter.ViewHolder2 onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);
        Log.d("moviwee","onCreateViewHolder" );

        return new SimilarAdapter.ViewHolder2(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder2 holder, int position) {
        holder.title.setText(movieList.get(position).getOriginal_title());
        String poster = "https://image.tmdb.org/t/p/w500" + movieList.get(position).getPoster_path();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.thumbnail);
        Log.d("moviwee","onBindViewHolder" );



    }



    @Override
    public int getItemCount(){
        Log.d("moviwee","getItemCount" );

        return movieList.size();

    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;
        public ViewHolder2(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            Log.d("moviwee","ViewHolder1" );

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        MovieDetails clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("movies", clickedDataItem );
                        intent.putExtra("sessionID", sessionID1 );
                        intent.putExtra("uID", UID1 );

                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
