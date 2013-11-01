package no.shortcut.scrum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.net.URL;
import java.util.List;

import no.shortcut.scrum.models.User;

/**
 * Created by HenningMosand on 31.10.13.
 */
public class UserImageAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;
    private LayoutInflater layoutInflater;

    public UserImageAdapter(Context context, List<User> users)
    {
        this.context = context;
        this.users = users;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).Id;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.user_view, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.userImageView);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        try
        {
            UrlImageViewHelper.setUrlDrawable(holder.imageView, getItem(i).Image, context.getResources().getDrawable(R.drawable.ic_launcher));
        } catch (Exception ex) {

        }


        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
