/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.aosqp.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.DialogPreference;

public class AosqpDialogPreference<T extends DialogInterface> extends DialogPreference {

    private AosqpPreferenceDialogFragment mFragment;

    public AosqpDialogPreference(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AosqpDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AosqpDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AosqpDialogPreference(Context context) {
        super(context);
    }

    public boolean isDialogOpen() {
        return getDialog() != null && getDialog() instanceof Dialog && ((Dialog)getDialog()).isShowing();
    }

    public T getDialog() {
        return (T) (mFragment != null ? mFragment.getDialog() : null);
    }

    protected void onPrepareDialogBuilder(AlertDialog.Builder builder,
            DialogInterface.OnClickListener listener) {
    }

    protected void onDialogClosed(boolean positiveResult) {
    }

    protected void onClick(T dialog, int which) {
    }

    protected void onBindDialogView(View view) {
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    protected void onPause() {
    }

    protected void onResume() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return null;
    }

    protected View onCreateDialogView(Context context) {
        return null;
    }

    private void setFragment(AosqpPreferenceDialogFragment fragment) {
        mFragment = fragment;
    }

    protected boolean onDismissDialog(T dialog, int which) {
        return true;
    }

    public static class AosqpPreferenceDialogFragment extends PreferenceDialogFragmentCompat {

        public static AosqpPreferenceDialogFragment newInstance(String key) {
            final AosqpPreferenceDialogFragment fragment = new AosqpPreferenceDialogFragment();
            final Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);
            return fragment;
        }

        private AosqpDialogPreference getAosqpizablePreference() {
            return (AosqpDialogPreference) getPreference();
        }

        private class OnDismissListener implements View.OnClickListener {
            private final int mWhich;
            private final DialogInterface mDialog;

            public OnDismissListener(DialogInterface dialog, int which) {
                mWhich = which;
                mDialog = dialog;
            }

            @Override
            public void onClick(View view) {
                AosqpPreferenceDialogFragment.this.onClick(mDialog, mWhich);
                if (getAosqpizablePreference().onDismissDialog(mDialog, mWhich)) {
                    mDialog.dismiss();
                }
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            if (getDialog() instanceof AlertDialog) {
                AlertDialog a = (AlertDialog)getDialog();
                if (a.getButton(Dialog.BUTTON_NEUTRAL) != null) {
                    a.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener(
                            new OnDismissListener(a, Dialog.BUTTON_NEUTRAL));
                }
                if (a.getButton(Dialog.BUTTON_POSITIVE) != null) {
                    a.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
                            new OnDismissListener(a, Dialog.BUTTON_POSITIVE));
                }
                if (a.getButton(Dialog.BUTTON_NEGATIVE) != null) {
                    a.getButton(Dialog.BUTTON_NEGATIVE).setOnClickListener(
                            new OnDismissListener(a, Dialog.BUTTON_NEGATIVE));
                }
            }
            getAosqpizablePreference().onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
            getAosqpizablePreference().onStop();
        }

        @Override
        public void onPause() {
            super.onPause();
            getAosqpizablePreference().onPause();
        }

        @Override
        public void onResume() {
            super.onResume();
            getAosqpizablePreference().onResume();
        }

        @Override
        protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
            super.onPrepareDialogBuilder(builder);
            getAosqpizablePreference().setFragment(this);
            getAosqpizablePreference().onPrepareDialogBuilder(builder, this);
        }

        @Override
        public void onDialogClosed(boolean positiveResult) {
            getAosqpizablePreference().onDialogClosed(positiveResult);
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);
            getAosqpizablePreference().onBindDialogView(view);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            super.onClick(dialog, which);
            getAosqpizablePreference().onClick(dialog, which);
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            getAosqpizablePreference().setFragment(this);
            final Dialog sub = getAosqpizablePreference().onCreateDialog(savedInstanceState);
            if (sub == null) {
                return super.onCreateDialog(savedInstanceState);
            }
            return sub;
        }

        @Override
        protected View onCreateDialogView(Context context) {
            final View v = getAosqpizablePreference().onCreateDialogView(context);
            if (v == null) {
                return super.onCreateDialogView(context);
            }
            return v;
        }
    }
}
