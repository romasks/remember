package com.remember.app.ui.cabinet.memory_pages.events.current_event.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
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

        void onShowDeleteVideoDialog(String link, int position);

        void openFull(int position);

        void closeFull();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VideosMain: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
                lifecycle.addObserver(youTubePlayerView);
                return new VideoViewHolder(view, listener);
            }
            case VideosFooter: {
                return new VideoFooterViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_footer, parent, false)));
            }
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoViewHolder)
            ((VideoViewHolder) viewHolder).onBind(videoIds, listener);
        else if (viewHolder instanceof VideoFooterViewHolder)
            ((VideoFooterViewHolder) viewHolder).onBind(listener);

    }

    public void updateList(int currentPosition) {
        videoIds.remove(currentPosition);
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, videoIds.size());
    }

    public void setList(ArrayList<EventVideos> newVideo) {
        videoIds = newVideo;
        notifyDataSetChanged();
    }

    public ArrayList<EventVideos> getItems() {
        return videoIds;
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
        private CardView container;
        private CustomTextView empty;
        private CustomTextView description;
        private AppCompatImageView delete;
        private AppCompatImageView playButton;
        private FrameLayout backgroundView;
        private YouTubePlayerTracker playerTracker = new YouTubePlayerTracker();
        boolean isPlay = false;

        VideoViewHolder(View view, VideoAdapterListener listener) {
            super(view);
            youTubePlayerView = view.findViewById(R.id.youtube_player_view);
            container = view.findViewById(R.id.container);
            description = view.findViewById(R.id.description);
            empty = view.findViewById(R.id.tvEmpty);
            backgroundView = view.findViewById(R.id.backgroundView);
            delete = view.findViewById(R.id.deleteVideo);
            playButton = view.findViewById(R.id.startButton);
            YouTubePlayerFullScreenListener fullScreenListener = new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerExitFullScreen() {
                    listener.closeFull();
                }

                @Override
                public void onYouTubePlayerEnterFullScreen() {
                    listener.openFull(getAdapterPosition());
                }
            };
            youTubePlayerView.addFullScreenListener(fullScreenListener);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                    youTubePlayer = initializedYouTubePlayer;
                 //   youTubePlayer.addListener(playerTracker);
                    if (currentVideoId != null)
                        youTubePlayer.cueVideo(currentVideoId, 0);
                }

                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                    if (state == PlayerConstants.PlayerState.PLAYING) {
                        isPlay = true;
                      //  backgroundView.setVisibility(View.INVISIBLE);
                        //youTubePlayerView.setVisibility(View.VISIBLE);
                    } else if (state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.UNSTARTED || state == PlayerConstants.PlayerState.ENDED) {
                        //backgroundView.setVisibility(View.VISIBLE);
                        //youTubePlayerView.setVisibility(View.GONE);
                        isPlay = false;
                    }
                }
            });
        }

        public void onBind(ArrayList<EventVideos> videoList, VideoAdapterListener listener) {
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
                delete.setOnClickListener(v -> listener.onShowDeleteVideoDialog(videoList.get(getAdapterPosition()).getLink(), getAdapterPosition()));
                playButton.setOnClickListener(v -> {
                    if (isPlay)
                        youTubePlayer.pause();
                    else
                        youTubePlayer.play();
                });
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
