package vn.ifactory.architectureexamplenote.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vn.ifactory.architectureexamplenote.R;
import vn.ifactory.architectureexamplenote.db.Note;

/**
 * Created by PC on 11/12/2018.
 */

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteHolder> {
    //private List<Note> notes;
    private IOnClickListener mListener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    // for case update item change and support animation
    // compare two list using DiffUtil
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };

    public interface IOnClickListener {
        void onItemClick(Note note);
    }

    public void setOnClickListener(IOnClickListener listener) {
        mListener = listener;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = getItem(position);
        holder.txtTitle.setText(currentNote.getTitle());
        holder.txtDescription.setText(currentNote.getDescription());
        holder.txtPriority.setText(String.valueOf(currentNote.getPriority()));
    }

    /*

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }
*/

    public Note getNoteAt(int position) {
        return getItem(position);
    }
    protected class NoteHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtDescription, txtPriority;
        public NoteHolder(View itemView) {
            super(itemView);
            this.txtTitle = itemView.findViewById(R.id.txtTitle);
            this.txtDescription = itemView.findViewById(R.id.txtDescription);
            this.txtPriority = itemView.findViewById(R.id.txtPriority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(getItem(position));
                        }
                    }
                }
            });
        }
    }


}
