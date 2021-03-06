/*
 * EduMsg is made available under the OSI-approved MIT license.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package edumsg.edumsg_android_app;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Allows the user to log in using a username and password. If successful, starts a {@link MainActivity},
 * putting the username and sessionId in the intent extras.
 */
public class LoginFragment extends AppCompatDialogFragment implements View.OnClickListener{

    @Bind(R.id.username) EditText mUsername;
    @Bind(R.id.password) EditText mPassword;
    @Bind(R.id.login_button) Button mLoginButton;
    @Bind(R.id.sign_up_btn) Button mSignupButton;
    @BindColor(R.color.colorPrimaryDark) int cPrimDark;

//    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        mLoginButton.setTextColor(cPrimDark);
//        00e5ff
        mLoginButton.setOnClickListener(this);
//        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE)
//                {
//                    mLoginButton.performClick();
//                }
//                return false;
//            }
//        });
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_layout, new RegisterFragment(), "register")
                        .addToBackStack("register")
                        .commit();
            }
        });
        return view;
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
////        mListener = null;
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button)
        {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getWindow()
                    .getDecorView().getRootView().getWindowToken(), 0);
            attemptLogin();
        }
    }

    private void attemptLogin() {
        mUsername.setError(null);
        mPassword.setError(null);

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(username)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            login(username, password);
        }
    }

    private void login(final String username, String password)
    {
        final LoadToast loadToast = new LoadToast(getContext());
        loadToast.setText("Authenticating...");
        loadToast.show();
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("queue", "USER");
        jsonParams.put("method", "login");
        jsonParams.put("username", username);
        jsonParams.put("password", password);
        JSONObject jsonRequest = new JSONObject(jsonParams);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                MainActivity.requestUrl, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                final ObjectMapper mapper = new ObjectMapper();
                try {
                    final Map<String, Object> responseMap = mapper
                            .readValue(response.toString(),
                                    new TypeReference<HashMap<String, Object>>() {
                                    });
                    if (responseMap.get("code").equals("200"))
                    {
                        loadToast.success();
                        new CountDownTimer(1000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                LinkedHashMap userMap = (LinkedHashMap) responseMap.get("user");
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("username", username);
                                intent.putExtra("sessionId", (String) userMap.get("session_id"));
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }.start();
                    }
                }
                catch (Exception e)
                {
                    loadToast.error();
                    Log.e("JSONMapper", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadToast.error();
                if (volleyError.networkResponse != null
                        && volleyError.networkResponse.data != null
                        && volleyError.networkResponse.statusCode == 400)
                {
                    try {
                        String errorJson = new String(volleyError.networkResponse.data);
                        JSONObject errorObj = new JSONObject(errorJson);
                        String error = errorObj.getString("message");
                        if (error.toLowerCase().contains("username"))
                        {
                            mUsername.setError(error);
                            mUsername.requestFocus();
                        }
                        if (error.toLowerCase().contains("password")) {
                            mPassword.setError(error);
                            mPassword.requestFocus();
                        }
                    }
                    catch (JSONException e)
                    {
                        loadToast.error();
                        Log.e("Response Error Msg", e.getMessage());
                    }
                }
                else {
                    loadToast.error();
                    Log.e("Volley", volleyError.toString());
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            };
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ((AuthActivity) getActivity()).getVolleyRequestQueue().add(jsonObjectRequest);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }



//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
