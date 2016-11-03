package com.razor.droidboard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.razor.droidboard.R;
import com.razor.droidboard.models.Quote;

/**
 * Created by ruan on 4/22/2016.
 */
public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>
{
    private Context m_context;
    private ArrayList<Quote> m_items;

    public QuoteAdapter(Context context, ArrayList<Quote> quotes)
    {
        m_context = context;
        m_items = quotes;
    }

    @Override
    public QuoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quote, parent, false);

        QuoteViewHolder holder = new QuoteViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(QuoteViewHolder holder, int position)
    {
        Quote quote = m_items.get(position);

        String author = quote.getAuthor();
        String quoteText = quote.getQuote();

        if (author != null && !author.isEmpty())
            holder.tvAuthorName.setText(author);

        if (quoteText != null && !quoteText.isEmpty())
            holder.tvQuote.setText(quoteText);
    }

    public void addQuote(Quote quote)
    {
        m_items.add(quote);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return (m_items != null ? m_items.size() : 0);
    }


    public class QuoteViewHolder extends RecyclerView.ViewHolder
    {
        //        protected ProgressBar progSync;
        protected View mainView;
        protected TextView tvAuthorName, tvQuote;

        public QuoteViewHolder(View itemView)
        {
            super(itemView);
            this.mainView = itemView;
            this.tvAuthorName = (TextView)itemView.findViewById(R.id.tvAuthor);
            this.tvQuote = (TextView)itemView.findViewById(R.id.tvQuote);
        }
    }
}
