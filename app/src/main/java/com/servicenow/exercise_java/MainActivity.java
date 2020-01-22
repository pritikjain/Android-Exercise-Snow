package com.servicenow.exercise_java;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.servicenow.client.GameController;
import com.servicenow.exercise.R;
import com.servicenow.resources.Game;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GameController.Callback {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    ProgressDialog mProgressDialog;
    GameController mGameController = new GameController();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview);


        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Loading ....");
        mProgressDialog.show();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load the game list
        mGameController.start(this);


    }

    @Override
    protected void onStop() {
        super.onStop();
        mGameController.stop();
    }

    @Override
    public void onResponse(List<Game> list) {
        mProgressDialog.dismiss();
        mAdapter = new GameAdapter(list, item -> {
            Intent i = new Intent(MainActivity.this, GameDetailActivity.class);
            i.putExtra("game", item);
            startActivity(i);

        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onFailure(Throwable t) {

        mProgressDialog.dismiss();
        Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
        t.printStackTrace();

    }


    static class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

        public interface onItemClickListener {
            void onItemClick(Game item);
        }

        private final List<Game> nesGames;
        private final onItemClickListener onItemClickListener;

        public GameAdapter(List<Game> gameList, onItemClickListener onItemClickListener) {
            this.nesGames = gameList;
            this.onItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final Game game = this.nesGames.get(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(game);
                }
            });

            holder.gameName.setText(game.getName());
            holder.gameDescription.setText(game.getShortDescription());
            holder.gameCover.setImageResource(Game.Companion.getIconResource(game.getCover()));
        }

        @Override
        public int getItemCount() {
            return nesGames.size();
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView gameCover;
            TextView gameName;
            TextView gameDescription;

            ViewHolder(View v) {
                super(v);
                gameCover = findViewById(R.id.image);
                gameName = findViewById(R.id.text1);
                gameDescription = findViewById(R.id.text2);
            }

            <T extends View> T findViewById(@IdRes int id) {
                return itemView.findViewById(id);
            }
        }
    }
}
