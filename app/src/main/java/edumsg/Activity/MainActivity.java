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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edumsg.Activity.base.BasePublicActivity;
import edumsg.Model.TimeLineResponse;
import edumsg.Model.Tweet;
import edumsg.Model.User;
import edumsg.R;
import edumsg.Tasks.DeleteTweetTask;
import edumsg.Tasks.LogoutTask;
import edumsg.Tasks.NewTweetTask;
import edumsg.Tasks.TimeLineCommandTask;
import edumsg.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BasePublicActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnFragmentInteractionListener{

    ActionBar actionBar;
    android.app.Fragment frag;
    List<Tweet> myTweets;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public void setmTitle(String s) {
        mTitle = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public User getLoggedIn(){
        return getCurrentUser();
    }

    public void getTweets(String id){

    }

    public Activity getCurrentAct(){
        return this;
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                frag = new NewsFeeds();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                frag = new Notifications();
                break;
            case 3:

                TimeLineCommandTask timeLineCommandTask = new TimeLineCommandTask("timeline", getCurrentUser().getId()+"", "user");
                ApiRouter.withoutToken().getProfile(timeLineCommandTask, new Callback<TimeLineResponse>() {
                    @Override
                    public void success(TimeLineResponse response, Response response2) {
                        myTweets = response.getTweets();
                        mTitle = getString(R.string.title_section3);
                        for(int i = 0; i < response.getTweets().size(); i++) {
                        }
                        frag = new Profile(response.getTweets());
                        changeFrag(frag);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        displayError(retrofitError);
                        frag = new NewsFeeds();
                    }
                });

                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                frag = new Groups();
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                frag = new Inbox();
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                LogoutTask logoutTask = new LogoutTask("logout", getCurrentUser().getId()+"", "user");
                ApiRouter.withoutToken().logout(logoutTask, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        setCurrentUser(null);
                        Toast.makeText(getCurrentAct(), "Good bye ! :'(", Toast.LENGTH_LONG);
                        Intent logout_intent = new Intent(getCurrentAct(), LoginActivity.class);
                        startActivity(logout_intent);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        displayError(retrofitError);
                    }
                });
                break;
        }
        if (frag != null) {

            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, frag).commit();

            // update selected item and title, then close the drawer

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error no Fragment");
        }

    }

    public void changeFrag(android.app.Fragment frag) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }

    public void edit_profile_btn(View v) {

    }

    public void delete_post_profile_btn (View v){

        TextView name = (TextView) findViewById(R.id.textView_ex2);
        TextView text = (TextView) findViewById(R.id.textView_ex3);
        String nameS = name.getText().toString();
        String textS = text.getText().toString();
        String tweet_id = "";
        for(int i = 0; i < myTweets.size(); i++){
            if(nameS.equals(myTweets.get(i).getCreator().getName())){
                if(textS.equals(myTweets.get(i).getTweet_text())) {
                    tweet_id = myTweets.get(i).getId();
                }
            }
        }
        if(!tweet_id.equals("")) {
            DeleteTweetTask deleteTweetTask = new DeleteTweetTask(tweet_id);

            ApiRouter.withoutToken().deleteTweet(deleteTweetTask, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Toast.makeText(getCurrentAct(), "Done ;)", Toast.LENGTH_LONG);
                    actionBar.setTitle(R.string.title_section1);
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new NewsFeeds()).commit();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    displayError(retrofitError);
                }
            });
        }
    }

    public void submit_new_post_btn(View v){
        EditText editText = (EditText) findViewById(R.id.new_post_txt);
        NewTweetTask newTweetTask = new NewTweetTask("tweet", editText.getText().toString(), getCurrentUser().getId()+"", "", "tweet");
        ApiRouter.withoutToken().tweet(newTweetTask, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(getCurrentAct(), "Done ;)", Toast.LENGTH_LONG);
                actionBar.setTitle(R.string.title_section1);
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new NewsFeeds()).commit();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                displayError(retrofitError);
            }
        });
    }

    public void about_me_click(View v) {
        actionBar.setTitle("About ME");
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new ProfileAboutMe()).commit();
    }

    public void about_me_back_click(View v){
        actionBar.setTitle(R.string.title_section3);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new Profile(myTweets)).commit();
    }

    public void like_btn_profile(View v){
        TextView tv = (TextView) findViewById(R.id.textView_ex5);
        String no_likes_st = tv.getText().toString();
        int no_likes = Integer.parseInt(no_likes_st);
        no_likes++;
        tv.setText(no_likes+"");
    }

    public void sentMsgbtn(View v) {
        actionBar.setTitle("Sent");
        android.app.Fragment frag = new SentMsgFrag();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }

    public void sendNewMsgBtn(View v){
        Toast.makeText(this,"Message Sent Successfully! :)",Toast.LENGTH_LONG);
        actionBar.setTitle("Inbox");
        android.app.Fragment frag = new Inbox();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }

    public void inboxbtn(View v) {
        actionBar.setTitle("Inbox");
        android.app.Fragment frag = new Inbox();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }
    public void sendMsgbtn(View v) {
        actionBar.setTitle("New Message");
        android.app.Fragment frag = new NewMsgFrag();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }

    public void newPostbtn(View v) {
        actionBar.setTitle("New Post");
        android.app.Fragment frag = new NewPost();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }

    public void newGroupbtn(View v) {
        actionBar.setTitle("New Group");
        android.app.Fragment frag = new NewGroup();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, frag).commit();
    }



    public void restoreActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getCurrentAct(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavFragmentInteraction(String string) {

    }

    @Override
    public void onContentFragmentInteraction(String string) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
