package ml.Admission.main.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ml.Admission.main.Interface.ItemClickListener;
import ml.Admission.main.R;

public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public TextView txtName;
    public TextView txtCourse;
    public TextView txtEmail;
    public TextView txtPhone;
    public TextView txtAddress;

    private ItemClickListener itemClickListener;


    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.name);
        txtCourse = itemView.findViewById(R.id.course);
        txtEmail = itemView.findViewById(R.id.email);
        txtPhone = itemView.findViewById(R.id.phone);
        txtAddress = itemView.findViewById(R.id.address);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select the Action");
        contextMenu.add(0,0,getAdapterPosition(),"Delete");
    }
}
