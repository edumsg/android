/*
EduMsg is made available under the OSI-approved MIT license.
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
IN THE SOFTWARE.
*/

package edumsg.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import edumsg.Activity.base.BasePublicActivity;
import edumsg.Model.LoginResponse;
import edumsg.Model.User;
import edumsg.Tasks.LoginTask;
import edumsg.Tasks.UserTask;
import edumsg.R;
import edumsg.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterationActivity extends BasePublicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registeration, menu);
        return true;
    }

    public void registerBtnOnclick(View v) {
        EditText email = (EditText) findViewById(R.id.reg_email);
        EditText password = (EditText) findViewById(R.id.reg_password);
        EditText repPassword = (EditText) findViewById(R.id.reg_rep_password);
        EditText firstName = (EditText) findViewById(R.id.reg_first_name);
        EditText lastName = (EditText) findViewById(R.id.reg_last_name);
        EditText country = (EditText) findViewById(R.id.reg_country);
        EditText city = (EditText) findViewById(R.id.reg_city);
        EditText username = (EditText) findViewById(R.id.reg_username);
        EditText gender = (EditText) findViewById(R.id.reg_gender);
        DatePicker dob_r = (DatePicker) findViewById(R.id.reg_dob);
        final User u;
        if(password.getText().toString().equals(repPassword.getText().toString())) {
            u = new User(new Long(1),email.getText().toString(),
                    password.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    "456ddfgdfg",
                    gender.getText().toString(),
                    dob_r.getDayOfMonth() + "." + dob_r.getMonth() + "." + dob_r.getYear(),
                    country.getText().toString(),
                    username.getText().toString(),
                    R.drawable.user1+"",
                    "none","none","none");
            startProgress();
            UserTask userTask = new UserTask(u.getUsername(), u.getEmail(), u.getPassword(), u.getFirst_name() + " " + u.getLast_name(), "imgur.com/dfsdf");
            ApiRouter.withoutToken().register(userTask, new Callback<Response>() {
                @Override
                public void success(Response r, Response response) {
                    LoginTask loginTask = new LoginTask(u.getUsername(),u.getPassword());
                    ApiRouter.withoutToken().login(loginTask, new Callback<LoginResponse>() {
                        @Override
                        public void success(LoginResponse r, Response response) {
                            User user = new User(new Long(Integer.parseInt(r.getSimpleUser().getId())), r.getSimpleUser().getEmail(), u.getPassword(), r.getSimpleUser().getName().split(" ")[0], r.getSimpleUser().getName().split(" ")[1], r.getSession_id(), r.getSimpleUser().getGender(), r.getSimpleUser().getDate_of_birth(), r.getSimpleUser().getCountry(), r.getSimpleUser().getUsername(), R.drawable.user1+"",r.getSimpleUser().getBio(),r.getSimpleUser().getWebsite(),r.getSimpleUser().getLanguage());
                            setCurrentUser(user);

                            Toast.makeText(RegisterationActivity.this, "Welcome " + getCurrentUser().getFirst_name() + " " + getCurrentUser().getLast_name() + "!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            displayError(error);
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    displayError(error);
                }
            });

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
