package com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.EventComments;

import java.util.ArrayList;
import java.util.List;

import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int CommentsMain = 1;
    final int CommentsFooter = 2;
    ArrayList<EventComments> comments = new ArrayList<>();

    private CommentsAdapter.CommentsAdapterListener listener;

    public interface CommentsAdapterListener {
        void showMoreComments();
        void onChangeComment();
        void onDeleteComment();
        void onShowAddCommentDialog();
    }

    public CommentsAdapter(CommentsAdapter.CommentsAdapterListener listener, ArrayList<EventComments> com) {
        this.listener = listener;
        this.comments = com;
    }

    public void updateList(ArrayList<EventComments> newComments) {
        comments.addAll(newComments);
        notifyDataSetChanged();
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
            ((CommentsViewHolder) holder).onBind(comments, position);
        else if (holder instanceof CommentsFooterViewHolder)
            ((CommentsFooterViewHolder) holder).onBind();

    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position >= comments.size()) return CommentsFooter;
        else return CommentsMain;
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

        CommentsViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.commentContainer);
            empty = v.findViewById(R.id.tvEmpty);
            showMore = v.findViewById(R.id.btnShowMore);
            root = v.findViewById(R.id.root);
        }

        void onBind(ArrayList<EventComments> comments, int position) {
//            boolean isOwnEpitaph = user.getId() == Integer.parseInt(Prefs.getString(PREFS_KEY_USER_ID, "0"));
//            delete.setVisibility(isOwnEpitaph ? View.VISIBLE : View.GONE);
            if (comments.size() == 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(root.getLayoutParams());
                marginLayoutParams.setMargins(0, 0, 0, 0);
                root.setLayoutParams(marginLayoutParams);
                container.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            } else {
                container.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
            }
            if (position == (comments.size() - 1) && (comments.size()) % 3 == 0)
                showMore.setVisibility(View.VISIBLE);
            else
                showMore.setVisibility(View.GONE);
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
