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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import movies.anandsingh.net.movies.DetailActivity;
import movies.anandsingh.net.movies.MovieDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delaroy on 5/18/17.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder1> {

    private Context mContext;
    private ArrayList<MovieDetails> movieList;
    String sessionID1,UID1;



    public MoviesAdapter(Context mContext, ArrayList<MovieDetails> movieList,String sessionID1,String UID1){
        this.mContext = mContext;
        this.movieList = movieList;
        this.sessionID1=sessionID1;
        this.UID1=UID1;
    }

    @Override
    public MoviesAdapter.ViewHolder1 onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.ViewHolder1 viewHolder, int i){
        viewHolder.title.setText(movieList.get(i).getOriginal_title());
          String poster = "https://image.tmdb.org/t/p/w500" + movieList.get(i).getPoster_path();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(viewHolder.thumbnail);

    }

    @Override
    public int getItemCount(){
        return movieList.size();
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
