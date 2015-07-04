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

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import edumsg.Model.Tweet;
import edumsg.R;

/**
 * The Class Profile is the Fragment class that is launched when the user clicks
 * on Profile option in Left navigation drawer.
 * This screen shows user's profile photo and it also shows user's photo and video feeds. 
 */
@SuppressLint("ValidFragment")
public class Profile extends Fragment
{

    private List<Tweet> tweetList;

    public Profile(List<Tweet> pList){
        this.tweetList = pList;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_profile, null);

        ListView list = (ListView) v.findViewById(R.id.prof_tweets);
        list.setAdapter(new CustomAdapter());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    Tweet tweet = tweetList.get(Integer.parseInt(l + ""));
                    Fragment frag = new PostFrag(tweet);
                    ((MainActivity)getActivity()).actionBar.setTitle(tweet.getCreator().getName());
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, frag).commit();
                } catch (Exception e){}
            }
        });
        return v;
    }

    /**
     * The Class CustomAdapter is the adapter for displaying User's photo and video feeds.
     * The current implementation simply display dummy photo and video feed. You need
     * to change it as per your needs.
     */
    private class CustomAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return tweetList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Tweet getItem(int arg0)
        {
            return tweetList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.profile_item, null);
            if (!getItem(pos).getImage_url().equals(""))
            {
                ImageView img = (ImageView) v.findViewById(R.id.post_item_img);
                img.setImageResource(R.drawable.feed_img);
            }

            Log.e("this tweet", getItem(pos).toString());
            TextView lbl = (TextView) v.findViewById(R.id.post_item_title);
            lbl.setText(getItem(pos).getCreator().getName());

            lbl = (TextView) v.findViewById(R.id.post_item_content);
            lbl.setText(getItem(pos).getTweet_text());

            lbl = (TextView) v.findViewById(R.id.post_item_date);
            lbl.setText(getItem(pos).getCreated_at());
            return v;
        }

    }
}
