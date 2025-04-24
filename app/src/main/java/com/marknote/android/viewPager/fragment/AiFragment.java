package com.marknote.android.viewPager.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.marknote.android.R;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AiFragment extends Fragment {

    private EditText editTextMessage;
    private Button buttonSend;
    private LinearLayout layoutMessages;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ai, container, false);

        editTextMessage = view.findViewById(R.id.edit_text_message);
        buttonSend = view.findViewById(R.id.button_send);
        layoutMessages = view.findViewById(R.id.layout_messages);
        progressBar = view.findViewById(R.id.progress_bar);

        setHasOptionsMenu(true);
        initListener();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // 不加载任何菜单资源
    }

    private void initListener() {
        buttonSend.setOnClickListener(this::onClickSend);
    }

    private void onClickSend(View view) {
        String query = editTextMessage.getText().toString().trim();
        if (!query.isEmpty()) {
            editTextMessage.setText(""); // 清空输入框
            displayUserMessage(query);
            progressBar.setVisibility(View.VISIBLE);
            queryModel(query);
        } else {
            Toast.makeText(getActivity(), "请输入消息", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUserMessage(String message) {
        TextView textView = new TextView(getActivity());
        textView.setText(message);
        textView.setPadding(8, 8, 8, 8);
        textView.setBackgroundResource(R.drawable.user_message_background);

        // 设置布局参数，增加间距
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 16, 0, 0);
        textView.setLayoutParams(layoutParams);

        layoutMessages.addView(textView);
    }

    private void queryModel(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String result = callApi(query);
                    // 更新UI需要在主线程中进行
                    requireActivity().runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        displayAiMessage(result);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void displayAiMessage(String message) {
        TextView textView = new TextView(getActivity());
        textView.setText(message);
        textView.setPadding(8, 8, 8, 8);
        textView.setBackgroundResource(R.drawable.ai_message_background);
        // 设置布局参数，增加间距
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 16, 0, 0); // 设置顶部间距为16dp，其他方向为0
        textView.setLayoutParams(layoutParams);

        layoutMessages.addView(textView);
    }


    private String callApi(String query) throws Exception {
        String userId = "10284711用户";
        String url = "https://spark-api-open.xf-yun.com/v2/chat/completions";
        String APIPassword = "gokWKtFpGPvPkuyrdEfV:gvAxolYzonJpbpJZVIAF"; // 替换为你的API密钥

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", userId);
        jsonObject.put("model", "x1");
        JSONArray messagesArray = new JSONArray();
        JSONObject messageObject = new JSONObject();
        messageObject.put("role", "user");
        messageObject.put("content", query);
        messageObject.put("temperature", 0.5);
        messagesArray.put(messageObject);
        jsonObject.put("messages", messagesArray);
        jsonObject.put("stream", false);
        jsonObject.put("max_tokens", 4096);

        String header = "Authorization: Bearer " + APIPassword;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", header);
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        os.write(jsonObject.toString().getBytes());
        os.flush();
        os.close();

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return extractResponse(response.toString());
    }

    private String extractResponse(String response) {
        try {
            JSONObject jsonResponse = JSONUtil.parseObj(response);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (!choices.isEmpty()) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                String content = message.getStr("content");
                return content;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error: Failed to parse response";
    }
}