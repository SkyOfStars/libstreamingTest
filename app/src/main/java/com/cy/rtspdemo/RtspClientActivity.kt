package com.cy.rtspdemo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.rtsp.RtspMediaSource
import androidx.media3.ui.PlayerView

/**
 * 在另外一个手机上启动这个播放器播放RTSP的视频流
 *
 * RTSP 播放器
 */
class RtspPlayerActivity : AppCompatActivity() {
    private val TAG = "RtspPlayer"
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView
    private lateinit var surfaceView: SurfaceView
    private var mediaPlayer: MediaPlayer? = null

    // RTSP 视频源地址（示例：请替换为实际的 RTSP 地址）
    private val rtspUrl = "rtsp://10.110.4.233:8086/"

    @UnstableApi // 标记使用 Media3 中的非稳定 API（RtspMediaSource 目前为非稳定状态）
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        setContentView(R.layout.activity_rtsp_player)
        playerView = findViewById(R.id.playerView)

        initPlayer()
    }


    @UnstableApi
    private fun initPlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            val rtspMediaSource = RtspMediaSource.Factory()
                .setTimeoutMs(5000) // 连接超时（毫秒）
                .createMediaSource(MediaItem.fromUri(rtspUrl))

            setMediaSource(rtspMediaSource)
            prepare()
            playWhenReady = true
        }

        // 5. 将播放器绑定到 PlayerView
        playerView.player = player

        // 6. 监听播放状态和错误
        player?.addListener(object : androidx.media3.common.Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    androidx.media3.common.Player.STATE_BUFFERING -> {
                        Log.d(TAG, "缓冲中...")
                    }
                    androidx.media3.common.Player.STATE_READY -> {
                        Log.d(TAG, "准备完成，开始播放")
                    }
                    androidx.media3.common.Player.STATE_ENDED -> {
                        Log.d(TAG, "播放结束（RTSP 通常为实时流，此状态可能不会触发）")
                    }
                    androidx.media3.common.Player.STATE_IDLE -> {
                        Log.d(TAG, "播放器空闲")
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.e(TAG, "播放错误: ${error.message}", error)
                // 可在此处处理错误（如重试连接）
            }
        })
    }

    // 生命周期管理：释放资源
    override fun onStart() {
        super.onStart()
        playerView.onResume()
    }

    override fun onPause() {
        super.onPause()
        playerView.onPause()
    }

    override fun onStop() {
        super.onStop()
        player?.release() // 释放播放器资源，避免内存泄漏
        player = null
    }
}
    