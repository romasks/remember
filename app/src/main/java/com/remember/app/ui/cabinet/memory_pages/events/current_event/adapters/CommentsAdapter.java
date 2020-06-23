package com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.EventComments;

import com.remember.app.ui.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import static com.remember.app.data.Constants.BASE_SERVICE_URL;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int CommentsMain = 1;
    final int CommentsFooter = 2;
    ArrayList<EventComments> comments = new ArrayList<>();

    private CommentsAdapterListener listener;

    public interface CommentsAdapterListener {
        void showMoreComments();
        void onChangeComment(int commentID, String newComment, int position);
        void onDeleteComment(int commentID, int position);
        void onShowAddCommentDialog();
    }


    public CommentsAdapter(CommentsAdapterListener listener, ArrayList<EventComments> com) {
        this.listener = listener;
        this.comments = com;
    }

    public void updateList(ArrayList<EventComments> newComments) {
        comments.addAll(newComments);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<EventComments> newComments) {
        comments.clear();
        comments.addAll(newComments);
        notifyDataSetChanged();
    }

    public void editList(EventComments editedComment, int position, boolean isDelete){
        if (!isDelete){
        comments.get(position).setComment(editedComment.getComment());
     //   notifyItemRangeChanged(position, 1);
            notifyItemChanged(position);
        }
        else {
            comments.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ArrayList<EventComments> getList(){
        return comments;
    }

    private int countItem() {
        if (comments.size() == 0)
            return 2;
        else return comments.size() + 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CommentsMain: {
                return new CommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false));
            }
            case CommentsFooter: {
                return new CommentsFooterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_footer, parent, false));
            }
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentsViewHolder)
            ((CommentsViewHolder) holder).onBind(comments);
        else if (holder instanceof CommentsFooterViewHolder)
            ((CommentsFooterViewHolder) holder).onBind();

    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position >= comments.size())
            return CommentsFooter;
        else
            return CommentsMain;
 }

    @Override
    public int getItemCount() {
        return countItem();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private CustomTextView empty;
        private CustomButton showMore;
        private ConstraintLayout root;
        private AppCompatImageView imgEdit;
        private CustomTextView change;
        private AppCompatImageView imgDelete;
        private CustomTextView delete;
        private AppCompatImageView avatar;
        private CustomTextView userName;
        private CustomTextView comment;
        private CustomTextView date;

        CommentsViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.commentContainer);
            empty = v.findViewById(R.id.tvEmpty);
            showMore = v.findViewById(R.id.btnShowMore);
            root = v.findViewById(R.id.root);
            imgEdit = v.findViewById(R.id.imgEdit);
            change = v.findViewById(R.id.change);
            imgDelete = v.findViewById(R.id.imgDelete);
            delete = v.findViewById(R.id.delete);
            avatar = v.findViewById(R.id.avatar);
            userName = v.findViewById(R.id.name_user);
            comment = v.findViewById(R.id.tvComment);
            date = v.findViewById(R.id.date);
        }

        void onBind(ArrayList<EventComments> comments) {
            if (comments.size() == 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(root.getLayoutParams());
                marginLayoutParams.setMargins(0, 0, 0, 0);
                root.setLayoutParams(marginLayoutParams);
                container.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            } else {
                setGlideImage(itemView.getContext(),BASE_SERVICE_URL + comments.get(getAdapterPosition()).getPicture(), avatar);
                userName.setText(comments.get(getAdapterPosition()).getName());
                comment.setText(comments.get(getAdapterPosition()).getComment());
//                String[] dates = comments.get(getAdapterPosition()).getCreatedAt().split("T");
//                date.setText(dates[0]);
                date.setText(DateUtils.convertRemoteToLocalFormat(comments.get(getAdapterPosition()).getCreatedAt()));
                container.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                boolean isOwn = comments.get(getAdapterPosition()).getUserId() == Integer.parseInt(Prefs.getString(PREFS_KEY_USER_ID, "0"));
                imgEdit.setVisibility(isOwn ? View.VISIBLE : View.GONE);
                change.setVisibility(isOwn ? View.VISIBLE : View.GONE);
                delete.setVisibility(isOwn ? View.VISIBLE : View.GONE);
                imgDelete.setVisibility(isOwn ? View.VISIBLE : View.GONE);
                delete.setOnClickListener(v -> {
                    listener.onDeleteComment(comments.get(getAdapterPosition()).getId(), getAdapterPosition());
                });
                change.setOnClickListener(v -> {
                    listener.onChangeComment(comments.get(getAdapterPosition()).getId(), comment.getText().toString(), getAdapterPosition());
                });
            }
//            if (position == (comments.size() - 1) && (comments.size()) % 3 == 0)
//                showMore.setVisibility(View.VISIBLE);
//            else
//                showMore.setVisibility(View.GONE);
            showMore.setOnClickListener(v -> {
                listener.showMoreComments();
            });
        }
    }

    class CommentsFooterViewHolder extends RecyclerView.ViewHolder {
        private CustomTextView title;
        private CustomButton addButton;


        CommentsFooterViewHolder(View v) {
            super(v);
            addButton = v.findViewById(R.id.btnAddComment);
        }

        public void onBind() {
            addButton.setOnClickListener(view -> {
                listener.onShowAddCommentDialog();
            });
        }
    }
}
