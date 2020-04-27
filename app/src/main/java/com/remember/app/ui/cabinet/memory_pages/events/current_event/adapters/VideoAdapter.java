package com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.EventVideos;

import java.util.ArrayList;

import static com.remember.app.ui.utils.StringUtils.getVideoIdFromUrl;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<EventVideos> videoIds;
    private Lifecycle lifecycle;
    VideoAdapterListener listener;
    final int VideosMain = 1;
    final int VideosFooter = 2;

    public VideoAdapter(ArrayList<EventVideos> videoIds, Lifecycle lifecycle, VideoAdapterListener listener) {
        this.videoIds = videoIds;
        this.lifecycle = lifecycle;
        this.listener = listener;
    }

    public interface VideoAdapterListener {
        void onShowAddVideoDialog();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VideosMain: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
                lifecycle.addObserver(youTubePlayerView);
                return new VideoAdapter.VideoViewHolder(view);
            }
            case VideosFooter: {
                return new VideoAdapter.VideoFooterViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_footer, parent, false)));
            }
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoViewHolder)
            ((VideoViewHolder) viewHolder).onBind(videoIds);
        else if (viewHolder instanceof VideoFooterViewHolder)
            ((VideoFooterViewHolder) viewHolder).onBind(listener);

    }

    public void updateList(ArrayList<EventVideos> newVideo) {
        videoIds.addAll(newVideo);
        notifyDataSetChanged();
    }

    public void setList(ArrayList<EventVideos> newVideo) {
        videoIds.clear();
        videoIds.addAll(newVideo);
        notifyDataSetChanged();
    }

    private int countItem() {
        if (videoIds.size() == 0)
            return 2;
        else return videoIds.size() + 1;
    }

    @Override
    public int getItemCount() {
        return countItem();
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position >= videoIds.size()) return VideosFooter;
        else return VideosMain;
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        private YouTubePlayerView youTubePlayerView;
        private YouTubePlayer youTubePlayer;
        private String currentVideoId;
        private CustomButton showMore;
        private CardView container;
        private CustomTextView empty;
        private CustomTextView description;
        private ConstraintLayout root;

        VideoViewHolder(View view) {
            super(view);
            youTubePlayerView = view.findViewById(R.id.youtube_player_view);
            container = view.findViewById(R.id.container);
            description = view.findViewById(R.id.description);
            empty = view.findViewById(R.id.tvEmpty);
            showMore = view.findViewById(R.id.btnShowMore);
            root = view.findViewById(R.id.root);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                    youTubePlayer = initializedYouTubePlayer;
                    if (currentVideoId != null)
                        youTubePlayer.cueVideo(currentVideoId, 0);
                }
            });
        }

        public void onBind(ArrayList<EventVideos> videoList) {
            if (videoList.size() == 0) {
                container.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
            } else {
                container.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                if (videoList.size() > getAdapterPosition()) {
                    description.setText(videoList.get(getAdapterPosition()).getNameLink());
                    cueVideo(getVideoIdFromUrl(videoList.get(getAdapterPosition()).getLink()));
                }
            }
        }

        void cueVideo(String videoId) {
            currentVideoId = videoId;
            if (youTubePlayer == null)
                return;
            youTubePlayer.cueVideo(videoId, 0);
        }

    }

    class VideoFooterViewHolder extends RecyclerView.ViewHolder {
        private CustomButton addButton;

        VideoFooterViewHolder(View v) {
            super(v);
            addButton = v.findViewById(R.id.btnAddVideo);
        }

        public void onBind(VideoAdapterListener listener) {
            addButton.setOnClickListener(view -> {
                listener.onShowAddVideoDialog();
            });
        }
    }
}
