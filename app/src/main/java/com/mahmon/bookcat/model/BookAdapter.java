package com.mahmon.bookcat.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmon.bookcat.R;
import com.mahmon.bookcat.ui.BookFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private Context mContext;
    private List<Book> booksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        public TextView title;
        public TextView isbn;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.book_isbn);
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
        // Cardview clicks
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "CARD CLICKED!!!", Toast.LENGTH_SHORT).show();
            }
        });
        // Load image into card view
        String isbn = book.getIsbn();
        String imageUrl = "http://covers.openlibrary.org/b/isbn/" + isbn + ".jpg";
        Picasso.with(mContext)
                .load(imageUrl)
                .fit().centerCrop()
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

}