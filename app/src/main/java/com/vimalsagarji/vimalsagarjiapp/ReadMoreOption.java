
package com.vimalsagarji.vimalsagarjiapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class ReadMoreOption {

    // required
    private Context context;
    // optional
    private int textLength;
    private String moreLabel;
    private String lessLabel;
    private int moreLabelColor;
    private int lessLabelColor;
    private boolean labelUnderLine;

    private ReadMoreOption(Builder builder) {
        this.context = builder.context;
        this.textLength = builder.textLength;
        this.moreLabel = builder.moreLabel;
        this.lessLabel = builder.lessLabel;
        this.moreLabelColor = builder.moreLabelColor;
        this.lessLabelColor = builder.lessLabelColor;
        this.labelUnderLine = builder.labelUnderLine;
    }

    public void addReadMoreTo(final TextView textView, final String text) {

        if (text.length() <= textLength) {
            textView.setText(text);
            return;
        }

        SpannableString ss = new SpannableString(text.substring(0, textLength) + "... " + moreLabel);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadLess(textView, text);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(labelUnderLine);
                ds.setColor(moreLabelColor);
            }
        };
        ss.setSpan(clickableSpan, ss.length() - moreLabel.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss)
        ;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addReadLess(final TextView textView, final String text) {
        SpannableString ss = new SpannableString(text + " " + lessLabel);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        addReadMoreTo(textView, text);
                    }
                });
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(labelUnderLine);
                ds.setColor(lessLabelColor);
            }
        };
        ss.setSpan(clickableSpan, ss.length() - lessLabel.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss)
        ;
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static class Builder {
        // required
        private Context context;
        // optional
        private int textLength = 100;
        private String moreLabel = "read more";
        private String lessLabel = "read less";
        private int moreLabelColor = Color.parseColor("#ff00ff");
        private int lessLabelColor = Color.parseColor("#ff00ff");
        private boolean labelUnderLine = false;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder textLength(int length) {
            this.textLength = length;
            return this;
        }

        public Builder moreLabel(String moreLabel) {
            this.moreLabel = moreLabel;
            return this;
        }

        public Builder lessLabel(String lessLabel) {
            this.lessLabel = lessLabel;
            return this;
        }

        public Builder moreLabelColor(int moreLabelColor) {
            this.moreLabelColor = moreLabelColor;
            return this;
        }

        public Builder lessLabelColor(int lessLabelColor) {
            this.lessLabelColor = lessLabelColor;
            return this;
        }

        public Builder labelUnderLine(boolean labelUnderLine) {
            this.labelUnderLine = labelUnderLine;
            return this;
        }

        public ReadMoreOption build() {
            return new ReadMoreOption(this);
        }

    }

}
