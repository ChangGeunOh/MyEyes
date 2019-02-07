package kr.pe.paran.myeyes.ui;

import android.os.Handler;
import android.view.View;

public class DoubleClick implements View.OnClickListener{

    public interface DoubleClickListener {
        void onSingleClick(final View view);
        void onDoubleClick(final View view);
    }

    private long DOUBLE_CLICK_INTERVAL;  // Time to wait the second click.

    private final Handler mHandler = new Handler();
    private final DoubleClickListener doubleClickListener;

    private int clicks;
    private boolean isBusy = false;

    public DoubleClick(final DoubleClickListener doubleClickListener) {
        this(doubleClickListener, 200L);
        DOUBLE_CLICK_INTERVAL = 200L; // default time to wait the second click.
    }

    public DoubleClick(final DoubleClickListener doubleClickListener, final long DOUBLE_CLICK_INTERVAL) {
        this.doubleClickListener = doubleClickListener;
        this.DOUBLE_CLICK_INTERVAL = DOUBLE_CLICK_INTERVAL; // developer specified time to wait the second click.
    }

    @Override
    public void onClick(final View view) {

        if (!isBusy) {
            isBusy = true;
            clicks++;

            mHandler.postDelayed(new Runnable() {
                public final void run() {

                    if (clicks >= 2) {  // Double tap.
                        doubleClickListener.onDoubleClick(view);
                    }
                    if (clicks == 1) {  // Single tap
                        doubleClickListener.onSingleClick(view);
                    }

                    clicks = 0;
                }
            }, DOUBLE_CLICK_INTERVAL);
            isBusy = false;
        }
    }
}
