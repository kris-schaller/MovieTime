package schaller.com.movetime.movies_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import schaller.com.movetime.R;

public class MovieListActivity extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popularity_action:
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(this, R.string.sort_by_popularity, Toast.LENGTH_SHORT);
                toast.show();
                return true;
            case R.id.sort_by_rating_action:
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(this, R.string.sort_by_rating, Toast.LENGTH_SHORT);
                toast.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
