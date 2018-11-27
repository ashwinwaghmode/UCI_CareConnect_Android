package com.devool.ucicareconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.ApptInstructionActivity;
import com.devool.ucicareconnect.activities.CancelAppointmentActivity;
import com.devool.ucicareconnect.activities.CancelFollowUpActivity;
import com.devool.ucicareconnect.activities.CancelReferralActivity;
import com.devool.ucicareconnect.activities.EventDetailsActivity;
import com.devool.ucicareconnect.helper.ContextualModelItems;

import java.util.ArrayList;

import javax.sql.DataSource;

public class MultiViewTypeAdapter extends RecyclerView.Adapter {

    private ArrayList<ContextualModelItems> contextualModelItems;
    ;
    private Context context;
    private Activity activity;


    public MultiViewTypeAdapter(Context context, ArrayList<ContextualModelItems> data, Activity activity) {
        this.context = context;
        this.contextualModelItems = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case 3:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_items_event_details, viewGroup, false);
                return new EventDetailViewHolder(view);
            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_item_requests, viewGroup, false);
                return new ViewHolder(view);
            case 4:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_item_requests, viewGroup, false);
                return new ViewHolder(view);
            case 2:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.confirm_appointment_items, viewGroup, false);
                return new ConfirmAppointmentHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch (contextualModelItems.get(position).getEventTypeId()) {
            case "3":
                return 3;
            case "1":
                return 1;
            case "4":
                return 4;
            case "2":
                return 2;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        ContextualModelItems items = contextualModelItems.get(i);
        {
            if (items != null) {
                switch (items.getEventTypeId()) {
                    case "3":
                        if (contextualModelItems.get(i).getEventDescription() == null) {
                            ((EventDetailViewHolder) viewHolder).tvEventName.setText("");
                        }else {
                            ((EventDetailViewHolder) viewHolder).tvEventName.setText(contextualModelItems.get(i).getEventDescription());
                        }
                        if (contextualModelItems.get(i).getAddress() != null) {
                            ((EventDetailViewHolder) viewHolder).tvEventAddress.setText(contextualModelItems.get(i).getAddress());
                        }else {
                            ((EventDetailViewHolder) viewHolder).tvEventAddress.setText("");
                        }
                        if (contextualModelItems.get(i).getEventDate() != null) {
                            ((EventDetailViewHolder) viewHolder).tvTime.setText(contextualModelItems.get(i).getEventDate());
                        }else {
                            ((EventDetailViewHolder) viewHolder).tvTime.setText("");
                        }
                        if (contextualModelItems.get(i).getStrImage() != null) {
                            Glide.with(activity.getBaseContext()).
                                    load(contextualModelItems.get(i).getStrImage()).
                                    placeholder(R.drawable.loading)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            ((EventDetailViewHolder) viewHolder).progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            ((EventDetailViewHolder) viewHolder).progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(((EventDetailViewHolder) viewHolder).img);
                        }
                        break;
                    case "1":
                        if (contextualModelItems.get(i).getEventTypeId().equals("1")) {
                            ((ViewHolder) viewHolder).tvHeadiing.setText(contextualModelItems.get(i).getEventDescription());
                            ((ViewHolder) viewHolder).tvSubheading.setText(contextualModelItems.get(i).getReqquestString());
                        }
                        break;
                    case "4":
                        if (contextualModelItems.get(i).getEventTypeId().equals("4")) {
                            ((ViewHolder) viewHolder).tvHeadiing.setText(contextualModelItems.get(i).getEventDescription());
                            ((ViewHolder) viewHolder).tvSubheading.setText(contextualModelItems.get(i).getReqquestString());
                        }
                        break;
                    case "2":
                        if (contextualModelItems.get(i).getEventDescription() != null) {
                            ((ConfirmAppointmentHolder) viewHolder).tvEventName.setText(contextualModelItems.get(i).getEventDescription());
                        }
                        if (contextualModelItems.get(i).getAddress() != null) {
                            ((ConfirmAppointmentHolder) viewHolder).tvEventAddress.setText(contextualModelItems.get(i).getAddress());
                        }
                        if (contextualModelItems.get(i).getEventDate() != null) {
                            ((ConfirmAppointmentHolder) viewHolder).tvDateAndTime.setText(contextualModelItems.get(i).getEventDate());
                        }
                        if (contextualModelItems.get(i).getPhysiciaanName() != null) {
                            ((ConfirmAppointmentHolder) viewHolder).tvDoctorName.setText(contextualModelItems.get(i).getPhysiciaanName());
                        }
                        if (contextualModelItems.get(i).getTimeRemaining() != null) {
                            ((ConfirmAppointmentHolder) viewHolder).tvTime.setText(contextualModelItems.get(i).getTimeRemaining());
                        }
                        if (contextualModelItems.get(i).getStrImage() != null) {
                            Glide.with(activity.getBaseContext()).
                                    load(contextualModelItems.get(i).getStrImage()).
                                    placeholder(R.drawable.loading)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            ((ConfirmAppointmentHolder) viewHolder).progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            ((ConfirmAppointmentHolder) viewHolder).progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .into(((ConfirmAppointmentHolder) viewHolder).img);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return contextualModelItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvHeadiing, tvSubheading;
        ImageView imgCallBtn;
        Button btnViewEvenetDetails;

        ViewHolder(View itemView) {
            super(itemView);
            imgCallBtn = itemView.findViewById(R.id.img_call_btn);
            tvHeadiing = (TextView) itemView.findViewById(R.id.tv_heading);
            tvSubheading = (TextView) itemView.findViewById(R.id.tv_subheading);
            btnViewEvenetDetails = itemView.findViewById(R.id.btn_view_event_details);
            imgCallBtn.setOnClickListener(this);
            btnViewEvenetDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_call_btn:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:(866) 698-2422"));
                    context.startActivity(intent);
                    break;
                case R.id.btn_view_event_details:
                    if (contextualModelItems.get(getAdapterPosition()).getEventTypeId().equals("1")) {
                        Intent i = new Intent(context, CancelAppointmentActivity.class);
                        i.putExtra("event_id", contextualModelItems.get(getAdapterPosition()).getEventId());
                        context.startActivity(i);
                        break;
                    } else if (contextualModelItems.get(getAdapterPosition()).getEventTypeId().equals("4")) {
                        Intent i = new Intent(context, CancelReferralActivity.class);
                        i.putExtra("event_id", contextualModelItems.get(getAdapterPosition()).getEventId());
                        context.startActivity(i);
                        break;
                    }
                    break;
            }
        }
    }

    class EventDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvEventName, tvEventAddress, tvTime;
        ImageView img, imgCallBtn;
        ProgressBar progressBar;
        LinearLayout btnEventDetails;

        EventDetailViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            imgCallBtn = itemView.findViewById(R.id.img_call_btn);
            tvEventName = (TextView) itemView.findViewById(R.id.tv_event_name);
            tvEventAddress = (TextView) itemView.findViewById(R.id.tv_event_address);
            tvTime = (TextView) itemView.findViewById(R.id.tv_event_time);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            btnEventDetails = itemView.findViewById(R.id.btn_view_event_details);

            imgCallBtn.setOnClickListener(this);
            btnEventDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_call_btn:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:(866) 698-2422"));
                    context.startActivity(intent);
                    break;
                case R.id.btn_view_event_details:
                    Intent i = new Intent(context, EventDetailsActivity.class);
                    i.putExtra("event_id", contextualModelItems.get(getAdapterPosition()).getEventId());
                    context.startActivity(i);
                    break;
            }
        }
    }

    class ConfirmAppointmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvEventName, tvEventAddress, tvDateAndTime, tvDoctorName, tvTime;
        ImageView img, imgCallBtn;
        ProgressBar progressBar;

        LinearLayout llInstruction, llReschedule, llCancel;

        ConfirmAppointmentHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            imgCallBtn = itemView.findViewById(R.id.img_call_btn);
            tvEventName = (TextView) itemView.findViewById(R.id.tv_event_name);
            tvEventAddress = (TextView) itemView.findViewById(R.id.tv_event_address);
            tvDateAndTime = (TextView) itemView.findViewById(R.id.tv_event_date_time);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            tvDoctorName = (TextView) itemView.findViewById(R.id.tv_doctor_name);
            tvTime = itemView.findViewById(R.id.tv_appointment_time);

            llInstruction = itemView.findViewById(R.id.ll_instruction);
            llReschedule = itemView.findViewById(R.id.ll_reschedule);
            llCancel = itemView.findViewById(R.id.ll_cancel);

            llInstruction.setOnClickListener(this);
            llReschedule.setOnClickListener(this);
            llCancel.setOnClickListener(this);
            imgCallBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_instruction:
                    Intent i = new Intent(context, ApptInstructionActivity.class);
                    i.putExtra("event_id", contextualModelItems.get(getAdapterPosition()).getEventId());
                    context.startActivity(i);
                    break;
                case R.id.ll_reschedule:
                    Toast.makeText(context, "WIP", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ll_cancel:
                    Toast.makeText(context, "WIP", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.img_call_btn:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:(866) 698-2422"));
                    context.startActivity(intent);
                    break;
            }

        }
    }
}
