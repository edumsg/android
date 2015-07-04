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

package edumsg.Activity.base;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.Toast;

import edumsg.Model.User;

public abstract class BaseActivity extends ActionBarActivity {
    private static final String PREF_USER_ID = "PREF_USER_ID";
    private static final String PREF_USER_NAME = "PREF_USER_NAME";
    private static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";
    private static final String PREF_USER_TOKEN = "PREF_USER_TOKEN";
    private static final String PREF_USER_GENDER = "PREF_USER_GENDER";
    private static final String PREF_USER_DATE_OF_BIRTH = "PREF_USER_DATE_OF_BIRTH";
    private static final String PREF_USER_COUNTRY = "PREF_USER_COUNTRY";
    private static final String PREF_USER_BIO = "PREF_USER_BIO";
    private static final String PREF_USER_LANGUAGE = "PREF_USER_LANGUAGE";
    private static final String PREF_USER_WEBSITE = "PREF_USER_WEBSITE";
    private static final String PREF_USER_IMAGE = "PREF_USER_IMAGE";
    private static final String PREF_USER_PASSWORD = "PREF_USER_PASSWORD";
    private static final String PREF_USER_FIRST_NAME = "PREF_USER_FIRST_NAME";
    private static final String PREF_USER_LAST_NAME = "PREF_USER_LAST_NAME";

    private User currentUser;
    private int inProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();
    }

    protected void startProgress() {
        setProgressBarIndeterminateVisibility(true);
        inProgress++;
    }

    protected void stopProgress() {
        if (--inProgress == 0) {
            setProgressBarIndeterminateVisibility(false);
        }
    }

    protected void displayError(Exception e) {
        setProgressBarIndeterminateVisibility(false);
        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    protected boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    protected User getCurrentUser() {
        if (currentUser == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            if (sharedPreferences.contains(PREF_USER_TOKEN)) {
                currentUser = new User();
                currentUser.setId(sharedPreferences.getLong(PREF_USER_ID, 0));
                currentUser.setUsername(sharedPreferences.getString(PREF_USER_NAME, null));
                currentUser.setEmail(sharedPreferences.getString(PREF_USER_EMAIL, null));
                currentUser.setToken(sharedPreferences.getString(PREF_USER_TOKEN, null));
                currentUser.setPassword(sharedPreferences.getString(PREF_USER_PASSWORD, null));
                currentUser.setFirst_name(sharedPreferences.getString(PREF_USER_FIRST_NAME, null));
                currentUser.setLast_name(sharedPreferences.getString(PREF_USER_LAST_NAME, null));
                currentUser.setGender(sharedPreferences.getString(PREF_USER_GENDER, null));
                currentUser.setDate_of_birth(sharedPreferences.getString(PREF_USER_DATE_OF_BIRTH, null));
                currentUser.setCountry(sharedPreferences.getString(PREF_USER_COUNTRY, null));
                currentUser.setBio(sharedPreferences.getString(PREF_USER_BIO, null));
                currentUser.setLanguage(sharedPreferences.getString(PREF_USER_LANGUAGE, null));
                currentUser.setWebsite(sharedPreferences.getString(PREF_USER_WEBSITE, null));
                currentUser.setImage(sharedPreferences.getString(PREF_USER_IMAGE, null));
            }
        }

        return currentUser;
    }

    protected void setCurrentUser(User user) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor p = sharedPreferences.edit();

        if ((currentUser = user) != null) {
            p.putLong(PREF_USER_ID, currentUser.getId());
            p.putString(PREF_USER_NAME, currentUser.getUsername());
            p.putString(PREF_USER_EMAIL, currentUser.getEmail());
            p.putString(PREF_USER_TOKEN, currentUser.getToken());
            p.putString(PREF_USER_PASSWORD, currentUser.getPassword());
            p.putString(PREF_USER_FIRST_NAME, currentUser.getFirst_name());
            p.putString(PREF_USER_LAST_NAME, currentUser.getLast_name());
            p.putString(PREF_USER_GENDER, currentUser.getGender());
            p.putString(PREF_USER_DATE_OF_BIRTH, currentUser.getDate_of_birth());
            p.putString(PREF_USER_COUNTRY, currentUser.getCountry());
            p.putString(PREF_USER_BIO, currentUser.getBio());
            p.putString(PREF_USER_LANGUAGE, currentUser.getLanguage());
            p.putString(PREF_USER_WEBSITE, currentUser.getWebsite());
            p.putString(PREF_USER_IMAGE, currentUser.getImage());


        } else {
            p.clear();
        }

        p.commit();
    }
}
