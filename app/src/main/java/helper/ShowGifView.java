package helper;

/**
 * Created by Matan on 6/7/2015
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import Network.Executor;

public class ShowGifView extends View {

    // Set true to use decodeStream
    // Set false to use decodeByteArray
    private static final boolean DECODE_STREAM = false;

    private InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long mMovieStart;
    Executor executor;

     String gifURL;

    public void setGifURL(String gifURL) {
        this.gifURL = gifURL;
        executor = new Executor();
        executor.execute(new Loder(), gifURL);
    }

    public ShowGifView(Context context, String url) {
        super(context);
        gifURL = url;
        init();
    }

    public ShowGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusable(true);

        gifMovie = null;
        movieWidth = 0;
        movieHeight = 0;
        movieDuration = 0;

//        Loder task = new Loder();
//        task.execute(gifURL );

    }

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(movieWidth, movieHeight);
    }

    public int getMovieWidth() {
        return movieWidth;
    }

    public int getMovieHeight() {
        return movieHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) { // first time
            mMovieStart = now;
        }

        if (gifMovie != null) {

            int dur = gifMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - mMovieStart) % dur);

            gifMovie.setTime(relTime);

            gifMovie.draw(canvas, 0, 0);
            invalidate();

        }

    }

    private class Loder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL gifURL;
            HttpURLConnection connection = null;
            try {
                gifURL = new URL(urls[0]);
                connection = (HttpURLConnection) gifURL
                        .openConnection();

                gifInputStream = connection.getInputStream();


                if (DECODE_STREAM) {
                    gifMovie = Movie.decodeStream(gifInputStream);
                } else {
                    byte[] array = streamToBytes(gifInputStream);
                    gifMovie = Movie.decodeByteArray(array, 0, array.length);
                }
                movieWidth = gifMovie.width();
                movieHeight = gifMovie.height();
                movieDuration = gifMovie.duration();


//                gifInputStream.close();
            } catch (IOException e) {

                e.printStackTrace();
            } finally {
                if (connection != null)
                connection.disconnect();

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            invalidate();
            requestLayout();
        }
    }

}