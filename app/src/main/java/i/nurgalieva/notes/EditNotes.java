package i.nurgalieva.notes;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import 	java.util.Date;
import java.text.SimpleDateFormat;

public class EditNotes extends AppCompatActivity {
//initialize views
    EditText edit_title,edit_text;
    ImageView image_save;
    Notes notes;
//we initialize object notes 2 times here and to prevent it:
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        edit_title = findViewById(R.id.edit_title);
        edit_text = findViewById(R.id.edit_text);
        image_save = findViewById(R.id.image_save);

        notes = new Notes();

            notes = (Notes) getIntent().getSerializableExtra("old_note");
            if (!(notes==null)) {
                edit_title.setText(notes.getTitle());
                edit_text.setText(notes.getText());
                isOldNote = true;
            }
//when click on save icon
        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = edit_title.getText().toString();
                String describtion = edit_text.getText().toString();
//empty note validation
                if (describtion.isEmpty()){
                    Toast.makeText(EditNotes.this,"Please add a note",Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                Date date = new Date();

                if(!isOldNote) {
                    notes = new Notes();
                }
                notes.setTitle(title);
                notes.setText(describtion);
                notes.setDate(dateFormat.format(date));

                Intent intent = new Intent();
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}