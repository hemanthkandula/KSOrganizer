package edu.sastra.ks.ksorganizer;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<Movie> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre,rank,name;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            genre = (TextView) view.findViewById(R.id.genre);
            rank = (TextView) view.findViewById(R.id.rank);
            name = (TextView) view.findViewById(R.id.name);

        }
    }


    public MyRecyclerViewAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.rank.setText(movie.getRank());
        holder.name.setText(movie.getName());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setFilter(List<Movie> movieList) {
        moviesList = new ArrayList<>();
        moviesList.addAll(movieList);
        notifyDataSetChanged();
    }
    public Movie getpos(int position){
        return moviesList.get(position);
    }

}