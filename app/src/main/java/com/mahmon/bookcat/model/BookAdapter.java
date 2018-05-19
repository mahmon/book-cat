package com.mahmon.bookcat.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmon.bookcat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    // Context variable to pass the context
    private Context mContext;
    // List to hold the books
    private List<Book> booksList;
    // Click listener object
    private OnItemClickListener mListener;

    // Constructor, passes conetext and book list
    public BookAdapter(Context mContext, List<Book> booksList) {
        this.mContext = mContext;
        this.booksList = booksList;
    }

    // Override method to create ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Instantiate new view and inflate an event item
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.book_cat_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    // Override method to bind data to ViewHolder
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.title.setText(book.getTitle());
        // Load image into card view
        String imageUrl = book.getCoverImageURL();
        Picasso.with(mContext)
                .load(imageUrl)
                .fit().centerCrop()
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    // ImageViewHolder class, implements Listener
    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Variables used in the view
        public CardView cardView;
        public TextView title;
        public TextView isbn;
        public ImageView coverImage;

        // Constructor
        public MyViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.book_title);
            coverImage = view.findViewById(R.id.book_cover_image);
            // Set the onClickListener
            view.setOnClickListener(this);
        }

        // Get book position
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

    }

    // Interface, allows click methods to be set outside of this Adapter
    public interface OnItemClickListener {
        // Contract to require @Override onItemClick method
        void onItemClick(int position);
    }

    // Attach listener to mListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}