package com.nmelihsensoy.homecontrol;

/**
 * Created by root on 15.09.2016.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RgbFragment extends Fragment {
    static View view;

    private Socket mSocket;
    private TextView textV, tempText;
    private Button lampToggle, doorOp, doorLo, relayOpen;
    private LinearLayout background, backgroundTemp;
    private boolean isConnected;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "publish", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
    private Emitter.Listener serverListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String topic;
                        String payload;
                        try {
                            topic = data.getString("topic");
                            payload = data.getString("payload");
                        } catch (JSONException e) {
                            return;
                        }

                        if (topic.equals("/melih/doorState")) {
                            if (payload.equals("ON")) {
                                textV.setText(R.string.door_open_warn);
                                background.setBackgroundColor(Color.parseColor("#689F38"));
                            } else {
                                textV.setText(R.string.door_close_warn);
                                background.setBackgroundColor(Color.parseColor("#388E3C"));
                            }
                        } else if (topic.equals("/melih/dht")) {
                            String[] paths = payload.split("/");
                            tempText.setText(paths[0] + "°C  " + paths[1] + "% Nem");
                        }

                    }
                });
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.rgb_fragment, container, false);
        }

        ((MainActivity) this.getActivity()).setToolbarGone();
        isConnected = ((MainActivity) this.getActivity()).getConnected();
        mSocket = ((MainActivity) this.getActivity()).getSocket();
        mSocket.on("mqtt", serverListener);
        mSocket.on("publish", onNewMessage);

        return view;
    }

    private void sendEmit(String topic, String payload) {
        if (mSocket.connected()) {
            final JSONObject obj = new JSONObject();
            try {
                obj.put("topic", topic);
                obj.put("payload", payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("publish", obj);
        }
    }
}