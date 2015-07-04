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

import edumsg.Model.Message;
import edumsg.R;

@SuppressLint("ValidFragment")
public class MsgFrag extends Fragment {

    Message msg;

    public MsgFrag(Message msg) {
        // Required empty public constructor
        this.msg = msg;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_msg, container, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.message_show_sent_img);
        TextView sender_name = (TextView) v.findViewById(R.id.message_show_sent_name);
        TextView date = (TextView) v.findViewById(R.id.message_show_date);
        TextView subject = (TextView) v.findViewById(R.id.message_show_subject);
        TextView desc = (TextView) v.findViewById(R.id.message_show_desc);
        imageView.setImageResource(Integer.parseInt(msg.getSender_img()));
        sender_name.setText(msg.getSender_name());
        date.setText(msg.getDate());
        subject.setText(msg.getSubject());
        desc.setText(msg.getBody()+"SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage SampleMessage");
        return v;
    }


}
