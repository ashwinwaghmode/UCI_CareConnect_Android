package com.devool.ucicareconnect.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.AppInstructionAttachmentDetailActivity;
import com.devool.ucicareconnect.helper.InstructionAttachmenthelper;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder>  {

    Context context;
    ArrayList<InstructionAttachmenthelper> instructionAttachmenthelperArrayList = new ArrayList<>();

    public InstructionAdapter(Context context, ArrayList<InstructionAttachmenthelper> customerItemList) {
        this.context = context;
        this.instructionAttachmenthelperArrayList = customerItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_instruction_attachments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvExtension.setText(instructionAttachmenthelperArrayList.get(position).getStrExtension());
        holder.tvFileName.setText(instructionAttachmenthelperArrayList.get(position).getStrFileName());
    }

    @Override
    public int getItemCount() {
        return instructionAttachmenthelperArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvExtension, tvFileName;
        RelativeLayout relAttachment;

        public ViewHolder(final View itemView) {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tv_file_name);
            tvExtension = itemView.findViewById(R.id.tv_extension);
            relAttachment = itemView.findViewById(R.id.rel_attachement);

            relAttachment.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rel_attachement:
                    Intent i = new Intent(context, AppInstructionAttachmentDetailActivity.class);
                    i.putExtra("attachment_url", instructionAttachmenthelperArrayList.get(getAdapterPosition()).getStrAttachmentURL());
                    i.putExtra("file_extension", instructionAttachmenthelperArrayList.get(getAdapterPosition()).getStrExtension());
                    context.startActivity(i);
                    break;
            }
        }
    }
}
