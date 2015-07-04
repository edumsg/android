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


import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edumsg.Model.Message;
import edumsg.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SentMsgFrag extends Fragment {

    private ArrayList<Message> actList;


    public SentMsgFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sent_msg, container, false);
        loadActivities();
        ListView list = (ListView) v.findViewById(R.id.sent_msg_list);
        list.setAdapter(new CutsomAdapter());
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Message msg = actList.get(Integer.parseInt(l + ""));
                    Fragment frag = new MsgFrag(msg);
                    ((MainActivity)getActivity()).actionBar.setTitle(msg.getSubject());
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, frag).commit();
                } catch (Exception e) {
                }
            }
        });
        return  v;
    }





    /**
     * This method currently loads a dummy list of activities. You can write the
     * actual implementation of loading categories.
     */
    private void loadActivities()
    {
        actList = new ArrayList<Message>();
        actList.add(new Message(R.drawable.user1+"","Ammar", "12.5.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "11.5.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "10.5.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "9.5.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "12.4.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "12.3.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "12.2.2015", "Thank You"));
        actList.add(new Message(R.drawable.user1+"","Ammar", "12.12.2014", "Thank You"));
    }

    /**
     * The Class CutsomAdapter is the adapter class for Activity ListView. The
     * currently implementation of this adapter simply display static dummy
     * contents. You need to write the code for displaying actual contents.
     */
    private class CutsomAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return actList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Message getItem(int arg0)
        {
            return actList.get(arg0);
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
                        R.layout.message_item, null);

            TextView lbl = (TextView) v.findViewById(R.id.message_sent_name);
            lbl.setText(getItem(pos).getSender_name());

            lbl = (TextView) v.findViewById(R.id.message_date);
            lbl.setText(getItem(pos).getDate());

            lbl = (TextView) v.findViewById(R.id.message_desc);
            lbl.setText(getItem(pos).getSubject());

            ImageView img = (ImageView) v.findViewById(R.id.message_sent_img);
            img.setImageResource(Integer.parseInt(getItem(pos).getSender_img()));

            return v;
        }

    }


}
