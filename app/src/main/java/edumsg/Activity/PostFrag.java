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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edumsg.Model.Tweet;
import edumsg.R;

/**
 * Created by ammar on 5/12/15.
 */
@SuppressLint("ValidFragment")
public class PostFrag extends Fragment {

    Tweet tweet;

    public PostFrag(Tweet tweet){
        this.tweet = tweet;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.post_frag, null);
        if (!tweet.getImage_url().equals(""))
        {
            ImageView img = (ImageView) v.findViewById(R.id.imageView1);
            img.setImageResource(R.drawable.feed_img);
        }

        TextView lbl = (TextView) v.findViewById(R.id.textView_ex2);
        lbl.setText(tweet.getCreator().getName());

        lbl = (TextView) v.findViewById(R.id.textView_ex3);
        lbl.setText(tweet.getTweet_text());
        return v;
    }
}
