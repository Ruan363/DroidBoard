package com.razor.droidboard.screens;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import com.razor.droidboard.R;
import com.razor.droidboard.adapters.QuoteAdapter;
import com.razor.droidboard.interfaces.IBaseMethods;
import com.razor.droidboard.models.Quote;
import com.razor.droidboard.screens.base.BaseDrawerActivity;

public class QuotesScreen extends BaseDrawerActivity implements IBaseMethods
{
    FloatingActionButton m_fabAddQuote;
    private static final String TAG = "RecyclerViewExample";
    private ArrayList<Quote> m_quotes;
    private RecyclerView m_recQuotes;
    private QuoteAdapter m_adapter;

    EditText m_edtAuthor, m_edtQuote;
    ImageView m_imgInfoAuthor, m_imgInfoQuote;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_quotes_screen);

        instantiateScreen();

        populateScreen();
    }

    @Override
    public void instantiateScreen()
    {
        m_fabAddQuote = (FloatingActionButton) findViewById(R.id.fab);
        m_recQuotes = (RecyclerView) findViewById(R.id.recQuotes);
        m_recQuotes.setLayoutManager(new LinearLayoutManager(this));

        m_quotes = new ArrayList<>();
    }

    @Override
    public void populateScreen()
    {
//        generateFakeQuotes();

        m_quotes = (ArrayList<Quote>) Quote.listAll(Quote.class);

        if (m_quotes == null)
            m_quotes = new ArrayList<>();

        m_adapter = new QuoteAdapter(this, m_quotes);
        m_recQuotes.setAdapter(m_adapter);
    }

    private void generateFakeQuotes()
    {
        Quote.deleteAll(Quote.class);

        Quote q1 = new Quote("James Dean", "You only live once, young blood.");
        q1.save();
        m_quotes.add(q1);
        Quote q2 = new Quote("Sean Connery", "Ah shave ah day keepsh the penniesh shafe.");
        q2.save();
        m_quotes.add(q2);
    }

    public void addQuoteClick(View view)
    {
        showAddQuotePopup();
    }

    private void showAddQuotePopup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = LayoutInflater.from(this).inflate(R.layout.popup_add_quote, null, false);
        m_edtAuthor = (EditText) v.findViewById(R.id.edtName);
        m_edtQuote = (EditText) v.findViewById(R.id.edtQuote);

        m_imgInfoAuthor = (ImageView) v.findViewById(R.id.imgInfoAuthor);
        m_imgInfoAuthor.setOnClickListener(m_infoAuthorCLick);
        m_imgInfoQuote = (ImageView) v.findViewById(R.id.imgInfoQuote);
        m_imgInfoQuote.setOnClickListener(m_infoQuoteCLick);

        builder.setView(v);
        builder.setPositiveButton("Add", m_addListener);
        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    View.OnClickListener m_infoAuthorCLick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            PopupMenu popupMenu = new PopupMenu(QuotesScreen.this, m_imgInfoAuthor);

            getMenuInflater().inflate(R.menu.info_menu, popupMenu.getMenu());

            MenuItem item = popupMenu.getMenu().findItem(R.id.info);
            item.setTitle("Who said this?");

            popupMenu.show();
        }
    };

    View.OnClickListener m_infoQuoteCLick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            PopupMenu popupMenu = new PopupMenu(QuotesScreen.this, m_imgInfoQuote);
            getMenuInflater().inflate(R.menu.info_menu, popupMenu.getMenu());

            MenuItem item = popupMenu.getMenu().findItem(R.id.info);
            item.setTitle("What did he say?");

            popupMenu.show();
        }
    };

    private DialogInterface.OnClickListener m_addListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            String author = m_edtAuthor.getText().toString();
            String quote = m_edtQuote.getText().toString();

            if (m_quotes == null)
                m_quotes = new ArrayList<>();

            Quote newQuote = new Quote(author, quote);
            newQuote.save();
            m_quotes.add(newQuote);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_sub_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_new:

                return true;
            case R.id.open:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
