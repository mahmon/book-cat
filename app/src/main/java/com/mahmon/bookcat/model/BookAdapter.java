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

// Class used to adapt book list data into recycler view
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    // Context variable to pass the context
    private Context mContext;
    // Book list array to hold the books
    private List<Book> booksList;
    // Click listener object for clicks on list items
    private OnItemClickListener mListener;

    // Constructor, passes context and book list
    public BookAdapter(Context mContext, List<Book> booksList) {
        this.mContext = mContext;
        this.booksList = booksList;
    }

    // Override method to create ViewHolder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Instantiate new view and inflate the bookCardView item
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.book_cat_cardview, parent, false);
        // Return a new instance of the view holder
        return new MyViewHolder(itemView);
    }

    // Override method to bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Instantiate book from the book at 'position' in the book list
        Book book = booksList.get(position);
        // Set the title of the holder to the book title
        holder.title.setText(book.getTitle());
        // Use Picasso to load image into card view using getter method from book
        String imageUrl = book.getCoverImageURL();
        Picasso.with(mContext)
                .load(imageUrl)
                .fit().centerCrop()
                .into(holder.coverImage);
    }

    // Method to get length of book list
    @Override
    public int getItemCount() {
        return booksList.size();
    }

    // Inner class - ImageViewHolder, extends the view holder and implements Listener
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
            // Link variables to xml view
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.book_title);
            coverImage = view.findViewById(R.id.book_cover_image);
            // Assign the onClickListener to this view
            view.setOnClickListener(this);
        }

        // Method called when Book is clicked in view
        @Override
        public void onClick(View v) {
            // TEST: if the listener is assigned (not null)
            if (mListener != null) {
                // Get the position of the book clicked
                int position = getAdapterPosition();
                // TEST: If the value position is not null
                if (position != RecyclerView.NO_POSITION) {
                    // Call onItemClick on the position
                    mListener.onItemClick(position);
                }
            }
        }

    }

    // Interface, allows click methods to be set outside of this Adapter
    public interface OnItemClickListener {
        // Contract to requires @Override onItemClick method
        // in class that implements the adapter
        void onItemClick(int position);
    }

    // Attach mListener when this method is called
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}