package i.nurgalieva.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
//create objects
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<Notes>  notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton add_btn;
    Notes longClickedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//initialize the view and db
        recyclerView = findViewById(R.id.recycler_view);
        add_btn = findViewById(R.id.add_btn);
        database = RoomDB.getInstance(this);
//get all notes from db and store them to notes
        notes = database.dao().getAll();
//update these notes to recycleview
        updateRecycler(notes);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditNotes.class);
//requestcode 0 - for add, 1 - for edit
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    //add new note
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_note = (Notes) data.getSerializableExtra("note");
                database.dao().insert(new_note);
                notes.clear();
                notes.addAll(database.dao().getAll());
                notesAdapter.notifyDataSetChanged();
            }}
    //edit note
        else if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Notes new_note = (Notes) data.getSerializableExtra("note");
                database.dao().update(new_note.getID(), new_note.getTitle(), new_note.getText());
                notes.clear();
                notes.addAll(database.dao().getAll());
                notesAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        notesAdapter = new NotesAdapter(MainActivity.this, notes, notesClickListener);
        recyclerView.setAdapter(notesAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, EditNotes.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 1);
        }
        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            longClickedNote = notes;
            showPopup(cardView);
        }
    };
//popup menu for deleting notes
    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.delete){
                database.dao().delete(longClickedNote);
                Toast.makeText(MainActivity.this,"Note is deleted",Toast.LENGTH_SHORT).show();
                notes.clear();
                notes.addAll(database.dao().getAll());
                notesAdapter.notifyDataSetChanged();
        }
            return false;
    }
}
