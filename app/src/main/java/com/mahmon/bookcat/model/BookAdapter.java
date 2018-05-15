package com.mahmon.bookcat.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmon.bookcat.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> booksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView isbn;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.book_title);
            isbn = view.findViewById(R.id.book_isbn);
            thumbnail = view.findViewById(R.id.book_thumbnail);
        }

    }

    public BookAdapter(Context mContext, List<Book> booksList) {
        this.mContext = mContext;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_cat_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.title.setText(book.getTitle());
        // TODO load image into card view
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

}