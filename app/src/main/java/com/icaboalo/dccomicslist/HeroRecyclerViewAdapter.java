package com.icaboalo.dccomicslist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by icaboalo on 9/11/2015.
 */
public class HeroRecyclerViewAdapter extends RecyclerView.Adapter<HeroRecyclerViewAdapter.ViewHolder> {

    Context context;
    private List<JusticeLeague> justiceLeagueList;
    private LayoutInflater inflater;
    private MyOnItemClickListener myOnItemClickListener;

    public HeroRecyclerViewAdapter(Context context, List<JusticeLeague> justiceLeagueList) {
        this.context = context;
        this.justiceLeagueList = justiceLeagueList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView, R.id.hero_name, R.id.hero_id, R.id.hero_description, R.id.hero_image, R.id.hero_image_2);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int lastposition = -1;
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastposition) ? R.anim.up_from_bottom :
                R.anim.down_from_top);

        holder.itemView.startAnimation(animation);
        lastposition = position;

        JusticeLeague justiceLeague = justiceLeagueList.get(position);
        holder.setHeroName(justiceLeague.getHeroName());
        holder.setHeroId(justiceLeague.getHeroId());
        holder.setHeroDescription(justiceLeague.getHeroDescription());
        holder.setHeroImage(justiceLeague.getHeroImage());
        Log.d("Hero", "######" + justiceLeague.getHeroImage());
//        holder.setHeroImage2(justiceLeague.getHeroImage2());
    }

    @Override
    public int getItemCount() {
        return justiceLeagueList.size();
    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }


    public void setData(List newData){
        justiceLeagueList = newData;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView heroName;
        TextView heroId;
        TextView heroDescription;
        ImageView heroImage, heroImage2;

        public ViewHolder(View itemView, int idHeroName, int idHeroId, int idHeroDescription, int idHeroImage, int idHeroImage2) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.heroName = (TextView) itemView.findViewById(idHeroName);
            this.heroId = (TextView) itemView.findViewById(idHeroId);
            this.heroDescription = (TextView) itemView.findViewById(idHeroDescription);
            this.heroImage = (ImageView) itemView.findViewById(idHeroImage);
            this.heroImage2 = (ImageView) itemView.findViewById(idHeroImage2);
        }

        public void setHeroName(String name) {
            heroName.setText(name);
        }

        public void setHeroId(String id) {
            heroId.setText(id);
        }

        public void setHeroDescription(String description) {
            heroDescription.setText(description);
        }

        public void setHeroImage(String image) {
            Picasso.with(context).load(image).placeholder(R.mipmap.ic_launcher).into(heroImage);
            Picasso.with(context).load(image).placeholder(R.mipmap.ic_launcher).into(heroImage2);
        }

        public void setHeroImage2(String urlImage) {
            Picasso.with(context).load(urlImage).into(heroImage2);
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
