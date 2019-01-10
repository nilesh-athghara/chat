package practice.notes.com.chat;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager m)
    {
        super(m);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case  0:
                RequestsFragment r=new RequestsFragment();
                return r;
            case  1:
               ChatFragment c=new ChatFragment();
                return c;
            case  2:
                FriendsFragment f=new FriendsFragment();
                return f;
             default:
                 return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Requests";
                case 1:
                    return "Chats";
                case 2:
                    return "Friends";
                default:
                    return null;
            }
        }
}
