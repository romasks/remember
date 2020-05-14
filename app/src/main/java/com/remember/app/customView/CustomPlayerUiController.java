package com.remember.app.customView;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener;
import com.remember.app.R;

import org.jetbrains.annotations.NotNull;

public class CustomPlayerUiController extends AbstractYouTubePlayerListener {

    private YouTubePlayer youTubePlayer;
    PlayerListener listener;
    PlayerFullListener fullListener;
    Context context;
    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    private int position = -99;
    private ImageView seekBar;
    YouTubePlayerSeekBar youTubePlayerSeekBar;
    private final YouTubePlayerTracker playerTracker;

    public CustomPlayerUiController(Context context, View customPlayerUi, YouTubePlayer youTubePlayer, PlayerListener listener, int position,  PlayerFullListener fullListener) {
        this.context = context;
        youTubePlayerSeekBar = new YouTubePlayerSeekBar(context, null);
        this.youTubePlayer = youTubePlayer;
        this.listener = listener;
        this.position = position;
        this.fullListener = fullListener;
        playerTracker = new YouTubePlayerTracker();
        youTubePlayer.addListener(playerTracker);
        initViews(customPlayerUi);
    }

    private void initViews(View playerUi) {
        panel = playerUi.findViewById(R.id.backgroundView);
        AppCompatImageView playPauseButton = playerUi.findViewById(R.id.startButton);
        AppCompatImageView deleteButton = playerUi.findViewById(R.id.deleteVideo);
         seekBar = playerUi.findViewById(R.id.youtube_player_seekbar);

        playPauseButton.setOnClickListener((view) -> {
            if (playerTracker.getState() == PlayerConstants.PlayerState.PLAYING)
                youTubePlayer.pause();
            else youTubePlayer.play();
        });

        deleteButton.setOnClickListener((view) -> {
            listener.videoItemCallback(position);
        });

        seekBar.setOnClickListener(v -> fullListener.full());
    }

    @Override
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.UNSTARTED || state == PlayerConstants.PlayerState.ENDED || state == PlayerConstants.PlayerState.BUFFERING) {
            panel.setVisibility(View.GONE);
            //seekBar.setVisibility(View.GONE);
        } else {
            panel.setVisibility(View.VISIBLE);
          //  seekBar.setVisibility(View.VISIBLE);
        }
    }




    public interface PlayerListener {
        void videoItemCallback(int position);
    }
    public interface PlayerFullListener {
        void full();
    }
}
