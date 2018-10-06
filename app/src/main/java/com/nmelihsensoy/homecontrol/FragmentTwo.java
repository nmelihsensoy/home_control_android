package com.nmelihsensoy.homecontrol;

/**
 * Created by root on 15.09.2016.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class FragmentTwo extends Fragment {

    String[] irArray;
    Dialog dialog;
    Spinner cmbToolbar;
    private Socket mSocket;
    private boolean isConnected;
    private int selectedItem;
    private Button powerButton, muteButton, inputButton, menuButton, exitButton, numbersButton, okButton, downButton, upButton, rightButton, leftButton, chUp, chDw, volUp, volDw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_two, container, false);
        isConnected = ((MainActivity) this.getActivity()).getConnected();
        mSocket = ((MainActivity) this.getActivity()).getSocket();
        ((MainActivity) this.getActivity()).setToolBarSpinner();
        createDialog();

        cmbToolbar = getActivity().findViewById(R.id.CmbToolbar);
        //toolbarItem = ((MainActivity)this.getActivity()).getToolBarItem();
        cmbToolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //text.setText("test "+i);
                selectedItem = i;
                if (selectedItem == 0) {
                    irArray = getResources().getStringArray(R.array.teledunya);
                } else if (selectedItem == 1) {
                    irArray = getResources().getStringArray(R.array.vestel);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        powerButton = v.findViewById(R.id.powerButton);
        muteButton = v.findViewById(R.id.muteButton);
        menuButton = v.findViewById(R.id.menuButton);
        inputButton = v.findViewById(R.id.inputButton);
        exitButton = v.findViewById(R.id.exitButton);
        numbersButton = v.findViewById(R.id.numbersButton);
        okButton = v.findViewById(R.id.okButton);
        downButton = v.findViewById(R.id.downButton);
        upButton = v.findViewById(R.id.upButton);
        rightButton = v.findViewById(R.id.rightButton);
        leftButton = v.findViewById(R.id.leftButton);
        chUp = v.findViewById(R.id.chUp);
        chDw = v.findViewById(R.id.chDw);
        volUp = v.findViewById(R.id.volUp);
        volDw = v.findViewById(R.id.volDw);

        numbersButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });

        powerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[0]);
            }
        });

        muteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[1]);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[18]);
            }
        });

        inputButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[19]);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[17]);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[16]);
            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[20]);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[21]);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[23]);
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmit(returnTopic(), irArray[22]);
            }
        });

        chUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[2]);
            }
        });

        chDw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[3]);
            }
        });

        volUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[4]);
            }
        });

        volDw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[5]);
            }
        });
        return v;
    }

    private String returnTopic() {
        String returnTop = null;
        if (selectedItem == 0) {
            //Teledünya
            returnTop = "/melih/oda/remote/nec";
        } else if (selectedItem == 1) {
            //Arka Oda
            returnTop = "/melih/oda/remote/rc5";
        } else if (selectedItem == 2) {
            //Salon
            returnTop = "/melih/oda/remote/nec";
        }

        return returnTop;
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

    private void createDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.numbers_dialog);
        dialog.setTitle("Number Pad");
        Button btnNumpadRight = dialog.findViewById(R.id.btnNumpadRight);
        Button btnNumpadLeft = dialog.findViewById(R.id.btnNumpadLeft);
        Button btnNumpad1 = dialog.findViewById(R.id.btnNumpad1);
        Button btnNumpad2 = dialog.findViewById(R.id.btnNumpad2);
        Button btnNumpad3 = dialog.findViewById(R.id.btnNumpad3);
        Button btnNumpad4 = dialog.findViewById(R.id.btnNumpad4);
        Button btnNumpad5 = dialog.findViewById(R.id.btnNumpad5);
        Button btnNumpad6 = dialog.findViewById(R.id.btnNumpad6);
        Button btnNumpad7 = dialog.findViewById(R.id.btnNumpad7);
        Button btnNumpad8 = dialog.findViewById(R.id.btnNumpad8);
        Button btnNumpad9 = dialog.findViewById(R.id.btnNumpad9);
        Button btnNumpad0 = dialog.findViewById(R.id.btnNumpad0);

        btnNumpad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[7]);
            }
        });

        btnNumpad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[8]);
            }
        });

        btnNumpad3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[9]);
            }
        });

        btnNumpad4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[10]);
            }
        });

        btnNumpad5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[11]);
            }
        });

        btnNumpad6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[12]);
            }
        });

        btnNumpad7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[13]);
            }
        });

        btnNumpad8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[14]);
            }
        });

        btnNumpad9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[15]);
            }
        });

        btnNumpad0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[6]);
            }
        });


        btnNumpadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), irArray[16]);
                dialog.dismiss();
            }
        });

        btnNumpadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmit(returnTopic(), "EX");
                dialog.dismiss();
            }
        });

    }

}