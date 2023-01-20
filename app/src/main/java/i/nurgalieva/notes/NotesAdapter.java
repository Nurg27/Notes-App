package i.nurgalieva.notes;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<Notes> list;
    NotesClickListener listener;

    public NotesAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }
 //create wrappers for notes items
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.title_view.setText(list.get(position).getTitle());
//for horisontal scrolling effect
        holder.title_view.setSelected(true);
        holder.text_view.setText(list.get(position).getText());
        holder.date_view.setText(list.get(position).getDate());
        holder.date_view.setSelected(true);
//manage clicks on items
        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()),holder.notes_container);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView title_view,text_view, date_view;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        title_view = itemView.findViewById(R.id.title_view);
        text_view = itemView.findViewById(R.id.text_view);
        date_view = itemView.findViewById(R.id.date_view);
    }
}